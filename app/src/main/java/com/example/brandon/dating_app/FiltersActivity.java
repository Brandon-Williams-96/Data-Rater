package com.example.brandon.dating_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FiltersActivity extends AppCompatActivity {
    private RatingBar personalityBar, responseRateBar, photosBar, trustworthyBar;
    private float personality, responseRate, photos, trustworthy;
    public RadioGroup LookingForGender, LookingForType;
    private Button save;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        Auth = FirebaseAuth.getInstance();
        final FirebaseUser User = Auth.getCurrentUser();
        personalityBar = (RatingBar) findViewById(R.id.ratingBarPersonality);
        personalityBar.setRating(1);
        responseRateBar = (RatingBar) findViewById(R.id.ratingBarResponse);
        responseRateBar.setRating(1);
        photosBar = (RatingBar) findViewById(R.id.ratingBarPhotos);
        photosBar.setRating(1);
        trustworthyBar = (RatingBar) findViewById(R.id.ratingBarTrust);
        trustworthyBar.setRating(1);
        save = (Button) findViewById(R.id.Save);
        LookingForType =(RadioGroup) findViewById(R.id.RadioType);
        LookingForGender =(RadioGroup) findViewById(R.id.RadioLookingFor);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                int ID2 = LookingForGender.getCheckedRadioButtonId();
                RadioButton radioGroupLooking = (RadioButton) findViewById(ID2);
                int ID3 = LookingForType.getCheckedRadioButtonId();
                RadioButton radioGroupLookingType = (RadioButton) findViewById(ID3);
                String radioLooking = radioGroupLooking.getText().toString();
                String radioType =radioGroupLookingType.getText().toString();
                personality= personalityBar.getRating();
                responseRate=responseRateBar.getRating();
                photos=photosBar.getRating();
                trustworthy=trustworthyBar.getRating();
                String User_UID =Auth.getCurrentUser().getUid();

                DatabaseReference UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Filter").child("Personality");
                UserDataBase.setValue(personality);
                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Filter").child("Response Rate");
                UserDataBase.setValue(responseRate);
                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Filter").child("Looks like Photos");
                UserDataBase.setValue(photos);
                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Filter").child("Trustworthy");
                UserDataBase.setValue(trustworthy);
                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Looking for Gender");
                UserDataBase.setValue(radioLooking);
                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Relationship, ONS, Casual");
                UserDataBase.setValue(radioType);

                Intent intent = new Intent(FiltersActivity.this, SwipingActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

}
