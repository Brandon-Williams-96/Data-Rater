package com.example.brandon.dating_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class MainActivity extends AppCompatActivity {
    public Button RegisterButton, LogInButton;

    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Auth = FirebaseAuth.getInstance();
        final FirebaseUser logged = Auth.getCurrentUser();



        RegisterButton = (Button) findViewById(R.id.register_button);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                if (logged!=null)
                {
                    LoggedIn();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        LogInButton = (Button) findViewById(R.id.sign_in_button);
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logged!=null)
                {
                    LoggedIn();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void LoggedIn() {
        Intent intent = new Intent(MainActivity.this, SwipingActivity.class);
        startActivity(intent);
        finish();

    }

}

