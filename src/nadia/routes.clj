(ns nadia.routes
  (:require
   [nadia.sider :as sider]
   [nadia.innlegg :as innlegg]
   [nadia.gammel :as gammel]
   [org.httpkit.server :as server]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.resource :refer [wrap-resource]]))

(defn sidevelger [req]
  (let [handler (condp = ((juxt :request-method :uri) req)
                  [:head "/"] gammel/ok
                  [:get "/"] innlegg/side
                  [:get "/gammel"] gammel/hovedside
                  [:post sider/si-hei] gammel/si-hei
                  [:post "/knapp2"] gammel/napp2
                  [:post sider/lagre-innlegg] gammel/lagre-innlegg
                  gammel/fant-ingen-side)]

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
