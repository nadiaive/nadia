(ns nadia.pokemon
  (:require
   [hiccup2.core :refer [html]]
   [next.jdbc :as j]))

(def dbfile (str (System/getenv "GARDEN_STORAGE") "/pokemon.db"))

(def db (j/get-datasource {:dbtype "sqlite" :dbname dbfile}))

(defn pokemonside []
  [:html
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
   [:body
    [:p "Dette er alle pokemonene:"]
    (for [p (j/execute! db ["SELECT pokedex_number, name from pokemon"])]
      [:p (:pokemon/name p)
       (:pokemon/pokedex_number p)])]])
#_(pokemonside)

(defn pokemon-handler [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str (html (pokemonside)))})
#_(pokemon-handler {})

(comment
  ;; NADIA SIN
  (set! *print-namespace-maps* false)
  ;; 1. lag tabell
  (j/execute! db ["create table pokemon (id integer primary key, pokedex_number integer unique not null, name text not null unique)"])
  (j/execute! db ["drop table pokemon"])

  ;; 2. putt in Bulbasaur
  (j/execute! db ["insert into pokemon(pokedex_number,name) VALUES (1,'Bulbasaur')"])
  (j/execute! db ["insert into pokemon(pokedex_number,name) VALUES (4,'Charmander')"])

  ;; 3. gjør en select.
  (j/execute! db ["SELECT pokedex_number,name from pokemon"])

  )

(comment
  (set! *print-namespace-maps* false)
  ;; TEODOR SIN

  (j/execute! db ["CREATE TABLE punkt (id INTEGER PRIMARY KEY, x REAL, y REAL)"])
  (j/execute! db ["DROP TABLE punkt"])
  (j/execute! db ["DELETE FROM punkt WHERE 1"])

  (j/execute! db ["SELECT * from punkt"])
  (j/execute! db ["INSERT INTO punkt(x,y) VALUES (12,13)"])
  )

(comment
  ;; Trene på maps og for-loop
  
  (def pokemonene [{:pokemon/pokedex_number 1, :pokemon/name "Bulbasaur"} {:pokemon/pokedex_number 4, :pokemon/name "Charmander"}]
)

  (for [p pokemonene]
    [:p (:pokemon/pokedex_number p)
     (:pokemon/name p)]) 
  )