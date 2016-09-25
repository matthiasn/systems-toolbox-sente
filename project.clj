(defproject matthiasn/systems-toolbox-sente "0.6.1-alpha8"
  :description "WebSocket components for systems-toolbox"
  :url "https://github.com/matthiasn/systems-toolbox"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/cljc" "src/clj" "src/cljs"]

  :dependencies [[org.clojure/tools.logging "0.3.1"]
                 [org.clojure/tools.reader "1.0.0-beta3"]
                 [com.taoensso/timbre "4.7.3"
                  :exclusions [io.aviso/pretty]]
                 [com.taoensso/sente "1.10.0"
                  :exclusions [org.clojure/core.async]]
                 [org.immutant/web "2.1.5" :exclusions [ring/ring-core]]
                 [org.clojure/core.match "0.3.0-alpha4"
                  :exclusions [org.clojure/core.memoize
                               org.clojure/tools.analyzer.jvm]]
                 [com.cognitect/transit-clj "0.8.288"]
                 [com.cognitect/transit-cljs "0.8.239"]
                 [ring "1.5.0"]
                 [compojure "1.5.1"]
                 [matthiasn/systems-toolbox "0.6.1-alpha7"]
                 [ring/ring-defaults "0.2.1"]
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]
                 [org.jboss.logging/jboss-logging "3.3.0.Final"]]

  :profiles {:dev {:dependencies [[org.clojure/clojure "1.9.0-alpha12"]
                                  [org.clojure/clojurescript "1.9.229"
                                   :exclusions [org.clojure/tools.reader]]]}}

  :plugins [[lein-codox "0.10.0" :exclusions [org.clojure/clojure]]
            [lein-cljsbuild "1.1.4"]])
