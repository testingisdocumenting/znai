(* This is a simple OCaml function *)
let add x y = x + y

(* This is a more complex function
   that demonstrates multiple parameter handling
   and return value documentation
   
   @param a the first integer
   @param b the second integer
   @return the sum of a and b
*)
let multiply a b = a * b

(* Single line comment *)
let subtract x y = x - y

(* Multi-line comment block
   with detailed documentation
   
   This function calculates the factorial of a number
   using recursive approach.
   
   Example usage:
   - factorial 5 returns 120
   - factorial 0 returns 1
*)
let rec factorial n =
  if n <= 1 then 1
  else n * factorial (n - 1)