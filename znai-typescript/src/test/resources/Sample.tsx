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

/**
 * top level doc string
 */
export class Sample {
    /**
     * name of a sample
     */
    firstName: string;

    lastName: string;

    /**
     * method A <b>description</b> and some
     * @param {string} input for <i>test</i>
     */
    methodA(input: string) {
        console.log('method a body');
        console.log('test22');

        const elementA = <Declaration firstName={this.firstName} lastName={this.lastName}/>;
        const elementB = (
            <Declaration
                firstName="placeholder"
                lastName={this.lastName}
            />
        );
    }
}

export interface Props {
    /**
     * first name
     */
    firstName: string;

    /**
     * last name
     */
    lastName?: string;
}

function demo() {
    const elementB = (
        <Declaration
            firstName="placeholder"
            lastName={this.lastName}
        />)
}

function Declaration({firstName, lastName}) {
    return null;
}
