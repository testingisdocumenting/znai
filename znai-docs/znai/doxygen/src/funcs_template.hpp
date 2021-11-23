/**
 * \file funcs_template.hpp
 *
 * utilities template functions
 */

/**
 * prints a value and a new line
 * @param v value to print
 * @tparam T type of the value to print
 */
template<typename T>
void println(const T& v) {
    std::cout << v << "\n";
}

// multi_println
/**
 * prints a value and a new line
 * @param v1 value to print
 * @param v2 value to print
 * @tparam T1 type of the value one to print
 * @tparam T2 type of the value two to print
 */
template<typename T1, typename T2>
void multi_println(const T1& v1, const T2& v2) {
    std::cout << v1 << ", " << v2 << "\n";
}
// multi_println

// ignore_template
/**
 * prints a value and a new line
 * @param v1 value to print
 * @param v2 value to print
 * @param v3 value to print
 * @param v4 value to print
 * @tparam T1 type of the value one to print
 * @tparam T2 type of the value two to print
 * @tparam T3 type of the value three to print
 * @tparam T4 type of the value four to print
 */
template<typename T1, typename T2, typename T3, typename T4, typename T5_doc_ignore>
void long_template_func(const T1& v1, const T2& v2, const T3& v3, const T4& v4) {
}
// ignore_template
