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
import com.example.xy_mental.skill.Skill;
import com.example.xy_mental.skill.SkillManager;
import com.example.xy_mental.settings.ThemeManager;

/**
 * The deck grid page contains all the skills badge that are available in the application.
 * NOTE: We don't attach the generated skill badge to the gird using the inflate() method.
 * NOTE: We are using one layout file and hide controls to view both control and plain activities.
 */
public class DeckActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Sets the application theme and loads activity_deck.xml layout file
        setTheme(ThemeManager.getInstance().getColorTheme());
        setContentView(R.layout.activity_deck);
        (findViewById(R.id.deck_activity_layout)).setBackgroundColor(ThemeManager.getInstance().getBackgroundColor());

        // Calculates how many rows are going to be in the deck and adds the skill badges
        int skills_count = SkillManager.getInstance().getSkills().size();
        GridLayout deck = findViewById(R.id.deck);
        deck.setRowCount(Math.max(1 , Math.round(skills_count / 3f)));

        addSkillBadgesToDeck(deck);

        setupBackButtonAction();
    }


    /**
     * Creates the skill badges using the generic layout skill file and then adds the appropriate.
     * frame, icon, title, description, and saved status from the SkillManager and SaveSkillManager.
     * We attach it manually to the deck because inflate() returns the root and not the child.
     *
     * @param deck gridLayout which we want to add the skill badges in it
     */
    private void addSkillBadgesToDeck(GridLayout deck) {
        int frame_color = ThemeManager.getInstance().getSkillBadgeFrameColor();

        for (Skill skill : SkillManager.getInstance().getSkills().values()) {
            FrameLayout skill_badge = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.layout_skill_badge, deck, false);

            ImageView skill_frame = skill_badge.findViewById(R.id.frame_placeholder);
            skill_frame.setImageResource(frame_color);

            ImageView skill_icon = skill_badge.findViewById(R.id.icon_placeholder);
            skill_icon.setImageResource(skill.getIcon());

            ((TextView) skill_badge.findViewById(R.id.title_placeholder)).setText(getString(skill.getTitle()));

            skill_badge.setOnClickListener(l ->
            {
                Intent intent = new Intent(this, SkillViewControlActivity.class);
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