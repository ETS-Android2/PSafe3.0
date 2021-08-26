package com.example.psafe.ui.login;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psafe.MainActivity;
import com.example.psafe.R;





import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;


import com.example.psafe.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.SQLOutput;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding activityLoginBinding;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = activityLoginBinding.getRoot();
        setContentView(view);


        fAuth = FirebaseAuth.getInstance();
        activityLoginBinding.progressBar.setVisibility(View.INVISIBLE);

        if(fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        activityLoginBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = activityLoginBinding.loginUsername.getText().toString().trim();
                String password = activityLoginBinding.loginPassword.getText().toString().trim();
                //activityLoginBinding.username.setText(password);
                activityLoginBinding.progressBar.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(email)) {
                    activityLoginBinding.loginUsername.setError("@");
                    activityLoginBinding.progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    activityLoginBinding.loginPassword.setError("password is required");
                    activityLoginBinding.progressBar.setVisibility(View.INVISIBLE);
                    return;
                }



                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    Toast.makeText(LoginActivity.this, "login successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();


                                } else {
                                    // If sign in fails, display a message to the user.
                                    activityLoginBinding.progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(LoginActivity.this, "error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                                }
                            }
                        });



            }


        });

        activityLoginBinding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });


    }
}