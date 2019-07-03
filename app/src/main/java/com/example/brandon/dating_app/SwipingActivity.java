package com.example.brandon.dating_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.lang.reflect.Field;

public class SwipingActivity extends AppCompatActivity {

    public String UserSex,UserLooking,UserType,UserName, OtherUserSex, OtherUserLooking, OtherUserType, OtherUserName, UserLike, UserDislike, Potential_M, Potential_M2;
    public String OtherUser_UID, User_UID;
    public Float Personality, Photos, TrustWorthy, ResponseRate, PersonalityAvg, PhotosAvg, TrustAvg, ResponseAvg;
    private Button like,dislike;
    private ImageButton Picture;
    public boolean Filter;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private TextView textView, text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiping);
        like = (Button) findViewById(R.id.like);
        dislike = (Button) findViewById(R.id.dislike);
        Picture = (ImageButton) findViewById(R.id.MainPicture);
        Auth = FirebaseAuth.getInstance();
        final FirebaseUser User = Auth.getCurrentUser();
        UserPreference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int click = item.getItemId();

        if (click==R.id.filterButton)
        {
            setFilterButton();
        }

        if (click==R.id.refreshButton)
        {
            setRefreshButton();
        }

        if (click==R.id.matchesButton)
        {
            setMatchesButton();
        }
        if (click==R.id.aboutMeButton)
        {
            setAboutMe();
        }

        if (click==R.id.signOutButton)
        {
            setSignOutButton();
        }
        return true;
    }

    public void setFilterButton(){

        Intent intent = new Intent(SwipingActivity.this, FiltersActivity.class);
        startActivity(intent);

            }
     public void setSignOutButton(){
         FirebaseAuth.getInstance().signOut();
         Intent intent = new Intent(SwipingActivity.this, MainActivity.class);
         startActivity(intent);
     }

     public void setMatchesButton(){
         Intent intent = new Intent(SwipingActivity.this, MatchesActivity.class);
         startActivity(intent);
     }

    public void setAboutMe(){
        Intent intent = new Intent(SwipingActivity.this, AboutMe.class);
        startActivity(intent);
    }

    public void setRefreshButton(){

        OtherUserPreference();
    }

    public void UserPreference(){
        final FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Users").addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded (DataSnapshot dataSnapshot, String prevChildKey) {
               //textView = (TextView) findViewById(R.id.txtout);
                User_UID =User.getUid();
                like.setEnabled(false);
                dislike.setEnabled(false);
                if (dataSnapshot.getKey().equals(User.getUid())) {
                        UserSex = dataSnapshot.child("Gender").getValue(String.class);
                        UserLooking = dataSnapshot.child("Looking for Gender").getValue(String.class);
                        UserType = dataSnapshot.child("Relationship, ONS, Casual").getValue(String.class);
                        UserName = dataSnapshot.child("Name").getValue(String.class);
                        Personality = dataSnapshot.child("Filter").child("Personality").getValue(Float.class);
                        Photos = dataSnapshot.child("Filter").child("Looks like Photos").getValue(Float.class);
                        ResponseRate = dataSnapshot.child("Filter").child("Response Rate").getValue(Float.class);
                        TrustWorthy = dataSnapshot.child("Filter").child("Trustworthy").getValue(Float.class);
                        //textView.setText(UserSex);
                        OtherUserPreference();
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

    public void OtherUserPreference(){
        DatabaseReference databaseOther = FirebaseDatabase.getInstance().getReference();
        text2 = (TextView) findViewById(R.id.txt2);
        text2.setText("No Users");
        databaseOther.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String prevChildKey) {
                //OtherUser_UID = dataSnapshot.getKey();
                UserLike = dataSnapshot.child("Like").child(User_UID).getValue(String.class);
                UserDislike = dataSnapshot.child("Dislike").child(User_UID).getValue(String.class);
                Boolean PersonUsed;
                PersonUsed = dataSnapshot.child("Like").child(User_UID).exists() || dataSnapshot.child("Dislike").child(User_UID).exists();
                if (!PersonUsed && dataSnapshot.exists() && !(dataSnapshot.getKey().equalsIgnoreCase(User_UID))) {
                    if (dataSnapshot.child("Review").child("Review Total").getValue(Float.class) == null) {
                        if ((UserLooking.equalsIgnoreCase(dataSnapshot.child("Gender").getValue(String.class)) || UserLooking.equalsIgnoreCase("Any"))
                                && dataSnapshot.child("Looking for Gender").getValue(String.class).equalsIgnoreCase(UserSex)) {
                            String urlImage = dataSnapshot.child("Information").child("Picture").getValue(String.class);

                           Glide.with(SwipingActivity.this)
                                    .load(urlImage)
                                    .into(Picture);
                            like.setEnabled(true);
                            dislike.setEnabled(true);
                            OtherUserSex = dataSnapshot.child("Gender").getValue(String.class);
                            OtherUserLooking = dataSnapshot.child("Looking for Gender").getValue(String.class);
                            OtherUser_UID = dataSnapshot.getKey();
                            OtherUserType = dataSnapshot.child("Relationship, ONS, Casual").getValue(String.class);
                            Picture.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PictureButton(OtherUser_UID);
                                }

                            });
                            OtherUserName = dataSnapshot.child("Name").getValue(String.class);
                            text2.setText(OtherUserName);
                        }
                    } else if (dataSnapshot.child("Review").child("Review Total").getValue(Float.class) != null) {
                        PersonalityAvg = dataSnapshot.child("Review").child("Personality").getValue(Float.class);
                        TrustAvg = dataSnapshot.child("Review").child("Trustworthy").getValue(Float.class);
                        PhotosAvg = dataSnapshot.child("Review").child("Looks like Photos").getValue(Float.class);
                        ResponseAvg = dataSnapshot.child("Review").child("Response Rate").getValue(Float.class);
                        if (Personality <= PersonalityAvg && TrustWorthy <= TrustAvg && Photos <= PhotosAvg && ResponseRate <= ResponseAvg) {
                            if (UserLooking.equalsIgnoreCase(dataSnapshot.child("Gender").getValue(String.class))
                                    && dataSnapshot.child("Looking for Gender").getValue(String.class).equalsIgnoreCase(UserSex)) {
                                String urlImage = dataSnapshot.child("Information").child("Picture").getValue(String.class);
                                Glide.with(SwipingActivity.this)
                                        .load(urlImage)
                                        .into(Picture);
                                like.setEnabled(true);
                                dislike.setEnabled(true);
                                OtherUser_UID = dataSnapshot.getKey();
                                OtherUserSex = dataSnapshot.child("Gender").getValue(String.class);
                                OtherUserLooking = dataSnapshot.child("Looking for Gender").getValue(String.class);
                                Picture.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PictureButton(OtherUser_UID);
                                    }

                                });
                                OtherUserType = dataSnapshot.child("Relationship, ONS, Casual").getValue(String.class);
                                OtherUserName = dataSnapshot.child("Name").getValue(String.class);
                                text2.setText(OtherUserName);
                            }
                        }
                    }
                }


                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference OtherUserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUser_UID).child("Like").child(User_UID);
                        OtherUserDataBase.setValue(User_UID);

                        DatabaseReference UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Like").child(OtherUser_UID);
                        UserDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    DatabaseReference OtherUserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUser_UID).child("Match").child(User_UID);
                                    OtherUserDataBase.setValue(User_UID);
                                    OtherUserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUser_UID).child("Chat").child(User_UID);
                                    OtherUserDataBase.setValue("Messages");
                                    DatabaseReference UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Match").child(OtherUser_UID);
                                    UserDataBase.setValue(OtherUser_UID);
                                    UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Chat").child((OtherUser_UID));
                                    UserDataBase.setValue("Messages");
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        text2.setText("No Users, Please Refresh");
                        like.setEnabled(false);
                        dislike.setEnabled(false);
                        OtherUserPreference();
                    }
                });

                dislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(OtherUser_UID).child("Dislike").child((User_UID));
                        UserDataBase.setValue(User_UID);
                        text2.setText("No Users");


                        like.setEnabled(false);
                        dislike.setEnabled(false);
                        OtherUserPreference();
                    }
                });


            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    public void PictureButton(String ID)
    {
                Intent intent = new Intent(SwipingActivity.this, AboutThem.class);
                intent.putExtra("AboutThemUser", ID);
                startActivity(intent);
    }


}
