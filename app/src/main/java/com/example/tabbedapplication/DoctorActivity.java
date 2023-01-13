package com.example.tabbedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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


public class DoctorActivity extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    String path_child;
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);

    String doctor_name;

    TextView patient_Title;
    Button btn_ViewData;
    Button btn_UploadFile;
    Button btn_logout;
    Button btn_data_trend_analysis;
    Button btn_List_of_user;


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.doctor_page); //si specifica la pagina xml che si vuole stampare a video

        patient_Title = (TextView)findViewById(R.id.id_previous_data_Doctor);
        btn_ViewData = (Button) findViewById(R.id.btn_ViewPreviousData_Doctor);
        btn_UploadFile = (Button) findViewById(R.id.btn_UploadFile_Doctor);
        btn_logout = (Button) findViewById(R.id.btn_logout_Doctor);
        btn_data_trend_analysis = (Button) findViewById(R.id.btn_AnalyticsData);
        btn_List_of_user = (Button) findViewById(R.id.btn_ViewUsers);


        path_child = connection.getUser().getUid(); //RECUPERA L'ID DELLUTENTE CHE SI LOGGA

        DatabaseReference myRef = database.getReference("Users").child("Doctor").child(path_child).child("Personal_data").child("cognome");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                doctor_name = String.valueOf(dataSnapshot.getValue(String.class));
                patient_Title.setText("Welcome back Dr. " + doctor_name + "! \n Choose the operation you want to carry out");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        btn_logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.loading);
                Toast.makeText(DoctorActivity.this, "LOG-OUT IN PROGRESS... PLEASE WAIT", Toast.LENGTH_SHORT).show();

                //FUNZIONE DI "LOADING" CHE STAMPA A VIDEO UNA PROGRESS BAR CIRCOLARE DI CARICAMENTO
                CountDownTimer countDownTimer = new CountDownTimer(1500, 1000){

                    @Override
                    public void onTick(long millisUntilFinished) {
                        System.out.println("Wait " + millisUntilFinished/1000);
                    }

                    @Override
                    public void onFinish() {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }.start();

                /////////FINE DELLE FUNZIONE DELLA PORZIONE DI CODICE DI "LOADING"//////////////////

            }
        });


        btn_ViewData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                /////////////////////////////////////////
                Intent new_page = new Intent(getApplicationContext(), ChoosePatientActivity.class);
                new_page.putExtra("parametro", 0);
                new_page.putExtra("doctor_id", path_child);
                startActivity(new_page);
                /////////////////////////////////////////

            }
        });

        btn_data_trend_analysis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                /////////////////////////////////////////
                Intent new_page = new Intent(getApplicationContext(), ChoosePatientActivity.class);
                new_page.putExtra("parametro", 2);
                new_page.putExtra("doctor_id", path_child);
                startActivity(new_page);
                /////////////////////////////////////////

            }
        });

        btn_UploadFile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                /////////////////////////////////////////
                Intent new_page = new Intent(getApplicationContext(), ChoosePatientActivity.class);
                new_page.putExtra("parametro", 1);
                new_page.putExtra("doctor_id", path_child);
                startActivity(new_page);
                /////////////////////////////////////////

            }
        });

        btn_List_of_user.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                /////////////////////////////////////////
                Intent new_page = new Intent(getApplicationContext(), ChoosePatientActivity.class);
                new_page.putExtra("parametro", 3);
                new_page.putExtra("doctor_id", path_child);
                startActivity(new_page);
                /////////////////////////////////////////

            }
        });


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        setContentView(R.layout.loading);
        Toast.makeText(DoctorActivity.this, "LOG-OUT IN PROGRESS... PLEASE WAIT", Toast.LENGTH_SHORT).show();

        //FUNZIONE DI "LOADING" CHE STAMPA A VIDEO UNA PROGRESS BAR CIRCOLARE DI CARICAMENTO
        CountDownTimer countDownTimer = new CountDownTimer(1500, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("Wait " + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }.start();

        /////////FINE DELLE FUNZIONE DELLA PORZIONE DI CODICE DI "LOADING"//////////////////
    }
}