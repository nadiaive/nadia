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
    [:h2 "Hei!"]
    (les-alle (fs/list-dir "posts"))
    ]])

(defn side [_request]
  {:status 200
   :header {"Content-Type" "text/html; charset=utf-8"}
   :body (str (html 
               {:escape-strings? false}
               (render)))})
