// Place this code where the current search index initialization is
function initializeSearchIndex() {
    if (window.znaiSearchData) {
        window.znaiSearchIdx = window.createLocalSearchIndex();
        window.populateLocalSearchIndexWithData(window.znaiSearchIdx, window.znaiSearchData);
    }
}

// Check immediately in case the data is already there
initializeSearchIndex();

// Set up an observer to watch for changes to window.znaiSearchData
const observer = new MutationObserver((mutations, obs) => {
    if (window.znaiSearchData) {
        initializeSearchIndex();
        obs.disconnect(); // Stop observing once we've initialized
    }
});

// Start observing
observer.observe(document, {
    childList: true,
    subtree: true
});