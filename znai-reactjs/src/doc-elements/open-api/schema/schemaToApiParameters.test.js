/*
 * Copyright 2020 znai maintainers
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

import {schemaToApiParameters} from './schemaToApiParameters'
import {checkSchema, customerSchema, idsSchema} from './OpenApiSchema.data.demo'

const expectedCustomerSchemaAsParameters = [
    {name: 'firstName', type: 'string', description: [{"text": "customer's first name", "type": "SimpleText"}]},
    {name: 'id', type: 'integer(int64)', description: [{"text": "customer's unique id", "type": "SimpleText"}]},
    {name: 'lastName', type: 'string', description: [{"text": "customer's last name", "type": "SimpleText"}]},
]

describe('schemaToApiParameters', () => {
    it('array of simple', () => {
        const parameters = schemaToApiParameters(idsSchema)
        expect(parameters).toEqual([{
            name: 'root',
            type: 'array of string'
        }])
    })

    it('array of objects', () => {
        const parameters = schemaToApiParameters(customerSchema)
        expect(parameters).toEqual(expectedCustomerSchemaAsParameters)
    })

    it('nested object', () => {
        const parameters = schemaToApiParameters(checkSchema)

        expect(parameters).toEqual([
            {name: 'id', 'type': 'string', description: [{text: 'id of a customer', type: 'SimpleText'}]},
            {name: 'confirmationIdsLongerTail', 'type': 'array of string'},
            {name: 'customer', type: 'object', description: [{text: 'Customer object', type: 'SimpleText'}], children: expectedCustomerSchemaAsParameters},
            {name: 'customers', type: 'array of objects', children: expectedCustomerSchemaAsParameters}])
    })
})
