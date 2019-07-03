package com.example.brandon.dating_app;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RaterActivity extends AppCompatActivity {

    private RatingBar personalityBar, responseRateBar, photosBar, trustworthyBar;
    private EditText title, review;
    private Button submit;
    private String OtherUserUID, UserUID;
    private Integer reviewTotal;
    private String titleFix, reviewFix, title1;
    private float personality, responseRate, photos, trustworthy, previousPersonality, previousResponse, previousPhotos, previousTrust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rater);

        title = (EditText) findViewById(R.id.reviewTitle);
        review = (EditText) findViewById(R.id.reviewPerson);
        personalityBar = (RatingBar) findViewById(R.id.ratingBarPersonality);
        personalityBar.setRating(1);
        responseRateBar = (RatingBar) findViewById(R.id.ratingBarResponse);
        responseRateBar.setRating(1);
        photosBar = (RatingBar) findViewById(R.id.ratingBarPhotos);
        photosBar.setRating(1);
        trustworthyBar = (RatingBar) findViewById(R.id.ratingBarTrust);
        trustworthyBar.setRating(1);
        submit = (Button) findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                personality= personalityBar.getRating();
                responseRate=responseRateBar.getRating();
                photos=photosBar.getRating();
                trustworthy=trustworthyBar.getRating();

                UserReview();
                OtherUserUID = getIntent().getStringExtra("UID");
                UserUID = getIntent().getStringExtra("Rater");
                Intent intent = new Intent(RaterActivity.this, SwipingActivity.class);
                startActivity(intent);

            }
        });

            }

    public void UserReview(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Users").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded (DataSnapshot dataSnapshot, String prevChildKey) {
                if (dataSnapshot.getKey().equals(OtherUserUID)) {
                    reviewTotal = dataSnapshot.child("Review").child("Review Total").getValue(Integer.class);

                    if (reviewTotal == null || reviewTotal == 0) {
                        reviewTotal=1;
                        personality=personality;
                        responseRate=responseRate;
                        photos=photos;
                        trustworthy=trustworthy;
                        titleFix = title.getText().toString();
                        reviewFix = review.getText().toString();

                    } else {
                        previousPersonality = dataSnapshot.child("Review").child("Personality").getValue(Float.class);
                        previousResponse = dataSnapshot.child("Review").child("Response Rate").getValue(Float.class);
                        previousPhotos = dataSnapshot.child("Review").child("Looks like Photos").getValue(Float.class);
                        previousTrust = dataSnapshot.child("Review").child("Trustworthy").getValue(Float.class);
                        personality=(personality + (previousPersonality*reviewTotal))/(reviewTotal+1);
                        reviewTotal=reviewTotal+1;
                        responseRate=(responseRate + (previousResponse*reviewTotal))/(reviewTotal+1);
                        photos=(photos + (previousPhotos*reviewTotal))/(reviewTotal+1);
                        trustworthy=(trustworthy + (previousTrust*reviewTotal))/(reviewTotal+1);
                        titleFix = title.getText().toString();
                        reviewFix = review.getText().toString();
                    }

                    DatabaseReference Review = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUserUID).child("Review").child("Review Total");
                    Review.setValue(reviewTotal);
                    Review = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUserUID).child("Review").child("Personality");
                    Review.setValue(personality);
                    Review = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUserUID).child("Review").child("Response Rate");
                    Review.setValue(responseRate);
                    Review = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUserUID).child("Review").child("Looks like Photos");
                    Review.setValue(photos);
                    Review = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUserUID).child("Review").child("Trustworthy");
                    Review.setValue(trustworthy);
                    Review = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUserUID).child("Review").child(reviewTotal.toString()).child("Review");
                    Review.setValue(reviewFix);
                    Review = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUserUID).child("Review").child(reviewTotal.toString()).child("Title");
                    Review.setValue(titleFix);
                }

            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }


}
