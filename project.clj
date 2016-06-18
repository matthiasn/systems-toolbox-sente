(defproject matthiasn/systems-toolbox-sente "0.6.1-SNAPSHOT"
  :description "WebSocket components for systems-toolbox"
  :url "https://github.com/matthiasn/systems-toolbox"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/cljc" "src/clj" "src/cljs"]

  :dependencies [[org.clojure/tools.logging "0.3.1"]
                 [io.aviso/pretty "0.1.26"]
                 [com.taoensso/encore "2.56.1" :exclusions [org.clojure/tools.reader]]
                 [com.taoensso/sente "1.9.0-beta2" :exclusions [org.clojure/tools.reader
                                                                org.clojure/core.async]]
                 [org.clojure/core.match "0.3.0-alpha4" :exclusions [org.clojure/core.memoize
                                                                     org.clojure/tools.analyzer.jvm]]
                 [com.cognitect/transit-clj "0.8.285"]
                 [com.cognitect/transit-cljs "0.8.237"]
                 [ring "1.5.0"]
                 [medley "0.8.2"]
                 [compojure "1.5.0" :exclusions [ring/ring-codec medley]]
                 [matthiasn/systems-toolbox "0.6.1-SNAPSHOT"]
                 [ring/ring-defaults "0.2.1"]
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]
                 [org.jboss.logging/jboss-logging "3.3.0.Final"]
                 [io.undertow/undertow-core "1.3.22.Final"]
                 [org.immutant/web "2.1.4" :exclusions [org.jboss.xnio/xnio-api
                                                        org.jboss.logging/jboss-logging
                                                        org.jboss.xnio/xnio-nio org.slf4j/slf4j-api]]]

  :profiles {:dev {:dependencies [[org.clojure/clojure "1.9.0-alpha7"]
                                  [org.clojure/clojurescript "1.9.76" :exclusions [org.clojure/tools.reader]]]}}

  :plugins [[lein-codox "0.9.5" :exclusions [org.clojure/clojure]]
            [lein-cljsbuild "1.1.3"]])
