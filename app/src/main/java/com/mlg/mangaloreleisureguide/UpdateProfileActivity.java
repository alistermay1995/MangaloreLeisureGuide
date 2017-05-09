package com.mlg.mangaloreleisureguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private EditText pfname;
    private EditText plname;
    private EditText pstreet;
    private Button mfirebasebtn;


    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        auth = FirebaseAuth.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid());
        pfname = (EditText) findViewById(R.id.fname);
        plname = (EditText) findViewById(R.id.lname);
        pstreet = (EditText) findViewById(R.id.street);
        mfirebasebtn = (Button) findViewById(R.id.firebasebtn);

        mfirebasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = pfname.getText().toString().trim();
                String lname = plname.getText().toString().trim();
                String street = pstreet.getText().toString().trim();

                //First Name not empty
                if (TextUtils.isEmpty(fname)) {
                    pfname.setError(REQUIRED);
                    return;
                }

                // Body is required
                if (TextUtils.isEmpty(lname)) {
                    plname.setError(REQUIRED);
                    return;
                }

                if (TextUtils.isEmpty(street)) {
                    {
                        pstreet.setError(REQUIRED);
                        return;
                    }
                }

                Toast.makeText(getApplicationContext(), "Posting...", Toast.LENGTH_SHORT).show();
                HashMap<String, String> datamap = new HashMap<String, String>();
                datamap.put("First Name", fname);
                datamap.put("Last Name", lname);
                datamap.put("Street", street);
                mDatabase.setValue(datamap);
            }
        });

    }

}