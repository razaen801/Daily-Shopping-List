package com.example.dailyshoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyshoppinglist.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private FloatingActionButton fab_btn;

    private DatabaseReference mDatabase;

   // private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar= findViewById(R.id.home_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daily Shopping List");

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Shopping List").child(uId);
      //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
       // DatabaseReference myRef = database.getReference("database_root_name").child("Shopping List");

       // myRef.setValue("Hello, World!");

        fab_btn= findViewById(R.id.fab);
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog();
            }
        });
    }

    private void customDialog() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View myView = inflater.inflate(R.layout.input_data,null);


        final AlertDialog dialog = myDialog.create();

        dialog.setView(myView);

        final EditText type = myView.findViewById(R.id.edt_type);
        final EditText amount = myView.findViewById(R.id.edt_amount);
        final EditText note = myView.findViewById(R.id.edt_note);
        Button save = myView.findViewById(R.id.btn_save);




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mType= type.getText().toString().trim();
                String mAmount= amount.getText().toString().trim();
                String mNote= note.getText().toString().trim();

                int amountt = Integer.parseInt(mAmount);

                if (TextUtils.isEmpty(mType)){
                    type.setError("Field Reqiured..");
                    return;
                }
                if (TextUtils.isEmpty(mAmount)){
                    amount.setError("Field Reqiured..");
                    return;
                }
                if (TextUtils.isEmpty(mNote)){
                    note.setError("Field Reqiured..");
                    return;
                }


                String id = mDatabase.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(mType,amountt,mNote,date,id);

                mDatabase.child(id).setValue(data);

                Toast.makeText(getApplicationContext(),"Added Successfully",Toast.LENGTH_SHORT).show();


                dialog.dismiss();
            }
        });


        dialog.show();
    }
}
