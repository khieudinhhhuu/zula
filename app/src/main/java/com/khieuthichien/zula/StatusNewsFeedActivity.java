package com.khieuthichien.zula;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khieuthichien.zula.adapter.PhotoAdapter;
import com.khieuthichien.zula.fragment.NewsFeedFragment;
import com.khieuthichien.zula.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StatusNewsFeedActivity extends AppCompatActivity {

    private EditText edt_stt_status_news_feed;
    private ImageView img_photo_status_news_feed;
    private ImageView img_select_status_news_feed;
    private Button btn_post_status_news_feed;
    private Button btn_cancel_status_news_feed;

    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_news_feed);

        mStorageReference = FirebaseStorage.getInstance().getReference("photos");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("photos");

        edt_stt_status_news_feed = findViewById(R.id.edt_stt_status_news_feed);
        img_photo_status_news_feed = findViewById(R.id.img_photo_status_news_feed);
        img_select_status_news_feed = findViewById(R.id.img_select_status_news_feed);
        btn_post_status_news_feed = findViewById(R.id.btn_post_status_news_feed);
        btn_cancel_status_news_feed = findViewById(R.id.btn_cancel_status_news_feed);
        progressDialog = new ProgressDialog(this);

        img_select_status_news_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        btn_post_status_news_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadStatus();
            }
        });

        btn_cancel_status_news_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Cancel the post", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            img_photo_status_news_feed.setVisibility(View.VISIBLE);
            Picasso.with(getApplication())
                    .load(filePath)
                    .into(img_photo_status_news_feed);
        }
    }

    private void uploadStatus() {
        String stt_status = edt_stt_status_news_feed.getText().toString().trim();

        if (stt_status.equals("")) {
            edt_stt_status_news_feed.setError(getString(R.string.edt_stt_status_news_feed));
            return;
        } else {
                if (filePath != null) {

                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    StorageReference ref = mStorageReference.child("photos/" + UUID.randomUUID().toString());
                    ref.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplication(), "Post Successful", Toast.LENGTH_SHORT).show();
                                    finish();

                                    Photo photo = new Photo(edt_stt_status_news_feed.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());
                                    String photoId = mDatabaseReference.push().getKey();
                                    mDatabaseReference.child(photoId).setValue(photo);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplication(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });

                }else{
                    Toast.makeText(this, "No file image selected", Toast.LENGTH_SHORT).show();
                }


        }

    }


}
