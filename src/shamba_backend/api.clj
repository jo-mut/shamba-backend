(ns shamba-backend.api
  (:require
   [shamba-backend.db.core :as db]
   [shamba-backend.uniswap.core :as uniswap]
   [cheshire.core :as json]))


(defn save-pools-to-db
  []
  (let [response (uniswap/get-pools)
        pools (-> (json/parse-string (:body response) true)
                  :data
                  :pools)]
    (doseq [pools pools]
      (db/insert-pools pools))))

(defn save-positions-to-db
  []
  (let [response (uniswap/get-positions)
        positions (-> (json/parse-string (:body response) true)
                      :data
                      :positions)]
    (println "positions " positions)

    (doseq [pool positions]
      (db/insert-positions pool))))


(defn create-tables []
  (db/create-positions-table)
  (db/create-pools-table))

(defn save-uniswap-data []
  (save-pools-to-db)
  (println (save-positions-to-db)))

(defn get-pools []
  (db/select :pools))

(defn get-positions []
  (db/select :positions))