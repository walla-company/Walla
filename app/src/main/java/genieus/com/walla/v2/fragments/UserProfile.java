package genieus.com.walla.v2.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import genieus.com.walla.R;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.ui.EditProfile;
import genieus.com.walla.v2.utils.Fonts;
import genieus.com.walla.v2.datatypes.User;
import genieus.com.walla.v2.utils.Conversions;
import genieus.com.walla.v2.utils.ImageUtils;

import static android.app.Activity.RESULT_OK;

public class UserProfile extends Fragment {
    @BindView(R.id.basic_info)
    LinearLayout basicInfoContainer;

    @BindView(R.id.more_info)
    LinearLayout moreInfoContainer;

    @BindView(R.id.profile_picture)
    ImageView profileIcon;

    private User mUser;

    private static final int CAMERA_INTENT_RESULT = 1;
    private static final int GALLERY_INTENT_RESULT = 2;

    private void initUi() {
        WallaApi.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                if (data instanceof User) {
                    mUser = (User) data;

                    loadProfileIcon();
                    loadUserBasicInfo();
                    loadUserMoreInfo();
                }
            }
        }, FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    private void loadProfileIcon() {
        ImageUtils.loadImageFromUrl(
                getContext(),
                profileIcon,
                Optional.of(mUser.getProfileUrl())
        );
    }

    private void loadUserMoreInfo() {
        moreInfoContainer.addView(
                getMoreInfoView(getString(R.string.about_me), mUser.getDescription())
        );

        moreInfoContainer.addView(
                getMoreInfoView(getString(R.string.why_school), mUser.getReasonSchool())
        );

        moreInfoContainer.addView(
                getMoreInfoView(getString(R.string.want_to_meet), mUser.getWannaMeet())
        );

        String goals = null;
        if (mUser.getGoal1() != null && !mUser.getGoal1().isEmpty()
                && mUser.getGoal2() != null &&! mUser.getGoal2().isEmpty()
                && mUser.getGoal2() != null && !mUser.getGoal2().isEmpty()) {
            goals = String.format("1. %s\n2. %s\n3. %s",
                    mUser.getGoal1(), mUser.getGoal2(), mUser.getGoal3());
        }
        moreInfoContainer.addView(
                getMoreInfoView("My 3 goals for this year are…", goals)
        );
    }

    private View getMoreInfoView(final String title, final String content) {
        final LinearLayout container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        final int padding = Conversions.dpToPx(16);
        container.setPadding(padding, padding, padding, padding);

        final TextView headerView = new TextView(getContext());
        final TextView contentView = new TextView(getContext());

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
        final TextView textView = new TextView(getContext());
        textView.setTypeface(Fonts.AzoSansMedium);
        textView.setText(text);

        return textView;
    }

    private TextView getNameTextView(final String text) {
        final TextView textView = new TextView(getContext());
        textView.setTypeface(Fonts.AzoSansMedium);
        textView.setTextSize(18);
        textView.setText(text);

        textView.setPadding(0, 0, 0, 16);

        return textView;
    }


    public UserProfile() {
        // Required empty public constructor
    }

    public static UserProfile newInstance() {
        return new UserProfile();
    }

    @OnClick(R.id.edit)
    public void OnEditProfileIconClicked() {
        new AlertDialog.Builder(getContext())
                .setTitle("Profile Picture")
                .setItems(new CharSequence[]{"Take Photo", "Choose from Library", "Cancel"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Take photo
                                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(camIntent, CAMERA_INTENT_RESULT);
                                break;
                            case 1: // Choose from gallery
                                final Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT_RESULT);
                                break;
                            default:
                                dialog.cancel();
                                break;
                        }
                    }
                })
                .setCancelable(true)
                .create()
                .show();
    }

    private void setProfileIcon(final Bitmap image) {
        profileIcon.setImageBitmap(image);
        ImageUtils.saveProfilePic(image, FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);
        initUi();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_INTENT_RESULT) {
                try {
                    final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    setProfileIcon(bitmap);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error retrieving picture", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == GALLERY_INTENT_RESULT) {
                final Uri uri = data.getData();
                try {
                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    setProfileIcon(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error retrieving picture", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(getContext(), "Error retrieving picture", Toast.LENGTH_LONG).show();
        }
    }
}
