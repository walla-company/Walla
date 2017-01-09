package genieus.com.walla.v2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;
import genieus.com.walla.v2.info.Fonts;

public class SignUp extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    private static final int CAMERA_INTENT_RESULT = 1;
    private static final int GALLERY_INTENT_RESULT = 2;

    private CircleImageView image;
    private EditText fname, lname, email, year, major, grad, pass, confirm;
    private TextView email_label, major_label, grad_label, pass_label, confirm_label, year_label;
    private Button signup;

    private Fonts fonts;
    private String[] sourceOptions = {"Take Photo", "Choose from Library", "Cancel"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUi();
    }

    private void initUi() {
        fonts = new Fonts(this);

        image = (CircleImageView) findViewById(R.id.profile_picture);

        email_label = (TextView) findViewById(R.id.email_label);
        email_label.setTypeface(fonts.AzoSansRegular);
        major_label = (TextView) findViewById(R.id.major_label);
        major_label.setTypeface(fonts.AzoSansRegular);
        year_label = (TextView) findViewById(R.id.year_label);
        year_label.setTypeface(fonts.AzoSansRegular);
        grad_label = (TextView) findViewById(R.id.grad_year_label);
        grad_label.setTypeface(fonts.AzoSansRegular);
        pass_label = (TextView) findViewById(R.id.password_label);
        pass_label.setTypeface(fonts.AzoSansRegular);
        confirm_label = (TextView) findViewById(R.id.confirm_label);
        confirm_label.setTypeface(fonts.AzoSansRegular);

        fname = (EditText) findViewById(R.id.fname_in);
        fname.setTypeface(fonts.AzoSansRegular);
        fname.setTextColor(email_label.getTextColors());
        lname = (EditText) findViewById(R.id.lname_in);
        lname.setTypeface(fonts.AzoSansRegular);
        lname.setTextColor(email_label.getTextColors());
        email = (EditText) findViewById(R.id.email_in);
        email.setTypeface(fonts.AzoSansRegular);
        email.setTextColor(email_label.getTextColors());
        year = (EditText) findViewById(R.id.year_in);
        year.setTypeface(fonts.AzoSansRegular);
        year.setTextColor(email_label.getTextColors());
        major = (EditText) findViewById(R.id.major_in);
        major.setTypeface(fonts.AzoSansRegular);
        major.setTextColor(email_label.getTextColors());
        grad = (EditText) findViewById(R.id.grad_year_in);
        grad.setTypeface(fonts.AzoSansRegular);
        grad.setTextColor(email_label.getTextColors());
        pass = (EditText) findViewById(R.id.password_in);
        pass.setTypeface(fonts.AzoSansRegular);
        pass.setTextColor(email_label.getTextColors());
        confirm = (EditText) findViewById(R.id.confirm_in);
        confirm.setTypeface(fonts.AzoSansRegular);
        confirm.setTextColor(email_label.getTextColors());

        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(this);
        changeBackGroundColor(signup, getResources().getColor(R.color.lightblue));
    }

    private void attemptSignup() {
        boolean valid = true;

        String fnameStr = fname.getText().toString().trim();
        if (fnameStr.isEmpty()) {
            fname.setError("required");
            valid = false;
        }

        String lnameStr = lname.getText().toString().trim();
        if (lnameStr.isEmpty()) {
            lname.setError("required");
            valid = false;
        }

        String emailStr = email.getText().toString().trim();
        if (emailStr.isEmpty()) {
            email.setError("required");
            valid = false;
        }

        String yearStr = year.getText().toString().trim();
        if (yearStr.isEmpty()) {
            year.setError("required");
        }

        String majorStr = major.getText().toString().trim();
        if (majorStr.isEmpty()) {
            major.setError("required");
            valid = false;
        }

        String gradStr = grad.getText().toString().trim();
        if (gradStr.isEmpty()) {
            grad.setError("required");
            valid = false;
        }

        String passStr = pass.getText().toString().trim();
        if (passStr.isEmpty()) {
            pass.setError("required");
            valid = false;
        }

        String confirmStr = confirm.getText().toString().trim();
        if (confirmStr.isEmpty()) {
            confirm.setError("required");
            valid = false;
        }

        if(valid){
            
        }
    }

    private void chooseProfilePicture() {
        showImageSourceOptions();
    }

    private void showImageSourceOptions() {
        new AlertDialog.Builder(this)
                .setTitle("Profile Picture")
                .setItems(sourceOptions, this)
                .setCancelable(true)
                .create()
                .show();
    }

    private void takePhoto() {
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camIntent, CAMERA_INTENT_RESULT);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT_RESULT);
    }

    private void setProfilePic(Bitmap bmp) {
        image.setImageBitmap(bmp);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_INTENT_RESULT) {
                try {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    setProfilePic(bitmap);
                } catch (Exception e) {
                    Toast.makeText(this, "Error retrieving picture", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == GALLERY_INTENT_RESULT) {
                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    setProfilePic(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error retrieving picture", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(this, "Error retrieving picture", Toast.LENGTH_LONG).show();
        }
    }

    private void changeBackGroundColor(View view, int color) {
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(color);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                //take photo
                takePhoto();
                break;
            case 1:
                //choose from gallery
                openGallery();
                break;
            case 2:
                //cancel
                dialog.cancel();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.signup:
                attemptSignup();
                break;
            case R.id.profile_picture:
                chooseProfilePicture();
                break;
            default:
                break;
        }
    }
}
