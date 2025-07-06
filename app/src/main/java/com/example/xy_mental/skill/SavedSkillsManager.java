package com.example.xy_mental.skill;

import java.util.HashSet;
import java.util.Set;

/**
 * The saved skills manager class that handles storing the saved skill locally in a HashSet.
 */
public class SavedSkillsManager {
    private final Set<String> savedSkills = new HashSet<>();

    private static SavedSkillsManager instance;

    private SavedSkillsManager() {
    }

    public static SavedSkillsManager getInstance() {
        if (instance == null) {
            instance = new SavedSkillsManager();
        }

        return instance;
    }

    /**
     * Gets all the skill badges that the users saved up to nine skills
     *
     * @return a set of string containing all the saved skill badge ids
     */
    public Set<String> getSavedSkills() {
        return savedSkills;
    }
}
