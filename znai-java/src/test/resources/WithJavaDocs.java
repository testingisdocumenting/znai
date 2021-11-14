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

class HelloWorld {

    /**
     * variable level java doc {@link package.Class}
     */
    private static String SAMPLE_CONST_WITH_LINK = "Sample";

    /**
     * variable level java doc
     */
    private static String SAMPLE_CONST = "Sample";

    /**
     * outer field description
     */
    private int myField;

    /**
     * method level java doc {@link package.Class}
     * @param test test param {@link package.Param}
     */
    public void sampleMethod(String test) {
        statement1();
        statement2();

        if (logic) {
            doAction();
        }
    }

    /**
     * overloaded method java doc
     * @param test test param
     * @param name name of the param
     * @return list of samples
     */
    public List<String> sampleMethod(String test,
                                     List<String> name) {
        statement3();
        statement4();
    }

    private class InnerClass {
        /**
         * inner field description
         */
        private int myField;

        /**
         * inner class method level java doc {@link package.Class}
         * @param test test param {@link package.Param}
         */
        public void sampleMethod(String test) {
            statement1();
            statement2();

            if (logic) {
                doAction();
            }
        }
    }
}