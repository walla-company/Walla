package genieus.com.walla;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, DialogInterface.OnClickListener{

    private DatabaseReference mDatabase;
    FirebaseUser user;
    CircleImageView profile_pic;
    ListView lv;
    RelativeLayout activities;
    TextView name, email;
    private static Bitmap prof_img;

    final String TAG = "msg";
    final String[] settings = new String[]{"My Interests", "Account Settings", "Logout"};
    final String[] profilePicSettings = new String[]{"Take Photo", "Choose from Library", "Cancel"};
    final int CAMERA_INTENT_RESULT = 1;
    final int GALLERY_INTENT_RESULT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initUi();

    }
    private void initUi(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        name = (TextView) findViewById(R.id.user_name);
        email = (TextView) findViewById(R.id.user_email);

        String fname = user.getDisplayName();
        String femail = user.getEmail();
        getName();

        name.setText(fname);
        email.setText(femail);

        lv = (ListView) findViewById(R.id.profile_options);
        SettingsAdapter adapter = new SettingsAdapter(getBaseContext(), R.layout.settings_template, settings);
        lv.setAdapter(adapter);

        activities = (RelativeLayout) findViewById(R.id.activities_btn);
        activities.setOnClickListener(this);

        lv.setOnItemClickListener(this);

        profile_pic = (CircleImageView) findViewById(R.id.profile_pic);
        profile_pic.setOnClickListener(this);

        if(prof_img == null)
            getProfilePic();
        else
            profile_pic.setImageBitmap(prof_img);



    }
    private void getName(){
        mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();
                        Log.d("map7", userInfo.toString());
                        name.setText((String) userInfo.get("name"));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });
    }

    private void getProfilePic(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://walla-launch.appspot.com");

        final long ONE_MEGABYTE = 1024 * 1024 * 5;
        storageRef.child("profile_images").child(user.getUid() + ".jpg").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d("ppic","pic found");
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                profile_pic.setImageBitmap(bmp);
            }
        });

    }


    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private void showInterests(){
        Intent intent = new Intent(this, MyInterests.class);
        startActivity(intent);
    }

    private void showSettings(){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void showProfilePicDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Profile Picture")
                .setItems(profilePicSettings, this)
                .setCancelable(true)
                .create()
                .show();
    }

    private void takePhoto(){
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camIntent, CAMERA_INTENT_RESULT);
    }

    private void setProfilePic(Bitmap bmp){
        prof_img = bmp;
        profile_pic.setImageBitmap(bmp);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://walla-launch.appspot.com");

        UploadTask uploadTask = storageRef.child("profile_images")
                .child(user.getUid() + ".jpg")
                .putBytes(compressToByteArray(bmp));

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT_RESULT);
    }

    private byte[] compressToByteArray(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byte_data = stream.toByteArray();
        return byte_data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_INTENT_RESULT){
                try
                {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    setProfilePic(bitmap);
                }
                catch (Exception e)
                {
                    showMsg("Error retrieving picture");
                }
            }
            else if(requestCode == GALLERY_INTENT_RESULT){
                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    setProfilePic(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    showMsg("Error retrieving picture");
                }
            }
        }else{
            showMsg("Error retrieving picture");
        }
    }

    private void showMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_write){
            Intent intent = new Intent(this, Create.class);
            startActivity(intent);
        }else if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.activities_btn){
            Intent intent = new Intent(this, Activities.class);
            intent.putExtra("login", false);
            startActivity(intent);
        }else if(id == R.id.profile_pic){
            showProfilePicDialog();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(settings[position]){
            case "Logout":
                logout();
                break;
            case "My Interests":
                showInterests();
                break;
            case "Account Settings":
                showSettings();
        }

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch(which){
            case 0: //take a photo
                takePhoto();
                break;
            case 1: //take phote from gallery
                openGallery();
                break;
            case 2: //cancel
                dialog.cancel();
        }
    }
}
