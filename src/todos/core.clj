(ns todos.core
  (:gen-class)
  (:require [todos.input :as inp])
  (:require [todos.todo-item :as t-item])
  (:require [todos.storage :as storage]))

(defn demo
  "This is currently just a demo!"
  []
  (let
    [todolist (t-item/todo-load-from-plain (storage/load-data
                                             (((t-item/todo-create "main") :plain))
                                             ))
     sub-a    (t-item/todo-create (inp/user-str-input "Beispielname für SubTodo-A: "))
     sub-b    (t-item/todo-create (inp/user-str-input "Beispielname für SubTodo-B: "))]
    ((todolist :note-add) (inp/user-str-input "Beispielnotiz: "))
    ((todolist :todo-add) sub-a)
    ((todolist :todo-add) sub-b)
    ((sub-a :set-done) true)
    ((sub-b :note-add) "arbeit arbeit")
    ((todolist :print) "")
    (println ((todolist :plain)))
    (storage/save-data ((todolist :plain)))
    ))

(defn -main
  "Start the demo ..."
  [& args]
  (demo))

