package genieus.com.walla.v2.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import genieus.com.walla.BuildConfig;
import genieus.com.walla.R;
import genieus.com.walla.v2.utils.Conversions;
import genieus.com.walla.v2.utils.Fonts;

public class Settings extends AppCompatActivity {
    @BindView(R.id.root)
    LinearLayout mRootView;

    private interface Action {
        void call();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initUi();
    }

    private void initUi() {
        mRootView.addView(getHeaderTextView("ABOUT"));
        mRootView.addView(getMenuRow("About Walla", Optional.<String>absent(), new Action() {
            @Override
            public void call() {
                final String url = "https://www.wallasquad.com";
                final Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        }));
        mRootView.addView(getMenuRow("Terms of Use", Optional.<String>absent(), new Action() {
            @Override
            public void call() {
                final String url = "https://www.wallasquad.com/terms-and-conditions/";
                final Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        }));
        mRootView.addView(getMenuRow("How can we improve Walla?", Optional.<String>absent(), new Action() {
            @Override
            public void call() {
                final Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:hollawalladuke@gmail.com"));
                intent.putExtra(
                        Intent.EXTRA_SUBJECT, "Walla" + BuildConfig.VERSION_NAME + " Feedback"
                );

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        }));
        mRootView.addView(getHeaderTextView("ACCOUNT"));
        mRootView.addView(getMenuRow("Account email",
                Optional.of(FirebaseAuth.getInstance().getCurrentUser().getEmail()), new Action() {
                    @Override
                    public void call() {

                    }
                }));
        mRootView.addView(getMenuRow("Change my password", Optional.<String>absent(), new Action() {
            @Override
            public void call() {
                final AlertDialog.Builder alert = new AlertDialog.Builder(Settings.this);
                alert.setTitle("Change my password");
                final View passwordModal = getResetPasswordModal();
                alert.setView(passwordModal);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //final String pass = input.getText().toString();
                        final EditText input = (EditText)
                                passwordModal.findViewWithTag("password_input");
                        final EditText confirm = (EditText)
                                passwordModal.findViewWithTag("password_confirm");

                        if (input.hasSelection() && confirm.hasSelection()) {
                            final String pass = input.getText().toString();
                            if (pass.equals(confirm.getText().toString())) {
                                FirebaseAuth.getInstance().getCurrentUser().updatePassword(pass);
                            } else {
                                confirm.setError("Passwords do not match");
                            }
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        }));
        mRootView.addView(getHeaderTextView(""));
        mRootView.addView(getMenuRow("Log out", Optional.<String>absent(), new Action() {
            @Override
            public void call() {
                FirebaseAuth.getInstance().signOut();
                final Intent intent = new Intent(Settings.this, LoginActivity.class);
                startActivity(intent);
            }
        }));
        mRootView.addView(getFooter());
    }

    private View getResetPasswordModal() {
        final LinearLayout inputContainer = new LinearLayout(Settings.this);
        inputContainer.setOrientation(LinearLayout.VERTICAL);
        inputContainer.setPadding(32, 16, 32, 0);

        final EditText input = new EditText(Settings.this);
        input.setTag("password_input");
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("New password");
        input.setMinimumHeight(Conversions.dpToPx(42));
        input.setPadding(16, 16, 0, 0);
        input.setBackground(getResources().getDrawable(R.drawable.rounded_corners));

        final EditText confirmInput = new EditText(Settings.this);
        confirmInput.setTag("password_confirm");
        confirmInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmInput.setHint("Confirm new password");
        confirmInput.setMinimumHeight(Conversions.dpToPx(42));
        confirmInput.setPadding(16, 16, 0, 0);
        confirmInput.setBackground(getResources().getDrawable(R.drawable.rounded_corners));
        final LinearLayout.LayoutParams confirmParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Conversions.dpToPx(42)
        );
        confirmParams.setMargins(0, Conversions.dpToPx(16), 0, 0);
        confirmInput.setLayoutParams(confirmParams);

        inputContainer.addView(input);
        inputContainer.addView(confirmInput);

        return inputContainer;
    }

    private LinearLayout getHeaderTextView(final String header) {
        final LinearLayout container = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.settings_row, null, false);
        container.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        container.findViewById(R.id.subtext).setVisibility(View.GONE);
        final TextView headerView = (TextView) container.findViewById(R.id.title);
        headerView.setText(header.toUpperCase());
        headerView.setTypeface(Fonts.AzoSansRegular);
        headerView.setTextSize(16);

        return container;
    }

    private LinearLayout getMenuRow(final String menuText,
                                    final Optional<String> subText,
                                    final Action action) {
        final LinearLayout container = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.settings_row, null, false);
        container.setBackgroundColor(getResources().getColor(R.color.white));

        if (subText.isPresent()) {
            final TextView subView = (TextView) container.findViewById(R.id.subtext);
            subView.setText(subText.get());
            subView.setTypeface(Fonts.AzoSansRegular);
            container.findViewById(R.id.subtext).setVisibility(View.VISIBLE);
        } else {
            container.findViewById(R.id.subtext).setVisibility(View.GONE);
        }

        final TextView menuView = (TextView) container.findViewById(R.id.title);
        menuView.setText(menuText);
        menuView.setTypeface(Fonts.AzoSansRegular);
        menuView.setTextColor(getResources().getColor(R.color.black));


        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.call();
            }
        });

        return container;
    }

    private TextView getFooter() {
        final TextView footer = new TextView(this);
        footer.setPadding(16, 32, 16, 16);
        footer.setText(String.format("%s %s\n© %d %s",
                "Walla Version",
                BuildConfig.VERSION_NAME,
                Calendar.getInstance().get(Calendar.YEAR),
                "GenieUs, Inc. All rights reserved."));

        return footer;
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
