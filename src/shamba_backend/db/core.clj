(ns shamba-backend.db.core
  [:require
   [clojure.string :as str]
   [clojure.java.jdbc :as jdbc]])

(def -db {:classname "org.postgresql.Driver"
          :dbtype "postgresql"
          :dbname "shamba_db"
          :host "localhost"
          :user "postgres"
          :password "postgres"})

(defn create-positions-table
  []
  (jdbc/execute! -db ["CREATE TABLE IF NOT EXISTS positions (
                       id SERIAL PRIMARY KEY, 
                       position_id TEXT UNIQUE,
                       liquidity TEXT,
                       owner TEXT,
                       fee_tier TEXT,
                       token0_symbol TEXT,
                       token0_decimal TEXT,
                       token1_symbol TEXT,                      
                       token1_decimal TEXT)"]))

(defn concat-fields
  "Concat field names for SQL"
  [fields]
  (str/join ", " (map name fields)))

(defn insert
  "Insert to database"
  [table position]
  (first (jdbc/insert! -db table position)))

(defn select
  "Select the database"
  [table fields]
  (jdbc/query -db [(str "select " (concat-fields fields) " from " (name table))]))