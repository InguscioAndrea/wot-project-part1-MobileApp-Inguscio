package com.example.tabbedapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.tabbedapplication.databinding.ActivityMainBinding;
import com.example.tabbedapplication.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Hospital_informationActivity extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    String path_child = "jsbQRkm7EoNjFsnA4mtAgKlnCmU2";
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);

    TextView textName; //si dichiarano tuti gli elementi presenti nella pagina xml
    TextView textSurname;
    TextView textTelephone;
    TextView textEmail;
    TextView textClinicInformation;
    Button button_back;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.hospital_information); //si specifica la pagina xml che si vuole stampare a video

        textName = (TextView) findViewById(R.id.edtText_newName);
        textSurname = (TextView) findViewById(R.id.edtText_newSurname);
        textTelephone = (TextView) findViewById(R.id.edtText_Telephone);
        textEmail = (TextView) findViewById(R.id.edtText_Email);
        textClinicInformation = (TextView) findViewById(R.id.textClinicInformation);
        button_back = (Button) findViewById(R.id.btn_back_20);

        //SI SETTANO LE EDITTEXT NON EDITABILI
        textName.setEnabled(false);
        textSurname.setEnabled(false);
        textTelephone.setEnabled(false);
        textEmail.setEnabled(false);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);


        DatabaseReference myRefName = database.getReference("Users").child("Doctor").child(path_child).child("Personal_data").child("nome");
        myRefName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textName.setText(result);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefCognome = database.getReference("Users").child("Doctor").child(path_child).child("Personal_data").child("cognome");
        myRefCognome.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textSurname.setText(result);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefTelefono = database.getReference("Users").child("Doctor").child(path_child).child("Personal_data").child("telefono");
        myRefTelefono.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textTelephone.setText(result);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefEmail = database.getReference("Users").child("Doctor").child(path_child).child("Personal_data").child("email");
        myRefEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textEmail.setText(result);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        String descrizione_clinica = "<b>" + "Clinica Sacro Cuore di Gesù" + "</b>"+ "<br>" + "via G. Ungaretti 12, Milano (20019)\ntel: +02 91387388 / +02 87356774\ndir.sanitaria@clinicasacrocuore.it";
        textClinicInformation.setText(Html.fromHtml(descrizione_clinica));

        button_back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), PatientActivity.class));
            }
        });

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), PatientActivity.class));
    }

}