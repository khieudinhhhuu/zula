package com.khieuthichien.zula.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khieuthichien.zula.R;
import com.khieuthichien.zula.adapter.MessageAdapter;
import com.khieuthichien.zula.model.Chat;
import com.khieuthichien.zula.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView profileImage;
    private TextView username;
    private RecyclerView recyclerviewMessage;
    private EditText edtTextSendMessage;
    private ImageButton btnSendMessage;

    private MessageAdapter messageAdapter;
    private List<Chat> chatList;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        recyclerviewMessage = findViewById(R.id.recyclerview_chat);
        edtTextSendMessage = findViewById(R.id.edt_text_send_message);
        btnSendMessage = findViewById(R.id.btn_send_message);

        recyclerviewMessage.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerviewMessage.setLayoutManager(linearLayoutManager);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent;
        intent = getIntent();
        final String userid = intent.getStringExtra("userid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")){
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profileImage);
                }

                readMessage( firebaseUser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_message = edtTextSendMessage.getText().toString().trim();
                if (!text_message.equals("")){
                    sendMessage(firebaseUser.getUid(), userid, text_message);
                }else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }

                edtTextSendMessage.setText("");
            }
        });

    }

    private void sendMessage (String sender, String receiver, String message){

        reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);

    }

    private void readMessage(final String myid, final String userid, final String imageurl){

        chatList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatList.clear();
                for ( DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid) ) {
                        chatList.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, chatList, imageurl);
                    recyclerviewMessage.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
