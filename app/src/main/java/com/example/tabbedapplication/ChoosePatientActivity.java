package com.example.tabbedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.ArrayList;


public class ChoosePatientActivity extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    String path_child;
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);

    TextView doctor_Title;
    Spinner spinner;
    Button continue_button;
    Button button_back;

    ArrayList<String> array_spinner = new ArrayList<String>();
    int last_id;
    String spinner_selection;
    String value_selected; //VARIABILE CHE CONTIENE IL VALORE SELEZIONATO DALLO SPINNER
    String temp;
    String id_patient_selected;
    int bool_view;
    String current_doctor;
    String sesso_paziente;

    String value_x_axis; //VALORE CHE SI STAMPA SULLE ASCISSE. CONTIENE L'ID OPERATION E LA CURRENT TIME IN CUI E' SVOLTA
    String value_y_axis; //VALORE CHE SI STAMPA SULLE ORDINATE. CONTIENE IL VALORE DI HAND_GRIP
    String value_y_axis_SMI; //VALORE CHE SI STAMPA SULLE ORDINATE. CONTIENE IL VALORE DI SMI
    String value_y_axis_Speed; //VALORE CHE SI STAMPA SULLE ORDINATE. CONTIENE IL VALORE DI SPEED


    ArrayList<String> Arr_X_axis = new ArrayList<String>();
    ArrayList<String> Arr_Y_axis = new ArrayList<String>();

    ArrayList<String> Arr_Y_axis_SMI = new ArrayList<String>();
    ArrayList<String> Arr_Y_axis_Speed = new ArrayList<String>();



    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // IL PARAMETRO bool_view SERVE PER CAPIRE QUALE VIEW PRECEDENTE CHIAMA QUESTA CLASSE, IN MODO DA
        // SFRUTTARLA DUE VOLTE E NON CREARNE UNA GEMELLA CHE SPRECHEREBBE SOLO MEMORIA
        // bool_view=0 ABILITA LA "PREVIOUS_DATA"
        // bool_view=1 ABILITA LA "PDF_HELPER"
        // bool_view=2 ABILITA LA "RIEPILOGO_GRAFICI"
        // bool_view=3 ABILITA LA "EDIT_PROFILE"
        bool_view = getIntent().getExtras().getInt("parametro");
        current_doctor = getIntent().getExtras().getString("doctor_id"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT


        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.choose_patient_data); //si specifica la pagina xml che si vuole stampare a video

        doctor_Title = (TextView)findViewById(R.id.id_title_DoctorPage);
        spinner = (Spinner) findViewById(R.id.id_spinner);
        continue_button = (Button)findViewById(R.id.btn_continue);
        button_back = (Button)findViewById(R.id.button_back_5);

        button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), DoctorActivity.class));

            }
        });

        DatabaseReference myRef = database.getReference("Users").child("Doctor").child("Patient").child("Last ID");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = String.valueOf(dataSnapshot.getValue(String.class));
                last_id = Integer.parseInt(result);

                for(int i=1; i<=last_id; i++) {
                    int x = i;
                    DatabaseReference myRef2 = database.getReference("Users").child("Doctor").child("Patient").child("ID_" + i).child("cognome");
                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String result = String.valueOf(dataSnapshot.getValue(String.class));
                            spinner_selection = "ID_" + x + ": " + result;
                            array_spinner.add(spinner_selection);

                            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, array_spinner);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);



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



        path_child = connection.getUser().getUid(); //RECUPERA L'ID DELLUTENTE CHE SI LOGGA

        continue_button.setOnClickListener(view -> {

            value_selected = spinner.getSelectedItem().toString();
            temp = value_selected.substring(0,4);

            DatabaseReference myRef3 = database.getReference("Users").child("Doctor").child("Patient").child(temp).child("id");
            myRef3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    id_patient_selected = String.valueOf(dataSnapshot.getValue(String.class));
                    System.out.println("ID E'... "+ id_patient_selected);
                    if (bool_view == 0) {

                        //  QUERY PER PRELEVARE IL SESSO DEL PAZIENTE E DIFFERENZIARE LE MISURAZIONI
                        DatabaseReference myRef_preleva_sesso = database.getReference("Users").child("Patient").child(id_patient_selected).child("Personal_data").child("gender");
                        myRef_preleva_sesso.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                sesso_paziente = String.valueOf(dataSnapshot.getValue(String.class));
                                // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                                /////////////////////////////////////////
                                Intent new_page = new Intent(getApplicationContext(), ChooseMeasureActivity.class);
                                new_page.putExtra("id_paziente", id_patient_selected);
                                System.out.println("L?ID PATIENT SELECTED E..."+ id_patient_selected);
                                new_page.putExtra("bool_view", 1);
                                new_page.putExtra("sesso_paziente", sesso_paziente);
                                /////////////////////////////////////////
                                startActivity(new_page);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });


                    } else if (bool_view == 1){
                        // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                        /////////////////////////////////////////
                        Intent new_page = new Intent(getApplicationContext(), PdfHelper.class);
                        new_page.putExtra("cifre_id", temp);
                        new_page.putExtra("id_paziente", id_patient_selected);
                        new_page.putExtra("id_dottore", current_doctor);
                        /////////////////////////////////////////
                        startActivity(new_page);

                    } else if (bool_view == 2){
                        DatabaseReference myRef4 = database.getReference("Users").child("Patient").child(id_patient_selected).child("Measure").child("Last Measure").child("id_op");
                        myRef4.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String temp_graph = String.valueOf(dataSnapshot.getValue(String.class));
                                int last_id_measure = Integer.parseInt(temp_graph);


                                for (int i = last_id_measure; i >= 1; i--) {
                                    int x = i;
                                    DatabaseReference myRef1 = database.getReference("Users").child("Patient").child(id_patient_selected).child("Measure").child("ID_" + i).child("current_time");
                                    myRef1.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String result = String.valueOf(dataSnapshot.getValue(String.class));
                                            value_x_axis = result;
                                            System.out.println("LA DATA VALE " + value_x_axis);
                                            Arr_X_axis.add(value_x_axis.substring(0, 8));

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }

                                    });
                                }

                                for (int i = last_id_measure; i >= 1; i--) {
                                    int x = i;
                                    DatabaseReference myRef2 = database.getReference("Users").child("Patient").child(id_patient_selected).child("Measure").child("ID_" + i).child("handgrip_max");
                                    myRef2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String result = String.valueOf(dataSnapshot.getValue(String.class));
                                            value_y_axis = result;

                                            Arr_Y_axis.add(value_y_axis);

                                            DatabaseReference myRef1 = database.getReference("Users").child("Patient").child(id_patient_selected).child("Measure").child("ID_" + x).child("muscle_mass");
                                            myRef1.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    String result = dataSnapshot.getValue(String.class);
                                                    value_y_axis_SMI = result;

                                                    Arr_Y_axis_SMI.add(value_y_axis_SMI);

                                                    DatabaseReference myRefSpeed = database.getReference("Users").child("Patient").child(id_patient_selected).child("Measure").child("ID_" + x).child("speed");
                                                    myRefSpeed.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String result = dataSnapshot.getValue(String.class);
                                                            value_y_axis_Speed = result;

                                                            Arr_Y_axis_Speed.add(value_y_axis_Speed);

                                                            //  QUERY PER PRELEVARE IL SESSO DEL PAZIENTE E DIFFERENZIARE LE MISURAZIONI
                                                            DatabaseReference myRef_preleva_sesso = database.getReference("Users").child("Patient").child(id_patient_selected).child("Personal_data").child("gender");
                                                            myRef_preleva_sesso.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    sesso_paziente = String.valueOf(dataSnapshot.getValue(String.class));
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {
                                                                }
                                                            });

                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                        }
                                                    });

                                                    if (x == 1){

                                                        setContentView(R.layout.loading);
                                                        Toast.makeText(ChoosePatientActivity.this, "LOADING ANALYTICAL DATA IN PROGRESS ...", Toast.LENGTH_SHORT).show();

                                                        //FUNZIONE DI "LOADING" CHE STAMPA A VIDEO UNA PROGRESS BAR CIRCOLARE DI CARICAMENTO
                                                        CountDownTimer countDownTimer = new CountDownTimer(1000, 1000){

                                                            @Override
                                                            public void onTick(long millisUntilFinished) {
                                                                System.out.println("Wait " + millisUntilFinished/1000);
                                                            }

                                                            @Override
                                                            public void onFinish() {
                                                                Intent new_page = new Intent(getApplicationContext(), RiepilogoGraficiActivity.class);
                                                                new_page.putExtra("id_paziente", id_patient_selected);
                                                                new_page.putExtra("X_axis", Arr_X_axis);
                                                                new_page.putExtra("Y_axis", Arr_Y_axis);
                                                                new_page.putExtra("Y_axis_SMI", Arr_Y_axis_SMI);
                                                                new_page.putExtra("Y_axis_Speed", Arr_Y_axis_Speed);
                                                                new_page.putExtra("sesso_paziente", sesso_paziente);
                                                                new_page.putExtra("bool_view", 2);
                                                                /////////////////////////////////////////
                                                                startActivity(new_page);
                                                            }
                                                        }.start();

                                                        /////////FINE DELLE FUNZIONE DELLA PORZIONE DI CODICE DI "LOADING"//////////////////


                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }

                                            });




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

                    } else if (bool_view == 3){
                        // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                        /////////////////////////////////////////
                        Intent new_page = new Intent(getApplicationContext(), EditProfileActivity.class);
                        new_page.putExtra("stringa_ID_scelta", id_patient_selected);
                        new_page.putExtra("bool_view", 0);
                        new_page.putExtra("ID_utente_scelto", temp);
                        new_page.putExtra("bool_editabile", 0);
                        /////////////////////////////////////////
                        startActivity(new_page);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });



        });




        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DoctorActivity.class));

    }

}