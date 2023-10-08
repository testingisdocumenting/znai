module ModelZero : sig
  type t
end

module ModelA : sig
  type t
  val calc: t -> int
  module Nested : sig
    type t
  end
end

module ModelC : sig
 type t
end