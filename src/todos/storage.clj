(ns todos.storage)

(def data-file-name "todos.clj.data")

(defn load-data
  "Load data from data-file-name."
  [default-data]
  (try (read-string (slurp data-file-name))
       (catch Exception e
         (do (println (format "Error on load-data: %s" (.getMessage e)))
             default-data
             ))))

(defn save-data
  "Save data to data-file-name."
  [data-to-save]
  (try (do
         (spit data-file-name data-to-save)
         true)
       (catch Exception e
         (do (println (format "Error on save-data: %s" (.getMessage e)))
             false
             ))))

