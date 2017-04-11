(ns matthiasn.systems-toolbox-sente.test-core
  (:require [matthiasn.systems-toolbox-sente.test-store :as store]
            [matthiasn.systems-toolbox-sente.test-ui :as cnt]
            [matthiasn.systems-toolbox.switchboard :as sb]
            [matthiasn.systems-toolbox-sente.client :as sente]))

(enable-console-print!)

(defonce switchboard (sb/component :client/switchboard))

(def sente-cfg {:relay-types #{:cnt/inc :cnt/dec :cnt/add :cnt/remove}
                ;; uncomment for testing via AJAX instead of WebSockets
                ;; :sente-opts  {:type :ajax}
                })

(defn init
  []
  (sb/send-mult-cmd
    switchboard
    [[:cmd/init-comp
      #{(sente/cmp-map :client/ws-cmp sente-cfg) ; WebSocket communication
        (store/cmp-map :client/store-cmp)
        (cnt/cmp-map :client/cnt-cmp)}]
     [:cmd/route {:from :client/cnt-cmp
                  :to #{;:client/store-cmp
                        :client/ws-cmp}}]
     [:cmd/route {:from :client/ws-cmp
                  :to #{:client/store-cmp}}]
     [:cmd/observe-state {:from :client/store-cmp :to :client/cnt-cmp}]]))

(init)
