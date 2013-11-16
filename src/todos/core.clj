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
        ; remove -> src/todos/executor/exec_remove.clj
        ;
        ; manipulation -> src/todos/executor/exec_manipulate.clj
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
        ; Start the Executor
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

