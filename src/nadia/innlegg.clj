(ns nadia.innlegg
  (:require
[hiccup2.core :refer [html]]))

(defn render []
  [:html
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
   [:body
    [:h2 "Hei!"]
    (slurp "posts/2026-02-12.htm")]])

(defn side [_request]
  {:status 200
   :header {"Content-Type" "text/html; charset=utf-8"}
   :body (str (html 
               {:escape-strings? false}
               (render)))})