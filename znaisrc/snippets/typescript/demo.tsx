import { PrimaryButton } from 'my-lib';

function buttonsDemo(registry) {
    registry
        .add('primary', <PrimaryButton label="Click Me"/>)
        .add('primary disabled', <PrimaryButton label="Click Me" disabled={true}/>);
}
