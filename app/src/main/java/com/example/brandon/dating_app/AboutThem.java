package com.example.brandon.dating_app;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AboutThem extends AppCompatActivity {
    public String OtherUserUID, Title, Description;
    private TextView AboutThem, ReviewTitle,Review;
    private RatingBar AveragePersonality, AverageResponseRate, AveragePhoto, AverageTrustworthy;
    private float Personality, ResponseRate, Photo, TrustWorthy;
    private int ReviewTotal;
    public ImageView Picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_them);
        Information();
    }

    public void Information(){
        OtherUserUID = getIntent().getStringExtra("AboutThemUser");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUserUID);
        database.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded (DataSnapshot dataSnapshot, String prevChildKey) {
                AboutThem = (TextView) findViewById(R.id.InformationAboutThem);
                ReviewTitle = (TextView) findViewById(R.id.TitleReview);
                Review = (TextView) findViewById(R.id.Review);
                AveragePersonality = (RatingBar) findViewById(R.id.AverageRatingBarPersonality);
                AverageResponseRate = (RatingBar) findViewById(R.id.AverageRatingBarResponse);
                AveragePhoto = (RatingBar) findViewById(R.id.AverageRatingBarPhotos);
                AverageTrustworthy = (RatingBar) findViewById(R.id.AverageRatingBarTrust);
                Picture = (ImageView) findViewById(R.id.MyPicture);

                if (dataSnapshot.child("Review Total").getValue(Float.class)==null) {
                    if (dataSnapshot.child("About Me").getValue(String.class)!=null) {
                        AboutThem.setText(dataSnapshot.child("About Me").getValue(String.class));
                        String urlImage = dataSnapshot.child("Picture").getValue(String.class);

                        Glide.with(AboutThem.this)
                                .load(urlImage)
                                .into(Picture);

                    }
                    else
                    {
                        AboutThem.setText("No Description");
                        String urlImage = dataSnapshot.child("Picture").getValue(String.class);

                        Glide.with(AboutThem.this)
                                .load(urlImage)
                                .into(Picture);

                    }
                    ReviewTitle.setText("NEW USER NO REVIEWS YET");
                    Review.setText("NEW USER NO REVIEWS YET");

                }
                else if (dataSnapshot.child("Review Total").getValue(Integer.class)!=null)
                {

                    Description =dataSnapshot.child("About Me").getValue(String.class);
                    ReviewTotal= dataSnapshot.child("Review Total").getValue(Integer.class);
                    Personality = dataSnapshot.child("Personality").getValue(Float.class);
                    ResponseRate = dataSnapshot.child("Response Rate").getValue(Float.class);
                    Photo = dataSnapshot.child("Looks like Photos").getValue(Float.class);
                    TrustWorthy = dataSnapshot.child("Trustworthy").getValue(Float.class);
                    String urlImage = dataSnapshot.child("Picture").getValue(String.class);

                    Glide.with(AboutThem.this)
                            .load(urlImage)
                            .into(Picture);
                    ReviewTitle.setText(String.valueOf(Personality));
                    AveragePersonality.setRating(Personality);
                    AverageResponseRate.setRating(ResponseRate);
                    AveragePhoto.setRating(Photo);
                    AverageTrustworthy.setRating(TrustWorthy);
                    if (dataSnapshot.child("About Me").getValue(String.class)==null) {
                        AboutThem.setText("No Description");
                    }
                    else
                    {
                        AboutThem.setText(Description);
                    }
                    Title = dataSnapshot.child(Integer.toString(ReviewTotal)).child("Title").getValue(String.class);
                    ReviewTitle.setText(Title);
                    Review.setText(dataSnapshot.child(Integer.toString(ReviewTotal)).child("Review").getValue(String.class));
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
