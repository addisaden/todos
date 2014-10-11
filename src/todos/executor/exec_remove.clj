(ns todos.executor.exec-remove
  (:use todos.status)
  (:require [todos.input :as inp])
  (:require [todos.todo-item :as t-item])
  (:require [clojure.string :as stri]))

(defn remove-this-
  []
  (if (< (count @current-stack) 1)
    (println "You cant remove root")
    (let [current-todo (current-todolist)
          current-name ((current-todo :name))]
      (println current-name)
      )))

(defn remove-
  [joined-args]
  (let [current-todo (current-todolist)
        m (find-todo-by-name ((current-todo :todos)) joined-args)]
    (if (and m (= "yes" (inp/user-str-input
                          (format "Are you sure to delete \"%s\"? (yes/no) " ((m :name)))
                          )))
      ((current-todo :todo-rm) m)
      )))

(defn rm-note-
  [joined-args]
  (((current-todolist) :note-rm) joined-args))

(def help
  {"remove" {"remove <str>" "remove todolist with the name of str"
             "remove!" "remove current todolist if its not root"
             "rm-note <str>"   "remove the note with the content of str"
             }})

(defn is-cmd?
  [cmd]
  (or (= cmd "remove") (= cmd "remove!") (= cmd "rm-note")))

(defn run-cmd
  [cmd & args]
  (let
    [joined-args (stri/join " " args)]
    (cond
      (= cmd "remove!")
      (remove-this-)

      (= cmd "remove")
      (remove- joined-args)

      (= cmd "rm-note")
      (rm-note- joined-args)
      )))

