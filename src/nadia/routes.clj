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
   [java.time Instant ZoneId LocalDate LocalTime]))

(def ^{:doc "Her kan vi lagre ting folk gjør!"}
  tilstand
  (duratom :local-file
           :file-path (str (System/getenv "GARDEN_STORAGE") "/duratom.edn")
           :commit-mode :sync
           :init {}))

(def inputfarge-kommentarfelt "#000009")
(def bakgrunnsfarge-kommentarboks "#CCCCFF")

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

(defn linjeskift [s]
  (interpose [:br]
             (str/split-lines s)))

(defn vis-innlegg [innlegg]
  (cond (string? innlegg)
        [:div {:style {:background-color bakgrunnsfarge-kommentarboks :padding "1rem" :margin-top "1rem" :margin-bottom "1rem"}}
         innlegg]
        (and (:overskrift innlegg) (:tekst innlegg))
        [:div {:style {:background-color bakgrunnsfarge-kommentarboks :padding "1rem" :margin-top "1rem" :margin-bottom "1rem"}}
         (when (not-empty (:overskrift innlegg))
           (list
            [:strong (:overskrift innlegg)]
            [:br]))
         (linjeskift (:tekst innlegg))
         (when-let [tidspunkt (:tidspunkt innlegg)]
           (list
            [:br]
            [:em (lokaltid (Instant/parse tidspunkt))]))]))

"Enda en ny boks!
Denne har ikke linjeskift som funker. "

[:div "Dette er en tekst"
 [:br]
 "dette er linjeskift"]

(defn hamburger [_]
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
      [:h1 {:style {:text-align :center
                    :padding-top :20px
                    :padding-bottom :20px
                    :font-family "Optima, sans-serif"
                    :color :white
                    :margin 0}}
       "Velkommen til Nadias hjemmeside!"]
      [:p {:style {:color :white
                   :font-family "Optima, sans-serif"
                   :text-align :center
                   :padding-bottom :20px
                   :font-size :20px
                   :margin-left :100px
                   :margin-right :100px}}
       "Du klarte å ramle inn her! Foreløpig er formålet med denne siden udefinert, så vi kan vel kanskje si at det mest av alt er for at jeg skal lære meg noen nye greier. Vi får se! Du kan uansett legge igjen en kommentar om du vil. Det hadde vært hyggelig!"]
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
       [:form {:style {:background-color bakgrunnsfarge-kommentarboks
                       :padding "1rem"
                       :margin-top "1rem"
                       :margin-bottom "1rem"}
               :hx-post sider/lagre-innlegg
               :hx-swap :htmx/outerHTML
               :hx-target (str "#" (name :id/innlegg-skjema))
               :id :id/innlegg-skjema}
        [:div
         [:input {:type :text
                  :name "overskrift"
                  :placeholder "Ta med en overskrift hvis du føler for det"
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
                     :placeholder "Skriv noe kult"}]]
        [:button "Lagre"]])
      (identity
       [:div {:style {:background-color bakgrunnsfarge-kommentarboks
                      :padding "1rem"}}
        [:div {:style {:font-size "1.2rem" :color inputfarge-kommentarfelt}}
         [:em "trykk for å si hei!"]]
        [:div {:style {:height "0.5rem"}}]
        [:div {:style {:text-align "center" :font-size "4rem" :color "black"}}
         [:button {:hx-post sider/si-hei} "Si hei!"]]
        [:div {:style {:font-size "1.2rem" :color inputfarge-kommentarfelt}}
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
        [:form {:style {:background-color bakgrunnsfarge-kommentarboks :padding "1rem" :margin-top "1rem" :margin-bottom "1rem"}
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

    (handler req)))

(def wrapped-handler
  (-> #'sidevelger
      (wrap-resource "/")
      wrap-params))

(defn run-server []
  (server/run-server #'wrapped-handler {:port 7777}))

(defonce ^:export server (run-server))

(defn ^:export klubbvask [_])

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
