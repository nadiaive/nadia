(ns nadia.routes
  (:require
   [org.httpkit.server :as server]
   [hiccup2.core :refer [html]])
  (:import
   [java.time Instant Duration]))

(def last-snus-time
  (Instant/parse "2024-09-04T22:00:00.0Z"))

#_(.toHours (Duration/between last-snus-time (Instant/now)))

3
"Nadia"
str
(str "Nadia" 5)
(str "Nadia er best")
(defn er-best [navn]
  (str navn " er best"))
(er-best "Teodor")

(def teodor-navn "Gimle")
"teodor-navnt"
teodor-navn

(html [:div
       [:p]])

+
(* 1 2 3 4)

(defn hamburger [current-time]
  (html
      [:html
       [:body {:style {:background-color :grey}}
        [:div {:style {:background-color :dodgerblue}}
         [:h2 {:style {:background-color :pink}}
          "Heiiiiii hallooo"]
         [:p {:style {:background-color :lightgreen
                      :font-size :120px
                      :color :deeppink}}
          "Dette er en skikkelig fin paragraf med masse spennendene tekstterer"]
         [:p "Det er "
          [:strong (.toHours (Duration/between last-snus-time current-time))
           " timer"]
          " siden siste snus."]]]]))

#_(hamburger (Instant/now))

(defn handler [req]
  {:headers {"Content-Type" "text/html"}
   :body (str (hamburger (Instant/now)))})

(defn run-server []
  (server/run-server #'handler {:port 7777}))

(defonce server (run-server))

(defn klubbvask [x])

(comment
  (server)
  (alter-var-root #'server (constantly (run-server)))
  server
  (clojure.repl/doc server/run-server))
