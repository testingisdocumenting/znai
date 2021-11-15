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

package org.testingisdocumenting.znai.java.parser

import org.junit.Test

class JavaIdentifierTest {
    @Test
    void "should match by name"() {
        def id = new JavaIdentifier(["MyClass"], "varName", "")
        id.matches("varName").should == true
        id.matches("wrongVarName").should == false
    }

    @Test
    void "should match by name when no parent is registered"() {
        def id = new JavaIdentifier([], "varName", "")
        id.matches("varName").should == true
        id.matches("wrongVarName").should == false
    }

    @Test
    void "should match by full or partially full name"() {
        def id = new JavaIdentifier(["MyClass", "InnerClass"], "varName", "")
        id.matches("InnerClass.varName").should == true
        id.matches("MyClass.varName").should == false

        id.matches("MyClass.InnerClass.varName").should == true
        id.matches("MyClass.WrongClass.varName").should == false
    }

    @Test
    void "should get full name and name without first parent"() {
        def id = new JavaIdentifier(["MyClass", "InnerClass"], "varName", "")
        id.getName().should == "varName"
        id.getFullName().should == "MyClass.InnerClass.varName"
        id.getFullNameWithoutFirstParent().should == "InnerClass.varName"
    }

    @Test
    void "should match by name with types"() {
        def id = new JavaIdentifier(["MyClass"], "methodName", "(int,String)")
        id.matches("methodName").should == true
        id.matches("methodName(int,String)").should == true
        id.matches("wrongVarName").should == false
        id.matches("wrongVarName(int,String)").should == false
    }

    @Test
    void "should match by full or partially full name with types"() {
        def id = new JavaIdentifier(["MyClass", "InnerClass"], "methodName", "(int,String)")
        id.matches("InnerClass.methodName(int,String)").should == true
        id.matches("MyClass.methodName(int,String)").should == false

        id.matches("MyClass.InnerClass.methodName(int,String)").should == true
        id.matches("MyClass.WrongClass.methodName(int,String)").should == false
    }

    @Test
    void "should get full name and name without first parent when types are present"() {
        def id = new JavaIdentifier(["MyClass", "InnerClass"], "varName", "(int,String)")
        id.getName().should == "varName(int,String)"
        id.getFullName().should == "MyClass.InnerClass.varName(int,String)"
        id.getFullNameWithoutFirstParent().should == "InnerClass.varName(int,String)"
    }
}
