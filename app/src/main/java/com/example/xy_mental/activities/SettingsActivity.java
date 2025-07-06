package com.example.xy_mental.activities;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_UNSPECIFIED;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static com.example.xy_mental.settings.AppColor.BLUE;
import static com.example.xy_mental.settings.AppColor.GREEN;
import static com.example.xy_mental.settings.AppColor.ORANGE;
import static com.example.xy_mental.settings.AppColor.PURPLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.xy_mental.settings.EffectsManager;
import com.example.xy_mental.R;
import com.example.xy_mental.settings.ThemeManager;
import com.google.android.material.materialswitch.MaterialSwitch;

/**
 * The settings page for controlling most of the application's features
 * NOTE: We destroy the current activity and switch roots when changing to the main activity
 */
public class SettingsActivity extends AppCompatActivity {
    private RadioButton darkModeButton;
    private RadioButton lightModeButton;
    private RadioButton systemModeButton;

    private RadioButton blueColorButton;
    private RadioButton greenColorButton;
    private RadioButton orangeColorButton;
    private RadioButton purpleColorButton;

    private MaterialSwitch soundSwitch;
    private MaterialSwitch vibrationSwitch;
    private MaterialSwitch animationSwitch;
    private MaterialSwitch circulationSwitch;
    private MaterialSwitch trueDarkSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Sets the application theme and loads activity_settings.xml layout file
        setTheme(ThemeManager.getInstance().getColorTheme());
        setContentView(R.layout.activity_settings);
        (findViewById(R.id.settings_activity_layout)).setBackgroundColor(ThemeManager.getInstance().getBackgroundColor());

        initializeUIElements();
        activateUIElements();
        refreshUIElements();

        setupBackButtonAction();
    }

    /**
     * Initializes all the field variables using their id in the main activity
     */
    private void initializeUIElements() {
        darkModeButton = findViewById(R.id.dark_mode_button);
        lightModeButton = findViewById(R.id.light_mode_button);
        systemModeButton = findViewById(R.id.system_mode_button);

        blueColorButton = findViewById(R.id.blue_color_button);
        greenColorButton = findViewById(R.id.green_color_button);
        orangeColorButton = findViewById(R.id.orange_color_button);
        purpleColorButton = findViewById(R.id.purple_color_button);

        soundSwitch = findViewById(R.id.sound_switch);
        vibrationSwitch = findViewById(R.id.vibration_switch);
        animationSwitch = findViewById(R.id.animation_switch);
        circulationSwitch = findViewById(R.id.circulation_switch);
        trueDarkSwitch = findViewById(R.id.true_dark_switch);

    }

    /**
     * Implements the onClickListener event for each button in the main activity
     */
    private void activateUIElements() {
        darkModeButton.setOnClickListener(l ->
        {
            updateIntegerSettingValue(getString(R.string.theme_mode), MODE_NIGHT_YES);
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
            EffectsManager.getInstance().playClickEffects(this);
            this.recreate();
        });

        lightModeButton.setOnClickListener(l ->
        {
            updateIntegerSettingValue(getString(R.string.theme_mode), MODE_NIGHT_NO);
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
            EffectsManager.getInstance().playClickEffects(this);

            disableAmoledMode(); // Turn off and disable amoled mode when light mode is selected
            this.recreate();
        });

        systemModeButton.setOnClickListener(l ->
        {
            updateIntegerSettingValue(getString(R.string.theme_mode), MODE_NIGHT_FOLLOW_SYSTEM);
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);
            EffectsManager.getInstance().playClickEffects(this);

            disableAmoledMode(); // Turn off and disable amoled mode when system mode is selected
            this.recreate();
        });

        blueColorButton.setOnClickListener(l ->
        {
            updateIntegerSettingValue(getString(R.string.theme_color), BLUE.ordinal());
            ThemeManager.getInstance().setColor(BLUE);
            EffectsManager.getInstance().playClickEffects(this);
            this.recreate();
        });

        greenColorButton.setOnClickListener(l ->
        {
            updateIntegerSettingValue(getString(R.string.theme_color), GREEN.ordinal());
            ThemeManager.getInstance().setColor(GREEN);
            EffectsManager.getInstance().playClickEffects(this);
            this.recreate();
        });

        orangeColorButton.setOnClickListener(l ->
        {
            updateIntegerSettingValue(getString(R.string.theme_color), ORANGE.ordinal());
            ThemeManager.getInstance().setColor(ORANGE);
            EffectsManager.getInstance().playClickEffects(this);
            this.recreate();
        });

        purpleColorButton.setOnClickListener(l ->
        {
            updateIntegerSettingValue(getString(R.string.theme_color), PURPLE.ordinal());
            ThemeManager.getInstance().setColor(PURPLE);
            EffectsManager.getInstance().playClickEffects(this);
            this.recreate();
        });

        soundSwitch.setOnClickListener(l ->
        {
            updateBooleanSettingValue(getString(R.string.effect_audio), soundSwitch.isChecked());
            EffectsManager.getInstance().setSoundEnabled(soundSwitch.isChecked());
            EffectsManager.getInstance().playClickEffects(this);
        });

        vibrationSwitch.setOnClickListener(l ->
        {
            updateBooleanSettingValue(getString(R.string.effect_vibration), vibrationSwitch.isChecked());
            EffectsManager.getInstance().setVibrationEnabled(vibrationSwitch.isChecked());
            EffectsManager.getInstance().playClickEffects(this);
        });

        animationSwitch.setOnClickListener(l ->
        {
            updateBooleanSettingValue(getString(R.string.effect_animation), animationSwitch.isChecked());
            EffectsManager.getInstance().setAnimationEnabled(animationSwitch.isChecked());
            EffectsManager.getInstance().playClickEffects(this);
        });

        circulationSwitch.setOnClickListener(l ->
        {
            updateBooleanSettingValue(getString(R.string.effect_circulation), circulationSwitch.isChecked());
            EffectsManager.getInstance().setCirculationEnabled(circulationSwitch.isChecked());
            EffectsManager.getInstance().playClickEffects(this);
        });

        trueDarkSwitch.setOnClickListener(l ->
        {
            updateBooleanSettingValue(getString(R.string.theme_true_dark), trueDarkSwitch.isChecked());
            ThemeManager.getInstance().setTrueDarkModeSelectionStatus(trueDarkSwitch.isChecked());
            EffectsManager.getInstance().playClickEffects(this);
            this.recreate();
        });
    }

    /**
     * Refreshes the UI elements selected values (color, theme, effects) when loading the activity.
     * This method is only called once when the activity loads for the first time using onCreate().
     * We are suppressing a missing switch statement case because it is deprecated.
     */
    @SuppressLint("SwitchIntDef")
    private void refreshUIElements() {
        // Refresh the selected theme mode value from the stored settings
        switch (AppCompatDelegate.getDefaultNightMode()) {
            case MODE_NIGHT_FOLLOW_SYSTEM:
                systemModeButton.setChecked(true);
                break;
            case MODE_NIGHT_NO:
            case MODE_NIGHT_UNSPECIFIED:
                lightModeButton.setChecked(true);
                break;
            case MODE_NIGHT_YES:
            case MODE_NIGHT_AUTO_BATTERY:
                darkModeButton.setChecked(true);
                break;
        }

        // Refresh the selected theme color value from the stored settings
        switch (ThemeManager.getInstance().getColor()) {
            case BLUE:
                blueColorButton.setChecked(true);
                break;
            case GREEN:
                greenColorButton.setChecked(true);
                break;
            case ORANGE:
                orangeColorButton.setChecked(true);
                break;
            case PURPLE:
                purpleColorButton.setChecked(true);
                break;
        }

        // Refresh the selected effects value from the stored settings
        soundSwitch.setChecked(EffectsManager.getInstance().isSoundEnabled());
        vibrationSwitch.setChecked(EffectsManager.getInstance().isVibrationEnabled());
        animationSwitch.setChecked(EffectsManager.getInstance().isAnimationEnabled());
        circulationSwitch.setChecked(EffectsManager.getInstance().isCirculationEnabled());

        // Refresh amoled mode enabled and selected values from the stored settings
        if (AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES) {
            trueDarkSwitch.setEnabled(true);

            if (ThemeManager.getInstance().getTrueDarkModeSelectionStatus()) {
                trueDarkSwitch.setChecked(true);
            }
        }
    }

    /**
     * Disabled the amoled switch and turns it off
     */
    private void disableAmoledMode() {
        updateBooleanSettingValue(getString(R.string.theme_true_dark), false);
        ThemeManager.getInstance().setTrueDarkModeSelectionStatus(false);
        trueDarkSwitch.setChecked(false);
    }

    /**
     * Override the back button behavior to control the transition animation between activities.
     * Also finishes the current activity and starts a new root activity from the main activity.
     */
    private void setupBackButtonAction() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    /**
     * Updates a boolean value in the settings file
     */
    private void updateBooleanSettingValue(String setting, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE).edit();
        editor.putBoolean(setting, value);
        editor.apply();
    }

    /**
     * Updates an integral value in the settings file
     */
    private void updateIntegerSettingValue(String setting, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE).edit();
        editor.putInt(setting, value);
        editor.apply();
    }
}