# spider-thesis
Spider Solitaire simulator and game analyzer.

The original use of the spider solitaire game analyzer is described within a thesis available from WesScholar with the following citation and link.

Weisser, Mark S.,"How Many Games of Spider Solitaire are Winnable?" (2012). Graduate Liberal Studies Works (MALS/MPhil). Paper 3.
http://wesscholar.wesleyan.edu/etd_gls/3

## Quick Start

Requires a Java 8 JDK.
A pom.xml is provided to build using maven.

For example, with JAVA_HOME and MAVEN_HOME build using:

    $MAVEN_HOME/bin/mvn clean install

Upon success, analyze a data file:

    $JAVA_HOME/bin/java -Xmx4g -Dusemax=32768 -jar target/spider-1.0.jar src/test/data/MSWinnable.txt

