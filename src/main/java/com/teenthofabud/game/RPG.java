package com.teenthofabud.game;

import com.teenthofabud.game.constants.charactertype.CharacterTypeException;
import com.teenthofabud.game.constants.charactertype.CharacterType;
import com.teenthofabud.game.constants.charactertype.service.CharacterTypeService;
import com.teenthofabud.game.constants.charactertype.service.impl.DefaultCharacterTypeServiceImpl;
import com.teenthofabud.game.resources.character.CharacterException;
import com.teenthofabud.game.resources.player.PlayerException;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.player.Player;
import com.teenthofabud.game.resources.character.service.CharacterService;
import com.teenthofabud.game.resources.player.service.PlayerService;
import com.teenthofabud.game.resources.character.service.impl.DefaultCharacterServiceImpl;
import com.teenthofabud.game.resources.player.service.DefaultPlayerServiceImpl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class RPG {

    private static final String MAIN_MENU_OPTIONS = """
            Main Menu:
            ====================
            C - Create character
            X - Exit game
            Enter your option:  
            """;

    private static final String CHARACTER_MENU_OPTIONS = """
            Character Menu:
            =====================
            S - Striker
            M - Midfielder
            D - Defender
            R - Referee
            G - Goalkeeper
            N - Random selection
            X - Back to main menu
            Enter your option:  
            """;

    private static final String PLAYER_MENU_OPTIONS = """
            P - New player
            F - Find player
            """;

    private static final String CHECKPOINT_DATA_DIRECTORY_NAME = "das-rpg";
    private static final String CHECKPOINT_DATA_FILE_NAME = "checkpoint.data";

    private static BufferedReader STDIN = new BufferedReader(new InputStreamReader(System.in));
    private static PlayerService PLAYER_SERVICE = new DefaultPlayerServiceImpl();
    private static CharacterTypeService CHARACTER_TYPE_SERVICE = new DefaultCharacterTypeServiceImpl();
    private static CharacterService CHARACTER_SERVICE = new DefaultCharacterServiceImpl();

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

    public void createCharacter() {
        try {
            System.out.println("Enter player name: ");
            Player player = PLAYER_SERVICE.createPlayer(STDIN.readLine());
            CharacterType type = CHARACTER_TYPE_SERVICE.retrieveCharacterType(player.hashCode());
            Character character = CHARACTER_SERVICE.createCharacter(player, type);
            System.out.println("Created character: " + character);
        } catch (PlayerException | CharacterException | CharacterTypeException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Game failure: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {

            RPG rpg = new RPG();

            Optional<Checkpoint> optionalCheckpoint = rpg.findCheckpoint();
            while(true) {
                System.out.print(MAIN_MENU_OPTIONS);
                String option = STDIN.readLine();
                switch (option.toUpperCase()) {
                    case "C" -> rpg.createCharacter();
                    case "R" -> System.out.println("Resuming game from last saved checkpoint");
                    case "S" -> System.out.println("Saving game....");
                    case "X" -> {
                        System.out.println("Exiting game....");
                        System.exit(0);
                    }
                    default -> System.err.println("Option " + option + " not supported. Try again!");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}