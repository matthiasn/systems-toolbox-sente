(defproject matthiasn/systems-toolbox-sente "0.5.19"
  :description "WebSocket components for systems-toolbox"
  :url "https://github.com/matthiasn/systems-toolbox"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/cljc" "src/clj" "src/cljs"]

  :dependencies [[org.clojure/tools.logging "0.3.1"]
                 [com.taoensso/sente "1.9.0" :exclusions [org.clojure/tools.reader]]
                 [org.clojure/core.match "0.3.0-alpha4" :exclusions [org.clojure/core.memoize
                                                                     org.clojure/tools.analyzer.jvm]]

                 [com.cognitect/transit-clj "0.8.285"]
                 [com.cognitect/transit-cljs "0.8.239"]
                 [compojure "1.5.0"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.2.0"]
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]
                 [org.jboss.logging/jboss-logging "3.3.0.Final"]
                 [io.undertow/undertow-core "1.3.22.Final"]
                 [org.immutant/web "2.1.4" :exclusions [org.jboss.xnio/xnio-api
                                                        org.jboss.logging/jboss-logging
                                                        org.jboss.xnio/xnio-nio org.slf4j/slf4j-api]]]

  :profiles {:dev {:dependencies [[org.clojure/clojure "1.9.0-alpha3"]
                                  [org.clojure/clojurescript "1.9.36"]]}}

  :plugins [[lein-codox "0.9.5" :exclusions [org.clojure/clojure]]
            [lein-cljsbuild "1.1.3"]])
