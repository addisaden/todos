(ns todos.executor.exec-show
  (:use todos.status)
  (:require [clojure.string :as stri]))

(defn current-todo- [] (or (first @current-stack) todolist))

(defn ls-
  [k]
  (do
    (doseq [n (((current-todo-) :notes))] (println (format "- %s" n)))
    (cond
      (= k :print)
      (doseq [t (((current-todo-) :todos))] ((t k) ""))

      (= k :status)
      (doseq [t (((current-todo-) :todos))] ((t k)))
      )))

(defn plain-
  []
  (do (println (((current-todo-) :plain)))))

(defn is-cmd?
  [cmd]
  (or (= cmd "ls") (= cmd "plain")))

(defn run-cmd
  [cmd & args]
  (let [joined-args (stri/join " " args)]
    (cond
      (and (= cmd "ls") (= joined-args "-a"))
      (ls- :print)

      (= cmd "ls")
      (ls- :status)

      (= cmd "plain")
      (plain-)
      )))

