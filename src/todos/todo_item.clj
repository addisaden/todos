(ns todos.todo-item)

(defn todo-create
  [n]
  (let [todo-name (atom n)
        done (atom false)]
    {:name     (fn [] @todo-name)
     :done?    (fn [] @done)
     :rename   (partial reset! todo-name)
     :set-done (partial reset! done)
     :plain    (fn [] {:name @todo-name :done? @done})
     }))

; (atom []) cant be saved! be aware to build a transformfunction
;
(defn todo-load-from-plain
  [todo-plain]
  (let [item (todo-create (todo-plain :name))]
    ((item :set-done) (todo-plain :done?))
    item
    ))

