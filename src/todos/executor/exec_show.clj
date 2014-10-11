(ns todos.executor.exec-show
  (:use todos.status)
  (:require [clojure.string :as stri]))

(defn ls-
  [k]
  (let [current-todo (current-todolist)]
    (do
      (doseq [n ((current-todo :notes))] (println (format "- %s" n)))
      (cond
        (= k :print)
        (doseq [t ((current-todo :todos))] ((t k) ""))

        (= k :idstatus)
        (loop [t ((current-todo :todos)) i 1]
               (((first t) k) i)
               (if (not (empty? (rest t)))
                 (recur (rest t) (inc i))
                 ))

        (= k :status)
        (doseq [t ((current-todo :todos))] ((t k)))
        ))))

(defn plain-
  []
  (do (println (((current-todolist) :plain)))))

(def help
  {"show" {"ls"    "list the todos (on current navigation)"
           "ls -a" "list the todos recursively"
           "ls -l" "list the todos with id"
           "plain" "shows the raw data"
           }})

(defn is-cmd?
  [cmd]
  (or (= cmd "ls") (= cmd "plain")))

(defn run-cmd
  [cmd & args]
  (let [joined-args (stri/join " " args)]
    (cond
      (and (= cmd "ls") (= joined-args "-a"))
      (ls- :print)

      (and (= cmd "ls") (= joined-args "-l"))
      (ls- :idstatus)

      (= cmd "ls")
      (ls- :status)

      (= cmd "plain")
      (plain-)
      )))

