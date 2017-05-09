package com.mlg.mangaloreleisureguide;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


/**
 * Created by hp on 11-04-2017.
 */

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private ImageView profile;
    private StorageReference mStorageRef;
    private ImageButton mImageButton;
    CharSequence options[] = new CharSequence[] {"Camera", "Gallery"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        profile = (ImageView) rootview.findViewById(R.id.add_friend);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mImageButton = (ImageButton) rootview.findViewById(R.id.user_profile_photo);

        //Open dialog when profile picture clicked

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileFragment.this.getActivity());
                    builder.setTitle("Select");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // the user clicked on colors[which]
                            if (options[which] == "Camera") {
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);//zero can be replaced with any action code
                            } else if (options[which] == "Gallery") {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
                            }
                        }
                    });
                    builder.show();

            }
        });



        //SignOut
        auth = FirebaseAuth.getInstance();

        Button button = (Button) rootview.findViewById(R.id.log_out);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(ProfileFragment.this.getActivity(), LoginActivity.class);
                startActivity(myIntent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileFragment.this.getActivity(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        return rootview;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        mImageButton = (ImageButton) getActivity().findViewById(R.id.user_profile_photo);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    mImageButton.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();

                    StorageReference mRef = mStorageRef.child(selectedImage.getPath());

                    mRef.putFile(selectedImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                    if(downloadUrl==null){
                                        Toast.makeText(getActivity(), "Download url null",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(getActivity(), "Download url valid",
                                                Toast.LENGTH_LONG).show();
                                        mImageButton.setImageURI(null);
                                        mImageButton.setImageURI(downloadUrl);
                                        mImageButton.invalidate();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                    Toast.makeText(getActivity(), "Download failed",
                                            Toast.LENGTH_LONG).show();
                                }
                            });


                }
                break;
        }
    }

}
