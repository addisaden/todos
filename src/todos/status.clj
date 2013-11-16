(ns todos.status
  (:require [todos.todo-item :as t-item])
  (:require [todos.storage :as storage]))

(def todolist
  (t-item/todo-load-from-plain
    (storage/load-data
      (((t-item/todo-create "todos") :plain)) ; if file cant be read this is the default.
      )))

(def current-stack (atom '() )) ; this have the subpath of opened todos. (subpath in todolist)

(defn current-todolist [] (or (first @current-stack) todolist))

(defn find-todo-by-name
  [todos-seq compare-name]
  (loop [todos todos-seq]
    (cond
      (empty? todos)
      nil
      (= (((first todos) :name)) compare-name)
      (first todos)
      :else
      (recur (rest todos))
      )))

