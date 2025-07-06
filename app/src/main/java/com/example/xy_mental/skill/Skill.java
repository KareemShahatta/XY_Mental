package com.example.xy_mental.skill;

/**
 * The skill badge object class which we use to create the skills
 */
public class Skill {
    private final int id;
    private final int icon;
    private final int title;
    private final int description;
    private boolean isSaved;

    public Skill(int id, int icon, int title, int description) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.description = description;
    }

    /**
     * Gets the skill id from the skill object
     *
     * @return the skill id of the current skill badge object
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the skill icon from the skill object
     *
     * @return the skill icon of the current skill badge object
     */
    public int getIcon() {
        return icon;
    }

    /**
     * Gets the skill title from the skill object
     *
     * @return the skill title of the current skill badge object
     */
    public int getTitle() {
        return title;
    }

    /**
     * Gets the skill desc from the skill object
     *
     * @return the skill desc of the current skill badge object
     */
    public int getDescription() {
        return description;
    }

    /**
     * Gets the skill saved status from the skill object
     *
     * @return whether the current skill badge object is saved by the user or not
     */
    public boolean isSavedStatus() {
        return isSaved;
    }

    /**
     * Changes the skill saved status for the skill object
     *
     * @param value whether the user wants to save this skill or unsave it
     */
    public void setSavedStatus(boolean value) {
        this.isSaved = value;
    }
}
