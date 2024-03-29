;; Drift migrations configuration template for Postgres and Korma
;; By Paul Legato (pjlegato at gmail dot com)
;; Requires Postgresql >= 9.0 (for IF NOT EXISTS clause)

(ns config.migrate-config
  (:use [korma db core])
  (:require [learn-smthng.models.schema :as schema]))

(defdb db (postgres schema/db-spec))

; Postgres 8.1 :(
;(defn- maybe-create-schema-table
;  "Creates the schema table if it doesn't already exist."
;  [& args]
;  (exec-raw "CREATE TABLE IF schema_version (version BIGINT NOT NULL, created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now())"))

(defn current-db-version []
;  (maybe-create-schema-table)
  (or (:version (first (select :schema_version (fields :version) (order :created_at :DESC) (limit 1)))) 0))

(defn update-db-version [version]
  (insert :schema_version (values {:version version})))

(defn migrate-config []
  { :directory "/src/migrations/"
   :ns-content "\n (:use [korma db core])"
   :namespace-prefix "migrations"
;   :init maybe-create-schema-table
   :current-version current-db-version
   :update-version update-db-version })