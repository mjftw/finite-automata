(ns finite-automata.rules-test
  (:require
   [clojure.test :refer :all]
   [finite-automata.rules :refer :all]))

(def dfa-rules [(->FARule 1 "a" 2)
                (->FARule 1 "b" 1)
                (->FARule 2 "a" 2)
                (->FARule 2 "b" 1)])

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
           (rule-for dfa-rules 2 "a"))))
  (testing "nil when no matching rule"
    (is (nil? (rule-for dfa-rules 9 "z"))))
  (testing "nil when no rules"
    (is (nil? (rule-for [] 2 "a")))))

(deftest test-next-state
  (let [rule (->FARule 1 "a" 2)]
    (testing "gets correct next state when matching"
      (is (= (:next-state rule)
             (next-state dfa-rules (:state rule) (:input rule)))))
    (testing "returns nil if no matching next state"
      (is (nil? (next-state dfa-rules 9 "z"))))))

(def nfa-rules [(->FARule 1 "a" 1)
                (->FARule 1 "b" 1)
                (->FARule 1 "b" 2)
                (->FARule 2 "a" 3)
                (->FARule 2 "b" 3)])

(deftest test-next-states
  (testing "correct next state when only one possibility"
    (is (= [1] (next-states nfa-rules [1] "a"))))
  (testing "correct next state when multiple possibilities"
    (is (= [1 2] (next-states nfa-rules [1] "b"))))
  (testing "correct next state when multiple start states"
    (is (= [1 3] (next-states nfa-rules [1 2] "a")))))