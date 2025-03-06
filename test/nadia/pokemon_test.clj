(ns nadia.pokemon-test
  (:require [clojure.test :refer [deftest is]]
            [nadia.pokemon :as pokemon]
            [next.jdbc :as j]
            [teodor.pokeapi :as pokeapi]))

(defn create-db []
  (let [db (j/get-datasource {:dbtype "sqlite" :dbname ":mem:"})]
    (j/execute! db ["create table if not exists image (id integer primary key, pokedex_number integer not null, url text not null)"])
    db))

(comment
  (def mydb (create-db))
  (j/execute-one! mydb ["select 4*4 as lol"])
  )

(def magnemite (pokeapi/entity :pokemon/magnemite))
(def gyarados (pokeapi/entity :pokemon/gyarados))

(comment
  (-> magnemite :sprites :front_default)
  (-> magnemite :sprites :back_default)
  (-> magnemite :id)

  (-> gyarados :sprites :front_default)
  (-> gyarados :sprites :back_default)

  (-> gyarados :id)
  )

(deftest pokemon->images
  (is (= (pokemon/pokemon->images magnemite)
         [{:image/pokedex_number 81,
           :image/url
           "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/81.png"}
          {:image/pokedex_number 81,
           :image/url
           "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/81.png"}]))

  (is (= (pokemon/pokemon->images gyarados)
         [{:image/pokedex_number 130,
           :image/url
           "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/130.png"}
          {:image/pokedex_number 130,
           :image/url
           "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/130.png"}])))

(deftest insert-pokemon-images!
  (let [db (create-db)]
    (pokemon/insert-pokemon-images! db magnemite)
    (j/execute! db ["select * from image"]))
  )
