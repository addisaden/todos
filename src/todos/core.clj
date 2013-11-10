(ns todos.core
  (:gen-class)
  (:require [todos.input :as inp])
  (:require [todos.todo-item :as t-item])
  (:require [todos.storage :as storage])
  (:require [clojure.string :as stri]))

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
      (((t-item/todo-create "todos") :plain)) ; if file cant be read this is the default.
      )))

(def current-stack (atom '() )) ; this have the subpath of opened todos. (subpath in todolist)

(defn command-repl
  []
  (do
    (println)
    (doseq [i @current-stack]
      (print (format "%s <- " ((i :name)) )))
    (println ((todolist :name)))
    (let [[cmd & args] (inp/user-cmd-input ">> ")
          current-todo (or (first @current-stack) todolist)
          joined-args  (stri/join " " args)]
      (cond
        (= cmd "ls")
        ((current-todo :print) "")
        ;
        (= cmd "create")
        ((current-todo :todo-add) (t-item/todo-create joined-args))
        ;
        (= cmd "note")
        ((current-todo :note-add) joined-args)
        ;
        (= cmd "plain")
        (println ((current-todo :plain)))
        ;
        (= cmd "first")
        (let [ft (first ((current-todo :todos)))]
          (if ft
            (swap! current-stack conj ft)
            (println "Es existieren keine Subtodos.")
            ))
        ;
        (and (= cmd "cd") (= joined-args ".."))
        (swap! current-stack rest)
        ;
        (= cmd "cd")
        (loop [todos ((current-todo :todos))]
          (if (= (((first todos) :name)) joined-args)
            (swap! current-stack conj (first todos))
            (recur (rest todos))
            ))
        )
      ; exit clause
      (if (not= cmd "exit")
        (recur))
      )))

(defn -main
  "Start the demo ..."
  [& args]
  (command-repl))

