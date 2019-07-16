import { ElementsLibrary } from './ElementsLibrary';

export interface DocElementProps {
    elementsLibrary: ElementsLibrary;
    type: string;
    content: DocElementProps[];
}