(ns matthiasn.systems-toolbox-sente.test-spec
  (:require
    #?(:clj  [clojure.spec.alpha :as s]
       :cljs [cljs.spec.alpha :as s])))

(s/def :test/counter #(and (integer? %) (>= % 0)))

(s/def :cnt/inc
  (s/keys :req-un [:test/counter]))

(s/def :cnt/dec
  (s/keys :req-un [:test/counter]))

(s/def :test/counters (s/coll-of integer?))
(s/def :test/store-spec (s/keys :req-un [:test/counters]))