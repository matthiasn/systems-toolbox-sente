(defproject matthiasn/systems-toolbox-sente "0.6.15"
  :description "WebSocket components for systems-toolbox"
  :url "https://github.com/matthiasn/systems-toolbox"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/cljc" "src/clj" "src/cljs"]

  :dependencies [[org.clojure/tools.logging "0.3.1"]
                 [com.taoensso/sente "1.11.0" :exclusions [org.clojure/core.async]]
                 [org.immutant/web "2.1.6" :exclusions [ring/ring-core]]
                 [org.clojure/core.match "0.3.0-alpha4"
                  :exclusions [org.clojure/core.memoize
                               org.clojure/tools.analyzer.jvm]]
                 [com.cognitect/transit-clj "0.8.300"]
                 [com.cognitect/transit-cljs "0.8.239"]
                 [ring "1.5.1"]
                 [compojure "1.5.2"]
                 [matthiasn/systems-toolbox "0.6.7"]
                 [ring/ring-defaults "0.2.3"]
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]
                 [org.jboss.logging/jboss-logging "3.3.1.Final"]]

  :profiles
  {:dev {:dependencies   [[org.clojure/clojure "1.9.0-alpha15"]
                          [org.clojure/clojurescript "1.9.521"]
                          [re-frame "0.9.2"]
                          [ch.qos.logback/logback-classic "1.2.3"]
                          [hiccup "1.0.5"]
                          [org.seleniumhq.selenium/selenium-java "3.3.1"
                           :exclusions [org.seleniumhq.selenium/selenium-support]]
                          [org.seleniumhq.selenium/selenium-api "3.3.1"]
                          [org.seleniumhq.selenium/selenium-server "3.3.1"
                           :exclusions [org.seleniumhq.selenium/selenium-support]]
                          [org.seleniumhq.selenium/selenium-support "3.3.1"]
                          [org.seleniumhq.selenium/selenium-remote-driver "3.3.1"]
                          [org.seleniumhq.selenium/selenium-chrome-driver "3.3.1"]
                          [com.codeborne/phantomjsdriver "1.4.2"
                           :exclusions [org.apache.httpcomponents/httpcore]]
                          [clj-webdriver "0.7.2"
                           :exclusions [org.clojure/core.cache
                                        org.apache.httpcomponents/httpcore
                                        commons-io]]]
         :resource-paths ["test-resources"]
         :jvm-opts       ["-Dwebdriver.chrome.driver=bin/chromedriver"
                          "-Dwebdriver.gecko.driver=bin/geckodriver"]}}

  :test-paths ["test/cljc" "test/clj"]

  :plugins [[lein-codox "0.10.3" :exclusions [org.clojure/clojure]]
            [test2junit "1.2.5"]
            [lein-cljsbuild "1.1.5"]]

  :test2junit-output-dir ~(or (System/getenv "CIRCLE_TEST_REPORTS") "target/test2junit")

  :clean-targets ^{:protect false} ["test-resources/public/js/build/" "target/"]

  :aliases
  {"integration-tests" ["do" "clean" ["cljsbuild" "once" "test"] "test2junit"]
   "test-coverage" ["do" "clean" ["cljsbuild" "once" "test"] "cloverage"]}

  :cljsbuild
  {:builds [{:id           "test"
             :source-paths ["src/cljs" "test/cljc" "test/cljs"]
             :figwheel     true
             :compiler     {:main          "matthiasn.systems-toolbox-sente.test-core"
                            :asset-path    "js/build"
                            :optimizations :advanced
                            :output-to     "test-resources/public/js/build/example.js"}}]})
