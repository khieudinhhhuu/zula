package com.khieuthichien.zula.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.khieuthichien.zula.R;
import com.squareup.picasso.Picasso;

public class UserFragment extends Fragment {

    public UserFragment() {
        // Required empty public constructor
    }

    private ImageView imgView;
    private TextView tvname;
    private TextView tvemail;
    private Button btnLogout;

    private static String TAG = "UserFragment";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        imgView = view.findViewById(R.id.imgView);
        tvname = view.findViewById(R.id.tvname);
        tvemail = view.findViewById(R.id.tvemail);
        btnLogout = view.findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    String name = user.getDisplayName();
                    String email = user.getEmail();
                    Uri photoUrl = user.getPhotoUrl();

                    tvname.setText(name);
                    tvemail.setText(email);
                    Picasso.with(getContext()).load(photoUrl).into(imgView);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                tvname.setText("No name");
                tvemail.setText("No email");
                imgView.setImageResource(R.mipmap.ic_launcher);
            }
        });

        return view;
    }

}
