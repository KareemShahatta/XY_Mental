package com.example.xy_mental.settings;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.example.xy_mental.R;

/**
 * The theme manager handles everything related to the application effect and audio
 */
public class EffectsManager {
    private boolean isSoundEnabled;
    private boolean isVibrationEnabled;
    private boolean isAnimationEnabled;
    private boolean isCirculationEnabled;
    private static EffectsManager instance;

    private EffectsManager() {
    }

    public static EffectsManager getInstance() {
        if (instance == null) {
            instance = new EffectsManager();
        }

        return instance;
    }

    /**
     * Plays the application open sound effect
     *
     * @param context the current activity that is displayed on the user's screen
     */
    public void playOpenSound(Context context) {
        if (isSoundEnabled) {
            MediaPlayer.create(context, R.raw.open).start();
        }
    }

    /**
     * Plays the button click sound effect
     *
     * @param context the current activity that is displayed on the user's screen
     */
    public void playClickEffects(Context context) {
        if (isSoundEnabled) {
            MediaPlayer.create(context, R.raw.select).start();
        }
        if (isVibrationEnabled) {
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    /**
     * Plays the error sound effect
     *
     * @param context the current activity that is displayed on the user's screen
     */
    public void playErrorEffects(Context context) {
        if (isSoundEnabled) {
            MediaPlayer.create(context, R.raw.error).start();
        }
        if (isVibrationEnabled) {
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    /**
     * Checks weather the user has the sound settings turn on or off
     *
     * @return the value of the sound settings
     */
    public boolean isSoundEnabled() {
        return isSoundEnabled;
    }

    /**
     * Checks weather the user has the vibration settings turn on or off
     *
     * @return the value of the vibration settings
     */
    public boolean isVibrationEnabled() {
        return isVibrationEnabled;
    }

    /**
     * Checks weather the user has the animation settings turn on or off.
     * Animation are displayed when the application opens for the first time,
     * when opening a skill from the deck or the saved skills page,
     * and when the user navigates from one skill to another.
     *
     * @return the value of the animation settings
     */
    public boolean isAnimationEnabled() {
        return isAnimationEnabled;
    }

    /**
     * Checks weather the user has the circulation settings turn on or off.
     * Circulation is the ability for the deck activity to loop through the skills with no end.
     *
     * @return the value of the circulation settings
     */
    public boolean isCirculationEnabled() {
        return isCirculationEnabled;
    }

    /**
     * Changes the sound effect selection status
     *
     * @param value whether the user wants the application to play sounds or not
     */
    public void setSoundEnabled(boolean value) {
        this.isSoundEnabled = value;
    }

    /**
     * Changes the vibration effect selection status
     *
     * @param value whether the user wants the application to vibrate or not
     */
    public void setVibrationEnabled(boolean value) {
        this.isVibrationEnabled = value;
    }

    /**
     * Changes the animation effect selection status
     *
     * @param value whether the user wants the application to play animations or not
     */
    public void setAnimationEnabled(boolean value) {
        this.isAnimationEnabled = value;
    }

    /**
     * Changes the circulation effect selection status
     *
     * @param value whether the user wants the deck to loop through the skills or leave them inline
     */
    public void setCirculationEnabled(boolean value) {
        this.isCirculationEnabled = value;
    }
}
