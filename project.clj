(defproject shamba-backend "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                  ; Clojure JDBC
                 [org.clojure/java.jdbc "0.7.10"]
                 ; Postgresql
                 [org.postgresql/postgresql "42.3.1"]
                 [cheshire "5.11.0"]
                 [clj-http "3.13.0"]
                 [http-kit "2.3.0"]
                  ; Ring defaults - for query params etc
                 [ring/ring-defaults "0.3.4"]
                 [ring/ring-devel "1.6.3"]
                 [ring/ring-json "0.5.0"] 
                 [lynxeyes/dotenv "1.0.2"]]
  :main ^:skip-aot shamba-backend.core
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev {:main shamba-backend.core}})
