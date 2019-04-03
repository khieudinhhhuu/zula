package com.khieuthichien.zula.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khieuthichien.zula.R;
import com.khieuthichien.zula.ui.LoginActivity;

import java.util.HashMap;

public class CreateNewAccountActivity extends AppCompatActivity {

    private EditText edtSignupUsername;
    private EditText edtSignupEmail;
    private EditText edtSignupPassword;
    private Button btnRegister;
    private Button btnCancel;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private static String TAG = "CreateNewAccountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Regiter");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtSignupUsername = findViewById(R.id.edt_signup_username);
        edtSignupEmail = findViewById(R.id.edt_signup_email);
        edtSignupPassword = findViewById(R.id.edt_signup_password);
        btnRegister = findViewById(R.id.btn_register);
        btnCancel = findViewById(R.id.btn_cancel);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtSignupUsername.getText().toString();
                String email = edtSignupEmail.getText().toString();
                String password = edtSignupPassword.getText().toString();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    if (username.equals("")) {
                        edtSignupUsername.setError(getString(R.string.notify_empty_username));
                        return;
                    }
                    if (email.equals("")) {
                        edtSignupEmail.setError(getString(R.string.notify_empty_email));
                        return;
                    }
                    if (password.equals("")) {
                        edtSignupPassword.setError(getString(R.string.notify_empty_password));
                        return;
                    }
                } else {
                    if (password.length() < 6) {
                        if (password.length() < 6) {
                            edtSignupPassword.setError(getString(R.string.notify_empty_password_length));
                            return;
                        }
                    } else {
                        Register(username, email, password);
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), LoginActivity.class));
                finish();
            }
        });

    }

    private void Register(final String username, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");

                            mDatabaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(CreateNewAccountActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(CreateNewAccountActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
