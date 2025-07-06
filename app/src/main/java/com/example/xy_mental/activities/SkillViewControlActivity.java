package com.example.xy_mental.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.xy_mental.settings.EffectsManager;
import com.example.xy_mental.R;
import com.example.xy_mental.skill.SavedSkillsManager;
import com.example.xy_mental.skill.Skill;
import com.example.xy_mental.skill.SkillManager;
import com.example.xy_mental.settings.ThemeManager;

/**
 * The skill badge view with bottom controls to save the current skill or navigate to other ones.
 * NOTE: The entire layout is split into three sections Head, Body, and Controls.
 */
public class SkillViewControlActivity extends AppCompatActivity {
    Skill originalSkill;
    Skill skill;

    FrameLayout skill_main_container;
    ImageView skill_main_icon;
    ImageView skill_main_frame;

    FrameLayout skill_side_container;
    ImageView skill_side_icon;
    ImageView skill_side_frame;

    TextView skill_title;
    TextView skill_desc;

    Button previous_button;
    Button save_button;
    Button next_button;

    TextView saved_skills_counter_text;

    boolean isAnimating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Sets the application theme and loads activity_skill_view_control.xml layout file
        setTheme(ThemeManager.getInstance().getColorTheme());
        setContentView(R.layout.activity_skill_view);
        (findViewById(R.id.skill_view_activity_layout)).setBackgroundColor(ThemeManager.getInstance().getBackgroundColor());

        displayInitialSkill();

        setupBackButtonAction();
    }

    /**
     * Displays the skill badge that was clicked from the deck activity
     */
    private void displayInitialSkill() {
        if (getIntent().getExtras() != null) {
            originalSkill = SkillManager.getInstance().getSkills().get((getIntent().getExtras()).getInt("skill_id"));
            skill = originalSkill;

            if (skill != null) {
                ((ImageView) findViewById(R.id.skill_side_frame_placeholder)).setImageResource(ThemeManager.getInstance().getSkillBadgeFrameColor());
                ((ImageView) findViewById(R.id.skill_main_frame_placeholder)).setImageResource(ThemeManager.getInstance().getSkillBadgeFrameColor());

                initializeUIElements();
                activateUIElements();

                refreshHeadUIElements();
                refreshBodyUIElements();
                refreshControlUIElements();
            } else {
                throw new RuntimeException("SkillViewControlActivity doesn't have a invalid skill id");
            }
        } else {
            throw new RuntimeException("SkillViewControlActivity doesn't have a skill id");
        }
    }

    /**
     * Displays a specific skill badge by loading it from SkillManager using its numerical skill id
     *
     * @param skill_id the id from the skill badge to be displayed
     */
    private void displaySpecificSkill(int skill_id) {
        skill = SkillManager.getInstance().getSkills().get(skill_id);
        refreshBodyUIElements();
        refreshControlUIElements();
    }

    /**
     * Initializes all the field variables using their id in the main activity
     */
    private void initializeUIElements() {
        skill_main_container = findViewById(R.id.skill_main_container);
        skill_main_frame = findViewById(R.id.skill_main_frame_placeholder);
        skill_main_icon = findViewById(R.id.skill_main_icon_placeholder);

        skill_side_container = findViewById(R.id.skill_side_container);
        skill_side_frame = findViewById(R.id.skill_side_frame_placeholder);
        skill_side_icon = findViewById(R.id.skill_side_icon_placeholder);

        skill_title = findViewById(R.id.skill_title_placeholder);
        skill_desc = findViewById(R.id.skill_desc_placeholder);

        previous_button = findViewById(R.id.previous_button);
        save_button = findViewById(R.id.save_button);
        next_button = findViewById(R.id.next_button);

        saved_skills_counter_text = findViewById(R.id.saved_skills_counter_text);
    }

    /**
     * Implements the onClickListener event for each button in the main activity
     */
    private void activateUIElements() {
        previous_button.setOnClickListener(l -> previousButtonOnClick());
        save_button.setOnClickListener(l -> saveButtonOnClick());
        next_button.setOnClickListener(l -> nextButtonOnClick());
    }

    /**
     * Refreshes the content to the UI elements in the head section
     */
    private void refreshHeadUIElements() {
        skill_main_icon.setImageResource(skill.getIcon());
        skill_side_icon.setImageResource(skill.getIcon());
    }

    /**
     * Refreshes the content to the UI elements in the body section
     */
    private void refreshBodyUIElements() {
        skill_title.setText(getString(skill.getTitle()));
        skill_desc.setText(getString(skill.getDescription()));
    }

    /**
     * Refreshes the content to the UI elements in the controls section
     */
    private void refreshControlUIElements() {
        refreshSaveButtonUI();
        refreshArrowButtonsUI();
        refreshSkillCounterUI();
    }

    /**
     * Refreshes the save button's text (Save or Unsave) and enabled status (Clickable or Not)
     */
    private void refreshSaveButtonUI() {
        // Check if the skill is saved or not
        if (skill.isSavedStatus()) {
            save_button.setText(R.string.unsave_button);
            save_button.setAlpha(1f);
        } else {
            save_button.setText(R.string.save_button);

            // Checks to see if the user has room to save the skill or not
            if (SavedSkillsManager.getInstance().getSavedSkills().size() >= 9) {
                save_button.setAlpha(0.5f);
            } else {
                save_button.setAlpha(1f);
            }
        }
    }

    /**
     * Refreshes the previous and next buttons enabled status (Clickable or Not)
     */
    private void refreshArrowButtonsUI() {
        // Check if we reached the end of the edge of skills list from the left
        if (skill.getId() <= 0 && !EffectsManager.getInstance().isCirculationEnabled()) {
            previous_button.setAlpha(0.5f);
        } else {
            previous_button.setAlpha(1f);
        }

        // Check if we reached the end of the edge of skills list from the right
        if (skill.getId() >= (SkillManager.getInstance().getSkills().size() - 1) && !EffectsManager.getInstance().isCirculationEnabled()) {
            next_button.setAlpha(0.5f);
        } else {
            next_button.setAlpha(1f);
        }
    }

    /**
     * Refreshes the skill counter text
     */
    private void refreshSkillCounterUI() {
        String skill_count = "You have " + (9 - SavedSkillsManager.getInstance().getSavedSkills().size()) + " empty slots available to save a skill";
        saved_skills_counter_text.setText(skill_count);
    }

    /**
     * Triggers changing the current skill save status if it is possible
     */
    private void saveButtonOnClick() {
        // Check if we are saving or un saving the skill
        if (skill.isSavedStatus()) {
            updateSkillSavedStatus(false);
        } else {
            // Check if the user has room to save the skill
            if (SavedSkillsManager.getInstance().getSavedSkills().size() < 9) {
                updateSkillSavedStatus(true);
            } else {
                EffectsManager.getInstance().playErrorEffects(this);
            }
        }
    }

    /**
     * Navigates to the previous skill if it is available
     */
    private void previousButtonOnClick() {
        if ((skill.getId() - 1) >= 0) {
            displaySpecificSkill(skill.getId() - 1);
            playPreviousButtonAnimation();
        } else if (EffectsManager.getInstance().isCirculationEnabled()) {
            displaySpecificSkill(SkillManager.getInstance().getSkills().size() - 1);
            playPreviousButtonAnimation();
        } else {
            EffectsManager.getInstance().playErrorEffects(this);
        }
    }

    /**
     * Navigates to the next skill if it is available
     */
    private void nextButtonOnClick() {
        if ((skill.getId() + 1) <= (SkillManager.getInstance().getSkills().size() - 1)) {
            displaySpecificSkill(skill.getId() + 1);
            playNextButtonAnimation();
        } else if (EffectsManager.getInstance().isCirculationEnabled()) {
            displaySpecificSkill(0);
            playNextButtonAnimation();
        } else {
            EffectsManager.getInstance().playErrorEffects(this);
        }
    }

    /**
     * Assigns some statements to be called in the animation ending event
     *
     * @param animation the animation we want to assign the statement to its ending event
     */
    private void setAnimationEndEvent(Animation animation) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                skill_side_icon.setImageResource(skill.getIcon());
                isAnimating = false;
                refreshArrowButtonsUI();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * Animates the skill badges to move to the right and display the next one
     */
    private void playNextButtonAnimation() {
        if (EffectsManager.getInstance().isAnimationEnabled()) {
            if (!isAnimating) {
                Animation animation_inward = AnimationUtils.loadAnimation(this, R.anim.move_in_right);
                Animation animation_outward = AnimationUtils.loadAnimation(this, R.anim.move_out_left);

                setAnimationEndEvent(animation_outward);

                skill_main_container.startAnimation(animation_inward);
                skill_side_container.startAnimation(animation_outward);

                skill_main_icon.setImageResource(skill.getIcon());
                playSkillDescriptionAnimation();

                isAnimating = true;
                next_button.setAlpha(0.5f);
            }
        } else {
            refreshHeadUIElements();
        }
    }

    /**
     * Animates the skill badges to move to the left and display the previous one
     */
    private void playPreviousButtonAnimation() {
        if (EffectsManager.getInstance().isAnimationEnabled()) {
            if (!isAnimating) {
                Animation animation_inward = AnimationUtils.loadAnimation(this, R.anim.move_in_left);
                Animation animation_outward = AnimationUtils.loadAnimation(this, R.anim.move_out_right);

                setAnimationEndEvent(animation_outward);

                skill_main_container.startAnimation(animation_inward);
                skill_side_container.startAnimation(animation_outward);

                skill_main_icon.setImageResource(skill.getIcon());
                playSkillDescriptionAnimation();

                isAnimating = true;
                previous_button.setAlpha(0.5f);
            }
        } else {
            refreshHeadUIElements();
        }
    }

    /**
     * Animates the skill badges description to fade and change in accordance with the new skill
     */
    private void playSkillDescriptionAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade);
        skill_desc.startAnimation(animation);
    }

    /**
     * Changes the skill badge saved status
     *
     * @param isSaving Determines if we are saving or un saving the current skill
     */
    private void updateSkillSavedStatus(boolean isSaving) {
        if (isSaving) {
            skill.setSavedStatus(true);
            save_button.setText(R.string.unsave_button);
            SavedSkillsManager.getInstance().getSavedSkills().add(skill.getId() + "");
        } else {
            skill.setSavedStatus(false);
            save_button.setText(R.string.save_button);
            SavedSkillsManager.getInstance().getSavedSkills().remove(skill.getId() + "");
        }

        SharedPreferences.Editor settings_editor = getSharedPreferences(getString(R.string.saved_skills_file), Context.MODE_PRIVATE).edit();
        settings_editor.putStringSet(getString(R.string.saved_skills), SavedSkillsManager.getInstance().getSavedSkills());
        settings_editor.apply();

        EffectsManager.getInstance().playClickEffects(this);
        refreshSkillCounterUI();
    }

    /**
     * Override the back button behavior to control the transition animation between activities.
     * Also during the exit transition it resets the skill badge icon to the original one clicked.
     */
    private void setupBackButtonAction() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                skill_main_icon.setImageResource(originalSkill.getIcon());
                supportFinishAfterTransition();
                overridePendingTransition(0, 0);
            }
        });
    }
}