-- Show an analog clock for your time zone.
--
-- Dependencies:
--   elm install elm/svg
--   elm install elm/time
--
-- For a simpler version, check out:
--   https://elm-lang.org/examples/time
--

import Browser
import Html exposing (Html)
import Svg exposing (..)
import Svg.Attributes exposing (..)
import Task
import Time

-- MAIN


main =
  Browser.element
    { init = init
    , view = view
    , update = update
    , subscriptions = subscriptions
    }



-- MODEL


type alias Model =
  { zone : Time.Zone
  , time : Time.Posix
  }


init : () -> (Model, Cmd Msg)
init _ =
  ( Model Time.utc (Time.millisToPosix 0)
  , Cmd.batch
      [
      ]
  )



-- UPDATE


type Msg
  = Tick Time.Posix
  | AdjustTimeZone Time.Zone



update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  ( model, Cmd.none)



-- SUBSCRIPTIONS


subscriptions : Model -> Sub Msg
subscriptions model =
  Time.every 1000 Tick



-- VIEW


view : Model -> Html Msg
view model =
  svg
    [ viewBox "0 0 400 400"
    , width "400"
    , height "400"
    ]
    [ circle [ cx "200", cy "200", r "120", fill "#1293D8" ] []
    , circle [ cx "150", cy "150", r "25", fill "white"] []
    , circle [ cx "151", cy "150", r "15", fill "hsl(150,60%,50%)"] []
    , circle [ cx "250", cy "150", r "25", fill "white"] []
    , circle [ cx "251", cy "150", r "15", fill "hsl(150,60%,50%)"] []
    , line [ x1 "150", x2 "250", y1 "230", y2 "230", stroke "white", strokeWidth "5%"] []

    ]
