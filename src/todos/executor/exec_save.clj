(ns todos.executor.exec-save
  (:use todos.status)
  (:require [todos.storage :as storage]))

(defn save-
  []
  (storage/save-data ((todolist :plain))))

(def help
  {"save" {"save" "save the complete todolist"
           }})

(defn is-cmd?
  [cmd]
  (= cmd "save"))

(defn run-cmd
  [cmd & args]
  (cond
    (= cmd "save")
    (save-)
    ))
