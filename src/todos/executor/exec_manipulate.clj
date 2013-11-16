(ns todos.executor.exec-manipulate
  (:use todos.status)
  (:require [todos.input :as inp])
  (:require [todos.todo-item :as t-item])
  (:require [clojure.string :as stri]))

(defn rename-
  [& args]
  (let [joined-args (stri/join " " args)
        current-todo (current-todolist)]
    ((current-todo :rename) joined-args)
    ))

(defn set-un-done-
  [value & args]
  (let [joined-args (stri/join " " args)
        current-todo (current-todolist)]
    (if (empty? args)
      ((current-todo :set-done) value)
      (let [m (find-todo-by-name ((current-todo :todos)) joined-args)]
        (if m
          ((m :set-done) value)
          (println "Can't find this todo.")
          )))))

(defn done-
  [& args]
  (apply (partial set-un-done- true) args))

(defn undone-
  [& args]
  (apply (partial set-un-done- false) args))

(def help
  {"manipulate" {"rename <str>" "rename the current todolist"
                 "done [<str>]" "set todo (str or current) to done"
                 "undone [<str>]" "reverse of done"
                 }})

(defn is-cmd?
  [cmd]
  (or (= cmd "rename") (= cmd "done") (= cmd "undone")))

(defn run-cmd
  [cmd & args]
  (cond
    (= cmd "rename")
    (apply rename- args)

    (= cmd "done")
    (apply done- args)

    (= cmd "undone")
    (apply undone- args)
    ))
