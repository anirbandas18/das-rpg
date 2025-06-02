package com.teenthofabud.game.engine.fight.impl;

import com.teenthofabud.game.constants.action.Action;
import com.teenthofabud.game.constants.action.ActionException;
import com.teenthofabud.game.constants.action.service.ActionService;
import com.teenthofabud.game.constants.status.FightState;
import com.teenthofabud.game.engine.fight.FightException;
import com.teenthofabud.game.engine.fight.FightSimulation;
import com.teenthofabud.game.engine.renderer.RenderingService;
import com.teenthofabud.game.resources.character.Character;
import com.teenthofabud.game.resources.enemy.Enemy;

import java.util.Random;

public class DefaultFightSimulationImpl implements FightSimulation {

    private RenderingService renderingService;
    private ActionService actionService;
    private Character character;
    private Enemy enemy;
    private int characterHealth;
    private int enemyHealth;
    private FightState state;

    @Override
    public void start(Character character, Integer experience, Enemy enemy) throws FightException {
        if(enemy == null) {
            throw new FightException("enemy is required");
        }
        if(character == null) {
            throw new FightException("is missing character");
        }
        this.enemy = enemy;
        this.character = character;
        this.characterHealth = experience;
        this.enemyHealth = enemy.getStrength();
        this.state = FightState.NEW;
        this.renderingService.info(character + " will fight with enemy " + enemy.getName());
    }

    private int enemyPower() {
        Long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        int randomNumber = random.ints(1, enemy.getStrength() + 1)
                .findFirst()
                .getAsInt();
        return randomNumber;
    }

    private boolean simulate(Action action) {
        state = state.compareTo(FightState.NEW) == 0 ? FightState.IN_PROGRESS : state;
        boolean success = true;
        int result = action.getPower() - enemyPower();
        characterHealth = characterHealth + result;
        if(result > 0) {
            enemyHealth = enemyHealth - result;
        } else {
            success = false;
        }
        if(characterHealth <= 0) {
            characterHealth = 0;
            state = FightState.LOSE;
        } else if (enemyHealth <= 0) {
            state = FightState.WIN;
        }
        return success;
    }

    @Override
    public boolean act(String actionKey) throws FightException {
        boolean success = true;
        try {
            switch (state) {
                case NEW, IN_PROGRESS -> {
                    Action action = actionService.retrieveAction(actionKey);
                    success = simulate(action);
                }
            }
        } catch (ActionException e) {
            throw new FightException(e.getMessage());
        }
        return success;
    }

    @Override
    public int experience() {
        return characterHealth;
    }

    @Override
    public FightState status() {
        switch (state) {
            case WIN -> renderingService.success(character + " has won over enemy " + enemy.getName());
            case LOSE -> renderingService.error(character + " has been defeated by enemy " + enemy.getName());
            case IN_PROGRESS -> renderingService.info(character + " is fighting with " + enemy + " with health " + characterHealth);
        }
        return state;
    }

    @Override
    public void stop() {
        this.character = null;
        this.characterHealth = 0;
        this.enemyHealth = 0;
        this.enemy = null;
        this.state = FightState.NEW;
    }

    private static volatile FightSimulation INSTANCE;

    private DefaultFightSimulationImpl(RenderingService renderingService, ActionService actionService) {
        this.renderingService = renderingService;
        this.actionService = actionService;
    }

    public static FightSimulation getInstance(RenderingService renderingService,
                                                 ActionService actionService) {
        FightSimulation result = INSTANCE;
        if (result != null) {
            return result;
        }
        synchronized(DefaultFightSimulationImpl.class) {
            if (INSTANCE == null) {
                INSTANCE = new DefaultFightSimulationImpl(renderingService, actionService);
            }
            return INSTANCE;
        }
    }
}
