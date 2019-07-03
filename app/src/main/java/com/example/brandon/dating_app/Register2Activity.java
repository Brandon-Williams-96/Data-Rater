package com.example.brandon.dating_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register2Activity extends AppCompatActivity {

    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Button completeButton;
    private EditText Name;
    private float personality, responseRate, photos, trustworthy, reviewTotal;
    private RadioGroup radioGroupIam, radioGroupLooking, radioGroupLookingType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);


        Auth = FirebaseAuth.getInstance();
        final FirebaseUser User = Auth.getCurrentUser();


        completeButton = (Button) findViewById(R.id.Complete_button);
        Name = (EditText) findViewById(R.id.Name);
        radioGroupIam = (RadioGroup) findViewById(R.id.RadioGroupIam);
        radioGroupLooking =(RadioGroup) findViewById(R.id.RadioLookingFor);
        radioGroupLookingType =(RadioGroup) findViewById(R.id.RadioType);
        personality=0;
        responseRate=0;
        photos=0;
        trustworthy=0;
        reviewTotal=0;



        completeButton=(Button)findViewById(R.id.Complete_button);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= Name.getText().toString();

                int ID = radioGroupIam.getCheckedRadioButtonId();
                RadioButton radioGroupIam = (RadioButton) findViewById(ID);
                int ID2 = radioGroupLooking.getCheckedRadioButtonId();
                RadioButton radioGroupLooking = (RadioButton) findViewById(ID2);
                int ID3 = radioGroupLookingType.getCheckedRadioButtonId();
                RadioButton radioGroupLookingType = (RadioButton) findViewById(ID3);
                        String radioIam = radioGroupIam.getText().toString();
                        String radioLooking = radioGroupLooking.getText().toString();
                        String radioType =radioGroupLookingType.getText().toString();
                String User_UID =Auth.getCurrentUser().getUid();
                DatabaseReference UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Name");
                UserDataBase.setValue(name);
                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Gender");
                UserDataBase.setValue(radioIam);
                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Looking for Gender");
                UserDataBase.setValue(radioLooking);
                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Relationship, ONS, Casual");
                UserDataBase.setValue(radioType);
                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Filter").child("Personality");
                UserDataBase.setValue(personality);
                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Filter").child("Response Rate");
                UserDataBase.setValue(responseRate);
                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Filter").child("Looks like Photos");
                UserDataBase.setValue(photos);
                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Filter").child("Trustworthy");
                UserDataBase.setValue(trustworthy);

                Intent intent = new Intent(Register2Activity.this, AboutMe.class);
                startActivity(intent);
            }
        });

    }


}
