package com.example.xy_mental.settings;

import android.graphics.Color;

import com.example.xy_mental.R;

/**
 * The theme manager handles everything related to the application color and style
 */
public final class ThemeManager {
    private AppColor color = AppColor.BLUE;
    private boolean hasTrueDarkMode = false;
    private static ThemeManager instance;

    private ThemeManager() {
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }

        return instance;
    }

    /**
     * Returns the color style associate with the current AppColor
     */
    public int getColorTheme() {
        switch (color) {
            case BLUE:
                return R.style.XY_Blue;
            case GREEN:
                return R.style.XY_Green;
            case ORANGE:
                return R.style.XY_Orange;
            case PURPLE:
                return R.style.XY_Purple;
        }
        return R.style.XY_Blue;
    }

    /**
     * Changes the current AppColor
     *
     * @param themeColor the desired color the user wants
     */
    public void setColor(AppColor themeColor) {
        this.color = themeColor;
    }

    /**
     * Gets the current AppColor
     *
     * @return the current AppColor
     */
    public AppColor getColor() {
        return color;
    }

    /**
     * Gets the current theme background color to reflect if amoled mode is selected or not
     *
     * @return a black color if amoled mode is selected or a transparent color if it is not.
     */
    public int getBackgroundColor() {
        if (hasTrueDarkMode) {
            return Color.BLACK;
        } else {
            return Color.TRANSPARENT;
        }
    }

    /**
     * Changes the amoled mode selection status
     *
     * @param value whether the user wants amoled mode on or off
     */
    public void setTrueDarkModeSelectionStatus(boolean value) {
        this.hasTrueDarkMode = value;
    }

    /**
     * Gets the amoled mode selection status
     *
     * @return whether or not the amoled mode is selected
     */
    public boolean getTrueDarkModeSelectionStatus() {
        return hasTrueDarkMode;
    }

    /**
     * Gets the skill badge colored frame image
     *
     * @return the integral value for the colored frame image
     */
    public int getSkillBadgeFrameColor() {
        switch (color) {
            case BLUE:
                return R.drawable.icon_frame_blue;
            case GREEN:
                return R.drawable.icon_frame_green;
            case ORANGE:
                return R.drawable.icon_frame_orange;
            case PURPLE:
                return R.drawable.icon_frame_purple;
            default:
                return 0;
        }
    }
}
