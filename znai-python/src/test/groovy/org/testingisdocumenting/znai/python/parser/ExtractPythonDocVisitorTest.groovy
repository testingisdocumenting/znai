/*
 * Copyright 2021 znai maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.python.parser

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RuleContext
import org.antlr.v4.runtime.tree.ParseTree
import org.junit.Test

class ExtractPythonDocVisitorTest {
    @Test
    void "todo"() {
        Python3Parser parser = createParser('def method():\n' +
                '  """test doc message\n' +
                '  """\n' +
                '\n' +
                '  print("hello")\n')

//        def visitor = new ExtractPythonDocVisitor()
//        parser.file_input().accept(visitor)

        new AstPrinter().print(parser.file_input())
//        println parser.file_input().toStringTree()
    }

    private static Python3Parser createParser(String code) {
        Python3Lexer lexer = new Python3Lexer(new ANTLRInputStream(code))
        CommonTokenStream tokens = new CommonTokenStream(lexer)

        tokens.consume()

        return new Python3Parser(tokens)
    }


    class AstPrinter {
        public void print(RuleContext ctx) {
            explore(ctx, 0);
        }

        private void explore(RuleContext ctx, int indentation) {
            String ruleName = Python3Parser.ruleNames[ctx.getRuleIndex()];
            for (int i=0;i<indentation;i++) {
                System.out.print("  ");
            }
            System.out.println(ruleName);
            for (int i=0;i<ctx.getChildCount();i++) {
                ParseTree element = ctx.getChild(i);
                if (element instanceof RuleContext) {
                    explore((RuleContext)element, indentation + 1);
                }
            }
        }
    }
}
