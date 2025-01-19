# Plan

- ✓ Rydd opp: skal hete "innlegg", ikke "tekst"
- ✓ Legg til uuid på innlegg
- ✓ Legg til tidspunkt på innlegg
- ✓ Mat
- ✓ Tur eller noe sånt som ikke er kode

# Hvordan starte - Nadia

- Finne riktig mappe å åpne
    - Sjekk under recents i vscode for eksempel

# Hvordan starte - Teodor

1. Åpne VSCode og lukk terminalen som kommer opp
2. Åpne `nadia`-prosjektet i VSCode
3. Åpne README.md
4. Hent nye endringer med Source Control i VSCode
5. Åpne en terminal i `nadia`-mappen
    - Bruk Kitty :)
6. Kjør `garden run` fra terminalen
7. Trykk på URL-en i terminalen for å se nettsiden lokalt.

# Hvordan koble til REPL

1. Åpne `routes.clj` (eventuelt en annen clojure-fil)
2. `Cmd+Shift+P`
3. Calva: Connect to a running REPL server in the project
4. Velg `deps.edn`
5. Velg port :6666 (Port of the beast - sitat Teodor 1.1.24 16.56)

Du kan nå bruke `Option+Enter` til å evaluere Clojure-uttrykk.

# Hvordan få ut endringer

1. Bruk alltid `option+enter`og sjekk hvordan det ser ut lokalt før du lagrer
2. Lagre alle filer
3. Commit alt (fra Source Control i VSCode)
4. `git push`
5. `garden deploy` 
6. Eller sjekk Teodors juksekode under som du bruker i terminalen:

Teodors juksekode:

1. `ctrl+c` for å stoppe
2. `git push && garden deploy && garden run`
3. `Cmd+Shift+P`
4. Calva: Connect to a running REPL server in the project
5. Velg `deps.edn`
6. Velg port :6666 (Port of the beast - sitat Teodor 1.1.24 kl 16.56)

# Hvordan endre navn på en variabel

1. Refactor rename (F2 på VSCode)
2. Evaluer hele fila med `ctrl+option+c` og så trykker du `enter`

# Mål 1.1.25

- Lage en ordentlig overskrift (h1?)
    - ✓ Endre teksten
    - ✓ Fjerne bakgrunnsfargen og tekstfargen
    - ✓ Midtstille skriften
    - ✓ Endre fonttype
    - ✓ Fjerne smilefjeset
    
# 3.1.25

- ✓ Skrive en brødtekst under overskriften om denne siden
    - Skriv teksten
    - Lag styling

# 19.1.25

- ✓ Endre bilde til noe annet
- ✓ Lage en basic logo

# Oppgaver til neste gang

- Hvis du tør, gjør noe nytt

# Ideer på sikt

- Kunne minimere kommentarene så man ikke får en evig scroll
- Få de nyeste kommentarene først
- sende folk som trykker på "hadet"-knappen til en tilfeldig URLOG-side (https://urlog.apps.garden/)
