(ns teodor.plukk
  (:require
   [clojure.string :as str]))

(defn tall-mellom [nedre øvre]
  (range nedre (inc øvre)))

(shuffle (tall-mellom 10 20))
;; => [16 10 19 14 11 18 20 15 17 12 13]

(defn ->tabellrad [xs]
  (str/join "\t" xs))

(->tabellrad [1 2 3])
;; => "1\t2\t3"

(defn ->tabellkolonne [xs]
  (str/join "\n" xs))

(->tabellkolonne [1 2 3])
;; => "1\n2\n3"

(comment
  ;; kjør denne for å lage tekstfil med tall du kan kopiere inn i Excel
  ;; juster på argumentene til tall-mellom for å styre nedre og øvre grense.
  (spit "excel-kolonne.tsv" (->tabellkolonne (shuffle (tall-mellom 20 60))))
  )
