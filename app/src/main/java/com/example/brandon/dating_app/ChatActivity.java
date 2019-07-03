package com.example.brandon.dating_app;

import android.content.Intent;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    public String MatchID, User_UID, Message, Name, date, time, MatchName, user, newMessage, URL;
    public long MessageNum;
    public boolean finished=false;
    public int Count, Position, Fix, Fix2=0;
    public EditText UserMessage;
    private FirebaseAuth Auth;
    public ImageButton send, userImage;
    private FirebaseAuth.AuthStateListener authStateListener;

    ArrayList<String> ChatMessage = new ArrayList<String>() {
    };

    ArrayList<String> ChatSender = new ArrayList<String>() {
    };

    ArrayList<String> ChatTime = new ArrayList<String>() {
    };

    ArrayList<String> ChatDate = new ArrayList<String>() {
    };

    ArrayList<String> ChatUID = new ArrayList<String>() {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        MatchID = getIntent().getStringExtra("MatchID");
        Auth = FirebaseAuth.getInstance();
        final FirebaseUser User = Auth.getCurrentUser();
        User_UID = User.getUid();
        send = (ImageButton) findViewById(R.id.Send);
        UserMessage =(EditText) findViewById(R.id.Message);
        MatchName = getIntent().getStringExtra("MatchName");
        userImage = (ImageButton) findViewById(R.id.UserPicture);
        URL = getIntent().getStringExtra("URL");

        Glide.with(ChatActivity.this)
                .load(URL)
                .into(userImage);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAboutThemButton();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        this.setTitle(MatchName);

        final DatabaseReference UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Chat");
        UserData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageNum = dataSnapshot.getChildrenCount();
                Count = (int) MessageNum;
                if (MessageNum >= 1 && Fix ==0) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        ChatMessage.add(child.child("Message").getValue(String.class));
                        ChatSender.add(child.child("Name").getValue(String.class));
                        ChatDate.add(child.child("Date").getValue(String.class));
                        ChatTime.add(child.child("Time").getValue(String.class));
                        ChatUID.add(user = child.child("UserID").getValue(String.class));

                    }
                    Fix=1;
                }
                ListView listView = (ListView) findViewById(R.id.messages_view);
                final ChatActivity.CustomAdapter customAdapter = new CustomAdapter();
                listView.setAdapter(customAdapter);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                MessageNum = dataSnapshot.getChildrenCount();
                Count = (int) MessageNum;
                if (Count==ChatMessage.size()+1 || Count==0 && finished)
                {
                    newMessage = dataSnapshot.child(Integer.toString(Count)).child("Message").getValue(String.class);
                    ChatSender.add(dataSnapshot.child(Integer.toString(Count)).child("Name").getValue(String.class));
                    ChatDate.add(dataSnapshot.child(Integer.toString(Count)).child("Date").getValue(String.class));
                    ChatTime.add(dataSnapshot.child(Integer.toString(Count)).child("Time").getValue(String.class));
                    ChatUID.add(dataSnapshot.child(Integer.toString(Count)).child("UserID").getValue(String.class));
                    ChatMessage.add(newMessage);

                    finished=true;
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference UserName = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Name");
        UserName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Name = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finished=false;
                Count=Count+1;
                DatabaseReference UserName = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Name");
                UserName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Name = dataSnapshot.getValue(String.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                Message = UserMessage.getText().toString();
                Date date1 = new Date();
                SimpleDateFormat day =
                        new SimpleDateFormat("dd.MM.YY");
                SimpleDateFormat Time =
                        new SimpleDateFormat("hh:mm:ss");

                date = day.format(date1).toString();
                time = Time.format(date1).toString();

                ChatSender.add(Name);
                ChatDate.add(date);
                ChatTime.add(time);
                ChatUID.add(User_UID);
                ChatMessage.add(Message);


                    DatabaseReference MessageDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Chat").child(MatchID).child(Integer.toString(Count)).child("Message");
                    MessageDataBase.setValue(Message);
                    MessageDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Chat").child(MatchID).child(Integer.toString(Count)).child("Name");
                    MessageDataBase.setValue(Name);
                    MessageDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Chat").child(MatchID).child(Integer.toString(Count)).child("Time");
                    MessageDataBase.setValue(time);
                    MessageDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Chat").child(MatchID).child(Integer.toString(Count)).child("Date");
                    MessageDataBase.setValue(date);
                    MessageDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Chat").child(MatchID).child(Integer.toString(Count)).child("UserID");
                    MessageDataBase.setValue(User_UID);

                    DatabaseReference OtherMessageDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(MatchID).child("Chat").child(User_UID).child(Integer.toString(Count)).child("Message");
                    OtherMessageDataBase.setValue(Message);
                    OtherMessageDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(MatchID).child("Chat").child(User_UID).child(Integer.toString(Count)).child("Name");
                    OtherMessageDataBase.setValue(Name);
                    OtherMessageDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(MatchID).child("Chat").child(User_UID).child(Integer.toString(Count)).child("Time");
                    OtherMessageDataBase.setValue(time);
                    OtherMessageDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(MatchID).child("Chat").child(User_UID).child(Integer.toString(Count)).child("Date");
                    OtherMessageDataBase.setValue(date);
                    MessageDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(MatchID).child("Chat").child(User_UID).child(Integer.toString(Count)).child("UserID");
                    MessageDataBase.setValue(User_UID);

                    UpdateList();
                    UserMessage.setText(null);

            }
        });


    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ChatMessage.size();
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
            MatchID = getIntent().getStringExtra("MatchID");
            final FirebaseUser User = Auth.getCurrentUser();
            User_UID = User.getUid();
            user=ChatUID.get(position);
            if (user!=null) {
                if (user.equalsIgnoreCase(User_UID)) {
                    convertView = getLayoutInflater().inflate(R.layout.chat_item_me, null);
                    TextView TextName = (TextView) convertView.findViewById(R.id.UserName);
                    TextView TextMessage = (TextView) convertView.findViewById(R.id.UserMessage);
                    TextView TextDate = (TextView) convertView.findViewById(R.id.UserDate);
                    TextView TextTime = (TextView) convertView.findViewById(R.id.UserTime);

                    TextName.setText(ChatSender.get(position));
                    TextMessage.setText(ChatMessage.get(position));
                    TextDate.setText(ChatDate.get(position));
                    TextTime.setText(ChatTime.get(position));
                }
                else if (user.equalsIgnoreCase(MatchID)) {
                    convertView = getLayoutInflater().inflate(R.layout.chat_item, null);
                    TextView TextName = (TextView) convertView.findViewById(R.id.UserName);
                    TextView TextMessage = (TextView) convertView.findViewById(R.id.UserMessage);
                    TextView TextDate = (TextView) convertView.findViewById(R.id.UserDate);
                    TextView TextTime = (TextView) convertView.findViewById(R.id.UserTime);

                    TextName.setText(ChatSender.get(position));
                    TextMessage.setText(ChatMessage.get(position));
                    TextDate.setText(ChatDate.get(position));
                    TextTime.setText(ChatTime.get(position));
                }
            }

            return convertView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int click = item.getItemId();

        if (click == R.id.Rater) {
            setFilterButton();
        }
        return true;
    }

    public void setFilterButton(){
        MatchID = getIntent().getStringExtra("MatchID");
        final FirebaseUser User = Auth.getCurrentUser();
        User_UID= User.getUid();
        Intent intent = new Intent(ChatActivity.this, RaterActivity.class);
        intent.putExtra("UID", MatchID);
        intent.putExtra("Rater", User_UID);
        startActivity(intent);
        finish();
    }

    public void setAboutThemButton(){
        MatchID = getIntent().getStringExtra("MatchID");
        Intent intent = new Intent(ChatActivity.this, AboutThem.class);
        intent.putExtra("AboutThemUser", MatchID);
        startActivity(intent);
        finish();

    }

    public void UpdateList(){
        ListView listView = (ListView) findViewById(R.id.messages_view);
        ChatActivity.CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
    }
}
