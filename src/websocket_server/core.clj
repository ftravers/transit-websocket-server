(ns websocket-server.core
  (:require [org.httpkit.server :as http]
            [taoensso.timbre :as log]
            [cognitect.transit :as transit])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]))

(defn json->edn [json]
  (-> json
      .getBytes
      ByteArrayInputStream.
      (transit/reader :json)
      transit/read))

(defn edn->json [edn]
  (let [out (ByteArrayOutputStream. 4096)
        writer (transit/writer out :json)]
    (transit/write writer edn)
    (.toString out)))

(defn websocket-server [cb req]
  (http/with-channel req channel
    (http/on-close
     channel
     (fn [status] (log/debug (str "Websocket channel closed with status: " status))))
    (http/on-receive
     channel
     (fn [json-data]
       (if (http/websocket? channel)
         (let [data (json->edn json-data)
               resp (cb data)
               json-resp (edn->json resp)]
           (log/debug "RECV: " data)
           (log/debug "RESP: " resp)
           (http/send! channel json-resp)))))))

(defn start-ws-server [port callback]
  (http/run-server (partial websocket-server callback)
                   {:port port}))
