(ns transit-websocket-server.core-test
  (:require [transit-websocket-server.core :refer [start-ws-server]]
            [taoensso.timbre :as log]
            [clojure.edn :as edn]))

(defonce ws-server (atom nil))

(defn request-handler-upcase [data]
  (log/debug "Uppercasing data: " data)
  (clojure.string/upper-case (str data)))

(defn start []
  "Demonstrate how to use the websocket server library."
  (let [port 7890]
    (reset! ws-server (start-ws-server port request-handler-upcase))))

(defn stop "Stop websocket server" [] (@ws-server))

(defn restart [] (stop) (start))


