(ns shamba-backend.routes.core
  (:require
   [shamba-backend.db.core :as db]
   [shamba-backend.api :as api]))

(defn echo-route
  "Echo back the request"
  [req]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (-> (str "GET '/' " req))})

(defn pools
  "Get pools"
  [req]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (-> (api/get-pools))})

(defn positions
  "Get positions"
  [req]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (-> (api/get-positions (:params req)))})