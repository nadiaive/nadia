(ns nadia.innlegg
  (:require
   [hiccup2.core :refer [html]]
   [babashka.fs :as fs]))

(defn les-alle [filbaner]
  (map (comp slurp fs/file)
       (reverse (sort filbaner))))

(defn render []
  [:html
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
   [:body
    [:h1 "Hei!"]
    [:p "Velkommen til siden min. Her finner du tanker og ideer jeg har. Noen større, noen mindre, noe dypt og noe veldig grunt. Knaskje det ikke er interessant i det hele tatt. I så fall ønsker jeg deg lykke til videre!"]
    (les-alle (fs/list-dir "posts"))
    ]])

(defn side [_request]
  {:status 200
   :header {"Content-Type" "text/html; charset=utf-8"}
   :body (str (html 
               {:escape-strings? false}
               (render)))})
