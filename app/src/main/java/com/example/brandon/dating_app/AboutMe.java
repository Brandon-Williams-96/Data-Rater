package com.example.brandon.dating_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AboutMe extends AppCompatActivity {

    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Uri filePath;
    public int ImageFix;
    public String User_UID;
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageReference mStorageRef;
    private EditText AboutMe;
    private ImageButton Picture;
    private Button Submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        Picture = (ImageButton) findViewById(R.id.MyPicture);
        AboutMe = (EditText) findViewById(R.id.AboutMe);
        Submit =(Button) findViewById(R.id.SubmitButton);
        Auth = FirebaseAuth.getInstance();
        final FirebaseUser User = Auth.getCurrentUser();
        User_UID = User.getUid();

        final DatabaseReference UserData = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID);
        UserData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("About Me").getValue(String.class)!=null) {
                    AboutMe.setText(dataSnapshot.child("About Me").getValue(String.class));
                    if (dataSnapshot.child("Picture").getValue(String.class)!=null) {
                        String urlImage = dataSnapshot.child("Picture").getValue(String.class);

                        Glide.with(AboutMe.this)
                                .load(urlImage)
                                .into(Picture);
                    }
                    else
                    {
                        final StorageReference UserRef = storageRef.child("images/User.jpg");
                        storageRef.child("images/User.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Information").child("Picture");
                                UserDataBase.setValue(uri.toString());
                                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Review").child("Picture");
                                UserDataBase.setValue(uri.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
                        final StorageReference UserRef2 = storageRef.child("images/UserIcon.jpg");
                        storageRef.child("images/UserIcon.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Information").child("PictureIcon");
                                UserDataBase.setValue(uri.toString());
                                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Review").child("PictureIcon");
                                UserDataBase.setValue(uri.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
                    }

                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});

                Picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                        ImageFix=1;



                    }

                });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String User_UID =Auth.getCurrentUser().getUid();
                final StorageReference UserRef = storageRef.child("images/" +User_UID+ "/User.jpg");
                final StorageReference UserRef2 = storageRef.child("images/" +User_UID+ "/UserIcon.jpg");
                String aboutMe= AboutMe.getText().toString();
                DatabaseReference UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Information").child("About Me");
                UserDataBase.setValue(aboutMe);
                if (ImageFix==1) {
                    Picture.setDrawingCacheEnabled(true);
                    Picture.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) Picture.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);


                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = UserRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef.child("images/" + User_UID + "/User.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Information").child("Picture");
                                    UserDataBase.setValue(uri.toString());
                                    UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Review").child("Picture");
                                    UserDataBase.setValue(uri.toString());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                }
                            });

                        }

                    });
                    Picture.setDrawingCacheEnabled(true);
                    Picture.buildDrawingCache();
                    Bitmap bitmap2 = ((BitmapDrawable) Picture.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                    bitmap2 = Bitmap.createScaledBitmap(bitmap2, 50, 50, true);
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
                byte[] data2 = baos2.toByteArray();
                UploadTask uploadTask2 = UserRef2.putBytes(data2);
                uploadTask2.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.child("images/" + User_UID + "/UserIcon.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Information").child("PictureIcon");
                                UserDataBase.setValue(uri.toString());
                                UserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(User_UID).child("Review").child("PictureIcon");
                                UserDataBase.setValue(uri.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });

                    }

                });
            }

                Intent intent = new Intent(AboutMe.this, SwipingActivity.class);
                startActivity(intent);
                finish();

            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = Bitmap.createScaledBitmap(bitmap,500,500,true);
                Picture.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
