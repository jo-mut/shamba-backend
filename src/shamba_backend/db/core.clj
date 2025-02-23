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

(defn create-pools-table
  []
  (jdbc/execute! -db ["CREATE TABLE IF NOT EXISTS pools (
                       id SERIAL PRIMARY KEY, 
                       pool_id TEXT UNIQUE,
                       token0_symbol TEXT,
                       token0_id TEXT,
                       token1_symbol TEXT,                      
                       token1_id TEXT)"]))

(defn concat-fields
  "Concat field names for SQL"
  [fields]
  (str/join ", " (map name fields)))

(defn insert
  "Insert a record to database"
  [table record]
  (first (jdbc/insert! -db table record)))

(defn select
  ([table]
   (jdbc/query -db [(str "SELECT * FROM " (name table))]))
  ([table fields]
   (jdbc/query -db [(str "SELECT " (if fields (concat-fields fields) "*") " FROM " (name table))]))
  ([table last-id limit]
   (println "placeholders " last-id " " limit)
   (jdbc/query -db [(str "SELECT * FROM " (name table) " WHERE id >= ? ORDER BY id ASC LIMIT ? ") last-id limit])))

(defn insert-pools
  [pool]
  (let [{:keys [id token0 token1]} pool
        new-pool      {:token0_id      (:id token0)
                       :token0_symbol (:symbol token0)
                       :token1_id     (:id token1)
                       :token1_symbol (:symbol token1)
                       :pool_id       id}
        columns (str/join ", " (map name (keys new-pool)))
        placeholders   (str/join ", " (repeat (count new-pool) "?"))
        sql     (str "INSERT INTO pools ( " columns " ) VALUES (" placeholders ") ON CONFLICT(pool_id) DO NOTHING")]
    (jdbc/execute! -db (into [sql] (vec (vals new-pool))))))

(defn insert-positions
  "Insert"
  [position]
  (let [{:keys [owner id liquidity]} position
        owner          (str owner)
        new-position   {:liquidity      liquidity
                        :position_id    id
                        :owner          owner
                        :fee_tier       (get-in position [:pool :feeTier])
                        :token0_symbol  (get-in position [:pool :token0 :symbol])
                        :token0_decimal (get-in position [:pool :token0 :decimal])
                        :token1_symbol  (get-in position [:pool :token0 :symbol])
                        :token1_decimal (get-in position [:pool :token0 :decimal])}
        columns        (str/join ", " (map name (keys new-position)))
        placeholders   (str/join ", " (repeat (count new-position) "?"))
        sql            (str "INSERT INTO positions (" columns ") VALUES (" placeholders ") ON CONFLICT(position_id) DO NOTHING")]
    (jdbc/execute! -db (into [sql] (vec (vals new-position))))))
