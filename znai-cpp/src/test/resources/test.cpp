int main() {
    int test = 2;

    // comment
    // in two lines
    int b = 3;
    int d = 3;

    /*
     * multi line comment
     * of multi lines text
    */
    int e = 5;
}

enum TestEnum1 {
    COLOR1,
    // comments in between
    COLOR2
};

void ClassName::method_name() {
   b= 2;
}

void another() {
}

void another(int a, int boor) {
}

class TestClass2 {
    //
    private:
    int s;

    void testMethod() {
        // inlined method
    }

    //comment
};

/**
 * doxygen style doc at the top of function
 */
int func1() {
    action1();
    action2();
}
