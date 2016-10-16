(ns matthiasn.systems-toolbox-sente.test-core
  (:require [matthiasn.systems-toolbox-sente.test-store :as store]
            [matthiasn.systems-toolbox-sente.test-ui :as cnt]
            [matthiasn.systems-toolbox.switchboard :as sb]
            [matthiasn.systems-toolbox-sente.client :as sente]))

(enable-console-print!)

(defonce switchboard (sb/component :client/switchboard))

(def sente-cfg {:relay-types #{:cnt/inc :cnt/dec :cnt/add :cnt/remove}})

(defn init
  []
  (sb/send-mult-cmd
    switchboard
    [[:cmd/init-comp (sente/cmp-map :client/ws-cmp sente-cfg)]
     [:cmd/init-comp (store/cmp-map :client/store-cmp)]
     [:cmd/init-comp (cnt/cmp-map :client/cnt-cmp)]
     [:cmd/route-all {:from :client/cnt-cmp :to :client/ws-cmp}]
     [:cmd/route {:from :client/ws-cmp :to :client/store-cmp}]
     [:cmd/observe-state {:from :client/store-cmp :to :client/cnt-cmp}]]))

(init)
