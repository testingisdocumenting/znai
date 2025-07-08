
(**
Domain knowledge of the most important thing in the library.
High level description that contains [User] information and some examples like
{[
  module My_module : sig
    ...
  end
]}
More text.
*)
type t

val act_on_domain : t -> unit Deferred.t