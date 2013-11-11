(ns todos.core
  (:gen-class)
  (:require [todos.input :as inp])
  (:require [todos.todo-item :as t-item])
  (:require [todos.storage :as storage])
  (:require [clojure.string :as stri]))

(def todolist
  (t-item/todo-load-from-plain
    (storage/load-data
      (((t-item/todo-create "todos") :plain)) ; if file cant be read this is the default.
      )))

(def current-stack (atom '() )) ; this have the subpath of opened todos. (subpath in todolist)

(defn command-repl
  []
  (do
    (println)
    (doseq [i @current-stack]
      (print (format "[%s] %s <- " (if ((i :done?)) "x" " ") ((i :name)) )))
    (println ((todolist :name)))
    (let [[cmd & args] (inp/user-cmd-input ">> ")
          current-todo (or (first @current-stack) todolist)
          joined-args  (stri/join " " args)
          find-by-name (fn [todos-seq compare-name]
                         (loop [todos todos-seq]
                           (cond
                             (empty? todos)
                             nil
                             (= (((first todos) :name)) compare-name)
                             (first todos)
                             :else
                             (recur (rest todos))
                             )))
          ]
      (cond
        ;
        ; save
        ;
        (= cmd "save")
        (storage/save-data ((todolist :plain)))
        ;
        ; show
        ;
        (and (= cmd "ls") (= joined-args "-a"))
        (do
          (doseq [n ((current-todo :notes))] (println (format "- %s" n)))
          (doseq [t ((current-todo :todos))] ((t :print) ""))
          )
        ;
        (= cmd "ls")
        (do
          (doseq [n ((current-todo :notes))] (println (format "- %s" n)))
          (doseq [t ((current-todo :todos))] ((t :status)))
          )
        ;
        (= cmd "plain")
        (println ((current-todo :plain)))
        ;
        ; create
        ;
        (= cmd "create")
        ((current-todo :todo-add) (t-item/todo-create joined-args))
        ;
        (= cmd "note")
        ((current-todo :note-add) joined-args)
        ;
        ; remove
        ;
        (= cmd "remove")
        (let [m (find-by-name ((current-todo :todos)) joined-args)]
          (if (and m (= "yes" (inp/user-str-input
                                (format "Are you sure to delete \"%s\"? (yes/no) " ((m :name)))
                                )))
            ((current-todo :todo-rm) m)
            ))
        ;
        (= cmd "rm-note")
        ((current-todo :note-rm) joined-args)
        ;
        ; manipulation
        ;
        (= cmd "rename")
        ((current-todo :rename) joined-args)
        ;
        (= cmd "done")
        (if (empty? args)
          ((current-todo :set-done) true)
          (let [m (find-by-name ((current-todo :todos)) joined-args)]
            (if m
              ((m :set-done) true)
              (println "Cant find this todo.")
              )))
        ;
        (= cmd "undone")
        (if (empty? args)
          ((current-todo :set-done) false)
          (let [m (find-by-name ((current-todo :todos)) joined-args)]
            (if m
              ((m :set-done) false)
              (println "Cant find this todo.")
              )))
        ;
        ; navigation
        ;
        (= cmd "first")
        (let [ft (first ((current-todo :todos)))]
          (if ft
            (swap! current-stack conj ft)
            (println "Subtodos doesnt exists.")
            ))
        ;
        (and (= cmd "cd") (= joined-args ".."))
        (swap! current-stack rest)
        ;
        (= cmd "cd")
        (let [m (find-by-name ((current-todo :todos)) joined-args)]
          (if m
            (swap! current-stack conj m)
            (println (format "There is no subtodo with the name \"%s\"." joined-args))
            ))
        ;
        (= cmd "nth")
        (let [n (- (read-string joined-args) 1)]
          (if (and (integer? n) (>= n 0) (< n (count ((current-todo :todos)))))
            (swap! current-stack conj (nth ((current-todo :todos)) n))
            (println "Id doesnt exist. There are only" (count ((current-todo :todos))) "ids")
            ))
        ;
        ; help
        ;
        (not= cmd "exit")
        (do
          (println "commands:")
          (println)
          (println "exit           - quit the app")
          (println)
          (println "s h o w")
          (println)
          (println "ls             - list the todos (on current navigation)")
          (println "ls -a          - list the todos recursively")
          (println "plain          - shows the raw data")
          (println)
          (println "c r e a t e")
          (println)
          (println "create <str>   - create a new todo on the current todo")
          (println "note <str>     - create a note on current todo")
          (println)
          (println "r e m o v e")
          (println)
          (println "remove <str>   - remove todolist with the name of str")
          (println "rm-note <str>  - remove the note with the content of str")
          (println)
          (println "m a n i p u l a t i o n")
          (println)
          (println "rename <str>   - rename the current todo")
          (println "done [<str>]   - set todo (str or current) to done")
          (println "undone [<str>] - reverse of done")
          (println)
          (println "n a v i g a t i o n")
          (println)
          (println "cd ..          - navigate to the parent todo")
          (println "cd <str>       - navigate to todo with given name")
          (println "nth <n>        - navigate to todo with position of n (1..)")
          ))
      ; exit clause
      (if (not= cmd "exit")
        (recur))
      )))

(defn -main
  "Start the demo ..."
  [& args]
  (do
    (println)
    (println "+++++  +++  ++++   +++   ++++")
    (println "  +   +   +  +  + +   + +")
    (println "  +   +   +  +  + +   +  +++")
    (println "  +   +   +  +  + +   +     +")
    (println "  +    +++  ++++   +++  ++++")
    (command-repl)
    (storage/save-data ((todolist :plain)))
    ))

