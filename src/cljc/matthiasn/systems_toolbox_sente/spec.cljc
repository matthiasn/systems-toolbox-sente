(ns matthiasn.systems-toolbox-sente.spec
  (:require
    #?(:clj  [clojure.spec :as s]
       :cljs [cljs.spec :as s])))

(defn namespaced-keyword? [k] (and (keyword? k) (namespace k)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Server-side Sente Spec
(s/def :st-sente/index-page-fn fn?)
(s/def :st-sente/middleware fn?)
(s/def :st-sente/user-id-fn fn?)
(s/def :st-sente/routes-fn fn?)
(s/def :st-sente/host string?)
(s/def :st-sente/port number?)
(s/def :st-sente/undertow-cfg map?)
(s/def :st-sente/sente-opts map?)
(s/def :st-sente/ssl-port number?)
(s/def :st-sente/keystore string?)
(s/def :st-sente/key-password string?)
(s/def :st-sente/relay-types (s/* namespaced-keyword?))

(s/def :st-sente/server-cfg
  (s/keys :req-un [:st-sente/index-page-fn]
          :opt-un [:st-sente/middleware
                   :st-sente/user-id-fn
                   :st-sente/routes-fn
                   :st-sente/host
                   :st-sente/port
                   :st-sente/undertow-cfg
                   :st-sente/sente-opts
                   :st-sente/ssl-port
                   :st-sente/keystore
                   :st-sente/key-password
                   :st-sente/relay-types]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Client-side Sente Spec
(s/def :st-sente/count-open-requests boolean?)

(s/def :st-sente/client-cfg
  (s/keys :opt-un [:st-sente/sente-opts
                   :st-sente/count-open-requests]))
