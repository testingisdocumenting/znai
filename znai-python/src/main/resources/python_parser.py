#  Copyright 2021 znai maintainers
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#  http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

import ast
import json
import sys
import traceback
import platform
from typing import Dict

content_lines = []
warnings = set([])

# contains a mapping between module alias and full module name
# import fin.money as finm case
# finm -> fin.money
module_name_by_alternative_name: Dict[str, str] = {}

# contains a mapping between import aliases and a full type name
# e.g.
# from fin.money import Money as SuperMoney:
# SuperMoney -> fin.money.Money
#
full_type_name_by_alternative_name: Dict[str, str] = {}


def read_and_parse(file_name):
    global content_lines
    with open(file_name) as file:
        content = file.read()
        content_lines = content.splitlines()
        warnings.clear()

    return ast.parse(content)


def node_to_dict(node_type, name_to_use, node, include_docstring=True):
    try:
        return {
            "type": node_type,
            "name": name_to_use,
            "content": extract_full_lines_content(node_type, name_to_use, node),
            "body_only": extract_body_only(node_type, name_to_use, node),
            "doc_string": ast.get_docstring(node) if include_docstring else None,
        }
    except Exception as e:
        raise Exception(f"Error processing {name_to_use} {node_type}") from e


def function_to_dict(func_node, name):
    func_dict = node_to_dict("function", name, func_node)
    func_dict["args"] = extract_func_args(name, func_node.args)

    return func_dict


def extract_func_args(name: str, args: ast.arguments):
    parsed_args: list[dict[str, str]] = []

    def extract_regular_args():
        for arg in args.args:
            parsed_args.append({
                "name": arg.arg,
                "type": extract_type(arg.annotation),
                "category": "regular"
            })

    def extract_pos_only_args():
        for pos_only in args.posonlyargs:
            parsed_args.append({
                "name": pos_only.arg,
                "type": extract_type(pos_only.annotation),
                "category": "pos_only"
            })

    def extract_vararg():
        vararg = args.vararg
        if vararg is None:
            return

        parsed_args.append({"name": vararg.arg, "type": extract_type(vararg.annotation), "category": "args"})

    def extract_kwarg():
        kwarg = args.kwarg
        if kwarg is None:
            return

        idx = 0
        for kw_only in args.kwonlyargs:
            parsed_args.append({
                "name": kw_only.arg,
                "type": extract_type(kw_only.annotation),
                "defaultValue": extract_default_value(args.kw_defaults[idx]),
                "category": "kw_only"})
            idx += 1

        parsed_args.append({"name": kwarg.arg, "type": "", "category": "kwargs"})

    def extract_default_value(node) -> str:
        default_value_text = extract_inlined_columns_sensitive_content(name + " argument default value", node)
        return default_value_text[1:] if default_value_text.startswith("=") else default_value_text

    # AST provides defaults without precise positional information
    # we go backwards as you can't assign defaults to a first argument and then skip default
    # if we are given 3 defaults and 5 total args, then the last 3 args are matching those defaults
    # these defaults are only for positional args, kwargs has its own default (according to AST)
    def extract_positional_defaults():
        idx = 0
        for default_value in reversed(args.defaults):
            parsed_args[len(parsed_args) - 1 - idx]["defaultValue"] = extract_default_value(default_value)
            idx += 1

    extract_pos_only_args()
    extract_regular_args()
    extract_positional_defaults()

    extract_vararg()
    extract_kwarg()

    return parsed_args


def extract_type(annotation):
    if annotation is None:
        return ""

    if isinstance(annotation, ast.Subscript):
        return extract_subscript_type(annotation)

    if isinstance(annotation, ast.Name):
        return extract_name_type(annotation)

    if isinstance(annotation, ast.Attribute):
        return extract_attribute_type(annotation)

    # AST api changes between python 3.8 and 3.9 :(
    if hasattr(annotation, "value") and isinstance(annotation.value, ast.Name):
        return extract_name_type(annotation.value)

    return ""


def extract_name_type(annotation: ast.Name):
    if annotation.id in full_type_name_by_alternative_name:
        return full_type_name_by_alternative_name[annotation.id]

    return annotation.id


def extract_subscript_type(annotation: ast.Subscript):
    def extract_types():
        # AST api changes between python 3.8 and 3.9 :(
        annotation_slice = annotation.slice.value if hasattr(annotation.slice, "value") else annotation.slice

        if isinstance(annotation_slice, ast.Name):
            return [extract_type(annotation_slice)]

        return [extract_type(type) for type in annotation_slice.elts]

    return {
        "name": extract_type(annotation.value),
        "types": extract_types()
    }


# type like package.module.ClassName is encoded inside Attribute type
def extract_attribute_type(annotation: ast.Attribute):
    def expand_module_name(name: str):
        if name in module_name_by_alternative_name:
            return module_name_by_alternative_name[name]
        else:
            return name

    parts = [expand_module_name(annotation.attr)]
    next_annotation = annotation.value

    while next_annotation is not None:
        if isinstance(next_annotation, ast.Attribute):
            parts.append(next_annotation.attr)
            next_annotation = next_annotation.value
        else:
            parts.append(expand_module_name(next_annotation.id))
            next_annotation = None

    parts.reverse()
    return ".".join(parts)


# extract multiline content for a node, ignoring column start/end data
def extract_full_lines_content(node_type, name_to_use, node):
    if not hasattr(node, "end_lineno"):
        warnings.add(f"skipping content extraction for {name_to_use} {node_type}, only supported with Python 3.8+")
        return None

    return "\n".join(content_lines[(node.lineno - 1): node.end_lineno])


# extract content respecting column start and column end information
# new lines are squashed and each line is trimmed
# e.g. default values
def extract_inlined_columns_sensitive_content(context_desc, node) -> str:
    if not hasattr(node, "end_lineno"):
        warnings.add(f"cannot extract inlined content for {context_desc}, only supported with Python 3.8+")
        return ""

    line_start = node.lineno
    line_end = node.end_lineno
    column_start = node.col_offset
    column_end = node.end_col_offset

    if line_start == line_end:
        line = content_lines[line_start - 1]
        return line[(column_start - 1) : column_end].strip()
    else:
        first_line = content_lines[line_start - 1][column_start - 1:].strip()
        last_line = content_lines[line_end - 1][:column_end].strip()
        middle_lines = content_lines[line_start:line_end - 1]

        return "".join([first_line] + [l.stip() for l in middle_lines] + [last_line])


def partition_assignment_first_line(assignment_node):
    first_line = content_lines[assignment_node.lineno - 1]
    name, _, first_line_content = first_line.partition("=")
    return name, first_line_content


def extract_assignment_value(assignment_node):
    _, first_line_content = partition_assignment_first_line(assignment_node)

    if assignment_node.lineno == assignment_node.end_lineno:
        return first_line_content.strip()

    return first_line_content.strip() + "\n" + "\n".join(
        content_lines[assignment_node.lineno: assignment_node.end_lineno])


def extract_body_only(node_type, name_to_use, node):
    if not hasattr(node, "end_lineno"):
        warnings.add(f"Skipping content extraction for {name_to_use} {node_type}, only supported with Python 3.8+")
        return None

    if node_type == "assignment":
        return extract_assignment_value(node)

    # skip py doc if present
    start_idx = 1 if is_py_doc(node.body[0]) else 0
    end_idx = len(node.body) - 1

    start_line_idx = node.body[start_idx].lineno - 1
    end_line_idx = node.body[end_idx].end_lineno - 1

    return "\n".join(content_lines[start_line_idx: (end_line_idx + 1)])


def is_py_doc(node):
    if hasattr(node, "value") and isinstance(node.value, ast.Constant) \
            and not hasattr(node, "targets") and not hasattr(node, "target"):
        return True

    return False


def class_to_list_of_dict(class_node):
    """
    flatten functions from class to put into the flat list alongside class definition
    :param class_node:
    :return:
    """
    return [node_to_dict("class", class_node.name, class_node)] + \
           [function_to_dict(node, class_node.name + "." + node.name) for node in
            class_node.body if isinstance(node, ast.FunctionDef)]


def parse_assignment(assignment_node):
    if len(assignment_node.targets) != 1:
        # Currently only support single variable assignment, i.e. no tuples, etc.
        return None

    name, _ = partition_assignment_first_line(assignment_node)
    return node_to_dict("assignment", name.strip(), assignment_node.value, include_docstring=False)


def generate_type_mappings_through_imports(import_nodes):
    for node in import_nodes:

        # import fin.money as finm case
        #
        if isinstance(node, ast.Import):
            for name in node.names:
                if name.asname is not None:
                    module_name_by_alternative_name[name.asname] = name.name

        # from fin.money import Money, Debt
        # from fin.money import Money as SuperMoney
        # type_name_by_alternative_name["SuperMoney"] = fin.money.Money
        #
        if isinstance(node, ast.ImportFrom):
            for name in node.names:
                full_name = node.module + "." + name.name
                if name.asname is not None:
                    full_type_name_by_alternative_name[name.asname] = full_name
                else:
                    full_type_name_by_alternative_name[name.name] = full_name


def parse_file(file_to_parse):
    module = read_and_parse(file_to_parse)

    imports = [node for node in module.body if isinstance(node, ast.Import) or isinstance(node, ast.ImportFrom)]
    generate_type_mappings_through_imports(imports)

    function_definitions = [node for node in module.body if isinstance(node, ast.FunctionDef)]
    class_definitions = [node for node in module.body if isinstance(node, ast.ClassDef)]
    variable_assignments = [node for node in module.body if isinstance(node, ast.Assign)]

    file_parse_result = [function_to_dict(f, f.name) for f in function_definitions]

    for class_node in class_definitions:
        dicts = class_to_list_of_dict(class_node)
        for class_dict in dicts:
            file_parse_result.append(class_dict)

    for assignment_node in variable_assignments:
        assignment_dict = parse_assignment(assignment_node)
        if assignment_dict is not None:
            file_parse_result.append(assignment_dict)

    return file_parse_result


def print_parse_completed():
    print("---parse_completed---", flush=True)


while True:
    line = sys.stdin.readline()

    parse_result = None
    error = None
    try:
        parse_result = parse_file(line.strip())
    except:
        error = traceback.format_exc()

    success = error is None
    result = {
        "success": success,
        "warnings": list(warnings),
        "version": platform.python_version(),
        "error": error,
        "result": parse_result
    }
    print(json.dumps(result), flush=True)
    print_parse_completed()
