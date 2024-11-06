(ns nadia.routes
  (:require
   [clojure.string :as str]
   [duratom.core :refer [duratom]]
   [hiccup.page :refer [include-js]]
   [hiccup2.core :refer [html]]
   [nadia.sider :as sider]
   [org.httpkit.server :as server]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.resource :refer [wrap-resource]])
  (:import
   [java.time Duration Instant ZoneId LocalDate LocalTime]))

(def ^{:doc "Her kan vi lagre ting folk gjør!"}
  tilstand
  (duratom :local-file
           :file-path (str (System/getenv "GARDEN_STORAGE") "/duratom.edn")
           :commit-mode :sync
           :init {}))

(comment
  @tilstand

  ,)

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
          " 👈")]]
   [:div {:style {:text-align "center" :font-size "4rem" :color "black"}}
    [:strong
     (str (* 3.75 (first (tidsvarighet->timer-minutter-sekunder tid-siden-siste-snus)))
          " kr spart 💸")]]])

(def tidssone-oslo (ZoneId/of "Europe/Oslo"))

(defn lokaltid [tidspunkt]
  (let [dato (LocalDate/ofInstant tidspunkt tidssone-oslo)
        tid (LocalTime/ofInstant tidspunkt tidssone-oslo)]
    (format "%02d.%02d.%02d kl. %02d:%02d:%02d"
            (.getDayOfMonth dato)
            (.getValue (.getMonth dato))
            (.getYear dato)
            (.getHour tid)
            (.getMinute tid)
            (.getSecond tid))))

(comment
  (def utc-tid (Instant/parse "2024-09-27T14:50:13.211633034Z"))
  (def lokal-dato (LocalDate/ofInstant utc-tid tidssone-oslo))

  (.getDayOfMonth lokal-dato)
  (.getValue (.getMonth lokal-dato))
  (.getYear lokal-dato)

  ,)
(defn linjeskift [s]
  (interpose [:br]
             (str/split-lines s)))
(defn vis-innlegg [innlegg]
  (cond (string? innlegg)
        [:div {:style {:background-color farge-knæsj-gul :padding "1rem" :margin-top "1rem" :margin-bottom "1rem"}}
         innlegg]
        (and (:overskrift innlegg) (:tekst innlegg))
        [:div {:style {:background-color farge-knæsj-gul :padding "1rem" :margin-top "1rem" :margin-bottom "1rem"}}
         (when (not-empty (:overskrift innlegg))
           (list
            [:strong (:overskrift innlegg)]
            [:br]))
         (linjeskift (:tekst innlegg))
         (when-let [tidspunkt (:tidspunkt innlegg)]
           (list
            [:br]
            [:em (lokaltid (Instant/parse tidspunkt))]))]))

(comment
  (vis-innlegg "hei")
  (vis-innlegg {:overskrift "I DAG"
                :tekst "ER DET BRA"})
  (vis-innlegg :lol)
  @tilstand
  (swap! tilstand
         update :alle-innleggene
         (fnil
          #(conj % {:overskrift "I DAG"
                    :tekst "ER DET BRA"})
          []))

  (swap! tilstand
         update :alle-innleggene
         (fnil
          #(conj % "Gammelt format.")
          []))

  @tilstand

  ,)

(comment
  (clojure.edn/read-string
   (pr-str
    (random-uuid)))

  (random-uuid))

"Enda en ny boks! 
Denne har ikke linjeskift som funker. "

[:div "Dette er en tekst"
 [:br]
 "dette er linjeskift"]

(do 
  
  (linjeskift "Enda en ny boks!
Denne har ikke linjeskift som funker.")
  )

(interpose [:br] ["Enda en ny boks!" "Denne har ikke linjeskift som funker."])

(defn hamburger [informasjon]
  (html
   [:html
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     (include-js "htmx.org@2.0.2.js")]
    [:body {:style {:background-color :grey
                    :margin 0
                    :padding :10px}}
     [:div {:style {:background-color :black
                    :padding-bottom :8px}} 
      [:p {:style {:background-color :lightgreen
                   :font-size :50px
                   :color :deeppink
                   :margin 0}}
       "Dette er siden min. Etterhvert skal jeg legge ut masse greier."]
      [:img {:style {:margin-left "auto"
                     :margin-right "auto"
                     :margin 20
                     :display "block"
                     :max-width "100%"}
             :src "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/Steilk%C3%BCste_bei_Ahrenshoop.jpg/600px-Steilk%C3%BCste_bei_Ahrenshoop.jpg"}]
      

      (identity
       (for [innlegg (:alle-innleggene @tilstand)]
         (vis-innlegg innlegg))) 
      
      (identity
       [:form {:style {:background-color farge-knæsj-gul :padding "1rem" :margin-top "1rem" :margin-bottom "1rem"}
               :hx-post sider/lagre-innlegg
               :hx-swap :htmx/outerHTML
               :hx-target (str "#" (name :id/innlegg-skjema))
               :id :id/innlegg-skjema}
        [:div
         [:input {:type :text
                  :name "overskrift"
                  :placeholder "Overskrift"
                  :style {:width "100%"
                          :font-size "1.0rem"
                          :border 0}}]
         [:textarea {:style {:width "100%"
                             :height "7.2rem"
                             :resize "vertical"
                             :font-size "1rem"
                             :font-family "sans-serif"
                             :border 0}
                     :name "tekst"
                     :placeholder "Tekst"}]]
        [:button "Lagre"]])
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
        ])
      [:div "Enda en ny boks! 
             Denne har ikke linjeskift som funker. "]
      [:div "Dette er en tekst"
       [:br]
       "dette er linjeskift"]
      ]]]))

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

(defn napp2 [_req]
  (println "napp 2"))

(defonce forrige-request (atom nil))

(comment
  @forrige-request
  @tilstand
  (when-let [innlegg (get-in @forrige-request [:params "innlegg"])]
    (swap! tilstand assoc :innlegg innlegg))
  (swap! tilstand dissoc :innlegg)

  (def ny-tilstand (atom {}))
  (reset! ny-tilstand {})
  (swap! ny-tilstand update :alle-innleggene (fnil conj []) (str "Nytt innlegg " (rand-int 100)))
  @ny-tilstand

  (conj '(element) 'ny)
  (conj '[element] 'ny)
  ,)

(defn lagre-innlegg [req]
  (let [{:strs [overskrift tekst]} (:params req)]
    (if (and overskrift tekst)
      (swap! tilstand update :alle-innleggene (fnil conj []) {:overskrift overskrift
                                                              :tekst tekst
                                                              :uuid (random-uuid)
                                                              :tidspunkt (str (Instant/now))})
      (println "Lagret ingenting!")))
  (reset! forrige-request req)
  {:status 200
   :body
   (str
    (html
        [:form {:style {:background-color farge-knæsj-gul :padding "1rem" :margin-top "1rem" :margin-bottom "1rem"}
                :hx-post sider/lagre-innlegg
                :hx-swap :htmx/outerHTML
                :hx-target (str "#" (name :id/innlegg-skjema))
                :id :id/innlegg-skjema}
         [:div "Innlegg lagret! Du kan skrive flere innlegg hvis du vil."
          " " (rand-nth ["🤔" "😼" "🚂" "💃"])]
         [:div
          [:input {:type :text
                   :name "overskrift"
                   :placeholder "Overskrift"
                   :style {:width "100%"
                           :font-size "1.0rem"
                           :border 0}}]
          [:textarea {:style {:width "100%"
                              :height "7.2rem"
                              :resize "vertical"
                              :font-size "1rem"
                              :font-family "sans-serif"
                              :border 0}
                      :name "tekst"
                      :placeholder "Tekst"}]]
         [:button "Lagre"]]))})

(comment
  (:alle-innleggene @tilstand)

  @tilstand

  (reset! tilstand
          {:sagt-hei-ganger 50,
           :innlegg
           "Og kanskje en overskrift med dato og evt header og så en tekstparagrsf",
           :alle-innleggene
           []})
  )

(defn sidevelger [req]
  (let [handler (condp = ((juxt :request-method :uri) req)
                  [:head "/"] ok
                  [:get "/"] hovedside
                  [:post sider/si-hei] si-hei
                  [:post "/knapp2"] napp2
                  [:post sider/lagre-innlegg] lagre-innlegg
                  fant-ingen-side)]
    (prn (merge {:handler handler}
                (select-keys req [:request-method :uri])))
    (handler req)))

(def wrapped-handler
  (-> #'sidevelger
      (wrap-resource "/")
      wrap-params))

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

