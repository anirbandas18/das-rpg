package com.teenthofabud.game.resources.experience.service.impl;

import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.experience.ExperienceException;
import com.teenthofabud.game.resources.experience.service.ExperienceService;

import java.util.HashMap;
import java.util.Map;

public class DefaultExperienceServiceImpl implements ExperienceService {

    private Map<Character, Integer> experienceCollection;

    @Override
    public void gain(Character character, Integer health) throws ExperienceException {
        if(character == null) {
            throw new ExperienceException("character is required");
        }
        if(health == null) {
            throw new ExperienceException("health is required");
        }
        Integer newHealth = 0;
        if(experienceCollection.containsKey(character)) {
            newHealth = health + experienceCollection.get(character);
        }
        experienceCollection.put(character, newHealth);
    }

    @Override
    public Integer show(Character character) throws ExperienceException {
        if(character == null) {
            throw new ExperienceException("character is required");
        }
        if(experienceCollection.containsKey(character)) {
            return experienceCollection.get(character);
        } else {
            throw new ExperienceException(character + " not available");
        }
    }

    @Override
    public boolean available(Character character) {
        if(character == null) {
            return false;
        }
        return experienceCollection.containsKey(character);
    }

    private static volatile ExperienceService INSTANCE;

    private DefaultExperienceServiceImpl() {
        this.experienceCollection = new HashMap<>();
    }

    public static ExperienceService getInstance() {
        ExperienceService result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultExperienceServiceImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultExperienceServiceImpl();
            }
            return INSTANCE;
        }
    }
}
