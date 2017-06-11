import { configure } from '@kadira/storybook';

function loadStories() {
  require('../src/report');
}

configure(loadStories, module);
