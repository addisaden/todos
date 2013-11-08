(ns todos.todo-item)

(defn todo-create
  [n]
  (let [todo-name (atom n)
        done      (atom false)
        notes     (atom [])]
    {:name     (fn [] @todo-name)
     :done?    (fn [] @done)
     :notes    (fn [] @notes)
     :rename   (partial reset! todo-name)
     :set-done (partial reset! done)
     :add-note (partial swap! notes conj)
     :plain    (fn [] {:name @todo-name :done? @done :notes @notes})
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

