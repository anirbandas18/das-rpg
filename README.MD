# Das Fussball RPG

## System requirements
* JDK 21
* Maven 3.9.x
* Permission for the game to write in the current working directory that hosts the game binary

## Build
```
mvn clean package -e
```

## Test
```
mvn test -e
```

## Coverage
```
mvn jacoco:report -e
```

## Run
```
java -jar target/das-fussball-rpg.jar
```

## Usage
You can find detailed instructions on how to play the game in HELP.MD 

## Assumptions
* Character type is randomly assigned by the game when a player creates a character
* No auto save is enabled
* Exploration can be done only on the default map
* Progress on the map can be saved and resumed
* Character can ask for fights in the game
* Gained experienced from fights can be saved and progressed upon
* Losing a fight reduces the gained experience

## MVP
* Player can create a random character
* Player can explore with the chosen character
* Player can save and resume the progress of exploration

## Trade off
* The MVP focuses on exploration and fights as the core feature to preserve the RPG experience. 
* Save and resume functionality is included to support this.
* Fights on a map when exploring is planned for the future but excluded from the MVP to ensure a stable, non-volatile, disconnected 
exploration vs fight experience and establish necessary workflows like progress tracking.
* Explorations can be performed on the default map to avoid map management complexity.
* Fights can fought against the default enemy to avoid enemy management complexity.


## Technical decision records
* Architectural components using ddd
* Feature workflows using mvc
* Business logic creation using singleton
* Component are composed with dependency injection
* State machine to manage game, exploration and fight behaviour

## Extensibility of the project
* Create new character types by adding new values in the CharacterType enum
* Players can choose their favorite character type during character creation by using string based overloaded variant of CharacterTypeService.retrieveCharacterType(String) method
* Map class can be extended to create other types of maps like rectangular, graph
* Map service follows SOLID principles to allow map generations of different types
* RPG API follows SOLID principles to allow more features like choosing a fight
* Default options can be changed or more options added
