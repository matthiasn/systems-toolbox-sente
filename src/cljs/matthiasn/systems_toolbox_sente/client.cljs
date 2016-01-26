(ns matthiasn.systems-toolbox-sente.client
  "This namespace contains a component for the client side of WebSockets communication."
  (:require [matthiasn.systems-toolbox-sente.util :as u]
            [cljs.core.match :refer-macros [match]]
            [taoensso.sente :as sente :refer (cb-success?)]
            [taoensso.sente.packers.transit :as sente-transit]))

(defn prepare-msg
  "Converts a map with :msg-type, :msg-payload and :msg-meta into a vector that can be passwed
  to send-fn. It also set :sente-uuid on a msg-meta."
  [state {:keys [msg-type msg-payload msg-meta]}]
  [msg-type {:msg msg-payload
             :msg-meta (merge msg-meta {:sente-uid (:uid @state)})}])

(defn handle-first-open
  "After component is ready and before WS connection is established, there's a small window during
  which another components might try to send something. Those messages would get lost, so they are
  buffered in :buffered-msgs until connection is ready."
  [put-fn ws]
  (put-fn [:first-open true])
  (let [{:keys [state send-fn]} ws
        buffered-msgs (:buffered-msgs @state)]
    (doall (map #(send-fn (prepare-msg state %)) buffered-msgs))
    (swap! state dissoc :buffered-msgs)))

(defn make-handler
  "Create handler function for messages from WebSocket connection. Calls put-fn with received
   messages."
  [put-fn ws]
  (fn [{:keys [event]}]
    (match event
           [:chsk/state {:first-open? true}] (handle-first-open put-fn ws)
           [:chsk/recv payload] (put-fn (u/deserialize-meta payload))
           [:chsk/handshake _] ()
           :else ())))

(defn mk-state
  "Return clean initial component state atom."
  [put-fn]
  (let [ws (sente/make-channel-socket! "/chsk" {:type :auto
                                                :packer (sente-transit/get-flexi-packer :edn)})]
    (sente/start-chsk-router! (:ch-recv ws) (make-handler put-fn ws))
    (swap! (:state ws) assoc :buffered-msgs [])
    {:state ws}))

(defn all-msgs-handler
  "Handle incoming messages: process / add to application state."
  [{:keys [cmp-state] :as msg-map}]
  (let [{:keys [state send-fn]} cmp-state
        msg (select-keys msg-map [:msg-type :msg-meta :msg-payload])]
    (if (:open? @state)
      (send-fn (prepare-msg state msg))
      (swap! state update-in [:buffered-msgs] conj msg))))

(defn cmp-map
  "Creates client-side WebSockets communication component"
  {:added "0.3.1"}
  [cmp-id]
  {:cmp-id           cmp-id
   :state-fn         mk-state
   :all-msgs-handler all-msgs-handler
   :opts             {:watch      :state
                      :reload-cmp false
                      :snapshots-on-firehose false}})
