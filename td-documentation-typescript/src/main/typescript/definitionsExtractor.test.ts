import { extractDefinitions } from './definitionsExtractor';

describe('definitionsExtractor', () => {
    it('parses js docs', () => {
        const definitions = extractDefinitions('src/main/typescript/test-data/Sample.ts');

        expect(definitions[0]).toEqual({
            'name': 'Sample',
            'documentation': 'top level doc string',
            'type': 'typeof Sample',
            'members': [{
                'name': 'firstName',
                'type': 'string',
                'documentation': 'name of a sample',
                'kind': 'property'
            }, {'name': 'lastName', 'type': 'string', 'documentation': '', 'kind': 'property'}, {
                'name': 'methodA',
                'kind': 'method',
                'documentation': 'method A <b>description</b> and some',
                'parameters': [{'name': 'input', 'type': ' string', 'documentation': 'for <i>test</i>'}],
                'body': ' {\n        console.log(\'method a body\');\n        console.log(\'test22\');\n    }'
            }]
        });
    });
});