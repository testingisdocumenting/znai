// @mdoc
// top level comment outside of any function

int func1() {
    // func1 just comment
    action1();

    // @mdoc
    // func1 special comment
    // because it was marked as documentation
    // related
    action2();

    /*@mdoc
    func1 comment that is across
    multiple lines
    */
    action3();
    action4();
}

int class_name::func2() {
    // func2 just comment
    action1();

    // @mdoc
    // func2 special comment
    // because it was marked as documentation
    // related
    action2();

    /*@mdoc
    
    func2 comment that is across
    multiple lines
    */
    action3();
    action4();
}
