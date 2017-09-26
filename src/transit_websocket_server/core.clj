(ns transit-websocket-server.core
  (:require [org.httpkit.server :as http]
            [taoensso.timbre :as log]
            [cognitect.transit :as transit])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]))

(defn json->edn [json]
  (log/debug "IN JSON: " json)
  (let [edn (-> json
                .getBytes
                ByteArrayInputStream.
                (transit/reader :json)
                transit/read)]
    (log/debug "OUT EDN: " edn)
    edn))

(defn edn->json [edn]
  (log/debug "IN EDN: " edn)
  (let [out (ByteArrayOutputStream. 4096)
        writer (transit/writer out :json)]
    (transit/write writer edn)
    (let [json (.toString out)]
      (log/debug "OUT JSON: " json)
      json)))

(defn websocket-server [cb req]
  (http/with-channel req channel
    (http/on-close
     channel
     (fn [status] (log/debug (str "Websocket channel closed with status: " status))))
    (http/on-receive
     channel
     (fn [json-data]
       (if (http/websocket? channel)
         (do
           (log/debug "WS Server Recvd Msg: " json-data)
           (let [data (json->edn json-data)
                 resp (cb data)
                 json-resp (edn->json resp)]
             (log/debug "WS Server Reply Msg: " json-resp)
             (http/send! channel json-resp))))))))

(defn start-ws-server [port callback]
  (http/run-server (partial websocket-server callback)
                   {:port port}))
