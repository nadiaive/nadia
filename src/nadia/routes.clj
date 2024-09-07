(ns nadia.routes
  (:require
   [hiccup2.core :refer [html]]
   [org.httpkit.server :as server])
  (:import
   [java.time Duration Instant]))

(def dk-morgen {:background-color "#9dd0de"
                :color "#ff0030"
                :padding-bottom :10px})
(def dk-ettermiddag {:background-color "#9e9e9e"
                     :color "#434d80"
                     :padding-bottom :10px})
(def dk-natt {:background-color :black
              :color :white
              :padding-bottom :10px})
(def dk-soloppgang {:background-color "#e91e63"
                    :color :white
                    :padding-bottom :10px})

(defn dagskommentar
  [dato tekst]
  [:p {:style (rand-nth [dk-morgen dk-ettermiddag dk-natt dk-soloppgang])}
   [:strong {:style {:font-size :33px 
                     :font :arial}} dato]
   [:br] 
   [:strong {:style {:font-size :16px}} tekst]])


(dagskommentar "11.8" "hei")

(def siste-snus-tidspunkt (Instant/parse "2024-09-04T22:00:00.0Z"))
(def farge-myk-svart "#0000009c")
(def farge-knæsj-gul "#ffef00")

(defn tidsvarighet->timer-minutter-sekunder [tidsvarighet]
  (let [timer (.toHours tidsvarighet)
        minutter (- (.toMinutes tidsvarighet)
                    (* 60 timer))
        sekunder (- (.toSeconds tidsvarighet)
                    (* 60 60 timer)
                    (* 60 minutter))]
    [timer minutter sekunder]))

#_ (tidsvarighet->timer-minutter-sekunder (Duration/between siste-snus-tidspunkt current-time))

(defn tidsvarighet->beskrivelse-på-norsk [tidsvarighet]
  (let [[timer minutter sekunder] (tidsvarighet->timer-minutter-sekunder tidsvarighet)]
    (str timer " timer"
         " " minutter " minutter"
         " " sekunder " sekunder")))

#_ (tidsvarighet->beskrivelse-på-norsk (Duration/between siste-snus-tidspunkt current-time))

(defn tid-siden-siste-snus-infoboks [tid-siden-siste-snus]
  [:div {:style {:background-color farge-knæsj-gul
                 :padding "1rem"}}
   [:div {:style {:font-size "1.2rem" :color farge-myk-svart}}
    [:em "siden siste snus"]]
   [:div {:style {:height "0.5rem"}}]
   [:div {:style {:text-align "center" :font-size "4rem" :color "black"}}
    [:strong
     (str "👉 "
          (tidsvarighet->beskrivelse-på-norsk tid-siden-siste-snus)
          " 👈")]]])

(defn hamburger [informasjon]
  (html
   [:html
    [:body {:style {:background-color :grey
                    :margin 0
                    :padding :10px}}
     [:div {:style {:background-color :dodgerblue
                    :padding-bottom :8px}}
      [:h2 {:style {:background-color :pink}}
       "Heiiiiii hallooo!!!!!!!!! Denne har jeg bygget selv (med litt hjelp da)"]
      [:p {:style {:background-color :lightgreen
                   :font-size :50px
                   :color :deeppink
                   :margin 0}}
       "Dette er en skikkelig fin paragraf med masse spennendene tekstterer"]
      [:img {:style {:margin-left "auto"
                     :margin-right "auto"
                     :margin 20
                     :display "block"}
             :src "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/Steilk%C3%BCste_bei_Ahrenshoop.jpg/600px-Steilk%C3%BCste_bei_Ahrenshoop.jpg"}]
      (let [tidspunkt-akkurat-nå (:nadia/tidspunkt-akkurat-nå informasjon)
            tid-siden-siste-snus (Duration/between siste-snus-tidspunkt tidspunkt-akkurat-nå)]
        (tid-siden-siste-snus-infoboks tid-siden-siste-snus))
      (dagskommentar "11.8:" "Dette var en fin dag. Jeg spiste frokost og lå i Iladalenparken. Takk for meg.")
      (dagskommentar "11.8" "hei")
      (dagskommentar "10.8" "dette var en bra dag, møtte teodor.")
      (dagskommentar "13.8" "Nå skriver jeg fordi jeg må øve på det vi gjorde. Det var gøy.")
      (dagskommentar "26.8" "I helgen spiste jeg pizza og var i bryllup.")
      ]]]))

(defn handler [req]
  {:headers {"Content-Type" "text/html; charset=utf-8"}
   :body (str (hamburger {:nadia/tidspunkt-akkurat-nå (Instant/now)}))})

(defn run-server []
  (server/run-server #'handler {:port 7777}))

(defonce server (run-server))

(defn klubbvask [x])

(comment
  (server)
  (alter-var-root #'server (constantly (run-server)))
  server
  (clojure.repl/doc server/run-server))
