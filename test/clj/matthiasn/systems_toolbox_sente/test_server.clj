(ns matthiasn.systems-toolbox-sente.test-server
  "Start HTTP server with static HTML required for test scenario."
  (:require [matthiasn.systems-toolbox.switchboard :as sb]
            [matthiasn.systems-toolbox-sente.server :as sente]
            [matthiasn.systems-toolbox-sente.test-store :as st]
            [hiccup.core :refer [html]]
            [clj-webdriver.taxi :as tx])
  (:import (java.net ServerSocket)))

(defn index-page
  "Generates index page HTML."
  [_]
  (html
    [:html
     {:lang "en"}
     [:head
      [:meta {:name "viewport" :content "width=device-width, minimum-scale=1.0"}]
      [:title "Counter"]
      [:link {:href "/css/example.css" :rel "stylesheet"}]]
     [:body
      [:div#counter]
      [:script {:src "/js/build/example.js"}]]]))

(defonce switchboard (sb/component :server/switchboard))

(defn get-free-port
  "Find open port."
  []
  (let [socket (ServerSocket. 0)
        port (.getLocalPort socket)]
    (.close socket)
    port))

(defn restart!
  "Starts or restarts system by asking switchboard to fire up the provided ws-cmp,
   a scheduler component and the ptr component, which handles and counts messages
   about mouse moves."
  []
  (let [port (get-free-port)]
    (sb/send-mult-cmd
      switchboard
      [[:cmd/init-comp
        #{(sente/cmp-map :server/ws-cmp {:index-page-fn index-page
                                         :relay-types   #{:cnt/inc
                                                          :cnt/dec
                                                          :cnt/add
                                                          :cnt/remove
                                                          :broadcast/msg}
                                         :port          port})
          (st/cmp-map :server/store-cmp)}]
       [:cmd/route {:from :server/ws-cmp :to #{:server/store-cmp}}]
       [:cmd/route {:from :server/store-cmp :to #{:server/ws-cmp}}]])
    (tx/set-driver! {:browser (condp = (get (System/getenv) "BROWSER")
                                "phantomjs" :phantomjs
                                "chrome" :chrome
                                :firefox)})
    (tx/to (str "http://localhost:" port))))

(defn one-time-teardown []
  (sb/send-cmd switchboard [:cmd/shutdown-all])
  (tx/quit tx/*driver*))

(defn send-to-ws-cmp [msg]
  (sb/send-cmd
    switchboard
    [:cmd/send {:to  :server/ws-cmp
                :msg msg}]))

(defn once-fixture [f]
  (restart!)
  (f)
  (one-time-teardown))
