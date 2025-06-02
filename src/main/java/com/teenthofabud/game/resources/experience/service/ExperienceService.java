package com.teenthofabud.game.resources.experience.service;

import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.experience.ExperienceException;

public interface ExperienceService {

    public void gain(Character character, Integer health) throws ExperienceException;

    public Integer show(Character character) throws ExperienceException;

    public boolean available(Character character);

}
