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

(def todolist
  (t-item/todo-load-from-plain
    (storage/load-data
      (((t-item/todo-create "< t o d o s >") :plain)) ; if file cant be read this is the default.
      )))

(def current-stack (atom '() )) ; this have the subpath of opened todos. (subpath in todolist)

(defn command-repl
  []
  (do
    (doseq [i @current-stack]
      (println (format "> %s <" ((i :name)) )))
    (let [[cmd & args] (inp/user-cmd-input ">> ")]
      (cond
        (= cmd "ls")
        (( ;evaluate the print of current todolist
          (or (first @current-stack) todolist)
          :print) "")
        )
      ; exit clause
      (if (not= cmd "exit")
        (recur))
      )))

(defn -main
  "Start the demo ..."
  [& args]
  (command-repl))

