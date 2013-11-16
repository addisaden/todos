(ns todos.executor.exec-navigate
  (:use todos.status)
  (:require [todos.todo-item :as t-item])
  (:require [clojure.string :as stri]))

(defn cmd-first-
  []
  (let [ft (first (((current-todolist) :todos)))]
    (if ft
      (swap! current-stack conj ft)
      (println "Subtodos doesnt exists.")
      )))

(defn cd-up-
  []
  (swap! current-stack rest))

(defn cd-
  [& args]
  (let [joined-args (stri/join " " args)
        m (find-todo-by-name (((current-todolist) :todos)) joined-args)]
    (if m
      (swap! current-stack conj m)
      (println (format "There is no subtodo with the name \"%s\"." joined-args))
      )))

(defn cmd-nth-
  [& args]
  (let [joined-args (stri/join " " args)
        current-todo (current-todolist)
        n (read-string joined-args)]
    (if (and (integer? n) (>= (dec n) 0) (< (dec n) (count ((current-todo :todos)))))
      (swap! current-stack conj (nth ((current-todo :todos)) n))
      (println "ID doesn't exist. There are only" (count ((current-todo :todos))) "ids")
      )))

(def help
  {"navigation" {"cd .." "navigate to the parent todo"
                 "cd <str>" "navigate to todo with given name"
                 "nth <n>" "navigate to todo with position of n (1..)"
                 "first" "navigate to the first todolist"
                 }})

(defn is-cmd?
  [cmd]
  (or (= cmd "first") (= cmd "cd") (= cmd "nth")))

(defn run-cmd
  [cmd & args]
  (cond
    (= cmd "first")
    (cmd-first-)

    (and (= cmd "cd") (= (stri/join " " args) ".."))
    (cd-up-)

    (= cmd "cd")
    (apply cd- args)

    (= cmd "nth")
    (apply cmd-nth- args)
    ))
