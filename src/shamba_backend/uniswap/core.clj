(ns shamba-backend.uniswap.core
  (:require
   [clj-http.client :as client]
   [cheshire.core :as json]
   [dotenv :refer [env]]))

(def api-key (env "UNISWAP_API_KEY"))
(def url     "https://gateway.thegraph.com/api/7a7a5b14802496de346f5dda0e3dae3e/subgraphs/id/5zvR82QoaXYFyDEKLZ9t6v9adgnptxYpKpSbxtgVENFV")

(def pools 
  "{ pools(first: 100, orderBy: liquidity, orderDirection: desc)
   { id token0 { id symbol } token1 { id symbol } }}")

(def positions
  "{ positions(first: 100, orderBy: liquidity, orderDirection: desc) 
   {id owner liquidity pool { token0 { symbol decimals } token1 { symbol decimals } feeTier}}}")

(defn get-pools
  []
  (client/post url {:headers {"Content-Type" "application/json"
                             "x-api-key" api-key}
                    :body   (json/generate-string {:query pools})}))
(defn get-positions
  []
  (client/post url {:headers {"Content-Type" "application/json"
                             "x-api-key" api-key}
                    :body   (json/generate-string {:query positions})}))
