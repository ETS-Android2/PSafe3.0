package com.example.psafe.ui.login;


import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import android.view.View;

import android.widget.Toast;

import com.example.psafe.BottomActivity;
import com.example.psafe.MainActivity;





import androidx.annotation.NonNull;


import android.content.Intent;

import android.text.TextUtils;


import com.example.psafe.R;
import com.example.psafe.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.SQLOutput;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding activitySignupBinding;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignupBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = activitySignupBinding.getRoot();
        setContentView(view);


        fAuth = FirebaseAuth.getInstance();

        activitySignupBinding.progressBar.setVisibility(View.INVISIBLE);

        if(fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        activitySignupBinding.signupButton.setOnClickListener(v -> {
            String email = activitySignupBinding.signupUsername.getText().toString().trim();
            String password = activitySignupBinding.signupPassword.getText().toString().trim();
            //activityLoginBinding.username.setText(password);


            activitySignupBinding.progressBar.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(email)) {
                activitySignupBinding.signupUsername.setError("@string/email_need");
                activitySignupBinding.progressBar.setVisibility(View.INVISIBLE);
                return;
            }
            if (TextUtils.isEmpty(password)) {
                activitySignupBinding.signupUsername.setError("password is required");
                activitySignupBinding.progressBar.setVisibility(View.INVISIBLE);
                return;
            }

            fAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {




                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(SignupActivity.this, getResources().getString(R.string.login_success) ,Toast.LENGTH_SHORT).show();
                                FirebaseUser user = fAuth.getCurrentUser();
                                startActivity(new Intent(getApplicationContext(), BottomActivity.class));

                            } else {
                                // If sign in fails, display a message to the user.
                                activitySignupBinding.progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        });


        activitySignupBinding.backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}