package com.example.brandon.dating_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Button loginButton, registerButton, forgotPasswordButton;
    private EditText Email,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Auth = FirebaseAuth.getInstance();
        final FirebaseUser User = Auth.getCurrentUser();

        registerButton = (Button) findViewById(R.id.register_button);
        forgotPasswordButton = (Button) findViewById(R.id.reset_password_button);
        loginButton = (Button) findViewById(R.id.email_sign_in_button);
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                Intent intentReg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentReg);
            }
        });



        loginButton=(Button)findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= Email.getText().toString();
                if (email.equalsIgnoreCase("") || (email==null))
                {
                    email="word";
                    Toast.makeText(LoginActivity.this, "Enter an Email", Toast.LENGTH_LONG).show();
                }
                String password= Password.getText().toString();
                if (password.equalsIgnoreCase("") || (password==null))
                {
                    password="word";
                    Toast.makeText(LoginActivity.this, "Enter a Password", Toast.LENGTH_LONG).show();
                }
                Auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    LoggedIn();
                                } else {
                                    Toast.makeText(LoginActivity.this, "User Name or Password incorrect.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                String email= Email.getText().toString();
                if (email.equalsIgnoreCase("") || (email==null))
                {
                    email="word";
                }
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Reset Email sent.",
                                            Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Enter an Email", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    public void LoggedIn() {
        Intent intent = new Intent(LoginActivity.this, SwipingActivity.class);
        startActivity(intent);

    }

}
