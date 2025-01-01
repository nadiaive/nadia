(ns nadia.learnsclojure)

;;Kommentarer starter med dobbel semi-kolon. Teodors metakinderegg til meg. 1.1.25 kl 17.34

;;Keywords starter med kolon.

:20px
;;Tall er tall og trenger ikke kolon eller fnutter for å forstås av CLOJURE
0
:sans-serif

;;Dette er et map. Den mapper nøkler (som fontfamilie) til verdier (som Optima, eller arial som er en mer kjent font, men som jeg ikke ville bruke fordi den er litt døll. Hehe.)
{:text-align :center
 :padding-top :20px
 :font-family "Optima, sans-serif"}

;;String har dobbelfnutter rundt seg. Man trenger ikke kolon foran disse når man skal bruke det i et map etter en nøkkel. Strings kan skrives overalt.
"Heihallo dette er en string."
"Optima, sans-serif"

;;Evaluer hele fila med `ctrl+option+c` og så trykker du `enter`