(ns shamba-backend.routes.core
  (:require
   [cheshire.core :as json]
   [shamba-backend.uniswap.core :as uniswap]
   [shamba-backend.db.core :as db]))

(defn echo-route
  "Echo back the request"
  [req]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (-> (str "GET '/' " req))})

(defn save-positions-to-db
  [body]
  (let [positions (-> (json/parse-string body true)
                      :data
                      :positions)]
    (println "positions ==== " positions)
    (doseq [{:keys [id liquidity pool owner] :as position} positions]
      (let [fee_tier (get-in position [:pool :feeTier])
            token0_symbol (get-in position [:pool :token0 :symbol])
            token0_decimal (get-in position [:pool :token0 :decimal])
            token1_symbol (get-in position [:pool :token0 :symbol])
            token1_decimal (get-in position [:pool :token0 :decimal])
            owner (str owner)
            new-position {:liquidity liquidity
                          :position_id id
                          :owner     owner
                          :fee_tier  fee_tier
                          :token0_symbol  token0_symbol
                          :token0_decimal token0_decimal
                          :token1_symbol  token1_symbol
                          :token1_decimal token1_decimal}]
        (db/insert "positions" new-position)))))

(defn positions
  "Echo back a name"
  [req]
  (let [response (uniswap/get-positions)
        body     (:body response)]
    (save-positions-to-db body)
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    body}))
