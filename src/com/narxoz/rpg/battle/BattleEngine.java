package com.narxoz.rpg.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BattleEngine {
    private static BattleEngine instance;
    private Random random = new Random(1L);

    private BattleEngine() {
    }

    public static BattleEngine getInstance() {
        if (instance == null) {
            instance = new BattleEngine();
        }
        return instance;
    }

    public BattleEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public void reset() {
        // reset any battle state if needed
    }

    public EncounterResult runEncounter(List<Combatant> teamA, List<Combatant> teamB) {
        EncounterResult result = new EncounterResult();
        List<Combatant> aliveA = new ArrayList<>(teamA);
        List<Combatant> aliveB = new ArrayList<>(teamB);
        int round = 0;

        result.addLog("Battle started!");
        result.addLog("Team A: " + teamA.size() + " fighters");
        result.addLog("Team B: " + teamB.size() + " fighters");

        while (!aliveA.isEmpty() && !aliveB.isEmpty()) {
            round++;
            result.addLog("\n--- ROUND " + round + " ---");

            // Team A attacks Team B
            for (Combatant attacker : new ArrayList<>(aliveA)) {
                if (aliveB.isEmpty()) break;
                Combatant target = aliveB.get(0);
                int damage = attacker.getAttackPower();
                target.takeDamage(damage);
                result.addLog(attacker.getName() + " hits " + target.getName() + " for " + damage);

                if (!target.isAlive()) {
                    result.addLog(target.getName() + " has died!");
                    aliveB.remove(target);
                }
            }

            // Team B attacks Team A
            for (Combatant attacker : new ArrayList<>(aliveB)) {
                if (aliveA.isEmpty()) break;
                Combatant target = aliveA.get(0);
                int damage = attacker.getAttackPower();
                target.takeDamage(damage);
                result.addLog(attacker.getName() + " hits " + target.getName() + " for " + damage);

                if (!target.isAlive()) {
                    result.addLog(target.getName() + " has died!");
                    aliveA.remove(target);
                }
            }
        }

        result.setRounds(round);
        if (aliveA.isEmpty() && aliveB.isEmpty()) {
            result.setWinner("Draw");
            result.addLog("\nThe battle ended in a draw!");
        } else if (aliveA.isEmpty()) {
            result.setWinner("Team B");
            result.addLog("\nTeam B wins!");
        } else {
            result.setWinner("Team A");
            result.addLog("\nTeam A wins!");
        }

        return result;
    }
}