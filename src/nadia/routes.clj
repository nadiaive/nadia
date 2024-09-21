(ns nadia.routes
  (:require
   [duratom.core :refer [duratom]]
   [hiccup.page :refer [include-js]]
   [hiccup2.core :refer [html]]
   [org.httpkit.server :as server]
   [ring.middleware.resource :refer [wrap-resource]]
   [nadia.sider :as sider])
  (:import
   [java.time Duration Instant]))

(def ^{:doc "Her kan vi lagre ting folk gjør!"}
  tilstand
  (duratom :local-file
           :file-path (str (System/getenv "GARDEN_STORAGE") "/duratom.edn")
           :commit-mode :sync
           :init {}))

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

(def siste-snus-tidspunkt (Instant/parse "2024-09-21T00:00:00.0Z"))
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
    [:head (include-js "htmx.org@2.0.2.js")]
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
      (identity
       [:div {:style {:background-color farge-knæsj-gul
                      :padding "1rem"}}
        [:div {:style {:font-size "1.2rem" :color farge-myk-svart}}
         [:em "trykk for å si hei!"]]
        [:div {:style {:height "0.5rem"}}]
        [:div {:style {:text-align "center" :font-size "4rem" :color "black"}}
         [:button {:hx-post sider/si-hei} "Si hei!"]]
        [:div {:style {:font-size "1.2rem" :color farge-myk-svart}}
         [:em (:sagt-hei-ganger @tilstand 0) " personer har sagt hei."]]
        [:button {:hx-post "/napp2"}"Si ha det"]
        ])]]]))

(defn hovedside [_req]
  {:headers {"Content-Type" "text/html; charset=utf-8"}
   :body (str (hamburger {:nadia/tidspunkt-akkurat-nå (Instant/now)}))})

(defn ok [_req]
  {:status 200})

(defn fant-ingen-side [_req]
  {:headers {"Content-Type" "text/html; charset=utf-8"}
   :body (str (html [:html [:body [:p "Fant ingen side her! "
                                   [:a {:href "/"} "Gå tilbake"] " til kos og fine ting."]]]))
   :status 404})

(def hei-alternativer
  ["Heiiii" "Hei! 🩵" "heiiuuuuuuuu" "hoi" "HOPP" "H E I 😸"])

(defn si-hei [_req]
  (swap! tilstand update :sagt-hei-ganger (fnil inc 0))
  {:status 200
   :body (rand-nth hei-alternativer)})
@tilstand
(defn napp2 [_req]
  (println "napp 2")
  )

(defn sidevelger [req]
  (let [handler (condp = ((juxt :request-method :uri) req)
                  [:head "/"] ok
                  [:get "/"] hovedside
                  [:post sider/si-hei] si-hei
                  [:post "/napp2"] napp2
                  fant-ingen-side)]
    (prn (merge {:handler handler}
                  (select-keys req [:request-method :uri])))
    (handler req)))

(def wrapped-handler
  (-> #'sidevelger
      (wrap-resource "/")))

(defn run-server []
  (server/run-server #'wrapped-handler {:port 7777}))

(defonce server (run-server))

(defn klubbvask [x])

(comment
  (server)
  (alter-var-root #'server (constantly (run-server)))
  server
  (clojure.repl/doc server/run-server))

(comment
  ;Vi skal lære hashmap og atom.
  {:teller 0}
  (inc 5)
  (def x 3)
  x
  (def t {:teller 3 :taller 5 :pollen "husvarm"})
  t
  (:teller t)
  (:teller ())
  (inc x)
  (inc (:taller t))
  (str "husvarm" "ere")
  (defn ere [s]
    (str s "ere")
    )
  (ere "husvarm")
  (ere "varm")

  )

