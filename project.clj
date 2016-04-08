(defproject matthiasn/systems-toolbox-sente "0.5.14"
  :description "WebSocket components for systems-toolbox"
  :url "https://github.com/matthiasn/systems-toolbox"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/cljc" "src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.reader "1.0.0-beta1"]
                 [org.clojure/clojurescript "1.8.40"]
                 [org.ow2.asm/asm-all "5.1"]
                 [org.clojure/core.async "0.2.374" :exclusions [org.clojure/tools.reader]]
                 [org.clojure/tools.logging "0.3.1"]
                 [io.aviso/pretty "0.1.25"]
                 [com.taoensso/sente "1.8.1"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [com.cognitect/transit-clj "0.8.285"]
                 [com.cognitect/transit-cljs "0.8.237"]
                 [compojure "1.5.0"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.2.0"]
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]
                 [org.jboss.logging/jboss-logging "3.3.0.Final"]
                 [io.undertow/undertow-core "2.0.0.Alpha1"]
                 [org.immutant/web "2.1.3" :exclusions [org.jboss.xnio/xnio-api
                                                        org.jboss.logging/jboss-logging
                                                        org.jboss.xnio/xnio-nio org.slf4j/slf4j-api]]]

  :plugins [[lein-codox "0.9.4" :exclusions [org.clojure/clojure]]
            [lein-cljsbuild "1.1.3"]])
