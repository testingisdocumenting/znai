from doc_test_snippets import *


def simple_ops():
    a = 10
    b = a + 15
    ensure(b)

    c = a + b
    ensure(c)

    f = "line"
    f += c * "*"
    ensure(f)

    f = "another"
    ensure(f)


generate_doc(simple_ops)
