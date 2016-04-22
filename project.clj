(defproject 인스타파스-연습 "0.1.0-SNAPSHOT"
  :description "learning to use instaparse"
  :url "https://github.com/hatemogi/instaparse-practice"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [instaparse "1.4.1"]
                 [org.clojure/clojurescript "1.8.40"]
                 [com.lucasbradstreet/instaparse-cljs "1.4.1.1"]]
  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.2"]]
  :cljsbuild
  {:builds [{:source-paths ["src-cljs"]
             :compiler {:output-to "public/js/main.js"
                        :optimizations :whitespace
                        :pretty-print true}}]}
  :test-paths ["test" "src"])
