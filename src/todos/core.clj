(ns todos.core
  (:gen-class)
  (:require [todos.todo-item :as t-item])
  (:require [todos.storage :as storage]))

(defn demo
  "This is currently just a demo!"
  []
  (let
    [todolist (t-item/todo-load-from-plain (storage/load-data
                                             (((t-item/todo-create "main") :plain))
                                             ))
     sub-a    (t-item/todo-create "Privat")
     sub-b    (t-item/todo-create "Arbeit")]
    ((todolist :note-add) "Allgemeine Notizen")
    ((todolist :todo-add) sub-a)
    ((todolist :todo-add) sub-b)
    ((sub-a :set-done) true)
    ((sub-b :note-add) "arbeit arbeit")
    ((todolist :print) "")
    (println ((todolist :plain)))
    ))

(defn -main
  "Start the demo ..."
  [& args]
  (demo))

