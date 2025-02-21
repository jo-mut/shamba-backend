(ns shamba-backend.api
  (:require
   [shamba-backend.db.core :as db]
   [shamba-backend.uniswap.core :as uniswap]
   [cheshire.core :as json]))


(defn create-tables []
  (db/create-positions-table)
  (db/create-pools-table))

(defn save-positions-to-db
  []
  (let [response (uniswap/get-pools)
        positions (-> (json/parse-string (:body response) true)
                      :data
                      :positions)]
    (doseq [position positions]
      (db/insert-positions position))))

(defn save-pools-to-db
  []
  (let [response (uniswap/get-positions)
        pools (-> (json/parse-string (:body response) true)
                  :data
                  :pools)]
    (doseq [pool pools]
      (db/insert-pools pool))))

(defn save-uniswap-data []
  (save-pools-to-db)
  (save-positions-to-db))

(defn get-pools []
  (db/select :pools))

(defn get-positions [fields]
  (db/select :positions fields))