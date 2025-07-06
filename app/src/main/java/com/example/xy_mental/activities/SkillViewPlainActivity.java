package com.example.xy_mental.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.xy_mental.R;
import com.example.xy_mental.skill.Skill;
import com.example.xy_mental.skill.SkillManager;
import com.example.xy_mental.settings.ThemeManager;

/**
 * The skill badge view without bottom controls is used with the shuffle view or saved skill view.
 * NOTE: We are passing the desired skill id from the previous activity using the .getExtras().
 * NOTE: We are using one layout file and hide controls to view both control and plain activities.
 */
public class SkillViewPlainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Sets the application theme and loads activity_skill_view_plain.xml layout file
        setTheme(ThemeManager.getInstance().getColorTheme());
        setContentView(R.layout.activity_skill_view);
        (findViewById(R.id.skill_view_activity_layout)).setBackgroundColor(ThemeManager.getInstance().getBackgroundColor());

        displaySkill();
        disableExtraUIElements();

        setupBackButtonAction();
    }

    /**
     * Displays a specific skill badge by loading it from SkillManager using its numerical skill id
     */
    private void displaySkill() {
        if (getIntent().getExtras() != null) {
            Skill skill = SkillManager.getInstance().getSkills().get((getIntent().getExtras()).getInt("skill_id"));

            if (skill != null) {
                ((ImageView) findViewById(R.id.skill_main_frame_placeholder)).setImageResource(ThemeManager.getInstance().getSkillBadgeFrameColor());
                ((ImageView) findViewById(R.id.skill_main_icon_placeholder)).setImageResource(skill.getIcon());
                ((TextView) findViewById(R.id.skill_title_placeholder)).setText(getString(skill.getTitle()));
                ((TextView) findViewById(R.id.skill_desc_placeholder)).setText(getString(skill.getDescription()));
            } else {
                throw new RuntimeException("SkillViewPlainActivity doesn't have a invalid skill id");
            }
        } else {
            throw new RuntimeException("SkillViewPlainActivity doesn't have a skill id");
        }
    }

    /**
     * Disables the control buttons and saved skills counter text from the activity file.
     * Those controls are not being used when viewing a saved skill badge, so we hide them.
     */
    private void disableExtraUIElements() {
        findViewById(R.id.skill_side_frame_placeholder).setVisibility(View.INVISIBLE);
        findViewById(R.id.skill_side_icon_placeholder).setVisibility(View.INVISIBLE);
        findViewById(R.id.controls_container).setVisibility(View.INVISIBLE);
        findViewById(R.id.saved_skills_counter_text).setVisibility(View.INVISIBLE);
    }

    /**
     * Override the back button behavior to control the transition animation between activities
     */
    private void setupBackButtonAction() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                supportFinishAfterTransition();
                overridePendingTransition(0, 0);
            }
        });
    }
}