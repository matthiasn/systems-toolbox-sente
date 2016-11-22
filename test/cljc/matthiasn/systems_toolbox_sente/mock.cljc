(ns matthiasn.systems-toolbox-sente.mock
  "This namespace contains a component that can be used on to mock WebSockets
   communication."
  #?(:cljs (:require-macros [cljs.core.async.macros :refer [go-loop]]))
  (:require
    [matthiasn.systems-toolbox-sente.util :as u]
    #?(:clj      [clojure.edn :as edn]
       :cljs     [cljs.reader :as edn])
    #?(:clj  [clojure.core.async :refer [<! chan put! pipe go-loop]]
       :cljs [cljs.core.async :refer [<! chan put! pipe]])))

(defn sente-mock-comp-fn
  "Component state function. Initializes the webserver that provides both the
   index-page of a single-page application plus WebSocket communication between
   the server and each connected client.
   Expects a single map as an argument."
  [put-fn]
  (let [sente-mock-in-chan (chan)]
    (go-loop []
      (let [msg (edn/read-string (<! sente-mock-in-chan))]
        (put-fn (u/deserialize-meta msg)))
      (recur))
    {:state (atom {:sente-mock-in-chan  sente-mock-in-chan
                   :sente-mock-out-chan (chan)})}))

(defn all-msgs-handler
  "Handler for all incoming messages. Reformats message into a map with the
   metadata stored separately as it would otherwise not survive the EDN/Transit
   serialization when the message is sent over the WebSocket connection.
   Here, the same is done so the communication has the comparable property of
   only containing EDN-serializable data. :sente-uid is assigned to message
   metadata."
  [{:keys [cmp-state msg-type msg-meta msg-payload cfg]}]
  (put! (:sente-mock-out-chan @cmp-state)
        (pr-str [msg-type {:msg      msg-payload
                           :msg-meta (if-let [sente-uid (:sente-uid cfg)]
                                       (merge msg-meta {:sente-uid sente-uid})
                                       msg-meta)}])))

(defn cmp-map
  "Returns map for creating server-side WebSockets communication component.
   Takes the cmp-id and a configuration map."
  {:added "0.5.4"}
  [cmp-id cfg]
  {:cmp-id           cmp-id
   :state-fn         sente-mock-comp-fn
   :all-msgs-handler all-msgs-handler
   :opts             cfg})

(defn connect-sente-mocks
  "Connects two sente mock components with each other.
   As of now, it's a 1-on-1 relationship."
  {:added "0.5.4"}
  [cmp-1 cmp-2]
  (let [cmp-1-state @(:cmp-state cmp-1)
        cmp-2-state @(:cmp-state cmp-2)]
    (pipe (:sente-mock-out-chan cmp-1-state) (:sente-mock-in-chan cmp-2-state))
    (pipe (:sente-mock-out-chan cmp-2-state) (:sente-mock-in-chan cmp-1-state))))
