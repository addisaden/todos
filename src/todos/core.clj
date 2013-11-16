(ns todos.core
  (:gen-class)
  (:use todos.status)
  (:require [todos.input :as inp])
  (:require [todos.todo-item :as t-item])
  (:require [todos.storage :as storage])
  (:require [clojure.string :as stri])
  (:require [todos.executor.core :as t-core]))


(defn command-repl
  []
  (do
    (println)
    (doseq [i @current-stack]
      (print (format "[%s] %s <- " (if ((i :done?)) "x" " ") ((i :name)) )))
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
        ; save -> src/todos/executor/exec_save.clj
        ;
        ; show -> src/todos/executor/exec_show.clj
        ;
        ; create -> src/todos/executor/exec_create.clj
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
        (= cmd "rename")
        ((current-todo :rename) joined-args)
        ;
        (= cmd "done")
        (if (empty? args)
          ((current-todo :set-done) true)
          (let [m (find-by-name ((current-todo :todos)) joined-args)]
            (if m
              ((m :set-done) true)
              (println "Cant find this todo.")
              )))
        ;
        (= cmd "undone")
        (if (empty? args)
          ((current-todo :set-done) false)
          (let [m (find-by-name ((current-todo :todos)) joined-args)]
            (if m
              ((m :set-done) false)
              (println "Cant find this todo.")
              )))
        ;
        ; navigation
        ;
        (= cmd "first")
        (let [ft (first ((current-todo :todos)))]
          (if ft
            (swap! current-stack conj ft)
            (println "Subtodos doesnt exists.")
            ))
        ;
        (and (= cmd "cd") (= joined-args ".."))
        (swap! current-stack rest)
        ;
        (= cmd "cd")
        (let [m (find-by-name ((current-todo :todos)) joined-args)]
          (if m
            (swap! current-stack conj m)
            (println (format "There is no subtodo with the name \"%s\"." joined-args))
            ))
        ;
        (= cmd "nth")
        (let [n (- (read-string joined-args) 1)]
          (if (and (integer? n) (>= n 0) (< n (count ((current-todo :todos)))))
            (swap! current-stack conj (nth ((current-todo :todos)) n))
            (println "Id doesnt exist. There are only" (count ((current-todo :todos))) "ids")
            ))
        ;
        ; help
        ;
        (not= cmd "exit")
        (apply t-core/exec (cons cmd args)))
      ; exit clause
      (if (not= cmd "exit")
        (recur))
      )))

(defn -main
  "Start the demo ..."
  [& args]
  (do
    (println)
    (println "+++++  +++  ++++   +++   ++++")
    (println "  +   +   +  +  + +   + +")
    (println "  +   +   +  +  + +   +  +++")
    (println "  +   +   +  +  + +   +     +")
    (println "  +    +++  ++++   +++  ++++")
    (command-repl)
    (storage/save-data ((todolist :plain)))
    ))

