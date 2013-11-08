(ns todos.todo-item)

(defn todo-create
  [n]
  (let [todo-name (atom n)
        done      (atom false)
        notes     (atom [])
        sub-todos (atom [])]
    {:name        (fn [] @todo-name)           ; fn Get name
     :rename      (partial reset! todo-name)   ; fn Rename     [string]

     :done?       (fn [] @done)                ; fn is done?
     :set-done    (partial reset! done)        ; fn set done   [true/false]

     :notes       (fn [] @notes)               ; fn Get notes
     :add-note    (partial swap! notes conj)   ; fn add a note [string]

     :sub-todos   (fn [] @sub-todos)           ; fn Get Sub-Todolist
     :sub-add     (fn [sub-todo-item]          ; fn Create a new Sub-Todo [todo-item]
                    (swap! sub-todos conj sub-todo-item))

     :plain       (fn [] {:name @todo-name     ; fn transform atoms in normal data
                          :done? @done         ;    atoms cant be saved!
                          :notes @notes
                          :sub-todos (vec (for [i @sub-todos]
                                       ((i :plain))))
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
    (doseq [sub-item (todo-plain :sub-todos)]
      ((item :sub-add) (todo-load-from-plain sub-item)))
    item
    ))

