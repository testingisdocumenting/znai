# Clone Project

```cli
git clone https://github.com/testingisdocumenting/znai.git
```

# Build Tools Setup

## SDK Man

Easy way to maintain Java specific dependencies below is by using [SDKMAN](https://sdkman.io)

## Java And Maven 

You need Java 17 and Maven to build Znai.

```cli 
sdk install java 17.0.13-amzn 
sdk install maven
```

## Graphviz

Diagram features depend on its presence of [dot](http://www.graphviz.org/) when build generates documentation for Znai.

Use [brew](https://brew.sh) to install [GraphViz](http://www.graphviz.org/)

```cli
brew install graphviz
```

## Firefox

Some automated tests in znai are using [WebTau](https://testingisdocumenting.org/webtau/getting-started/what-is-this),
which in turn use Selenium WebDriver and firefox.

A firefox executable has to be on the system path.

Use [brew](https://brew.sh) to install [Firefox](https://www.mozilla.org/en-US/firefox/).

```cli
brew install firefox
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
