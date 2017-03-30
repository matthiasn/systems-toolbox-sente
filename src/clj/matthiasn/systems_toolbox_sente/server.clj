(ns matthiasn.systems-toolbox-sente.server
  "This namespace contains a component for the server side of WebSockets communication."
  (:require
    [matthiasn.systems-toolbox-sente.util :as u]
    [clojure.tools.logging :as log]
    [ring.middleware.defaults :as rmd]
    [ring.util.response :refer [resource-response response content-type]]
    [compojure.core :refer (routes wrap-routes GET POST)]
    [compojure.route :as route]
    [clojure.core.async :refer [<! chan put! mult tap pub sub timeout go-loop sliding-buffer]]
    [immutant.web :as immutant]
    [immutant.web.undertow :as undertow]
    [taoensso.sente :as sente]
    [taoensso.sente.server-adapters.immutant :refer (sente-web-server-adapter)]
    [taoensso.sente.packers.transit :as sente-transit]))

(def ring-defaults-config
  (assoc-in rmd/site-defaults [:security :anti-forgery]
            {:read-token (fn [req] (-> req :params :csrf-token))}))

(defn random-user-id-fn
  "Generates random and unique ID for each WebSocket connection request. Can be overridden by the
  :user-id-fn key in the component's cfg-map, for example in order to use the ring session ID."
  [req]
  (let [uid (str (java.util.UUID/randomUUID))]
    (log/debug "Connected:" (:remote-addr req) uid)
    uid))

(defn make-handler
  "Create handler function for messages from WebSocket connection. Calls put-fn with received messages
  while reconstructing the metadata on each message to comply with the message format expected by the
  systems-toolbox library."
  [_ put-fn]
  (fn [{:keys [event]}]
    (log/debug "Received over WebSockets:" event)
    (put-fn (u/deserialize-meta event))))

(def default-host (get (System/getenv) "HOST" "localhost"))
(def default-port (get (System/getenv) "PORT" "8888"))
(def http2? (get (System/getenv) "HTTP2" false))

(defn sente-comp-fn
  "Component state function. Initializes the webserver that provides both the index-page of a
  single-page application plus WebSocket communication between the server and each connected client.
  Expects a single map as an argument, where only the :index-page-fn entry is mandatory. This
  function is expected to deliver the index page, e.g. by using hiccup as can be seen in the
  example applications.
  When host and port are not specified, they are taken from environment variables, with a default
  when those are not defined (as can be seen above).
  It is also possible to specify additional middleware, for example for optimized asset delivery
  or additional security headers.
  A :routes-fn can be specified, which expects a function that will be called with a single argument
  map that currently contains the put-fn for communicating with the rest of the system.
  The server can listen both via http and via https, where the https configuration would be specified
  under the :undertow-cfg as follows:

      {:ssl-port     8443
       :keystore     \"/path/to/certificate\"
       :key-password \"some-random-password\"}

  In order to disable unencrypted listening altogether, the :port key with a nil value can be specified."
  [{:keys [index-page-fn middleware user-id-fn routes-fn host port undertow-cfg sente-opts]
    :or   {user-id-fn random-user-id-fn
           host default-host
           port default-port}}]
  (fn [put-fn]
    (let [undertow-cfg (merge {:host host :port port :http2? http2?} undertow-cfg)
          wrap-routes-defaults #(wrap-routes (apply routes %) rmd/wrap-defaults ring-defaults-config)
          user-routes (if-not routes-fn
                        []
                        (routes-fn {:put-fn put-fn
                                    :wrap-routes-defaults wrap-routes-defaults}))
          opts (merge {:user-id-fn user-id-fn
                       :packer (sente-transit/get-transit-packer)}
                      sente-opts)
          ws (sente/make-channel-socket! sente-web-server-adapter opts)
          {:keys [ch-recv ajax-get-or-ws-handshake-fn ajax-post-fn]} ws
          cmp-routes-1 [(GET "/" req (content-type (response (index-page-fn req)) "text/html"))
                        (GET "/chsk" req (ajax-get-or-ws-handshake-fn req))
                        (POST "/chsk" req (ajax-post-fn req))]
          cmp-routes-2 [(route/resources "/")
                        (route/not-found "Page not found")]
          ;; Sente's and resources' routes are wrapped in ring-defaults. User routes are not,
          ;; by default. However, user can use (wrap-routes-defaults) to use defaults, if needed.
          all-routes (routes (wrap-routes-defaults cmp-routes-1)
                             (apply routes user-routes)
                             (wrap-routes-defaults cmp-routes-2))]
      (let [wrapped-in-middleware (if middleware (middleware all-routes) all-routes)
            server (immutant/run wrapped-in-middleware (undertow/options undertow-cfg))]
        (when (:port undertow-cfg)
          (log/info "Immutant-web is listening on port" port "on interface" host))
        (when-let [ssl-port (:ssl-port undertow-cfg)]
          (log/info "Immutant-web is listening on SSL-port" ssl-port "on interface" host))
        (sente/start-chsk-router! ch-recv (make-handler ws put-fn))
        {:state       ws
         :shutdown-fn #(immutant/stop server)}))))

(defn all-msgs-handler
  "Handler all incoming messages. Reformats message into a map with the metadata stored separately
  as it would otherwise not survive the EDN/Transit serialization when the message is sent over the
  WebSocket connection.
  When a message contains the :sente-uid on the metadata, it will be sent to that specific client, otherwise
  it will be broadcast to all connected clients."
  [{:keys [cmp-state msg-type msg-meta msg-payload]}]
  (let [ws cmp-state
        chsk-send! (:send-fn ws)
        connected-uids (:any @(:connected-uids ws))
        dest-uid (:sente-uid msg-meta)
        msg-w-ser-meta [msg-type {:msg msg-payload :msg-meta msg-meta}]]
    (when (contains? connected-uids dest-uid)
      (chsk-send! dest-uid msg-w-ser-meta))
    (when (= :broadcast dest-uid)
      (doseq [uid connected-uids]
        (chsk-send! uid msg-w-ser-meta)))))

(defn cmp-map
  "Returns map for creating server-side WebSockets communication component. Takes the cmp-id and either
  a configuration map or a function that generates the index page of a single page application. The latter
  is for backwards compatibility only, when a map is provided, the same function is expected under the
  :index-page-fn key."
  {:added "0.3.1"}
  [cmp-id cfg-map-or-index-page-fn]
  (let [cfg-map (if (map? cfg-map-or-index-page-fn)
                  cfg-map-or-index-page-fn
                  {:index-page-fn cfg-map-or-index-page-fn})]
    {:cmp-id           cmp-id
     :state-fn         (sente-comp-fn cfg-map)
     :all-msgs-handler all-msgs-handler
     :opts             {:watch :connected-uids
                        :snapshots-on-firehose false}}))
