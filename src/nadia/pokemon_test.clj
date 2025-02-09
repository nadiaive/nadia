(ns nadia.pokemon-test
  (:require [next.jdbc :as j]))

(def dbfile "pokemon.db")

(def db (j/get-datasource {:dbtype "sqlite" :dbname dbfile}))

(comment
  (set! *print-namespace-maps* false)
  (j/execute! db ["SELECT pokedex_number, name FROM pokemon"])
  )
