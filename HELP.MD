# Das Fussball RPG Instructions

## How to interact with the game?

The in gameplay menu is presented to the player with the following options on the console at startup when the game is run.

```
            ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
            ▐          Play           ▌
            ▐=========================▌
            ▐  C - Create character   ▌
            ▐  E - Explore            ▌
            ▐  S - Save game          ▌
            ▐  D - Delete game        ▌
            ▐  R - Resume game        ▌
            ▐  X - Exit game          ▌
            ▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌
```

* Enter option C to create a new character for exploration. You will be asked to enter the player name and the game will assign you a random character type
* Enter option E to start exploring with a character. You can either create a new character and start exploring or resume a saved game to continue exploration
* Enter option S to save the game at the current state of the character's exploration
* Enter option D to delete any saved game
* Enter option R to resume a saved game from the last position that the character explored
* Enter option X to exit the game

## What to expect from the game?

* You can create a character and explore but if you resume a game after creating a new character, any previously saved character and its progress will overwrite your unsaved progress with new character 
* Explicitly save a character and its progress otherwise progress will be lost
* There is only one default map to play the game
* Saving a game will override a previously saved game without warning
* Deleting a game will ask you to confirm your decision before actually deleting
* Resuming a game will overwrite any unsaved character and its progress
* Creating a new character resets previous progress with any other character
* Game should be explicitly saved otherwise progress will be lost
* Player can only explore with a new or saved character

## How to explore with a character?

* Character can explore only the default map
* The progress of the character's exploration in the map can be saved and resumed later

```
            ▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌
            ▐       Explore       ▌
            ▐=====================▌
            ▐  U - Move up        ▌
            ▐  D - Move down      ▌
            ▐  L - Move left      ▌
            ▐  R - Move right     ▌
            ▐  S - Save position  ▌
            ▐  X - Back           ▌
            ▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌
```

* Enter option U to move one step up in the map from the current position
* Enter option D to move one step down in the map from the current position
* Enter option L to move one step left in the map from the current position
* Enter option R to move one step right in the map from the current position
* Enter option S to save the current position in the map
* Enter option X to go back to the game menu

## How does the default map look like?

* The following example is a representation of the default map that comes with this game for a character to explore
* The default map is called 3xGrid and has a dimension of 3x3
* The character starts at the top left corner of the map in a new game or from the last position that it explored when the game was saved

```
+---------+
| 0  .  . |
| .  .  . |
| .  .  . |
+---------+
```

## How much can you explore with a character?

* Exploration is only possible by moving one step at a time in any direction until the edge of the map
* You can roam around freely with your character in any direction on the map
* If you reach the top left corner of the map you can only go down or right from there
* If you reach the top right corner of the map you can only go down or left from there
* If you reach the bottom left corner of the map you can only go up or right from there
* If you reach the bottom right corner of the map you can only go up or left from there
* If you reach the top edge of the map you can only go left, right or down from there
* If you reach the bottom edge of the map you can only go left, right or up from there
* If you reach the left edge of the map you can only go right, up or down from there
* If you reach the right edge of the map you can only go left, up or down from there
