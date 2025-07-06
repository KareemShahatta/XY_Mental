package com.example.xy_mental.skill;

import com.example.xy_mental.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The skills manager class that handles initializing and storing the skills locally in a HashMap.
 * NOTE: We create the all the skill badges once when the singleton constructor is called at first.
 */
public class SkillManager {
    private final Map<Integer, Skill> skills = new HashMap<>();

    private static SkillManager instance;

    /**
     * Singleton constructor that creates and loads all the application's skill badges in the map.
     */


    private SkillManager() {
        // Add a new skill instance here with icon, title, and description
        int i = 0;
        skills.put(i, new Skill(i++, R.drawable.icon_placeholder, R.string.demo_title, R.string.demo_desc));
    }

    public static SkillManager getInstance() {
        if (instance == null) {
            instance = new SkillManager();
        }

        return instance;
    }

    /**
     * Gets all the skill badges that exist in the application
     *
     * @return a map of all the integral skill badge ids and their respective skill badge object
     */
    public Map<Integer, Skill> getSkills() {
        return skills;
    }
}
