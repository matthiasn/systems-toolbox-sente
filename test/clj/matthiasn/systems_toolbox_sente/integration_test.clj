(ns matthiasn.systems-toolbox-sente.integration-test
  (:require [clojure.test :refer :all]
            [matthiasn.systems-toolbox-sente.test-server :as ts]
            [matthiasn.systems-toolbox-sente.test-store :as st]
            [clj-webdriver.taxi :as tx]
            [clojure.string :as s]))

(deftest open-page
  (testing "open counter example, interact, UI should change"
    (testing "click first counter thrice, should be 5"
      (tx/wait-until
        #(tx/exists? ".counters div:nth-of-type(1) button:nth-of-type(2)"))
      (dotimes [_ 10]
        (tx/click ".counters div:nth-of-type(1) button:nth-of-type(2)"))
      (tx/wait-until
        #(= "12" (tx/text (tx/find-element
                           {:css ".counters div:nth-of-type(1) h1"}))))
      (is (= "12" (tx/text (tx/find-element
                            {:css ".counters div:nth-of-type(1) h1"}))))
      (dotimes [_ 5]
        (tx/click ".counters div:nth-of-type(1) button:nth-of-type(1)"))
      (tx/wait-until
        #(= "7" (tx/text (tx/find-element
                           {:css ".counters div:nth-of-type(1) h1"}))))
      (is (= "7" (tx/text (tx/find-element
                            {:css ".counters div:nth-of-type(1) h1"})))))

    (testing "add counter, click 100 times, should be 100"
      (tx/wait-until #(tx/exists? ".counters button:nth-of-type(2)"))
      (tx/click ".counters button:nth-of-type(2)")
      (tx/wait-until
        #(tx/exists? ".counters div:nth-of-type(4) button:nth-of-type(2)"))
      (dotimes [_ 100]
        (tx/click ".counters div:nth-of-type(4) button:nth-of-type(2)"))
      (tx/wait-until
        #(= "100" (tx/text (tx/find-element
                           {:css ".counters div:nth-of-type(4) h1"}))))
      (is (= "100" (tx/text (tx/find-element
                            {:css ".counters div:nth-of-type(4) h1"}))))
      (dotimes [_ 50]
        (tx/click ".counters div:nth-of-type(4) button:nth-of-type(1)"))
      (tx/wait-until
        #(= "50" (tx/text (tx/find-element
                             {:css ".counters div:nth-of-type(4) h1"}))))
      (is (= "50" (tx/text (tx/find-element
                              {:css ".counters div:nth-of-type(4) h1"})))))

    (testing "fourth counter exists, click remove, should be gone"
      (tx/wait-until #(tx/exists? ".counters div:nth-of-type(4)"))
      (is (tx/find-element {:css ".counters div:nth-of-type(4)"}))
      (tx/click ".counters button:nth-of-type(1)")
      (tx/wait-until #(not (tx/exists? ".counters div:nth-of-type(4)")))
      (is (not (tx/find-element {:css ".counters div:nth-of-type(4)"}))))

    (testing "Backend store component has received all messages from frontend."
      (is (= @st/state
             {:cnt/inc 110, :cnt/dec 55, :cnt/add 1, :cnt/remove 1})))))

(deftest broadcast-test
  (testing "broadcast message is relayed to connected client"
    (ts/send-to-ws-cmp (with-meta [:broadcast/msg "broadcast message"]
                                  {:sente-uid :broadcast}))
    (let [pre-code-text #(tx/text (tx/find-element {:css "pre code"}))]
      (tx/wait-until #(s/includes? (pre-code-text) "broadcast message"))
      (is (s/includes? (pre-code-text) "broadcast message")))))

(use-fixtures :once ts/once-fixture)
