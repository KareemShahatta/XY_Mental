package com.example.xy_mental.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

import com.example.xy_mental.settings.EffectsManager;
import com.example.xy_mental.R;
import com.example.xy_mental.skill.SavedSkillsManager;
import com.example.xy_mental.skill.Skill;
import com.example.xy_mental.skill.SkillManager;
import com.example.xy_mental.settings.ThemeManager;

/**
 * The saved deck grid page contains up to nine skill badges saved by the user.
 * NOTE: We don't attach the generated skill badge to the gird using the inflate() method.
 * NOTE: We are using one layout file and hide controls to view both control and plain activities.
 */
public class SavedDeckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Sets the application theme and loads activity_saved_deck.xml layout file
        setTheme(ThemeManager.getInstance().getColorTheme());
        setContentView(R.layout.activity_saved_deck);
        (findViewById(R.id.saved_deck_activity_layout)).setBackgroundColor(ThemeManager.getInstance().getBackgroundColor());

        addSkillBadgesToSavedDeck(findViewById(R.id.saved_skills));

        setupBackButtonAction();
    }

    /**
     * Creates the skill badges using the generic layout skill file and then adds the appropriate.
     * frame, icon, title, description, and saved status from the SkillManager and SaveSkillManager.
     * We attach it manually to the deck because inflate() returns the root and not the child.
     *
     * @param deck gridLayout which we want to add the saved skill badges in it
     */
    private void addSkillBadgesToSavedDeck(GridLayout deck) {
        int frame_color = ThemeManager.getInstance().getSkillBadgeFrameColor();

        for (String skill_id : SavedSkillsManager.getInstance().getSavedSkills()) {
            Skill skill = SkillManager.getInstance().getSkills().get(Integer.parseInt(skill_id));
            if (skill != null) {
                FrameLayout skill_badge = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.layout_skill_badge, deck, false);

                ImageView skill_frame = skill_badge.findViewById(R.id.frame_placeholder);
                skill_frame.setImageResource(frame_color);

                ImageView skill_icon = skill_badge.findViewById(R.id.icon_placeholder);
                skill_icon.setImageResource(skill.getIcon());

                ((TextView) skill_badge.findViewById(R.id.title_placeholder)).setText(getString(skill.getTitle()));

                skill_badge.setOnClickListener(l ->
                {
                    Intent intent = new Intent(this, SkillViewPlainActivity.class);
                    intent.putExtra("skill_id", skill.getId());

                    if (EffectsManager.getInstance().isAnimationEnabled()) {
                        Pair<View, String> skill_frame_animation = Pair.create(skill_frame, "shared_transition_frame");
                        Pair<View, String> skill_icon_animation = Pair.create(skill_icon, "shared_transition_icon");
                        //noinspection unchecked
                        ActivityOptionsCompat intent_animation = ActivityOptionsCompat.makeSceneTransitionAnimation(this, skill_frame_animation, skill_icon_animation);
                        startActivity(intent, intent_animation.toBundle());
                    } else {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }

                    EffectsManager.getInstance().playClickEffects(this);
                });

                deck.addView(skill_badge);
            }
        }

        // If the grid is not entirely filled, then add empty badges to make it look consistent.
        if (deck.getChildCount() < 9) {
            addEmptyBadgesToSavedDeck(frame_color, deck);
        }
    }

    /**
     * Creates the skill badges using the generic layout skill file and then adds the appropriate.
     * frame, icon, title, description, and saved status from the SkillManager and SaveSkillManager.
     * We attach it manually to the deck because inflate() returns the root and not the child.
     *
     * @param frame_color the badge's frame color
     * @param deck        gridLayout which we want to add the empty badges in it
     */
    private void addEmptyBadgesToSavedDeck(int frame_color, GridLayout deck) {
        int skills_count = deck.getChildCount();
        FrameLayout empty_badge;
        String empty_badge_title;

        for (int i = (skills_count + 1); i <= 9; i++) {
            empty_badge = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.layout_skill_badge, deck, false);

            ((ImageView) empty_badge.findViewById(R.id.frame_placeholder)).setImageResource(frame_color);
            ((ImageView) empty_badge.findViewById(R.id.icon_placeholder)).setImageResource(android.R.color.transparent);

            empty_badge_title = "Empty Save #" + i;
            ((TextView) empty_badge.findViewById(R.id.title_placeholder)).setText(empty_badge_title);

            deck.addView(empty_badge);
        }
    }

    /**
     * Override the back button behavior to control the transition animation between activities
     */
    private void setupBackButtonAction() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }
}
