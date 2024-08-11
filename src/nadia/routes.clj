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
    [:body {:style {:background-color :grey
                    :margin 0}}
     [:div {:style {:background-color :dodgerblue
                    :padding-top :px
                    :padding-bottom :8px}}
      [:h2 {:style {:background-color :pink}}
       "Heiiiiii hallooo!!!!!!!!! :D"]
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
      [:p "11.8:"
       [:br]
       "Dette var en fin dag. Jeg spiste frokost og lå i Iladalenparken. Takk for meg."
       ]
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
