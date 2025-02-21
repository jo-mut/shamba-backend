(ns shamba-backend.core
  (:require
   [shamba-backend.routes.core :as routes]
   [shamba-backend.api :as api]
   [compojure.core :refer [defroutes GET POST]]
   [dotenv :refer [env]]
   [org.httpkit.server :as server]
   [ring.middleware.json :as js]
   [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(defroutes app-routes
  (GET "/" [] routes/positions)
  (GET "/pools" [] routes/pools))

(defn -main
  "Production"
  [& args]
  (api/create-tables)
  (api/save-uniswap-data)
  (let [port  (Integer/parseInt (or (env :PORT) "3000"))]
    (server/run-server
     (js/wrap-json-params
      (js/wrap-json-response
       (wrap-defaults app-routes api-defaults)))
     {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))


(defn -dev-main
  "Development"
  [& args]
  (let [port  (Integer/parseInt (or (env :PORT) "3000"))]
    (server/run-server
     (js/wrap-json-params
      (js/wrap-json-response
       (wrap-defaults app-routes api-defaults)))
     {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))

