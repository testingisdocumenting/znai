# Doxygen

Znai parses generated [Doxygen](https://www.doxygen.nl/index.html) XML files to extract and embed
* Comments
* Signatures
* Parameters

Use the building blocks to mix and match API documentation with visuals and other artifacts.
Or use the built-in plugins to generate the whole blocks of API reference.

# Setup

Specify `index.xml` location inside `<docroot>/doxygen.json`

:include-file: doxygen.json {autoTitle: true}

Note: To read doxygen XMLs from zip, add path to the zip to [lookup-paths](flow/lookup-paths#zip-and-jar-lookup)
