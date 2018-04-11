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
