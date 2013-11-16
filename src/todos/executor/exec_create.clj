(ns todos.executor.exec-create
  (:use todos.status)
  (:require [todos.todo-item :as t-item])
  (:require [clojure.string :as stri]))

(defn create-
  [& args]
  (let [joined-args (stri/join " " args)]
    (((current-todolist) :todo-add) (t-item/todo-create joined-args))
    ))

(defn note-
  [& args]
  (let [joined-args (stri/join " " args)]
    (((current-todolist) :note-add) joined-args)
    ))

(def help
  {"create" {"create <str>" "create a new todo on the current node"
             "note <str>"   "create a note for current todolist"
             }})

(defn is-cmd?
  [cmd]
  (or (= cmd "create") (= cmd "note")))

(defn run-cmd
  [cmd & args]
  (cond
    (= cmd "create")
    (apply create- args)

    (= cmd "note")
    (apply note- args)
    ))
