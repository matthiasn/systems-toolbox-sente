(ns matthiasn.systems-toolbox-sente.test-store
  "This namespace contains the functions necessary to instantiate the store-cmp,
   which then holds the server side application state."
  (:require [matthiasn.systems-toolbox-sente.test-spec]))

(defonce state (atom {}))

(defn state-fn
  "Initial component state."
  [_put-fn]
  {:state state})

(defn msg-handler
  "Update message counter, then emit same message, which is then routed to
   client store to facilitate UI update."
  [{:keys [current-state msg msg-type]}]
  (let [new-state (assoc-in current-state [msg-type]
                            (inc (get current-state msg-type 0)))]
    {:new-state new-state
     :emit-msg  msg}))

(defn cmp-map
  "Generates component map for state-cmp."
  [cmp-id]
  {:cmp-id      cmp-id
   :state-fn    state-fn
   :handler-map {:cnt/inc    msg-handler
                 :cnt/dec    msg-handler
                 :cnt/add    msg-handler
                 :cnt/remove msg-handler}})
