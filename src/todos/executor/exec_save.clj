(ns todos.executor.exec-save
  (:use todos.status)
  (:require [todos.storage :as storage]))

(defn save-
  []
  (storage/save-data ((todolist :plain))))

(defn is-cmd?
  [cmd]
  (= cmd "save"))

(defn run-cmd
  [cmd & args]
  (cond
    (= cmd "save")
    (save-)
    ))
