(defproject fentontravers/transit-websocket-server "0.4.12-SNAPSHOT"
  :description "WebSocket Server Library"
  :url "https://github.com/ftravers/websocket-server"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [http-kit "2.2.0"]
                 [com.cognitect/transit-clj "0.8.300"]
                 [com.taoensso/timbre "4.8.0"]
                 [org.clojure/core.async "0.2.395"]]
  :target-path "target/%s"

  :profiles {:dev {:source-paths ["dev" "src"]}
             :uberjar {:aot :all}})
