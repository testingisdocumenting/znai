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


def read_and_parse(file_name):
    with open(file_name) as file:
        content = file.read()

    return ast.parse(content)


def node_no_dict(node_type, name_to_use, node):
    return {
        "type": node_type,
        "name": name_to_use,
        "body": extract_body(node),
        "doc_string": ast.get_docstring(node)
    }


def function_to_dict(func_node):
    return node_no_dict("function", func_node.name, func_node)


def extract_body(node):
    # TODO
    # isinstance(node.body[0].value, ast.Constant)

def class_to_list_of_dict(class_node):
    """
    flatten functions from class to put into the flat list alongside class defintion
    :param class_node:
    :return:
    """
    return [node_no_dict("class", class_node.name, class_node)] + \
           [node_no_dict("function", class_node.name + "." + node.name, node) for node in
            class_node.body if isinstance(node, ast.FunctionDef)]


def parse_file(file_to_parse):
    module = read_and_parse(file_to_parse)

    function_definitions = [node for node in module.body if isinstance(node, ast.FunctionDef)]
    class_definitions = [node for node in module.body if isinstance(node, ast.ClassDef)]

    parse_result = [function_to_dict(f) for f in function_definitions]

    for class_node in class_definitions:
        dicts = class_to_list_of_dict(class_node)
        for class_dict in dicts:
            parse_result.append(class_dict)

    print(json.dumps(parse_result), flush=True)
    print_parse_completed()


def print_parse_completed():
    print("---parse_completed---", flush=True)


while True:
    line = sys.stdin.readline()

    try:
        parse_file(line.strip())
    except Exception as e:
        print(e)
        print_parse_completed()
