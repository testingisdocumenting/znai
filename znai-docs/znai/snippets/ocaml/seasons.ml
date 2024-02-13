type weather_season =
  | Spring
  | Summer
  | Autumn
  | Winter

let weight_season season =
  match season with
  | Spring -> 4
  | Summer -> 5
  | Autumn -> 3
  | Winter -> -1

let describe_season season =
(** human readable season description *)
  match season with
  | Spring -> "It's springtime! Flowers are blooming."
  | Summer -> "It's summertime! Enjoy the warm weather."
  | Autumn -> "It's autumn! Leaves are changing colors."
  | Winter -> "It's winter! Time to bundle up and enjoy the snow."
