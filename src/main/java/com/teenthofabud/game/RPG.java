package com.teenthofabud.game;

import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.constants.charactertype.service.impl.DefaultCharacterTypeServiceImpl;
import com.teenthofabud.game.controller.MainMenuException;
import com.teenthofabud.game.controller.MainMenuAPI;
import com.teenthofabud.game.controller.impl.DefaultMainMenuController;
import com.teenthofabud.game.persistence.FileManager;
import com.teenthofabud.game.persistence.impl.DefaultCheckpointFileManagerImpl;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.checkpoint.Checkpoint;
import com.teenthofabud.game.resources.player.service.PlayerService;
import com.teenthofabud.game.resources.character.service.impl.DefaultCharacterServiceImpl;
import com.teenthofabud.game.resources.player.service.DefaultPlayerServiceImpl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class RPG {

    /*

    private static final String PLAYER_MENU_OPTIONS = """
            P - New player
            F - Find player
            """;

*/

    private static final String CHECKPOINT_DATA_DIRECTORY_NAME = "das-rpg";
    private static final String CHECKPOINT_DATA_FILE_NAME = "checkpoint.data";

    public void writeObjectToFile(Checkpoint checkpoint, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(checkpoint);
            oos.flush();
        }
    }

    public Optional<Checkpoint> readObjectFromFile(File file) throws IOException, ClassNotFoundException {
        Optional<Checkpoint> optionalCheckpoint = Optional.empty();
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Checkpoint checkpoint = (Checkpoint) ois.readObject();
            optionalCheckpoint = Optional.of(checkpoint);
        }
        return optionalCheckpoint;
    }

    private byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        ObjectOutputStream ois = new ObjectOutputStream(boas);
        ois.writeObject(object);
        return boas.toByteArray();
    }

    private Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        InputStream is = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(is);
        return ois.readObject();
    }

    public Optional<Checkpoint> findCheckpoint() {
        Optional<Checkpoint> optionalCheckpoint = Optional.empty();
        Path dataPath = Paths.get(System.getProperty("user.dir"), CHECKPOINT_DATA_DIRECTORY_NAME, CHECKPOINT_DATA_FILE_NAME);
        try {
            if(Files.exists(dataPath) && !Files.isDirectory(dataPath)) {
                byte[] rawBytes = Files.readAllBytes(dataPath);
                Checkpoint checkpoint = (Checkpoint) deserialize(rawBytes);
                optionalCheckpoint = Optional.of(checkpoint);
            } else if(!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return optionalCheckpoint;
    }

    public void saveCheckpoint(Checkpoint checkpoint) {
        Path dataPath = Paths.get(System.getProperty("user.dir"), CHECKPOINT_DATA_DIRECTORY_NAME, CHECKPOINT_DATA_FILE_NAME);
        try {
            if(Files.exists(dataPath)) {
                byte[] rawBytes = serialize(checkpoint);
                Files.write(dataPath, rawBytes);
            } else {
                throw new RuntimeException(dataPath + " does not exist");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            FileManager<Checkpoint> checkpointFileManager = DefaultCheckpointFileManagerImpl.getInstance(Optional.empty());
            PlayerService playerService = DefaultPlayerServiceImpl.getInstance();
            CharacterTypeService characterTypeService = DefaultCharacterTypeServiceImpl.getInstance();
            CharacterService characterService = DefaultCharacterServiceImpl.getInstance();
            MainMenuAPI mainMenuAPI = DefaultMainMenuController.getInstance(stdin, playerService, characterTypeService, characterService, checkpointFileManager);
            RPG rpg = new RPG();
            Character character = null;
            Checkpoint checkpoint = null;
            while(true) {
                System.out.print(mainMenuAPI.getOptions());
                String option = stdin.readLine();
                switch (option.toUpperCase()) {
                    case "C" -> {
                        character = mainMenuAPI.createCharacter();
                    }
                    case "S" -> {
                        checkpoint = new Checkpoint.Builder().character(character).build();
                        mainMenuAPI.saveGame(checkpoint);
                    }
                    case "R" -> {
                        Optional<Checkpoint> optionalCheckpoint = mainMenuAPI.resumeGame();
                        if(optionalCheckpoint.isPresent()) {
                            checkpoint = optionalCheckpoint.get();
                        }
                    }
                    case "X" -> mainMenuAPI.exitGame();
                    default -> System.err.println("Option " + option + " not supported. Try again!");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MainMenuException e) {
            System.err.println("RPG failure: " + e.getMessage());
        }
    }
}