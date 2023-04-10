package com.example.slingo2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadProfilePicture extends AppCompatActivity {
    private ProgressBar progressBar;
    private ImageView newProfilePic;
    Button uploadPicChoose,uploadNewPic;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    FirebaseAuth uAuth;
    private static final int PICK_IMAGE_REQUEST=1;
    private Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);
        uploadPicChoose=findViewById(R.id.profile_pic_new_btn);
        uploadNewPic=findViewById(R.id.upload_profile_pic_btn);
        progressBar=findViewById(R.id.newProfilePicBar);
        newProfilePic=findViewById(R.id.image_new_profile_pic);
        uAuth=FirebaseAuth.getInstance();
        firebaseUser=uAuth.getCurrentUser();

        storageReference= FirebaseStorage.getInstance().getReference("DisplayPics");
        Uri uri=firebaseUser.getPhotoUrl();
        Picasso.get().load(uri).into(newProfilePic);
        uploadPicChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        uploadNewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(view.VISIBLE);
                uploadPic();
            }
        });
    }
    private void openFileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    private void uploadPic(){
        if(uriImage!=null){
            StorageReference fileRefrence=storageReference.child(uAuth.getCurrentUser().getUid()+"."+getFileExtension(uriImage));
            fileRefrence.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri=uri;
                            firebaseUser=uAuth.getCurrentUser();
                            UserProfileChangeRequest profileChangeRequests=new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileChangeRequests);
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadProfilePicture.this, "Successfully uploaded new profile picture", Toast.LENGTH_SHORT).show();

                }
            });
        }
        else{
            Toast.makeText(this, "Select an image to upload", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            uriImage=data.getData();
            newProfilePic.setImageURI(uriImage);
        }
    }

}