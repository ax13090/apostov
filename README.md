# apostov
The title stands for Axel's poker stove.

## About
This is the author's take at creating a piece of software which computes the odds of winning of a set of opposing poker hands.
The rest of this documentation requires basic knowledge of the game of [Texas Holdem Poker](https://en.wikipedia.org/wiki/Texas_hold_'em).

The poker variant is considered to be No-Limit Texas Holdem through all the code and documentation. 

The program takes a set of holecards as input, and computes all the possible boards. All the contesting hands will be matched against each board, which allows us to compute the odds of winning for each hand. In other words, what this program computes is the same thing as the percentages showed in poker television programmes, when the players are all-in before the flop and table (or show) their hands.

For now all that is provided is a library of classes, with a command-line interface but no graphical user-interface.

## Building
The code is pure Java 8, and uses a few dependencies. The building and dependencies is managed by Gradle configuration files. To build it, all you need is a JDK 1.8.
To obtain an über-JAR of the compiled code and all its dependencies, grab a clone and call Gradle from the project root. This should work even if you have no Gradle distribution installed on your machine, thanks to the [Gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html). 
```
git clone 'https://github.com/ax13090/apostov.git'
cd apostov
./gradlew shadowJar
```
## Examples

```
# Let's compare the odds of pocket rockets against 72o.
java -ea -jar build/libs/apostov-all.jar 2c7s AcAs
2c7s    11,02%
AcAs    88,98%
```

## Author and license
This project is written by Axel de Sablet and release into the public domain for use by anyone who finds it useful. 
I would appreciate feedback but of course, there is no requirement for that.
