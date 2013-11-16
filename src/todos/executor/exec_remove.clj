(ns todos.executor.exec-remove
  (:use todos.status)
  (:require [todos.input :as inp])
  (:require [todos.todo-item :as t-item])
  (:require [clojure.string :as stri]))

(defn remove-
  [& args]
  (let [current-todo (current-todolist)
        joined-args (stri/join " " args)
        m (find-todo-by-name ((current-todo :todos)) joined-args)]
    (if (and m (= "yes" (inp/user-str-input
                          (format "Are you sure to delete \"%s\"? (yes/no) " ((m :name)))
                          )))
      ((current-todo :todo-rm) m)
      )))

(defn rm-note-
  [& args]
  (((current-todolist) :note-rm) (stri/join " " args)))

(def help
  {"remove" {"remove <str>" "remove todolist with the name of str"
             "rm-note <str>"   "remove the note with the content of str"
             }})

(defn is-cmd?
  [cmd]
  (or (= cmd "remove") (= cmd "rm-note")))

(defn run-cmd
  [cmd & args]
  (cond
    (= cmd "remove")
    (apply remove- args)

    (= cmd "rm-note")
    (apply rm-note- args)
    ))

