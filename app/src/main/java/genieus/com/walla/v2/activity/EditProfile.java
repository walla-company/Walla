package genieus.com.walla.v2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import genieus.com.walla.R;
import genieus.com.walla.v2.api.WallaApi;
import genieus.com.walla.v2.fragment.Home;
import genieus.com.walla.v2.info.Fonts;
import genieus.com.walla.v2.info.RoundedTransformation;
import genieus.com.walla.v2.info.UserInfo;
import genieus.com.walla.v2.info.Utility;

public class EditProfile extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    private static final int CAMERA_INTENT_RESULT = 1;
    private static final int GALLERY_INTENT_RESULT = 2;


    private UserInfo info;
    private FirebaseAuth auth;
    private WallaApi api;
    private Fonts fonts;
    private CircleImageView profile_pic;
    private NestedScrollView parent;
    private EditText hometown_in, description_in, fname_in, year_in, major_in, lname_in;
    private TextView profile_pic_label, year_label, major_label, hometown_label, description_label,
            fname_label, lname_label;

    private String[] sourceOptions = {"Take Photo", "Choose from Library", "Cancel"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        auth = FirebaseAuth.getInstance();

        String uid = auth.getCurrentUser().getUid();

        api = new WallaApi(this);
        api.getUserInfo(new WallaApi.OnDataReceived() {
            @Override
            public void onDataReceived(Object data, int call) {
                info = (UserInfo) data;
                initUi();
            }
        }, uid);
    }

    private void initUi() {
        fonts = new Fonts(this);

        parent = (NestedScrollView) findViewById(R.id.content_edit_profile);
        profile_pic_label = (TextView) findViewById(R.id.profile_picture_label);
        profile_pic_label.setTypeface(fonts.AzoSansRegular);
        year_label = (TextView) findViewById(R.id.year_label);
        year_label.setTypeface(fonts.AzoSansRegular);
        major_label = (TextView) findViewById(R.id.major_label);
        major_label.setTypeface(fonts.AzoSansRegular);
        hometown_label = (TextView) findViewById(R.id.hometown_label);
        hometown_label.setTypeface(fonts.AzoSansRegular);
        description_label = (TextView) findViewById(R.id.description_label);
        description_label.setTypeface(fonts.AzoSansRegular);
        fname_label = (TextView) findViewById(R.id.fname_label);
        fname_label.setTypeface(fonts.AzoSansRegular);
        lname_label = (TextView) findViewById(R.id.lname_label);
        lname_label.setTypeface(fonts.AzoSansRegular);
        year_in = (EditText) findViewById(R.id.year_in);
        year_in.setTypeface(fonts.AzoSansRegular);
        year_in.setText(info.getYear());
        year_in.setTextColor(fname_label.getTextColors());
        major_in = (EditText) findViewById(R.id.major_in);
        major_in.setTypeface(fonts.AzoSansRegular);
        major_in.setText(info.getMajor());
        major_in.setTextColor(fname_label.getTextColors());
        hometown_in = (EditText) findViewById(R.id.hometown_in);
        hometown_in.setTypeface(fonts.AzoSansRegular);
        hometown_in.setText(info.getHometown());
        hometown_in.setTextColor(fname_label.getTextColors());
        description_in = (EditText) findViewById(R.id.description_in);
        description_in.setTypeface(fonts.AzoSansRegular);
        description_in.setText(info.getDescription());
        description_in.setTextColor(fname_label.getTextColors());
        fname_in = (EditText) findViewById(R.id.fname_in);
        fname_in.setTypeface(fonts.AzoSansRegular);
        fname_in.setText(info.getFirst_name());
        fname_in.setTextColor(fname_label.getTextColors());
        lname_in = (EditText) findViewById(R.id.lname_in);
        lname_in.setTypeface(fonts.AzoSansRegular);
        lname_in.setText(info.getLast_name());
        lname_in.setTextColor(fname_label.getTextColors());
        profile_pic = (CircleImageView) findViewById(R.id.profile_image_in);

        Log.d("picdata", "url: " + info.getProfile_url());
        if(info.getProfile_url() != null && !info.getProfile_url().equals("")) {
            Picasso.with(this) //Context
                    .load(info.getProfile_url()) //URL/FILE
                    .into(profile_pic);//an ImageView Object to show the loaded image;
        }

        profile_pic.setOnClickListener(this);
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

    private void setProfilePic(Bitmap bmp){
        profile_pic.setImageBitmap(bmp);
        Utility.initApi(this);
        Utility.saveProfilePic(bmp, auth.getCurrentUser().getUid());
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

    private void saveData(){
        api.saveUserFirstName(info.getUid(), fname_in.getText().toString());
        api.saveUserLastName(info.getUid(), lname_in.getText().toString());
        api.saveUserAcademicLevel(info.getUid(), year_in.getText().toString());
        api.saveUserMajor(info.getUid(), major_in.getText().toString());
        api.saveUserHometown(info.getUid(), hometown_in.getText().toString());
        api.saveUserDescription(info.getUid(), description_in.getText().toString());
        //api.saveUserProfileImageUrl(info.getUid(), "");

        Snackbar.make(parent, "Profile saved!", Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.profile_image_in:
                chooseProfilePicture();
                break;
            default:
                break;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.action_save:
                saveData();
                break;
            case android.R.id.home:
                onBackPressed();
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
        MainContainer.refresh();
    }
}
