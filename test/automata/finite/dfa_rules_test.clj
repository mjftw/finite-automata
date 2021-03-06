(ns automata.finite.dfa-rules-test
  (:require
   [clojure.test :refer :all]
   [automata.finite.dfa-rules :refer :all]))

(def  rules #{(->FARule 1 "a" 2)
              (->FARule 1 "b" 1)
              (->FARule 2 "a" 2)
              (->FARule 2 "b" 1)})

(deftest test-rule-applies?
  (let [rule (->FARule 1 "a" 2)]
    (testing "returns true"
      (is (true? (rule-applies? rule
                                (:state rule)
                                (:input rule)))))
    (testing "returns false"
      (is (false? (rule-applies? rule 9 "z"))))))

(deftest test-follow
  (let [rule (->FARule 1 "a" 2)]
    (testing "returns next state"
      (is (= (:next-state rule) (follow rule))))))

(deftest test-rule-for
  (testing "finds matching rule"
    (is (= (->FARule 2 "a" 2)
           (rule-for rules 2 "a"))))
  (testing "nil when no matching rule"
    (is (nil? (rule-for rules 9 "z"))))
  (testing "nil when no rules"
    (is (nil? (rule-for #{} 2 "a")))))

(deftest test-next-state
  (let [rule (->FARule 1 "a" 2)]
    (testing "gets correct next state when matching"
      (is (= (:next-state rule)
             (next-state rules (:state rule) (:input rule)))))
    (testing "returns nil if no matching next state"
      (is (nil? (next-state rules 9 "z"))))))

(deftest test-rules-for-state?
  (testing "returns true if rule with matching start state"
    (is (true? (rules-for-state? #{(->FARule 1 "a" 2)} 1))))
  (testing "returns true if rule with matching start state"
    (is (true? (rules-for-state? #{(->FARule 2 "a" 1)} 1))))
  (testing "returns false if no rule with matching start or end state"
    (is (false? (rules-for-state? #{(->FARule 1 "a" 2)} 3)))))

(deftest test-rules-for-input?
  (testing "returns true if rule with matching input"
    (is (true? (rules-for-input? #{(->FARule 1 "a" 2)} "a"))))
  (testing "returns false if no rule with matching input"
    (is (false? (rules-for-input? #{(->FARule 1 "a" 2)} "z")))))
