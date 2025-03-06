(ns nadia.pokemon
  (:require
   [hiccup2.core :refer [html]]
   [next.jdbc :as j]
   [teodor.pokeapi :as pokeapi]))

(def dbfile (str (System/getenv "GARDEN_STORAGE") "/pokemon.db"))

(def db (j/get-datasource {:dbtype "sqlite" :dbname dbfile}))

(j/execute! db ["create table if not exists image (id integer primary key, pokedex_number integer not null, url text not null)"])
#_(j/execute! db ["drop table image"])

(defn pokemon->images [pokemon]
  (->> [(:front_default (:sprites pokemon))
        (:back_default (:sprites pokemon))]
       (remove nil?)
       (mapv (fn [url]
               {:image/pokedex_number (:id pokemon)
                :image/url url}))))

(defn insert-pokemon-images! [db pokemon]
  (doseq [image (pokemon->images pokemon)]
    (j/execute-one! db ["insert into image(pokedex_number, url) values (?, ?)"
                        (:image/pokedex_number image)
                        (:image/url image)])))

(comment
  ;; bilder for de første 151 pokemon
  (doseq [pokemon (pmap pokeapi/entity (pokeapi/pokemon-fra-dex :pokedex/kanto))]
    (insert-pokemon-images! db pokemon))

  (j/execute-one! db ["select count(*) from image"])
  )

(defn hent-pokemon [nummer]
  (pokeapi/call (str "https://pokeapi.co/api/v2/pokemon/" nummer)))

(defn sett-inn-pokemon! [nummer]
  (let [pokemon (hent-pokemon nummer)]
    (j/execute! db ["insert into pokemon(pokedex_number,name) VALUES (?,?)"
                    (:id pokemon)
                    (:name pokemon)]))
  )
(comment 
  (hent-pokemon 25)
  (keys (hent-pokemon 25))
  (:name (hent-pokemon 25))
  (:id (hent-pokemon 25))
  (sett-inn-pokemon! 25)
  )



(defn pokemonside []
  [:html
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
   [:body
    [:p "Dette er alle pokemonene (som jeg har orket å legge inn riktignok):"]
    (for [p (j/execute! db ["SELECT pokedex_number, name from pokemon ORDER BY pokedex_number"])]
      [:p (:pokemon/pokedex_number p) " "
       (:pokemon/name p)])

    [:p "Bilde av Mew:"]
    [:img {:src (-> (pokeapi/entity :pokemon/mew)
                    :sprites :front_default)}]

    [:p "Bilde av Mewtwo:"]
    [:img {:src (-> (pokeapi/entity :pokemon/mewtwo)
                    :sprites :front_default)}]
    
    [:p "Bilde av Pikachu:"]
    [:img {:src (-> (pokeapi/entity :pokemon/pikachu)
                    :sprites :front_default)}]

    [:p "Video av U2:"]
    [:iframe
     {:width "560",
      :height "315",
      :src "https://www.youtube.com/embed/co6WMzDOh1o?si=IClQR9kqRrn8EalQ",
      :title "YouTube video player",
      :frameborder "0",
      :allow
      "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share",
      :referrerpolicy "strict-origin-when-cross-origin",
      :allowfullscreen ""}]

    ]])
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
  (j/execute! db ["insert into pokemon(pokedex_number,name) VALUES (2,'Ivysaur')"])
  (j/execute! db ["insert into pokemon(pokedex_number,name) VALUES (3,'Venusaur')"])
  (j/execute! db ["insert into pokemon(pokedex_number,name) VALUES (4,'Charmander')"])

  ;; 3. gjør en select.
  (j/execute! db ["SELECT pokedex_number,name from pokemon ORDER BY pokedex_number"])
  (let [nummer 6
        pokemon (hent-pokemon nummer)]
    (:id pokemon)
    (:name pokemon)
(j/execute! db ["insert into pokemon(pokedex_number,name) VALUES (?,?)"
                (:id pokemon)
                (:name pokemon)])

    )

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
  
  (def pokemonene [{:pokemon/pokedex_number 1, :pokemon/name "Bulbasaur"} {:pokemon/pokedex_number 2, :pokemon/name "Ivysaur"} {:pokemon/pokedex_number 4, :pokemon/name "Charmander"}]
)

  (for [p pokemonene]
    [:p (:pokemon/pokedex_number p)
     (:pokemon/name p)]) 
  )
