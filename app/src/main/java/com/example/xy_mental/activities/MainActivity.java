package com.example.xy_mental.activities;

import static com.example.xy_mental.settings.AppColor.BLUE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.xy_mental.settings.AppColor;
import com.example.xy_mental.settings.EffectsManager;
import com.example.xy_mental.R;
import com.example.xy_mental.skill.SavedSkillsManager;
import com.example.xy_mental.skill.Skill;
import com.example.xy_mental.skill.SkillManager;
import com.example.xy_mental.settings.ThemeManager;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * XY_Mental is an Android application designed to provide users with mental skills that they can
 * access anytime throughout the day. The purpose of the app is to offer a collection of skill
 * badges, which users can navigate through during moments of stress to help them calm down and
 * better manage their social and mental well-being.
 * <p>
 * The main menu is the root of all the other activities, except the settings.
 * NOTE: We destroy the current activity and switch roots when changing to the settings activity.
 *
 * @author Kareem Shahatta
 * @version 1.1
 */
public class MainActivity extends AppCompatActivity {
    private static boolean isFirstCreation = true; // Figures if main activity was already created

    private TextView titleText;
    private Button browseButton;
    private Button savedButton;
    private Button shuffleButton;
    private Button settingsButton;

    private TextView creditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        loadSettings();

        // Sets the application theme and loads activity_main.xml layout file
        setTheme(ThemeManager.getInstance().getColorTheme());
        setContentView(R.layout.activity_main);
        (findViewById(R.id.main_activity_layout)).setBackgroundColor(ThemeManager.getInstance().getBackgroundColor());

        initializeUIElements();
        activateUIElements();

        initiateStartUpEffects();
    }


    /**
     * Loads the application theme, effects, and saved skills
     */
    private void loadSettings() {

        if (isFirstCreation) {
            loadThemeStyle();
            loadAppEffects();
            loadSavedSkills();
        }
    }

    /**
     * Loads the theme style, color, and true dark status from the shared preferences settings file
     */
    private void loadThemeStyle() {
        int theme_color = getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE).getInt(getString(R.string.theme_color), BLUE.ordinal());
        int theme_mode = getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE).getInt(getString(R.string.theme_mode), AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        boolean theme_true_dark = getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE).getBoolean(getString(R.string.theme_true_dark), false);

        ThemeManager.getInstance().setColor(AppColor.values()[theme_color]);
        AppCompatDelegate.setDefaultNightMode(theme_mode);
        ThemeManager.getInstance().setTrueDarkModeSelectionStatus(theme_true_dark);
    }

    /**
     * Loads the application effects from the shared preferences settings file
     */
    private void loadAppEffects() {
        boolean audio = getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE).getBoolean(getString(R.string.effect_audio), true);
        boolean vibration = getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE).getBoolean(getString(R.string.effect_vibration), false);
        boolean animation = getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE).getBoolean(getString(R.string.effect_animation), true);
        boolean circulation = getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE).getBoolean(getString(R.string.effect_circulation), false);

        EffectsManager.getInstance().setSoundEnabled(audio);
        EffectsManager.getInstance().setVibrationEnabled(vibration);
        EffectsManager.getInstance().setAnimationEnabled(animation);
        EffectsManager.getInstance().setCirculationEnabled(circulation);
    }

    /**
     * Loads the user's saved skills from the shared preferences saved_skills file
     */
    private void loadSavedSkills() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.saved_skills_file), Context.MODE_PRIVATE);
        Set<String> savedSkills = sharedPreferences.getStringSet(getString(R.string.saved_skills), new HashSet<>());

        for (String skill_id : savedSkills) {
            Skill skill = SkillManager.getInstance().getSkills().get(Integer.parseInt(skill_id));

            if (skill != null) {
                skill.setSavedStatus(true);
            }

            SavedSkillsManager.getInstance().getSavedSkills().add(skill_id);
        }
    }


    /**
     * Initializes all the field variables using their id in the main activity
     */
    private void initializeUIElements() {
        titleText = findViewById(R.id.title_text);

        browseButton = findViewById(R.id.browse_button);
        savedButton = findViewById(R.id.saved_button);
        shuffleButton = findViewById(R.id.shuffle_button);
        settingsButton = findViewById(R.id.settings_button);

        creditText = findViewById(R.id.credit_text);
    }

    /**
     * Implements the onClickListener event for each button in the main activity
     */
    private void activateUIElements() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        browseButton.setOnClickListener(l -> {
            EffectsManager.getInstance().playClickEffects(this);
            intent.setClass(this, DeckActivity.class);
            startActivity(intent);
        });

        savedButton.setOnClickListener(l -> {
            EffectsManager.getInstance().playClickEffects(this);
            intent.setClass(this, SavedDeckActivity.class);
            startActivity(intent);
        });

        shuffleButton.setOnClickListener(l -> {
            EffectsManager.getInstance().playClickEffects(this);
            intent.setClass(this, SkillViewPlainActivity.class);
            intent.putExtra("skill_id", ThreadLocalRandom.current().nextInt(SkillManager.getInstance().getSkills().size()));
            startActivity(intent);
        });

        settingsButton.setOnClickListener(l -> {
            isFirstCreation = false;
            EffectsManager.getInstance().playClickEffects(this);
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
            finishAffinity();
        });
    }


    /**
     * Checks if the application is starting up for the first time and begins the startup effects
     */
    private void initiateStartUpEffects() {
        if (isFirstCreation) {
            browseButton.setVisibility(View.INVISIBLE);
            savedButton.setVisibility(View.INVISIBLE);
            shuffleButton.setVisibility(View.INVISIBLE);
            settingsButton.setVisibility(View.INVISIBLE);

            if (EffectsManager.getInstance().isAnimationEnabled()) {
                startAnimations();
            }

            if (EffectsManager.getInstance().isSoundEnabled()) {
                EffectsManager.getInstance().playOpenSound(this);
            }
        }
    }

    /**
     * Starts the fade animation  first then after half a second starts scale animation
     */
    private void startAnimations() {
        playFadeAnimation();
        creditText.postDelayed(this::playScaleAnimation, 500); //This is called a method reference which is a shortcut for lambda
    }

    /**
     * Plays the fade animation for the title and credit texts
     */
    private void playFadeAnimation() {
        Animation fade_animation = new AlphaAnimation(0f, 1f);
        fade_animation.setDuration(1500);

        titleText.startAnimation(fade_animation);
        creditText.startAnimation(fade_animation);
    }

    /**
     * Calculates the scale animation for each button with an offset time
     */
    private void playScaleAnimation() {
        int DURATION_TIME = 700;
        int DURATION_OFFSET = 200;

        createScaleAnimation(browseButton, DURATION_TIME);
        createScaleAnimation(savedButton, DURATION_TIME + (DURATION_OFFSET));
        createScaleAnimation(shuffleButton, DURATION_TIME + (DURATION_OFFSET * 3));
        createScaleAnimation(settingsButton, DURATION_TIME + (DURATION_OFFSET * 4));
    }

    /**
     * Plays the scale animation for each button after creating it
     */
    private void createScaleAnimation(Button button, int duration) {
        button.setVisibility(View.VISIBLE);
        Animation scaleAnimation = new ScaleAnimation(0f, 1f, 1f, 1f, (float) button.getWidth() / 2, 0);
        scaleAnimation.setDuration(duration);
        button.startAnimation(scaleAnimation);
    }
}
