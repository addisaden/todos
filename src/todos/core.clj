(ns todos.core
  (:gen-class)
  (:require [todos.input :as inp])
  (:require [todos.todo-item :as t-item])
  (:require [todos.storage :as storage])
  (:require [clojure.string :as stri]))

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
          joined-args  (stri/join " " args)
          find-by-name (fn [todos-seq compare-name]
                         (loop [todos todos-seq]
                           (cond
                             (empty? todos)
                             nil
                             (= (((first todos) :name)) compare-name)
                             (first todos)
                             :else
                             (recur (rest todos))
                             )))
          ]
      (cond
        ;
        ; show
        ;
        (= cmd "ls")
        ((current-todo :print) "")
        ;
        (= cmd "plain")
        (println ((current-todo :plain)))
        ;
        ; create
        ;
        (= cmd "create")
        ((current-todo :todo-add) (t-item/todo-create joined-args))
        ;
        (= cmd "note")
        ((current-todo :note-add) joined-args)
        ;
        ; remove
        ;
        (= cmd "remove")
        (let [m (find-by-name ((current-todo :todos)) joined-args)]
          (if (and m (= "yes" (inp/user-str-input
                                (format "Are you sure to delete \"%s\"? (yes/no) " ((m :name)))
                                )))
            ((current-todo :todo-rm) m)
            ))
        ;
        (= cmd "rm-note")
        ((current-todo :note-rm) joined-args)
        ;
        ; manipulation
        ;
        (= cmd "done")
        (if (empty? args)
          ((current-todo :set-done) true)
          (let [m (find-by-name ((current-todo :todos)) joined-args)]
            (if m
              ((m :set-done) true)
              (println "Konnte Todo nicht finden")
              )))
        ;
        (= cmd "undone")
        (if (empty? args)
          ((current-todo :set-done) false)
          (let [m (find-by-name ((current-todo :todos)) joined-args)]
            (if m
              ((m :set-done) false)
              (println "Konnte Todo nicht finden")
              )))
        ;
        ; navigation
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
        (let [m (find-by-name ((current-todo :todos)) joined-args)]
          (if m
            (swap! current-stack conj m)
            (println (format "Es existiert kein Subtodo namens \"%s\"." joined-args))
            ))
        ;
        (= cmd "nth")
        (let [n (- (read-string joined-args) 1)]
          (if (and (integer? n) (>= n 0) (< n (count ((current-todo :todos)))))
            (swap! current-stack conj (nth ((current-todo :todos)) n))
            (println "Id ist ungültig. Es gibt nur" (count ((current-todo :todos))))
            ))
        )
      ; exit clause
      (if (not= cmd "exit")
        (recur))
      )))

(defn -main
  "Start the demo ..."
  [& args]
  (do
    (command-repl)
    (storage/save-data ((todolist :plain)))
    ))

