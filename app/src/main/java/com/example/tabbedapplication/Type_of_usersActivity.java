package com.example.tabbedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.util.ArrayList;


public class Type_of_usersActivity extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    String path_child;
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);

    TextView title;
    Spinner spinner;
    Button continue_button;
    Button button_back;

    ArrayList<String> array_lista_utenti = new ArrayList<String>();

    String value_selected; //VARIABILE CHE CONTIENE IL VALORE SELEZIONATO DALLO SPINNER
    int bool_view; //VARIABILE CHE SE "1" RIGUARDA IL DOTTORE, SE "0" IL PAZIENTE
    String last_ID_user_scelto;


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.type_of_users); //si specifica la pagina xml che si vuole stampare a video

        title = (TextView)findViewById(R.id.id_title_type_of_user);
        spinner = (Spinner) findViewById(R.id.id_spinner_type_of_user);
        continue_button = (Button)findViewById(R.id.btn_continue);
        button_back = (Button)findViewById(R.id.button_back_6);

        button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdministratorActivity.class));            }
        });

        String[] arraySpinner = new String[] {"Doctor", "Patient"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        continue_button.setOnClickListener(view -> {

            value_selected = spinner.getSelectedItem().toString();

            if (value_selected.equals("Doctor")){
                bool_view = 1;

                DatabaseReference myRef = database.getReference("Users").child("Admin").child("List_of_doctor").child("Last ID");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        last_ID_user_scelto = String.valueOf(dataSnapshot.getValue(String.class));
                        int last_ID = Integer.parseInt(last_ID_user_scelto);

                        for(int i=last_ID; i>=1; i--) {
                            int x = i;
                            DatabaseReference myRef6 = database.getReference("Users").child("Admin").child("List_of_doctor").child("ID_"+i).child("cognome");
                            myRef6.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String result = String.valueOf(dataSnapshot.getValue(String.class));
                                    array_lista_utenti.add("ID_" +x+ " " +result);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } else if (value_selected.equals("Patient")){
                bool_view = 0;

                DatabaseReference myRef = database.getReference("Users").child("Admin").child("List_of_patient").child("Last ID");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        last_ID_user_scelto = String.valueOf(dataSnapshot.getValue(String.class));
                        int last_ID = Integer.parseInt(last_ID_user_scelto);

                        for(int i=last_ID; i>=1; i--) {
                            int x = i;
                            DatabaseReference myRef6 = database.getReference("Users").child("Admin").child("List_of_patient").child("ID_"+i).child("cognome");
                            myRef6.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String result = String.valueOf(dataSnapshot.getValue(String.class));
                                    array_lista_utenti.add("ID_" +x+ " " +result);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            setContentView(R.layout.loading);

            //FUNZIONE DI "LOADING" CHE STAMPA A VIDEO UNA PROGRESS BAR CIRCOLARE DI CARICAMENTO
            CountDownTimer countDownTimer = new CountDownTimer(300, 1000){

                @Override
                public void onTick(long millisUntilFinished) {
                    System.out.println("Wait " + millisUntilFinished/1000);
                }

                @Override
                public void onFinish() {
                    // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                    /////////////////////////////////////////
                    Intent new_page = new Intent(getApplicationContext(), List_of_usersActivity.class);
                    new_page.putExtra("array_lista_utenti", array_lista_utenti);
                    new_page.putExtra("last_id_user_scelto", last_ID_user_scelto);
                    new_page.putExtra("bool_view", bool_view);
                    /////////////////////////////////////////
                    startActivity(new_page);
                }
            }.start();

            /////////FINE DELLE FUNZIONE DELLA PORZIONE DI CODICE DI "LOADING"//////////////////

        });


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AdministratorActivity.class));

    }

}