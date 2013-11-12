(ns todos.todo-item)

(defn todo-create
  [n]
  (let [todo-name (atom n)
        done      (atom false)
        notes     (atom [])
        sub-todos (atom [])
        status-done (fn [] (if (= 0 (count @sub-todos))
                              1
                              (apply (partial *
                                              (/ (count (filter #((% :done?)) @sub-todos))
                                                 (count @sub-todos)))
                                     (for [i @sub-todos] ((i :status-done)) )
                                     )))
        ]
    {:name        (fn [] @todo-name)           ; fn Get name
     :rename      (partial reset! todo-name)   ; fn Rename     [string]

     :done?       (fn [] @done)                ; fn is done?
     :set-done    (partial reset! done)        ; fn set done   [true/false]

     :notes       (fn [] @notes)               ; fn Get notes
     :note-add    (partial swap! notes conj)   ; fn add a note [string]
     :note-rm     (fn [note] (swap! notes
                                    (fn [e] (vec (remove #(= % note) e)))
                                    ))

     :todos       (fn [] @sub-todos)           ; fn Get Sub-Todolist
     :todo-add    (fn [sub-todo-item]          ; fn Create a new Sub-Todo [todo-item]
                    (swap! sub-todos conj sub-todo-item))
     :todo-rm     (fn [sub-todo-item] (swap! sub-todos
                                             (fn [e] (vec (remove #(= % sub-todo-item) e)))
                                             ))

     :plain       (fn [] {:name @todo-name     ; fn transform atoms in normal data
                          :done? @done         ;    atoms cant be saved!
                          :notes @notes
                          :todos (vec (for [i @sub-todos]
                                   ((i :plain))))
                          })
     :status-done status-done
     :status      (fn []
                    (let [data-dones (count (filter #((% :done?)) @sub-todos))
                          data-todos-all (count @sub-todos)
                          count-notes (count @notes)
                          percent-dones (format "%.1f" (* 100 (float (status-done))))
                          status-str (format "notes: %3s todos: %3s/%-3s %5s%%" count-notes data-dones data-todos-all percent-dones)]
                      (do
                        (println (format "%s   [%s] %s" status-str (if @done "X" " ") @todo-name))
                        )))
     :print       (fn [pre-string]
                      (println (format "%s[ %s ] %s" pre-string (if @done "X" " ") @todo-name))
                      (doseq [note @notes] (println (format "%s    | - %s" pre-string note)))
                      (doseq [item @sub-todos] ((item :print) (str pre-string "    | ")))
                      nil
                      )
     }))

; (atom []) cant be saved! be aware to build a transformfunction
;
(defn todo-load-from-plain
  [todo-plain]
  (let [item (todo-create (todo-plain :name))]
    ((item :set-done) (todo-plain :done?))
    (doseq [note (todo-plain :notes)]
      ((item :note-add) note))
    (doseq [sub-item (todo-plain :todos)]
      ((item :todo-add) (todo-load-from-plain sub-item)))
    item
    ))

