(ns todos.executor.core
  (:use todos.status)
  (:require [todos.executor.exec-save :as exec-save])
  (:require [todos.executor.exec-create :as exec-create])
  (:require [todos.executor.exec-remove :as exec-remove])
  (:require [todos.executor.exec-manipulate :as exec-manipulate])
  (:require [todos.executor.exec-navigate :as exec-navigate])
  (:require [todos.executor.exec-show :as exec-show]))

(defn exec
  "The Executor of the todos-repl"
  [cmd & args]
  (cond
    (exec-save/is-cmd? cmd)
    (apply exec-save/run-cmd (cons cmd args))

    (exec-show/is-cmd? cmd)
    (apply exec-show/run-cmd (cons cmd args))

    (exec-create/is-cmd? cmd)
    (apply exec-create/run-cmd (cons cmd args))

    (exec-remove/is-cmd? cmd)
    (apply exec-remove/run-cmd (cons cmd args))

    (exec-manipulate/is-cmd? cmd)
    (apply exec-manipulate/run-cmd (cons cmd args))

    (exec-navigate/is-cmd? cmd)
    (apply exec-navigate/run-cmd (cons cmd args))

    (not= cmd "exit")
    (do
      (let [help-map (conj {"exit" {"exit" "quit the app"}}
                           exec-save/help
                           exec-create/help
                           exec-remove/help
                           exec-manipulate/help
                           exec-navigate/help
                           exec-show/help)
            count-length (apply max (map
                                      (fn [i]
                                        (apply max (map count (keys (help-map i)))))
                                      (keys help-map)
                                      ))]
        (doseq [topic (sort (keys help-map))]
          (println)
          (println (clojure.string/join " " (seq topic)))
          (println)
          (doseq [cmd-help (sort (keys (help-map topic)))]
            (println (format (str "%-" count-length "s - %s") cmd-help ((help-map topic) cmd-help)))
            ))))))
   
