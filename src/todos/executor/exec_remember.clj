(ns todos.executor.exec-remember
  (:use todos.status)
  (:require [clojure.string :as stri]))

(def remembered (atom {}))

(defn cmd-rem-
  [& args]
  (let [joined-args (stri/join " " args)]
    (swap! remembered conj {joined-args @current-stack})
    ))

(defn cmd-rem-ls-
  []
  (doseq [k (sort (keys @remembered))]
    (if (empty? (@remembered k))
      (println (format "%s -> %s" k ((todolist :name))))
      (println (format "%s -> %s" k (((first (@remembered k)) :name)) ))
      )))

(defn cmd-go-
  [& args]
  (let [joined-args (stri/join " " args)
        loaded-rem (@remembered joined-args)]
    (if (nil? loaded-rem)
      (println "this is not remembered: get the full list with rem-ls")
      (reset! current-stack loaded-rem)
      )))

(def help {"remember" {"rem <str>" "remember the current navigation"
                       "rem-ls"    "list the remembered todos"
                       "go <str>"  "go to the remembered position"
                       }})

(defn is-cmd?
  [cmd]
  (or (= cmd "rem") (= cmd "rem-ls") (= cmd "go")))


(defn run-cmd
  [cmd & args]
  (cond
    (= cmd "rem")
    (apply cmd-rem- args)

    (= cmd "rem-ls")
    (cmd-rem-ls-)

    (= cmd "go")
    (apply cmd-go- args)
    ))
