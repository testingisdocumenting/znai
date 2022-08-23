# Clone Project

```cli
git clone https://github.com/testingisdocumenting/znai.git
```

# Build Tools Setup

## SDK Man

Easy way to maintain Java specific dependencies below is by using [SDKMAN](https://sdkman.io)

## Java And Maven 

You need Java 8 and Maven to build Znai.

```cli 
sdk install java 8.0.342-amzn 
sdk install maven
```

## Graphviz

Diagram features depend on its presence of [dot](http://www.graphviz.org/) when build generates documentation for Znai.

Use [brew](https://brew.sh) to install [GraphViz](http://www.graphviz.org/)

```cli
brew install graphviz
```

# Build

```cli
cd znai
mvn clean install
```

# Run Local Version

```cli
cd znai-dist/target
unzip dist-znai.zip 
export PATH=$(pwd)/dist:$PATH
znai --version
```
