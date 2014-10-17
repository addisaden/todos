(ns todos.storage)

(def data-file-name "todos.clj.data")

(defn load-data
  "Load data from data-file-name."
  [default-data]
  (try (read-string
         (clojure.string/replace
           (slurp data-file-name)
           #"\s+"
           " "
           ))
       (catch Exception e
         (do (println (format "Error on load-data: %s" (.getMessage e)))
             default-data
             ))))

(defn save-data
  "Save data to data-file-name."
  [data-to-save]
  (try (do
         (spit data-file-name 
               (clojure.string/replace
                 (str data-to-save)
                 #"\s+"
                 "\n"
                 ))
         (println "data saved.")
         true)
       (catch Exception e
         (do (println (format "Error on save-data: %s" (.getMessage e)))
             false
             ))))

