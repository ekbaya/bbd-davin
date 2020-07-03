package com.example.hbddavin.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hbddavin.R;
import com.example.hbddavin.apis.PostAPI;
import com.example.hbddavin.modals.ModelPost;
import com.example.hbddavin.services.PostListener;
import com.example.hbddavin.services.SoundService;
import com.example.hbddavin.utils.Loader;
import com.example.hbddavin.utils.Permissions;
import com.google.firebase.database.DatabaseError;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener, PostListener {
    private EditText editName;
    private ImageView imageView;
    private EditText editMessage;
    private Button btnUpload;

    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int STORAGE_REQUEST_CODE = 200;
    private String[] storagePermissions;
    private Permissions permissions;

    //image picked will be saved in this Uri
    Uri image_uri = null;

    private PostAPI postAPI;
    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Write on Davin's birthday");
        setContentView(R.layout.activity_add_post);
        editName = findViewById(R.id.editName);
        imageView = findViewById(R.id.imageView);
        editMessage = findViewById(R.id.editMessage);
        btnUpload = findViewById(R.id.btnUpload);

        imageView.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        //init arrays of permissions
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        permissions = new Permissions(this);

        postAPI = new PostAPI();
        postAPI.setPostListener(this);
        loader = new Loader(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
       if (view.equals(imageView)){
           if (!permissions.checkStoragePermission()){
               requestStoragePermission();
           }
           else {
               pickFromGallery();
           }
       }
       if (view.equals(btnUpload)){
           if (validated()){
               //start uploading
               //get Image from imageview
               Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
               ByteArrayOutputStream baos = new ByteArrayOutputStream();
               //image compress
               bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
               byte[] data = baos.toByteArray();
               String name = editName.getText().toString();
               String message = editMessage.getText().toString();

               //calling publish post api
               postAPI.publishPost(name, data, message);
               loader.showDialogue();

           }
       }
    }

    private boolean validated() {
        if (TextUtils.isEmpty(editName.getText().toString())){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (imageView.getDrawable().getConstantState() == getResources().getDrawable( R.drawable.add_image).getConstantState()){
            Toast.makeText(this, "Please add a photo to upload", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (TextUtils.isEmpty(editMessage.getText().toString())){
            Toast.makeText(this, "Please write a message to Davin", Toast.LENGTH_LONG).show();
            return false;
        }
        else return true;
    }

    private void pickFromGallery() {
        //pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        //request runtime storage permission
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*This method called when the user press Allow or Deny from permission request dialog
         * Im handling permission cases (allowed or denied)*/
        if (requestCode == STORAGE_REQUEST_CODE) {// picking from, gallery first check if storage are allowed or not
            if (grantResults.length > 0) {
                boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (writeStorageAccepted) {
                    //Permission enabled
                    pickFromGallery();
                } else {
                    //permission denied
                    Toast.makeText(this, "Please enable storage permission ", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go to previous activity
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode== RESULT_OK){
            if (requestCode== IMAGE_PICK_GALLERY_CODE){
                //image is picked from gallery, get the uri of image
                image_uri = data.getData();

                //set to image view
                imageView.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImageUploadFailure(Exception e) {
        loader.hideDialogue();
        Toast.makeText(this, "Error uploading Image: "+e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostUploaded() {
        loader.hideDialogue();
        Toast.makeText(this, "Thanks for your post...", Toast.LENGTH_LONG).show();
        editName.setText("");
        imageView.setImageResource(R.drawable.add_image);
        editMessage.setText("");
    }

    @Override
    public void onPostUploadFailure(Exception e) {
        loader.hideDialogue();
        Toast.makeText(this, "Error uploading Post: "+e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostDataReceived(List<ModelPost> postList) {

    }

    @Override
    public void onDatabaseCancelled(DatabaseError error) {

    }

    @Override
    protected void onResume() {
        //start service and play music
        startService(new Intent(AddPostActivity.this, SoundService.class));
        super.onResume();
    }
}