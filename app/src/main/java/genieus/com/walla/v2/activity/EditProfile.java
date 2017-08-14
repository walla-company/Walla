package genieus.com.walla.v2.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import genieus.com.walla.R;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.info.EditProfileSection;
import genieus.com.walla.v2.info.Fonts;

public class EditProfile extends AppCompatActivity {
    private WallaApi api;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        auth = FirebaseAuth.getInstance();
        api = WallaApi.getInstance(this);

        initUi();
    }

    private void initUi() {
        final List<EditProfileSection> sections = new ArrayList<>();

        sections.add(getFirstName());
        sections.add(getLastName());
        sections.add(getGraduationYear());
        sections.add(getMajor());
        sections.add(getLastHometown());
        sections.add(getPersonalDescription());
        sections.add(getSchoolChoiceReason());
        sections.add(getGoalHeader());
        sections.add(getGoal1());
        sections.add(getGoal2());
        sections.add(getGoal3());
        sections.add(getEmoji());

        render(sections);
    }

    private void render(final List<EditProfileSection> sections) {
        final Fonts fonts = new Fonts(this);
        final LinearLayout mSectionContainer = (LinearLayout) findViewById(R.id.section_container);


        for (final EditProfileSection section : sections) {
            final RelativeLayout row = (RelativeLayout) getLayoutInflater()
                    .inflate(R.layout.edit_profile_row, null, false);

            final TextView title = (TextView) row.findViewById(R.id.field_title);
            final TextView description = (TextView) row.findViewById(R.id.field_description);
            final TextView charCount = (TextView) row.findViewById(R.id.char_counter);
            final EditText action = (EditText) row.findViewById(R.id.field_action);

            applyFont(fonts.AzoSansRegular, title, description, action);
            title.setText(section.getTitle());
            if (section.getDescription().isPresent()) {
                description.setText(section.getDescription().get());
            } else {
                description.setVisibility(View.GONE);
            }

            if (section.getOnFinishAction() == null) {
                action.setVisibility(View.GONE);
            } else {
                action.setVisibility(View.VISIBLE);
            }

            if (section.isShowCharCount()) {
                charCount.setText("0/100 chars");
                action.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        charCount.setText(String.format("%d/100 chars",
                                action.getText().toString().length()));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });
            } else {
                charCount.setVisibility(View.GONE);
            }

            if (section.getHintText().isPresent()) {
                action.setHint(section.getHintText().get());
            }

            action.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        section.getOnFinishAction().onFinishAction(action.getText().toString());
                    }
                }
            });

            mSectionContainer.addView(row);
        }
    }

    private void applyFont(final Typeface font, final TextView...textViews) {
        for (TextView textView : textViews) {
            textView.setTypeface(font);
        }
    }

    private EditProfileSection getFirstName() {
        return new EditProfileSection(
                "First name",
                Optional.<String>absent(),
                false,
                Optional.of("Judy"),
                new EditProfileSection.Action() {
                    @Override
                    public void onFinishAction(String data) {
                        WallaApi.saveUserFirstName(auth.getCurrentUser().getUid(), data);
                    }
                }
        );
    }

    private EditProfileSection getLastName() {
        return new EditProfileSection(
                "Last name",
                Optional.<String>absent(),
                false,
                Optional.of("Zhang"),
                new EditProfileSection.Action() {
                    @Override
                    public void onFinishAction(String data) {
                        WallaApi.saveUserLastName(auth.getCurrentUser().getUid(), data);
                    }
                }
        );
    }

    private EditProfileSection getGraduationYear() {
        return new EditProfileSection(
                "Graduation year",
                Optional.<String>absent(),
                false,
                Optional.of("2021"),
                new EditProfileSection.Action() {
                    @Override
                    public void onFinishAction(String data) {
                        WallaApi.saveUserAcademicLevel(auth.getCurrentUser().getUid(), data);
                    }
                }
        );
    }

    private EditProfileSection getMajor() {
        return new EditProfileSection(
                "Major",
                Optional.<String>absent(),
                false,
                Optional.of("Mechanical Engineering"),
                new EditProfileSection.Action() {
                    @Override
                    public void onFinishAction(String data) {
                        WallaApi.saveUserMajor(auth.getCurrentUser().getUid(), data);
                    }
                }
        );
    }

    private EditProfileSection getLastHometown() {
        return new EditProfileSection(
                "Where are you from?",
                Optional.<String>absent(),
                false,
                Optional.of("Calgary, Canada"),
                new EditProfileSection.Action() {
                    @Override
                    public void onFinishAction(String data) {
                        WallaApi.saveUserHometown(auth.getCurrentUser().getUid(), data);
                    }
                }
        );
    }

    private EditProfileSection getPersonalDescription() {
        return new EditProfileSection(
                "Describe yourself in a few sentences.",
                Optional.<String>absent(),
                true,
                Optional.of("I like to play piano and sing with friends. I enjoy reading under trees. I also like to spend my time volunteering to help people who are in need."),
                new EditProfileSection.Action() {
                    @Override
                    public void onFinishAction(String data) {
                        WallaApi.saveUserDescription(auth.getCurrentUser().getUid(), data);
                    }
                }
        );
    }

    private EditProfileSection getSchoolChoiceReason() {
        return new EditProfileSection(
                "Why did you pick Duke?",
                Optional.<String>absent(),
                true,
                Optional.<String>absent(),
                new EditProfileSection.Action() {
                    @Override
                    public void onFinishAction(String data) {
                        // TODO(anesu): add server call
                    }
                }
        );
    }

    private EditProfileSection getGoalHeader() {
        return new EditProfileSection(
                "What are your goals this year? ",
                Optional.<String>of("These can be academic, social or personal goals."),
                false,
                Optional.<String>absent(),
                null
        );
    }

    private EditProfileSection getGoal1() {
        return new EditProfileSection(
                "Goal 1",
                Optional.<String>absent(),
                true,
                Optional.<String>of("ex. Learn a martial art"),
                new EditProfileSection.Action() {
                    @Override
                    public void onFinishAction(String data) {
                        // TODO(anesu) make server call
                    }
                }
        );
    }

    private EditProfileSection getGoal2() {
        return new EditProfileSection(
                "Goal 2",
                Optional.<String>absent(),
                true,
                Optional.<String>of("ex. Become involved in student politics"),                new EditProfileSection.Action() {
            @Override
            public void onFinishAction(String data) {
                // TODO(anesu) make server call
            }
        }
        );
    }

    private EditProfileSection getGoal3() {
        return new EditProfileSection(
                "Goal 3",
                Optional.<String>absent(),
                true,
                Optional.<String>of("ex. Volunteer locally"),
                new EditProfileSection.Action() {
                    @Override
                    public void onFinishAction(String data) {
                        // TODO(anesu) make server call
                    }
                }
        );
    }

    private EditProfileSection getEmoji() {
        return new EditProfileSection(
                "Pick your signature emoji!",
                Optional.<String>of("Your emoji will be displayed after your  name when you post or comment."),
                false,
                Optional.<String>absent(),
                new EditProfileSection.Action() {
                    @Override
                    public void onFinishAction(String data) {
                        // TODO(anesu) make server call
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }

        return true;
    }
}
