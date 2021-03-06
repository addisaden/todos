(defproject todos "0.1.3"
  :description "A small todo-tool for nested todolists written in clojure"
  :url "https://github.com/addisaden/todos"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot todos.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
