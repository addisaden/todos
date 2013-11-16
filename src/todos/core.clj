(ns todos.core
  (:gen-class)
  (:use todos.status)
  (:require [todos.input :as inp])
  (:require [todos.storage :as storage])
  (:require [todos.executor.core :as t-core]))


(defn command-repl
  []
  (do
    (println)
    (doseq [i @current-stack]
      (print (format "[%s] %s <- " (if ((i :done?)) "x" " ") ((i :name)) )))
    (println ((todolist :name)))
    (let [[cmd & args] (inp/user-cmd-input ">> ")]
      ;
      ; save -> src/todos/executor/exec_save.clj
      ; show -> src/todos/executor/exec_show.clj
      ; create -> src/todos/executor/exec_create.clj
      ; remove -> src/todos/executor/exec_remove.clj
      ; manipulation -> src/todos/executor/exec_manipulate.clj
      ; navigation -> src/todos/executor/exec_navigate.clj
      ;
      ; Start the Executor
      ;
      (apply t-core/exec (cons cmd args))
      ;
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

