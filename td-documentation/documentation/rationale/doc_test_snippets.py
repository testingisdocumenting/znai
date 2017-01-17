import inspect
import json
import trace

doc_last_ensure_line = 0
doc_function_start_lineno = 0
doc_function_lines = []
doc_snippet_results = []


# TODO handle function stretched into multiple lines. Do sort of regexp till a closing parentheses
def ensure(value):
    global doc_last_ensure_line
    global doc_snippet_results
    global doc_function_lines
    global doc_function_start_lineno

    previous_frame = inspect.currentframe().f_back

    print("----")
    print(previous_frame.f_code)
    print(previous_frame.f_code.co_firstlineno)
    print("----")

    doc_function_lines = inspect.getsourcelines(previous_frame.f_code)[0]
    doc_function_start_lineno = previous_frame.f_code.co_firstlineno

    ensure_lineno = inspect.currentframe().f_back.f_lineno
    print("value " + str(value) + " @ " + str(ensure_lineno))

    print(doc_function_lines)
    print(doc_function_start_lineno)

    snippet_start_lineno = doc_last_ensure_line + 1
    snippet_end_lineno = ensure_lineno - doc_function_start_lineno

    print(str(snippet_start_lineno) + ":" + str(snippet_end_lineno))

    snippet = extract_code_snippet(snippet_start_lineno, snippet_end_lineno)
    print(snippet)

    doc_last_ensure_line = snippet_end_lineno

    snippet_result = {'snippet': snippet, 'result': str(value)}
    print(snippet_result)

    doc_snippet_results.append(snippet_result)


def extract_code_snippet(begin_idx, end_idx):
    lines = doc_function_lines[begin_idx:end_idx]
    number_of_spaces_to_trim = min_number_of_trailing_spaces(lines)
    trimmed = trim(lines, number_of_spaces_to_trim)

    return "\n".join(trimmed).strip()


def trim(lines, number_of_spaces_to_trim):
    trimmed_lines = [line[number_of_spaces_to_trim:].rstrip() for line in lines]
    return trimmed_lines


def min_number_of_trailing_spaces(lines):
    if not lines:
        return 0

    return min([number_of_trailing_spaces(line) for line in lines if line.strip()])


def number_of_trailing_spaces(text):
    return len(text) - len(text.lstrip(' '))


def trace_call(frame, event, arg):
    print(frame.f_code.co_filename + " " + str(frame.f_lineno))

    if frame.f_code.co_filename != __file__:
        return

    global doc_function_start_lineno
    doc_function_start_lineno = frame.f_lineno

    print(str(doc_function_start_lineno) + " " + str(frame.f_code))

    # print(event)
    # print(arg)
    # print("\n")


def generate_doc(func):
    global doc_function_lines
    doc_function_lines = inspect.getsourcelines(func)[0]

    print(doc_function_lines)

    print(inspect.getfullargspec(func))

    func()

    trace.Trace()

    result_name = func.__name__
    with open(result_name + '.json', 'w') as outfile:
        json.dump(doc_snippet_results, outfile)
        print(doc_snippet_results)
