package genieus.com.walla.v2.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import genieus.com.walla.R;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.datatypes.User;
import genieus.com.walla.v2.utils.Conversions;
import genieus.com.walla.v2.utils.Fonts;
import genieus.com.walla.v2.utils.ImageUtils;

public class ViewProfile extends AppCompatActivity {
    @BindView(R.id.basic_info)
    LinearLayout basicInfoContainer;

    @BindView(R.id.more_info)
    LinearLayout moreInfoContainer;

    @BindView(R.id.profile_picture)
    ImageView profileIcon;

    @BindView(R.id.edit)
    View editView;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editView.setVisibility(View.GONE);

        initUi();
    }

    private void initUi() {
        final Optional<String> uidOptional = Optional.of(
                getIntent().getExtras().getString("uid", null)
        );

        if (uidOptional.isPresent()) {
            WallaApi.getUserInfo(new WallaApi.OnDataReceived() {
                @Override
                public void onDataReceived(Object data, int call) {
                    if (data instanceof User) {
                        mUser = (User) data;
                        getSupportActionBar().setTitle(
                                String.format("%s %s", mUser.getFirstName(), mUser.getLastName())
                        );
                        loadProfileIcon();
                        loadUserBasicInfo();
                        loadUserMoreInfo();
                    }
                }
            }, uidOptional.get());
        }
    }

    private void loadProfileIcon() {
        ImageUtils.loadImageFromUrl(
                this,
                profileIcon,
                Optional.of(mUser.getProfileUrl())
        );
    }

    private void loadUserMoreInfo() {
        // TODO(anesu): Add strings to R.strings
        moreInfoContainer.addView(
                getMoreInfoView(getString(R.string.about_me), mUser.getDescription())
        );

        // TODO(anesu): add field
        moreInfoContainer.addView(
                getMoreInfoView(getString(R.string.why_school), "")
        );

        // TODO(anesu): add field
        moreInfoContainer.addView(
                getMoreInfoView(getString(R.string.want_to_meet), "")
        );

        moreInfoContainer.addView(
                getMoreInfoView("My 3 goals for this year are…", "")
        );
    }

    private View getMoreInfoView(final String title, final String content) {
        final LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        final int padding = Conversions.dpToPx(16);
        container.setPadding(padding, padding, padding, padding);

        final TextView headerView = new TextView(this);
        final TextView contentView = new TextView(this);

        headerView.setTextColor(getResources().getColor(R.color.colorPrimary));
        headerView.setTypeface(Fonts.AzoSansMedium);
        headerView.setText(title);
        headerView.setTextSize(16);
        container.addView(headerView);

        final LinearLayout.LayoutParams contentViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int marginTop = Conversions.dpToPx(8);
        contentViewParams.setMargins(0, marginTop, 0, 0);
        contentView.setLayoutParams(contentViewParams);
        contentView.setTextSize(16);
        container.addView(contentView);

        if (content == null || content.isEmpty()) {
            contentView.setText(R.string.no_response_entered);
            contentView.setTypeface(Fonts.AzoSansRegular, Typeface.ITALIC);
        } else {
            contentView.setText(content);
            contentView.setTypeface(Fonts.AzoSansRegular);
            contentView.setTextColor(Color.BLACK);
        }

        return container;
    }

    private void loadUserBasicInfo() {
        basicInfoContainer.addView(
                getNameTextView(String.format("%s %s", mUser.getFirstName(), mUser.getLastName()))
        );

        if (mUser.getYear() != null && !mUser.getYear().isEmpty()) {
            basicInfoContainer.addView(
                    getBasicInfoTextView(mUser.getYear())
            );
        }

        if (mUser.getMajor() != null && !mUser.getMajor().isEmpty()) {
            basicInfoContainer.addView(
                    getBasicInfoTextView(mUser.getMajor())
            );
        }

        if (mUser.getHometown() != null && !mUser.getHometown().isEmpty()) {
            basicInfoContainer.addView(
                    getBasicInfoTextView(String.format("From %s", mUser.getHometown()))
            );
        }

        // TODO(anesu): Add brownie points
    }

    private TextView getBasicInfoTextView(final String text) {
        final TextView textView = new TextView(this);
        textView.setTypeface(Fonts.AzoSansMedium);
        textView.setText(text);

        return textView;
    }

    private TextView getNameTextView(final String text) {
        final TextView textView = new TextView(this);
        textView.setTypeface(Fonts.AzoSansMedium);
        textView.setTextSize(18);
        textView.setText(text);

        textView.setPadding(0, 0, 0, 16);

        return textView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
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
