(ns matthiasn.systems-toolbox-sente.client
  "This namespace contains a component for the client side of WebSockets
   communication."
  (:require [matthiasn.systems-toolbox-sente.util :as u]
            [matthiasn.systems-toolbox.spec :as st-spec]
            [matthiasn.systems-toolbox-sente.spec]
            [cljs.core.match :refer-macros [match]]
            [taoensso.sente :as sente]
            [taoensso.sente.packers.transit :as sente-transit]
            [matthiasn.systems-toolbox.log :as l]
            [clojure.set :as set]))

(defn set-cookie
  "Helper function for setting a cookie with the provided name."
  [name value valid]
  (.set goog.net.cookies name value valid))

(defn prepare-msg
  "Converts a map with :msg-type, :msg-payload and :msg-meta into a vector that
   can be passed to send-fn. It also sets :sente-uuid on msg-meta."
  [state {:keys [msg-type msg-payload msg-meta]}]
  [msg-type {:msg      msg-payload
             :msg-meta (merge msg-meta {:sente-uid (:uid @state)})}])

(defn handle-first-open
  "After component is ready and before WS connection is established, there's a
   small window during which another components might try to send something.
   Those messages would get lost, so they are buffered in :buffered-msgs until
   connection is ready."
  [put-fn ws]
  (let [{:keys [state send-fn]} ws
        buffered-msgs (:buffered-msgs @state)]
    (doall (map #(send-fn (prepare-msg state %)) buffered-msgs))
    (put-fn [:sente/first-open])
    (swap! state dissoc :buffered-msgs)))

(defn update-open-request
  "Updates a cookie with the number of open requests. This cookie can then be
   used in tests to wait for all backend requests to be answered before making
   any assertions about the UI. The cookie is valid for 15 minutes for now.
   This value is entirely arbitrary - feel free to submit a PR that makes this
   value configurable, should the need arise."
  [request-tags]
  (set-cookie "systems-toolbox-open-requests" (count request-tags) 900))

(defn make-handler
  "Creates a handler function for messages from WebSocket connection. Calls
   put-fn with received messages. Also updates the open requests cookie by
   decreasing the number when a message is encountered for which there was an
   expectation for a response from the backend."
  [put-fn cmp-state cfg]
  (fn sente-handler [{:keys [event]}]
    (let [request-tags (:request-tags cmp-state)]
      (match event
             [:chsk/state [_ {:first-open? true}]]
             (handle-first-open put-fn cmp-state)

             [:chsk/recv payload]
             (let [msg-w-meta (u/deserialize-meta payload)]
               (when (:count-open-requests cfg)
                 (swap! request-tags dissoc (:tag (meta msg-w-meta)))
                 (update-open-request @request-tags))
               (put-fn msg-w-meta))

             [:chsk/handshake _] ()
             :else ()))))

(defn client-state-fn
  "Returns a function that returns the initial component state for the sente
   client component.
   Also sets the systems-toolbox-open-requests cookie when the component is
   configured to record the number of open backend requests."
  [cfg]
  (fn
    [put-fn]
    (let [opts (merge {:packer (sente-transit/get-transit-packer)}
                      (:sente-opts cfg))
          ws (sente/make-channel-socket-client! "/chsk" opts)
          cmp-state (merge ws {:request-tags (atom {})})]
      (sente/start-chsk-router! (:ch-recv ws) (make-handler put-fn cmp-state cfg))
      (swap! (:state ws) assoc :buffered-msgs [])
      (when (:count-open-requests cfg)
        ; set cookie to initial value for testing
        (set-cookie "systems-toolbox-open-requests" "0" 10))
      {:state cmp-state})))

(defn all-msgs-handler
  "Handler function for all incoming messages. By default, all messages received
   by this component are forwarded to the server. When message filtering is
   enabled for this component, it will instead only forward messages to the
   server for which the :forward-to-backend key is set in the message metadata."
  [{:keys [cmp-state msg-meta msg-type cfg] :as msg-map}]
  (let [fwd? (if (:msg-filtering cfg) (:forward-to-backend msg-meta) true)
        expect-response? (:expect-backend-response msg-meta)
        request-tags (:request-tags cmp-state)
        {:keys [state send-fn]} cmp-state
        msg (select-keys msg-map [:msg-type :msg-meta :msg-payload])]
    (when fwd?
      (when (and expect-response? (:count-open-requests cfg))
        (swap! request-tags assoc-in [(:tag msg-meta)] msg-type)
        (update-open-request @request-tags))
      (if (:open? @state)
        (send-fn (prepare-msg state msg))
        (swap! state update-in [:buffered-msgs] conj msg))))
  {})

(defn cmp-map
  "Returns configuration map for creating a client-side WebSockets communication
   component. In any case, it requires to specify the component ID. Additional
   configuration options can be provided in the second argument. Here, we can
   configure behavior such as only forwarding messages that have the
   :forward-to-backend key set on the message metadata. This has been added in
   version 0.5.10 - specifically requiring to enable this filtering mechanism
   avoids breaking backward compatibility. Also, there's an option to count open
   requests. This option should be enabled together with :msg-filtering and will
   keep a set of open backend requests and record the count in a cookie.
   This can be useful in testing when we want to determine when the UI is stable.
   Note that for inclusion in the counter, the :expect-backend-response key
   needs to be set on the message metadata."
  {:added "0.3.1"}
  ([cmp-id] (cmp-map cmp-id {}))
  ([cmp-id cfg]
   (st-spec/valid-or-no-spec? :st-sente/client-cfg cfg)
   (merge {:cmp-id   cmp-id
           :state-fn (client-state-fn cfg)
           :opts     (merge {:watch                 :state
                             :reload-cmp            false
                             :snapshots-on-firehose false
                             :msg-filtering         false
                             :count-open-requests   false
                             :validate-in           false
                             :validate-out          false
                             :validate-state        false}
                            cfg)}
          (if-let [msg-types (set/union (set (:relay-types cfg))
                                        #{:firehose/cmp-put
                                          :firehose/cmp-recv
                                          :firehose/cmp-publish-state
                                          :firehose/cmp-recv-state})]
            {:handler-map (zipmap msg-types (repeat all-msgs-handler))}
            (do (l/warn
                  "DEPRECATED: using sente-cmp without specifying :relay-types")
                {:all-msgs-handler all-msgs-handler})))))
