package com.khieuthichien.zula.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khieuthichien.zula.R;
import com.khieuthichien.zula.adapter.MessageAdapter;
import com.khieuthichien.zula.adapter.RequestsUserAdapter;
import com.khieuthichien.zula.model.Chat;
import com.khieuthichien.zula.model.User;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    private RecyclerView recyclerviewMessage;

    private RequestsUserAdapter requestsUserAdapter;
    private List<User> userList;

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;

    private List<String> strings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        recyclerviewMessage = view.findViewById(R.id.recyclerview_message);

        recyclerviewMessage.setHasFixedSize(true);
        recyclerviewMessage.setLayoutManager(new LinearLayoutManager(getContext()));

        strings = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                strings.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getSender().equals(firebaseUser.getUid())){
                        strings.add(chat.getReceiver());
                    }

                    if (chat.getReceiver().equals(firebaseUser.getUid())){
                        strings.add(chat.getSender());
                    }

                }

                //readChats();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

//    private void readChats(){
//
//        userList = new ArrayList<>();
//
//        reference = FirebaseDatabase.getInstance().getReference("Users");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                userList.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    User user = snapshot.getValue(User.class);
//
//                    //display 1 user from chats
//                    for ( String id : strings){
//                        if (user.getId().equals(id)){
//                            if (userList.size() != 0){
//                                for (User user1234 : userList){
//                                    if (!user.getId().equals(user1234.getId())){
//                                        userList.add(user);
//                                    }
//                                }
//                            }else {
//                                userList.add(user);
//                            }
//                        }
//                    }
//                }
//
//                requestsUserAdapter = new RequestsUserAdapter(getContext(), userList);
//                recyclerviewMessage.setAdapter(requestsUserAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


}
