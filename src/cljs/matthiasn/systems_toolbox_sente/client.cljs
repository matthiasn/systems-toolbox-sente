(ns matthiasn.systems-toolbox-sente.client
  "This namespace contains a component for the client side of WebSockets communication."
  (:require [matthiasn.systems-toolbox-sente.util :as u]
            [cljs.core.match :refer-macros [match]]
            [taoensso.sente :as sente :refer (cb-success?)]
            [taoensso.sente.packers.transit :as sente-transit]))

(defn handle-first-open
  "After component is ready and before WS connection is established, there's a small window during
  which another components might try to send something. Those messages would get lost, so they are
  buffered in :buffered-msgs until connection is ready."
  [put-fn ws]
  (put-fn [:first-open true])
  (let [{:keys [state send-fn]} ws
        buffered-msgs (:buffered-msgs @state)]
    (doall (map (partial send-fn) buffered-msgs))
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
  [{:keys [cmp-state msg-type msg-meta msg-payload]}]
  (let [{:keys [state send-fn]} cmp-state
        ;msg-w-ser-meta (assoc-in (merge msg-meta {}) [:sente-uid] (:uid @state))
        ;msg [msg-type {:msg msg-payload :msg-meta msg-w-ser-meta}]
        msg [msg-type {:msg msg-payload
                       :msg-meta (merge msg-meta {:sente-uid (:uid @state)})}]]
    (if (:open? @state)
      (send-fn msg)
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
