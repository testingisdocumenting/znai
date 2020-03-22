/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.znai.java.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.util.List;

public class JavaCode {
    private final JavaCodeVisitor codeVisitor;
    private String fileContent;

    public JavaCode(String fileContent) {
        this.fileContent = fileContent;
        codeVisitor = parse(fileContent);
    }

    public String getFileContent() {
        return fileContent;
    }

    public String getClassJavaDocText() {
        return codeVisitor.getTopLevelJavaDoc();
    }

    public List<EnumEntry> getEnumEntries() {
        return codeVisitor.getEnumEntries();
    }

    public String findJavaDoc(String methodNameWithOptionalTypes) {
        return codeVisitor.findJavaDoc(methodNameWithOptionalTypes);
    }

    public boolean hasType(String typeName) {
        return codeVisitor.hasType(typeName);
    }

    public JavaType findType(String typeName) {
        return codeVisitor.findTypeDetails(typeName);
    }

    public JavaMethod findMethod(String methodNameWithOptionalTypes) {
        return codeVisitor.findMethodDetails(methodNameWithOptionalTypes);
    }

    public List<JavaMethod> findAllMethods(String methodNameWithOptionalTypes) {
        return codeVisitor.findAllMethodDetails(methodNameWithOptionalTypes);
    }

    public JavaField fieldByName(String fieldName) {
        return codeVisitor.findFieldDetails(fieldName);
    }

    private static JavaCodeVisitor parse(String fileContent) {
        CompilationUnit compilationUnit = new JavaParser().parse(fileContent).getResult().get();
        JavaCodeVisitor visitor = new JavaCodeVisitor(fileContent);
        compilationUnit.accept(visitor, "JavaCode");
        return visitor;
    }
}
