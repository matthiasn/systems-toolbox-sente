(ns matthiasn.systems-toolbox-sente.server-cmp-test
  (:require [clojure.test :refer :all]
            [matthiasn.systems-toolbox-sente.server :as sente]
            [clojure.tools.logging :as log]))

(deftest cmp-map-test
  (testing "Warnings logged when called in deprecated way."
    (let [log-msgs (atom [])]
      (with-redefs [log/log* (fn [_ _ _ msg] (swap! log-msgs conj msg))]
        (sente/cmp-map :test/ws-cmp (fn [_put-fn]))
        (is (= "DEPRECATED: use of index-page-fn in sente-cmp use cfg-map instead"
               (first @log-msgs)))
        (is (= "using sente-cmp without specifying :relay-types is DEPRECATED"
               (last @log-msgs)))))))
