(ns teodor.pokeapi
  (:require
   [babashka.http-client :as http]
   [charred.api :as charred]))

(comment
  (set! *print-namespace-maps* false)
  (http/head "https://pokeapi.co/api/v2/pokemon")
  (http/get "https://pokeapi.co/api/v2/pokemon")
  )

(def call
  (memoize
   (fn [uri & [opts]]
     (-> uri
         (http/get opts)
         :body
         (charred/read-json :key-fn keyword)))))

(comment
  ;; Alle pokemon som en lang liste.
  ;; Her ber vi om pokemon de første 151 pokemon
  (-> "https://pokeapi.co/api/v2/pokemon"
      (call {:query-params {:offset 0
                            :limit 151}})
      :results)

  ;; Vi kan hente pokemon enten etter nummer i pokedexen (1) eller
  ;; navn (bulbasaur)
  (call "https://pokeapi.co/api/v2/pokemon/1/")
  (call "https://pokeapi.co/api/v2/pokemon/bulbasaur/")

  )

(defn entity [k]
  (case (namespace k)
    "pokemon" (call (str "https://pokeapi.co/api/v2/pokemon/" (name k)))
    "pokedex" (call (str "https://pokeapi.co/api/v2/pokedex/" (name k)))
    nil))

(comment
  ;; funksjonen entity lar oss hente info om pokemon vi vet navnet på
  (entity :pokemon/arcanine)
  (entity :pokemon/mew)

  )

;; ... men noen ganger vet vi ikke navnet. Da kan vi finne navnet i en dex.
(defn pokemon-fra-dex [pokedex]
  (->> (entity pokedex)
       :pokemon_entries
       (map :pokemon_species)
       (map (fn [mon]
              (keyword "pokemon" (:name mon))))))

(comment
  (pokemon-fra-dex :pokedex/kanto) ; de første 151
  (pokemon-fra-dex :pokedex/national)  ; obs, 1000+
  )

(comment
  ;; Teodors jukselapp og R&D-hjørne

  ;; Hent alle pokedexene
  (->> (call "https://pokeapi.co/api/v2/pokedex/" {:query-params {:limit 50}})
       :results
       (map (fn [dex]
              (keyword "pokedex" (:name dex)))))

  ;; Alle pokedexene
  #{:pokedex/national
    :pokedex/kanto
    :pokedex/original-johto
    :pokedex/hoenn
    :pokedex/original-sinnoh
    :pokedex/extended-sinnoh
    :pokedex/updated-johto
    :pokedex/original-unova
    :pokedex/updated-unova
    :pokedex/conquest-gallery
    :pokedex/kalos-central
    :pokedex/kalos-coastal
    :pokedex/kalos-mountain
    :pokedex/updated-hoenn
    :pokedex/original-alola
    :pokedex/original-melemele
    :pokedex/original-akala
    :pokedex/original-ulaula
    :pokedex/original-poni
    :pokedex/updated-alola}

  (entity :pokemon/lugia)
  (entity :pokemon/tyranitar)

  )
