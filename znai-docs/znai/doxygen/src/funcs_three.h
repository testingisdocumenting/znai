namespace utils {
namespace nested {
/**
 * @param one instance of my class
 * @param two instance of another class
 */
utils::second::MyClass another_func(const utils::second::MyClass& one, const utils::second::AnotherClass& two) {
}

utils::second::MyClass func_with_pointers(const utils::second::MyClass* one, const utils::second::AnotherClass* two) {
}

void my_func(bool second_param) {
}
}
}
