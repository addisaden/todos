(ns todos.executor.core
  (:use todos.status)
  (:require [todos.executor.exec-save :as exec-save])
  (:require [todos.executor.exec-show :as exec-show]))

(defn exec
  "The Executor of the todos-repl"
  [cmd & args]
  (cond
    (exec-save/is-cmd? cmd)
    (apply exec-save/run-cmd (cons cmd args))

    (exec-show/is-cmd? cmd)
    (apply exec-show/run-cmd (cons cmd args))

    (not= cmd "exit")
    (do
      (println ((todolist :plain)))
      (println "commands:")
      (println)
      (println "save           - save the current todolist")
      (println "exit           - quit the app")
      (println)
      (println "s h o w")
      (println)
      (println "ls             - list the todos (on current navigation)")
      (println "ls -a          - list the todos recursively")
      (println "plain          - shows the raw data")
      (println)
      (println "c r e a t e")
      (println)
      (println "create <str>   - create a new todo on the current todo")
      (println "note <str>     - create a note on current todo")
      (println)
      (println "r e m o v e")
      (println)
      (println "remove <str>   - remove todolist with the name of str")
      (println "rm-note <str>  - remove the note with the content of str")
      (println)
      (println "m a n i p u l a t i o n")
      (println)
      (println "rename <str>   - rename the current todo")
      (println "done [<str>]   - set todo (str or current) to done")
      (println "undone [<str>] - reverse of done")
      (println)
      (println "n a v i g a t i o n")
      (println)
      (println "cd ..          - navigate to the parent todo")
      (println "cd <str>       - navigate to todo with given name")
      (println "nth <n>        - navigate to todo with position of n (1..)")
      )))
   
