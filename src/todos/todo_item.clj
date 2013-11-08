(ns todos.todo-item)

(defn todo-create
  [n]
  (let [todo-name (atom n)
        done      (atom false)
        notes     (atom [])]
    {:name     (fn [] @todo-name)           ; fn Get name   []
     :done?    (fn [] @done)                ; fn is done?   []
     :notes    (fn [] @notes)               ; fn Get notes  []
     :rename   (partial reset! todo-name)   ; fn Rename     [string]
     :set-done (partial reset! done)        ; fn set done   [true/false]
     :add-note (partial swap! notes conj)   ; fn add a note [string]
     :plain    (fn [] {:name @todo-name     ; fn transform atoms in normal data
                       :done? @done         ;    atoms cant be saved!
                       :notes @notes
                       })
     }))

; (atom []) cant be saved! be aware to build a transformfunction
;
(defn todo-load-from-plain
  [todo-plain]
  (let [item (todo-create (todo-plain :name))]
    ((item :set-done) (todo-plain :done?))
    (doseq [note (todo-plain :notes)]
      ((item :add-note) note))
    item
    ))

