(ns nadia.routes
  (:require
   [org.httpkit.server :as server]
   [hiccup2.core :refer [html]]))

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

(defn hamburger []
  (html
   [:html
    [:body {:style {:background-color :grey}}
     [:div {:style {:background-color :dodgerblue}}
      [:h2 {:style {:background-color :pink}}
       "Heiiiiii hallooo"]
      [:p {:style {:background-color :lightgreen
                   :font-size :120px
                   :color :deeppink}}
       "Dette er en skikkelig fin paragraf med masse spennendene tekstterer"]]]]))

(defn handler [req]
  {:headers {"Content-Type" "text/html"}
   :body (str (hamburger))})

(defn run-server []
  (server/run-server #'handler {:port 7777}))

(defonce server (run-server))

(defn klubbvask [])

(comment
  (server)
  (alter-var-root #'server (constantly (run-server)))
  server
  (clojure.repl/doc server/run-server))
