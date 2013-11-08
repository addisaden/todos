(ns todos.input
  (:require [clojure.string :as stri]))

(defn print-pre-
  [pre-string]
  (do (print pre-string)
      (flush)
      ))

(defn user-str-input
  [pre-string]
  (do (print-pre- pre-string)
      (stri/trim (read-line))
      ))

(defn user-cmd-input
  [pre-string]
  (stri/split (user-str-input pre-string) #"\s+"))
