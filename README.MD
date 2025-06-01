# Das Fussball RPG

## System requirements
* JDK 21
* Maven 3.9.x
* Permission for the game to write in the current directory

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
java -jar target/das-fussball-rpg.jar
```

## Usage
You can find detailed instructions on how to play the game in HELP.MD 

## Extensibility of the project
* Create new character types by adding new values in the CharacterType enum
* Players can choose their favorite character type during character creation by using string based overloaded variant of CharacterTypeService.retrieveCharacterType(String) method  
* Map class can be extended to create other types of maps like rectangular, graph 
* Map service follows SOLID principles to allow map generations of different types
* RPG API follows SOLID principles to allow more features like choosing a fight
* Default options can be changed or more options added

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
To preserve the RPG nature of the game, exploration has been focussed for the MVP as the main feature towards engagement.
To complement it, save and resume functionality for the progress on exploration is also considered to track progress in the MVP.
Fights to gain experience are a new feature set but is out of the MVP scope to provide a wholesome experience in favour of exploration
so that a single feature can be highly feature ready while establishing the supporting workflows like saving and resuming game which would
be important when fights for gaining experience are in the scope because starting over a RPG game from the beginning is not a good experience vs 
extreme high engagement over stability and volatility.