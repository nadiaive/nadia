(ns nadia.routes
  (:require
   [org.httpkit.server :as server]
   [hiccup2.core :refer [html]]))

(def dk-morgen {:background-color "#9dd0de"
                :color "#ff0030"})
(def dk-ettermiddag {:background-color "#9e9e9e"
                     :color "#434d80"})
(def dk-natt {:background-color :black
              :color :white})
(def dk-soloppgang {:background-color :##e91e63
                    :color :white})

(defn dagskommentar
  [dato tekst]
  [:p {:style (rand-nth [dk-morgen dk-ettermiddag dk-natt dk-soloppgang])}
   [:strong {:style {:font-size :18px 
                     :font :arial}} dato]
   [:br] tekst])


(dagskommentar "11.8" "hei")

(defn hamburger []
  (html
   [:html
    [:body {:style {:background-color :grey
                    :margin 0}}
     [:div {:style {:background-color :dodgerblue
                    :padding-bottom :8px}}
      [:h2 {:style {:background-color :pink}}
       "Heiiiiii hallooo!!!!!!!!! Denne har jeg bygget selv (med litt hjelp da)"]
      [:p {:style {:background-color :lightgreen
                   :font-size :50px
                   :color :deeppink
                   :margin 0
                   }} 
       "Dette er en skikkelig fin paragraf med masse spennendene tekstterer"]
      [:img {:style {:margin-left "auto"
                     :margin-right "auto"
                     :margin 20
                     :display "block"}
             :src "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/Steilk%C3%BCste_bei_Ahrenshoop.jpg/600px-Steilk%C3%BCste_bei_Ahrenshoop.jpg"}]
      (dagskommentar "11.8:" "Dette var en fin dag. Jeg spiste frokost og lå i Iladalenparken. Takk for meg.")
      (dagskommentar "11.8" "hei")
      (dagskommentar "10.8" "dette var en bra dag, møtte teodor.")
      (dagskommentar "13.8" "Nå skriver jeg fordi jeg må øve på det vi gjorde. Det var gøy.")
      ]]]))

(defn handler [req]
  {:headers {"Content-Type" "text/html; charset=utf-8"}
   :body (str (hamburger))})

(defn run-server []
  (server/run-server #'handler {:port 7777}))

(defonce server (run-server))

(defn klubbvask [x])

(comment
  (server)
  (alter-var-root #'server (constantly (run-server)))
  server
  (clojure.repl/doc server/run-server))
