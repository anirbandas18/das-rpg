# DAS RPG

## System requirements
* JDK 21
* Maven 3.9.x

## Build
```
mvn clean package -e
```

## Coverage
```
mvn test -e
```

## Run
```
java -jar target/das-rpg.jar
```
*Note: Further instructions on how to play the game can be found in HELP.MD*

## Extensibility of the project
* Create new character types by adding new values in the CharacterType enum
* Players can choose their favorite character type during character creation by switching the implementation of CharacterTypeService from X to Y 

## Assumptions
* Character type is randomly assigned by the game to the character when a player creates a character 