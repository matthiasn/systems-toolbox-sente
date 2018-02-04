(defproject matthiasn/systems-toolbox-sente "0.6.22"
  :description "WebSocket components for systems-toolbox"
  :url "https://github.com/matthiasn/systems-toolbox"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/cljc" "src/clj" "src/cljs"]

  :dependencies
  [[org.clojure/tools.logging "0.4.0"]
   [com.taoensso/sente "1.12.0" :exclusions [org.clojure/core.async
                                             org.clojure/tools.reader]]
   [org.immutant/web "2.1.10" :exclusions [ring/ring-core]]
   [org.clojure/core.match "0.3.0-alpha4" :exclusions [org.clojure/tools.analyzer.jvm
                                                       org.clojure/core.memoize]]
   [com.cognitect/transit-clj "0.8.300"]
   [com.cognitect/transit-cljs "0.8.243"]
   [ring "1.6.3"]
   [compojure "1.6.0"]
   [ring/ring-defaults "0.3.1"]
   [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]
   [org.jboss.logging/jboss-logging "3.3.1.Final"]]

  :profiles
  {:dev {:dependencies   [[org.clojure/clojure "1.9.0"]
                          [org.clojure/clojurescript "1.9.946"]
                          [re-frame "0.10.4" :exclusions [org.clojure/tools.reader]]
                          [matthiasn/systems-toolbox "0.6.32"
                           :exclusions [org.clojure/tools.reader]]
                          [ch.qos.logback/logback-classic "1.2.3"]
                          [hiccup "1.0.5"]
                          [org.seleniumhq.selenium/selenium-java "3.8.1"
                           :exclusions [org.seleniumhq.selenium/selenium-support
                                        org.seleniumhq.selenium/selenium-api
                                        org.seleniumhq.selenium/selenium-support]]
                          [org.seleniumhq.selenium/selenium-api "3.8.1"]
                          [org.seleniumhq.selenium/selenium-server "3.8.1"
                           :exclusions [org.seleniumhq.selenium/selenium-support
                                        org.seleniumhq.selenium/selenium-api]]
                          [org.seleniumhq.selenium/selenium-support "3.8.1"]
                          [org.seleniumhq.selenium/selenium-remote-driver "3.8.1"]
                          [org.seleniumhq.selenium/selenium-chrome-driver "3.8.1"]
                          [com.codeborne/phantomjsdriver "1.4.4"
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
            [test2junit "1.3.3"]
            [lein-cljsbuild "1.1.7"]]

  :test2junit-output-dir ~(or (System/getenv "CIRCLE_TEST_REPORTS") "target/test2junit")

  :clean-targets ^{:protect false} ["test-resources/public/js/build/" "target/"]

  :aliases
  {"integration-tests" ["do" "clean" ["cljsbuild" "once" "test"] "test2junit"]
   "test-coverage"     ["do" "clean" ["cljsbuild" "once" "test"] "cloverage"]}

  :cljsbuild
  {:builds [{:id           "test"
             :source-paths ["src/cljs" "test/cljc" "test/cljs"]
             :figwheel     true
             :compiler     {:main          "matthiasn.systems-toolbox-sente.test-core"
                            :asset-path    "js/build"
                            :optimizations :advanced
                            :output-to     "test-resources/public/js/build/example.js"}}]})
