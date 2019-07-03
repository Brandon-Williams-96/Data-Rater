package com.example.brandon.dating_app;

import android.content.Intent;
import android.media.Image;
import android.service.autofill.FieldClassification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes;


public class MatchesActivity extends AppCompatActivity {

    public String MatchID, UserMessage, MatchChat;
    public long Matches, NumMessages;
    public int NumMatch, Count=1, messageCount, Fix;

    ArrayList<String> MatchName = new ArrayList<String>() {
    };

    ArrayList<String> MatchImage = new ArrayList<String>() {
    };

    ArrayList<String> MatchImageIcon = new ArrayList<String>() {
    };

    ArrayList<String> MatchMessage = new ArrayList<String>() {
    };

    ArrayList<String> Match = new ArrayList<String>() {
    };

    ArrayList<String> MatchCheck = new ArrayList<String>() {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        Matches();

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return MatchImage.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.list_item, null);
            ImageView UserImage = (ImageView) convertView.findViewById(R.id.UserPicture);
            TextView TextName = (TextView) convertView.findViewById(R.id.UserName);
            TextView TextMessage = (TextView) convertView.findViewById(R.id.UserMessage);


            Glide.with(MatchesActivity.this)
                    .load(MatchImage.get(position))
                    .into(UserImage);
            TextName.setText(MatchName.get(position));
            TextMessage.setText(MatchMessage.get(position));

            if (MatchImage.size() == 0) {
                TextMessage.setText("No Matches Keep Swiping");
            }

            return convertView;
        }


    }

    public void Matches() {
        final FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        final String UserUID = User.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(UserUID).child("Chat");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Matches = dataSnapshot.getChildrenCount();
                for (DataSnapshot child: dataSnapshot.getChildren()) {

                    MatchID = child.getKey();
                    NumMatch = (int) Matches;
                    if (MatchID !=null & !MatchCheck.contains(MatchID)) {
                        MatchInformation(MatchID, NumMatch);
                    }
                    else if (MatchID == null || MatchID.equalsIgnoreCase(""))
                    {
                        Toast.makeText(MatchesActivity.this, "No Matches, keep swiping", Toast.LENGTH_LONG).show();
                    }
                    NumMessages=child.getChildrenCount();
                    messageCount = (int) NumMessages;
                    MatchCheck.add(MatchID);
                }

            }





            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }

    public void MatchInformation(final String MatchID , final Integer NumMatch) {
        final FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        final String UserUID = User.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("Users").child(MatchID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MatchName.add(dataSnapshot.child("Name").getValue(String.class));
                MatchImage.add(dataSnapshot.child("Information").child("Picture").getValue(String.class));
                if (dataSnapshot.child("Information").child("PictureIcon").exists()) {
                    MatchImageIcon.add(dataSnapshot.child("Information").child("PictureIcon").getValue(String.class));
                    Fix=1;
                }
                Match.add(MatchID);
                if (!dataSnapshot.child("Chat").child(UserUID).child(Integer.toString(messageCount)).child("Message").exists()){
                    MatchMessage.add("Send a message.");
                }
                else {
                    MatchMessage.add(dataSnapshot.child("Chat").child(UserUID).child(Integer.toString(messageCount)).child("Message").getValue(String.class));
                }

                ListView listView = (ListView) findViewById(R.id.list);
                CustomAdapter customAdapter = new CustomAdapter();
                listView.setAdapter(customAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MatchChat = Match.get(position);
                        Intent intent = new Intent(MatchesActivity.this, ChatActivity.class);
                        intent.putExtra("MatchName", MatchName.get(position));
                        intent.putExtra("MatchID", MatchChat);
                        if (Fix==1) {
                            intent.putExtra("URL", MatchImageIcon.get(position));
                        }
                        else {
                            intent.putExtra("URL", MatchImage.get(position));
                        }
                        startActivity(intent);

                    }
                });

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
