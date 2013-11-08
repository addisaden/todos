(ns todos.core
  (:gen-class)
  (:require [todos.storage :as storage]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (storage/load-data [])))
