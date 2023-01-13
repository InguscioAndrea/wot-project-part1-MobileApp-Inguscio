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
import com.example.tabbedapplication.ui.Entities.MeasureData;
import com.example.tabbedapplication.ui.main.SectionsPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class PatientActivity extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    String path_child;
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);

    String altezza;
    String id_op;
    String handgrip_min;
    String handgrip_max;
    String handgrip_postexercise;
    String weight_measured;
    String fat_mass;
    String bone_mass;
    String muscle_mass;
    String step_count;
    String step_frequency;
    String speed;
    String current_time;
    String BMI;
    static String counter_misurazioni;
    String[] Arr_Misure;
    ArrayList<String> Dataset_SMI_ML = new ArrayList<String>();
    ArrayList<String> Dataset_Hgrip_ML = new ArrayList<String>();
    ArrayList<String> Dataset_Speed_ML = new ArrayList<String>();
    String patient_name;
    int temp;
    String dieta;
    String farmaci;
    String scheda_allenamento;


    String temp_graph;
    int last_id_measure;
    String value_x_axis; //VALORE CHE SI STAMPA SULLE ASCISSE. CONTIENE L'ID OPERATION E LA CURRENT TIME IN CUI E' SVOLTA
    String value_y_axis; //VALORE CHE SI STAMPA SULLE ORDINATE. CONTIENE IL VALORE DI HAND_GRIP
    String value_y_axis_SMI; //VALORE CHE SI STAMPA SULLE ORDINATE. CONTIENE IL VALORE DI SMI
    String value_y_axis_Speed; //VALORE CHE SI STAMPA SULLE ORDINATE. CONTIENE IL VALORE DI SPEED


    ArrayList<String> Arr_X_axis = new ArrayList<String>();
    ArrayList<String> Arr_Y_axis = new ArrayList<String>();

    ArrayList<String> Arr_Y_axis_SMI = new ArrayList<String>();
    ArrayList<String> Arr_Y_axis_Speed = new ArrayList<String>();




    TextView patient_Title;
    TextView txt_new;
    Button btn_StartMesure;
    Button btn_ViewData;
    Button btn_AnalyticsData;
    Button btn_logout;
    Button btn_medical_prescriptions;
    Button btn_suggestions;
    TextView icona_ospedale;
    TextView contact_us;

    String sesso_paziente;





    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.patient_page); //si specifica la pagina xml che si vuole stampare a video

        patient_Title = (TextView)findViewById(R.id.id_previous_data);
        btn_StartMesure = (Button)findViewById(R.id.btn_start_measuring);
        btn_ViewData = (Button) findViewById(R.id.btn_ViewPreviousData);
        btn_AnalyticsData = (Button) findViewById(R.id.btn_AnalyticsData);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_medical_prescriptions = (Button) findViewById(R.id.btn_medical_prescriptions);
        btn_suggestions = (Button) findViewById(R.id.btn_suggestions);
        icona_ospedale = (TextView)findViewById(R.id.id_icona_hospital);
        contact_us = (TextView)findViewById(R.id.id_text_our_contacts);

        path_child = connection.getUser().getUid(); //RECUPERA L'ID DELLUTENTE CHE SI LOGGA

        DatabaseReference myRef = database.getReference("Users").child("Patient").child(path_child).child("Personal_data").child("cognome");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patient_name = String.valueOf(dataSnapshot.getValue(String.class));
                patient_Title.setText("Welcome back " + patient_name + "! \n Choose the operation you want to carry out");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRef5 = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("id_op");
        myRef5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                counter_misurazioni = String.valueOf(dataSnapshot.getValue(String.class));

                if (counter_misurazioni == "null"){
                    counter_misurazioni = "1";

                } else if (counter_misurazioni != "null"){
                    temp = Integer.valueOf(counter_misurazioni);
                    temp = temp + 1;
                    counter_misurazioni = String.valueOf(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRef10 = database.getReference("Users").child("Patient").child(path_child).child("Prescriptions").child("dieta");
        myRef10.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dieta = String.valueOf(dataSnapshot.getValue(String.class));
                DatabaseReference myRef11 = database.getReference("Users").child("Patient").child(path_child).child("Prescriptions").child("farmaci");
                myRef11.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        farmaci = String.valueOf(dataSnapshot.getValue(String.class));
                        DatabaseReference myRef12 = database.getReference("Users").child("Patient").child(path_child).child("Prescriptions").child("scheda_allenamento");
                        myRef12.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                scheda_allenamento = String.valueOf(dataSnapshot.getValue(String.class));
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btn_StartMesure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setContentView(R.layout.loading);
                Toast.makeText(PatientActivity.this, "Data recovery from connected sensors...", Toast.LENGTH_SHORT).show();
                Toast.makeText(PatientActivity.this, "Loading result in progress...", Toast.LENGTH_SHORT).show();


                //FUNZIONE DI "LOADING" CHE STAMPA A VIDEO UNA PROGRESS BAR CIRCOLARE DI CARICAMENTO
                CountDownTimer countDownTimer = new CountDownTimer(2000, 1000){

                    @Override
                    public void onTick(long millisUntilFinished) {
                        System.out.println("Wait " + millisUntilFinished/1000);
                    }

                    @Override
                    public void onFinish() {
                        //  QUERY PER PRELEVARE IL SESSO DEL PAZIENTE E DIFFERENZIARE LE MISURAZIONI
                        DatabaseReference myRef_preleva_sesso = database.getReference("Users").child("Patient").child(path_child).child("Personal_data").child("gender");
                        myRef_preleva_sesso.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                sesso_paziente = String.valueOf(dataSnapshot.getValue(String.class));
                                NewMeasure(counter_misurazioni, sesso_paziente);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });


                    }
                }.start();

                /////////FINE DELLE FUNZIONE DELLA PORZIONE DI CODICE DI "LOADING"//////////////////



            }
        });

        btn_suggestions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Choose_tipsActivity.class));

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setContentView(R.layout.loading);
                Toast.makeText(PatientActivity.this, "LOG-OUT IN PROGRESS... PLEASE WAIT", Toast.LENGTH_SHORT).show();

                //FUNZIONE DI "LOADING" CHE STAMPA A VIDEO UNA PROGRESS BAR CIRCOLARE DI CARICAMENTO
                CountDownTimer countDownTimer = new CountDownTimer(2000, 1000){

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

                //  QUERY PER PRELEVARE IL SESSO DEL PAZIENTE E DIFFERENZIARE LE MISURAZIONI
                DatabaseReference myRef_preleva_sesso = database.getReference("Users").child("Patient").child(path_child).child("Personal_data").child("gender");
                myRef_preleva_sesso.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sesso_paziente = String.valueOf(dataSnapshot.getValue(String.class));

                        // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                        /////////////////////////////////////////
                        //Intent new_page = new Intent(getApplicationContext(), Previous_dataActivity.class);
                        Intent new_page = new Intent(getApplicationContext(), ChooseMeasureActivity.class);
                        new_page.putExtra("dieta", dieta);
                        new_page.putExtra("farmaci", farmaci);
                        new_page.putExtra("scheda_allenamento", scheda_allenamento);
                        new_page.putExtra("id_paziente", path_child);
                        new_page.putExtra("bool_view", 0);
                        new_page.putExtra("sesso_paziente", sesso_paziente);
                        /////////////////////////////////////////
                        startActivity(new_page);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


            }
        });

        btn_medical_prescriptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                /////////////////////////////////////////
                Intent new_page = new Intent(getApplicationContext(), Choose_prescriptionsActivity.class);
                new_page.putExtra("dieta", dieta);
                new_page.putExtra("farmaci", farmaci);
                new_page.putExtra("scheda_allenamento", scheda_allenamento);
                /////////////////////////////////////////
                startActivity(new_page);

            }
        });

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Hospital_informationActivity.class));

            }
        });

        icona_ospedale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Hospital_informationActivity.class));

            }
        });


        btn_AnalyticsData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatabaseReference myRef4 = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("id_op");
                myRef4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        temp_graph = String.valueOf(dataSnapshot.getValue(String.class));
                        last_id_measure = Integer.parseInt(temp_graph);


                        for (int i = last_id_measure; i >= 1; i--) {
                            int x = i;
                            DatabaseReference myRef1 = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" + i).child("current_time");
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
                            DatabaseReference myRef2 = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" + i).child("handgrip_max");
                            myRef2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String result = String.valueOf(dataSnapshot.getValue(String.class));
                                    value_y_axis = result;

                                    Arr_Y_axis.add(value_y_axis);

                                    DatabaseReference myRef1 = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" + x).child("muscle_mass");
                                    myRef1.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String result = dataSnapshot.getValue(String.class);
                                            value_y_axis_SMI = result;

                                            Arr_Y_axis_SMI.add(value_y_axis_SMI);

                                            DatabaseReference myRefSpeed = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" + x).child("speed");
                                            myRefSpeed.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    String result = dataSnapshot.getValue(String.class);
                                                    value_y_axis_Speed = result;

                                                    Arr_Y_axis_Speed.add(value_y_axis_Speed);

                                                    //  QUERY PER PRELEVARE IL SESSO DEL PAZIENTE E DIFFERENZIARE LE MISURAZIONI
                                                    DatabaseReference myRef_preleva_sesso = database.getReference("Users").child("Patient").child(path_child).child("Personal_data").child("gender");
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
                                                Toast.makeText(PatientActivity.this, "LOADING ANALYTICAL DATA IN PROGRESS ...", Toast.LENGTH_SHORT).show();

                                                //FUNZIONE DI "LOADING" CHE STAMPA A VIDEO UNA PROGRESS BAR CIRCOLARE DI CARICAMENTO
                                                CountDownTimer countDownTimer = new CountDownTimer(1000, 1000){

                                                    @Override
                                                    public void onTick(long millisUntilFinished) {
                                                        System.out.println("Wait " + millisUntilFinished/1000);
                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        Intent new_page = new Intent(getApplicationContext(), RiepilogoGraficiActivity.class);
                                                        new_page.putExtra("id_paziente", path_child);
                                                        new_page.putExtra("X_axis", Arr_X_axis);
                                                        new_page.putExtra("Y_axis", Arr_Y_axis);
                                                        new_page.putExtra("Y_axis_SMI", Arr_Y_axis_SMI);
                                                        new_page.putExtra("Y_axis_Speed", Arr_Y_axis_Speed);
                                                        new_page.putExtra("sesso_paziente", sesso_paziente);
                                                        new_page.putExtra("bool_view", 0);
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


            }
        });


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }



    private void NewMeasure(String count, String sesso_paziente) {

        /*
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        SEZIONE DEDICATA ALLA SIMULAZIONE DEI DATI.

        I DATI SONO INSERITI TRAMITE ARRAYLIST (POICHE' LA LIBRERIA DI LETTURA DA FILE .CSV NON STA FUNZIONANDO)
        HO CREATO DUE TIPOLOGIE DI DATI, RIFERITI AL PAZIENTE 1 (MASCHIO) E PAZIENTE 2 (FEMMINA).
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        */

        if(sesso_paziente.equals("Male"))
        {
            if (count.equals("1")){
                Arr_Misure = new String[]{"1","23.71","24.70","25.76","77.7","14.22","56.83","6.64","34.071","1.357","1.04","24.52"};
            } else if (count.equals("2")){
                Arr_Misure = new String[]{"2","23.90","24.90","25.97","79.9","14.62","57.62","7.65","34.071","1.357","1.11","25.22"};
            }else if (count.equals("3")){
                Arr_Misure = new String[]{"3","17.66","18.40","19.19","78.1","14.30","58.23","5.60","34.171","1.357","0.82","24.66"};
            }else if (count.equals("4")){
                Arr_Misure = new String[]{"4","13.63","14.20","14.81","78.6","14.38","57.62","6.57","34.671","1.457","0.79","24.80"};
            }else if (count.equals("5")){
                Arr_Misure = new String[]{"5","24.67","25.70","26.81","79.5","14.54","58.34","6.57","34.671","1.457","0.93","25.08"};
            }else if (count.equals("6")){
                Arr_Misure = new String[]{"6","12.96","13.50","14.08","77.0","14.10","55.87","7.06","34.971","1.457","0.54","24.31"};
            }else if (count.equals("7")){
                Arr_Misure = new String[]{"7","10.66","11.10","11.58","79.2","14.50","59.24","5.49","35.071","1.557","1.07","25.01"};
            }else if (count.equals("8")){
                Arr_Misure = new String[]{"8","22.27","23.20","24.20","77.7","14.22","56.87","6.60","35.271","1.557","0.93","24.52"};
            }else if (count.equals("9")){
                Arr_Misure = new String[]{"9","22.56","23.50","24.51","79.2","14.50","57.02","7.71","35.571","1.557","1.20","25.01"};
            }else if (count.equals("10")){
                Arr_Misure = new String[]{"10","23.42","24.40","25.45","79.0","14.46","57.49","7.06","35.671","1.657","1.50","24.94"};
            }else if (count.equals("11")){
                Arr_Misure = new String[]{"11","27.65","28.80","30.04","78.1","14.30","56.98","6.85","35.671","1.657","0.76","24.66"};
            }else if (count.equals("12")){
                Arr_Misure = new String[]{"12","12.86","13.40","13.98","77.0","14.10","56.52","6.41","35.671","1.657","0.38","24.31"};
            }else if (count.equals("13")){
                Arr_Misure = new String[]{"13","21.89","22.80","23.78","79.2","14.50","58.45","6.28","35.871","1.757","0.94","25.01"};
            }else if (count.equals("14")){
                Arr_Misure = new String[]{"14","37.73","39.30","40.99","79.5","14.54","57.12","7.79","35.971","1.757","0.72","25.08"};
            }else if (count.equals("15")){
                Arr_Misure = new String[]{"15","7.49","7.80","8.14","79.9","14.62","60.32","4.95","36.171","1.857","0.08","25.22"};
            }else if (count.equals("16")){
                Arr_Misure = new String[]{"16","17.86","18.60","19.40","78.8","14.42","56.90","7.47","36.471","1.857","0.55","24.87"};
            }else if (count.equals("17")){
                Arr_Misure = new String[]{"17","29.76","31.00","32.33","77.7","14.22","56.67","6.80","36.671","1.857","0.74","24.52"};
            }else if (count.equals("18")){
                Arr_Misure = new String[]{"18","23.33","24.30","25.34","77.5","14.18","57.38","5.91","37.271","1.957","1.09","24.45"};
            }else if (count.equals("19")){
                Arr_Misure = new String[]{"19","5.86","6.10","6.36","78.1","14.30","57.14","6.69","38.271","1.957","0.36","24.66"};
            }else if (count.equals("20")){
                Arr_Misure = new String[]{"20","18.53","19.30","20.13","78.3","14.34","58.85","5.16","38.371","1.957","0.74","24.73"};
            } else if (count.equals("21")){
                Arr_Misure = new String[]{"21","18.43","19.20","20.03","79.2","14.50","57.58","7.15","34.071","1.357","0.83","25.01"};
            }else if (count.equals("22")){
                Arr_Misure = new String[]{"22","20.26","21.10","22.01","78.1","14.30","56.67","7.16","34.071","1.357","0.89","24.66"};
            }else if (count.equals("23")){
                Arr_Misure = new String[]{"23","21.22","22.10","23.05","78.6","14.38","57.79","6.40","34.171","1.357","1.13","24.80"};
            }else if (count.equals("24")){
                Arr_Misure = new String[]{"24","23.90","24.90","25.97","77.2","14.14","57.45","5.66","34.671","1.457","0.93","24.38"};
            }else if (count.equals("25")){
                Arr_Misure = new String[]{"25","20.16","21.00","21.90","79.7","14.58","57.51","7.58","34.671","1.457","0.77","25.15"};
            }else if (count.equals("26")){
                Arr_Misure = new String[]{"26","19.20","20.00","20.86","78.8","14.42","57.33","7.04","34.971","1.457","0.53","24.87"};
            }else if (count.equals("27")){
                Arr_Misure = new String[]{"27","24.86","25.90","27.01","79.5","14.54","58.21","6.70","35.071","1.557","1.17","25.08"};
            }else if (count.equals("28")){
                Arr_Misure = new String[]{"28","20.16","21.00","21.90","79.7","14.58","59.49","5.60","35.271","1.557","0.85","25.15"};
            }else if (count.equals("29")){
                Arr_Misure = new String[]{"29","35.62","37.10","38.70","77.5","14.18","55.91","7.38","35.571","1.557","0.79","24.45"};
            }else if (count.equals("30")){
                Arr_Misure = new String[]{"30","24.67","25.70","26.81","77.5","14.18","57.11","6.18","34.071","1.357","0.81","24.45"};
            }else if (count.equals("31")){
                Arr_Misure = new String[]{"31","19.10","19.90","20.76","77.5","14.18","56.72","6.57","34.071","1.357","0.88","24.45"};
            }else if (count.equals("32")){
                Arr_Misure = new String[]{"32","17.86","18.60","19.40","78.1","14.30","57.22","6.61","34.171","1.357","0.86","24.66"};
            }else if (count.equals("33")){
                Arr_Misure = new String[]{"33","15.74","16.40","17.11","79.2","14.50","59.00","5.73","34.671","1.457","0.70","25.01"};
            }else if (count.equals("34")){
                Arr_Misure = new String[]{"34","21.60","22.50","23.47","77.0","14.10","57.02","5.91","34.671","1.457","1.32","24.31"};
            }else if (count.equals("35")){
                Arr_Misure = new String[]{"35","19.20","20.00","20.86","79.5","14.54","58.34","6.57","34.971","1.457","1.26","25.08"};
            }else if (count.equals("36")){
                Arr_Misure = new String[]{"36","24.77","25.80","26.91","77.2","14.14","55.96","7.15","35.071","1.557","1.20","24.38"};
            }else if (count.equals("37")){
                Arr_Misure = new String[]{"37","17.47","18.20","18.98","79.0","14.46","56.95","7.60","35.271","1.557","1.01","24.94"};
            }else if (count.equals("38")){
                Arr_Misure = new String[]{"38","13.44","14.00","14.60","78.6","14.38","58.96","5.23","35.571","1.557","0.65","24.80"};
            }else if (count.equals("39")){
                Arr_Misure = new String[]{"39","20.54","21.40","22.32","78.6","14.38","59.49","4.70","35.671","1.657","0.65","24.80"};
            }else if (count.equals("40")){
                Arr_Misure = new String[]{"40","15.46","16.10","16.79","79.0","14.46","56.79","7.76","35.671","1.657","0.98","24.94"};
            }else if (count.equals("41")){
                Arr_Misure = new String[]{"41","23.33","24.30","25.34","77.7","14.22","55.97","7.50","35.671","1.657","1.15","24.52"};
            }else if (count.equals("42")){
                Arr_Misure = new String[]{"42","24.19","25.20","26.28","77.9","14.26","57.65","6.00","35.871","1.757","1.07","24.59"};
            }else if (count.equals("43")){
                Arr_Misure = new String[]{"43","24.00","25.00","26.07","79.9","14.62","58.31","6.96","35.971","1.757","0.76","25.22"};
            }else if (count.equals("44")){
                Arr_Misure = new String[]{"44","25.15","26.20","27.33","78.3","14.34","56.39","7.62","36.171","1.857","0.64","24.73"};
            }else if (count.equals("45")){
                Arr_Misure = new String[]{"45","24.86","25.90","27.01","78.3","14.34","57.93","6.08","36.471","1.857","0.84","24.73"};
            }else if (count.equals("46")){
                Arr_Misure = new String[]{"46","19.49","20.30","21.17","77.0","14.10","55.18","7.75","36.671","1.857","0.83","24.31"};
            }else if (count.equals("47")){
                Arr_Misure = new String[]{"47","25.63","26.70","27.85","77.5","14.18","56.65","6.64","37.271","1.957","0.76","24.45"};
            }else if (count.equals("48")){
                Arr_Misure = new String[]{"48","23.62","24.60","25.66","79.2","14.50","57.15","7.58","38.271","1.957","0.77","25.01"};
            }else if (count.equals("49")){
                Arr_Misure = new String[]{"49","22.18","23.10","24.09","79.0","14.46","58.74","5.81","38.371","1.957","0.84","24.94"};
            }

        }
        else if (sesso_paziente.equals("Female"))
        {
            if (count.equals("1")){
                Arr_Misure = new String[]{"1","24.96","26.00","27.118","63.0","11.53","45.78","5.70","34.071","1.357","0.70","23.718"};
            } else if (count.equals("2")){
                Arr_Misure = new String[]{"2","11.71","12.20","12.7246","63.0","11.53","46.58","4.90","34.071","1.357","0.46","23.718"};
            }else if (count.equals("3")){
                Arr_Misure = new String[]{"3","13.34","13.90","14.4977","63.4","11.60","46.08","5.69","34.171","1.357","0.59","23.848"};
            }else if (count.equals("4")){
                Arr_Misure = new String[]{"4","10.94","11.40","11.8902","61.8","11.31","45.58","4.91","34.671","1.457","0.60","23.261"};
            }else if (count.equals("5")){
                Arr_Misure = new String[]{"5","16.70","17.40","18.1482","62.7","11.47","45.28","5.92","34.671","1.457","0.37","23.587"};
            }else if (count.equals("6")){
                Arr_Misure = new String[]{"6","11.71","12.20","12.7246","62.1","11.37","45.03","5.74","34.971","1.457","0.56","23.391"};
            }else if (count.equals("7")){
                Arr_Misure = new String[]{"7","15.07","15.70","16.3751","63.5","11.63","46.49","5.42","35.071","1.557","0.71","23.914"};
            }else if (count.equals("8")){
                Arr_Misure = new String[]{"8","13.73","14.30","14.9149","62.1","11.37","46.58","4.19","35.271","1.557","0.94","23.391"};
            }else if (count.equals("9")){
                Arr_Misure = new String[]{"9","9.70","10.10","10.5343","62.5","11.44","45.57","5.49","35.571","1.557","0.69","23.522"};
            }else if (count.equals("10")){
                Arr_Misure = new String[]{"10","10.66","11.10","11.5773","63.0","11.53","47.08","4.40","35.671","1.657","0.40","23.718"};
            }else if (count.equals("11")){
                Arr_Misure = new String[]{"11","10.66","11.10","11.5773","63.4","11.60","46.11","5.66","35.671","1.657","0.75","23.848"};
            }else if (count.equals("12")){
                Arr_Misure = new String[]{"12","15.17","15.80","16.4794","62.8","11.50","45.74","5.60","35.671","1.657","0.99","23.652"};
            }else if (count.equals("13")){
                Arr_Misure = new String[]{"13","14.78","15.40","16.0622","61.8","11.31","44.89","5.60","35.871","1.757","0.64","23.261"};
            }else if (count.equals("14")){
                Arr_Misure = new String[]{"14","9.60","10.00","10.43","63.4","11.60","46.64","5.13","35.971","1.757","0.76","23.848"};
            }else if (count.equals("15")){
                Arr_Misure = new String[]{"15","11.90","12.40","12.9332","62.8","11.50","45.86","5.48","36.171","1.857","0.76","23.652"};
            }else if (count.equals("16")){
                Arr_Misure = new String[]{"16","16.70","17.40","18.1482","62.0","11.34","44.85","5.78","36.471","1.857","0.84","23.326"};
            }else if (count.equals("17")){
                Arr_Misure = new String[]{"17","15.94","16.60","17.3138","62.3","11.40","45.19","5.73","36.671","1.857","0.62","23.456"};
            }else if (count.equals("18")){
                Arr_Misure = new String[]{"18","13.92","14.50","15.1235","62.3","11.40","45.88","5.04","37.271","1.957","0.75","23.456"};
            }else if (count.equals("19")){
                Arr_Misure = new String[]{"19","12.86","13.40","13.9762","62.3","11.40","46.83","4.09","38.271","1.957","0.42","23.456"};
            }else if (count.equals("20")){
                Arr_Misure = new String[]{"20","11.90","12.40","12.9332","62.8","11.50","46.16","5.18","38.371","1.957","0.66","23.652"};
            } else if (count.equals("21")){
                Arr_Misure = new String[]{"21","19.97","20.80","21.6944","61.6","11.28","45.55","4.80","34.071","1.357","0.79","23.195"};
            }else if (count.equals("22")){
                Arr_Misure = new String[]{"22","18.53","19.30","20.1299","62.3","11.40","44.93","5.99","34.071","1.357","0.71","23.456"};
            }else if (count.equals("23")){
                Arr_Misure = new String[]{"23","21.22","22.10","23.0503","62.8","11.50","45.38","5.96","34.171","1.357","0.74","23.652"};
            }else if (count.equals("24")){
                Arr_Misure = new String[]{"24","9.31","9.70","10.1171","62.8","11.50","46.04","5.30","34.671","1.457","0.99","23.652"};
            }else if (count.equals("25")){
                Arr_Misure = new String[]{"25","18.34","19.10","19.9213","63.2","11.56","46.73","4.90","34.671","1.457","0.79","23.783"};
            }else if (count.equals("26")){
                Arr_Misure = new String[]{"26","13.92","14.50","15.1235","61.5","11.25","44.80","5.41","34.971","1.457","0.78","23.130"};
            }else if (count.equals("27")){
                Arr_Misure = new String[]{"27","12.48","13.00","13.559","62.5","11.44","45.55","5.51","35.071","1.557","0.95","23.522"};
            }else if (count.equals("28")){
                Arr_Misure = new String[]{"28","22.56","23.50","24.5105","62.8","11.50","45.41","5.93","35.271","1.557","0.79","23.652"};
            }else if (count.equals("29")){
                Arr_Misure = new String[]{"29","14.02","14.60","15.2278","62.5","11.44","45.19","5.87","35.571","1.557","0.80","23.522"};
            }


        }


        // SI FORZANO LA "CURRENT_DATE" DELLE PRIME MISURE INSERITE DI DEFAULT NEL DB. SI SCEGLIE DI FORZARLE PER
        // SIMULARE LE MISURAZIONI GRADUALI NEL CORSO DI PIU' MESI.
        if(sesso_paziente.equals("Male"))
        {
            if (count.equals("1")){
                current_time = "08/11/22 10:00";
            } else if (count.equals("2")){
                current_time = "10/11/22 10:00";
            }else if (count.equals("3")){
                current_time = "12/11/22 10:00";
            }else if (count.equals("4")){
                current_time = "14/11/22 10:00";
            }else if (count.equals("5")){
                current_time = "16/11/22 10:00";
            }else if (count.equals("6")){
                current_time = "18/11/22 10:00";
            }else if (count.equals("7")){
                current_time = "20/11/22 10:00";
            }else if (count.equals("8")){
                current_time = "22/11/22 10:00";
            }else if (count.equals("9")){
                current_time = "24/11/22 10:00";
            }else if (count.equals("10")){
                current_time = "26/11/22 10:00";
            }else if (count.equals("11")){
                current_time = "28/11/22 10:00";
            }else if (count.equals("12")){
                current_time = "30/11/22 10:00";
            }else if (count.equals("13")){
                current_time = "02/12/22 10:00";
            }else if (count.equals("14")){
                current_time = "04/12/22 10:00";
            }else if (count.equals("15")){
                current_time = "06/12/22 10:00";
            }else if (count.equals("16")){
                current_time = "08/12/22 10:00";
            }else if (count.equals("17")){
                current_time = "10/12/22 10:00";
            }else if (count.equals("18")){
                current_time = "12/12/22 10:00";
            }else if (count.equals("19")){
                current_time = "14/12/22 10:00";
            }else if (count.equals("20")){
                current_time = "16/12/22 10:00";
            } else if (count.equals("21")){
                current_time = "18/12/22 10:00";
            }else if (count.equals("22")){
                current_time = "20/12/22 10:00";
            }else if (count.equals("23")){
                current_time = "22/12/22 10:00";
            }else if (count.equals("24")){
                current_time = "24/12/22 10:00";
            }else if (count.equals("25")){
                current_time = "26/12/22 10:00";
            }else if (count.equals("26")){
                current_time = "28/12/22 10:00";
            }else if (count.equals("27")){
                current_time = "30/12/22 10:00";
            }else if (count.equals("28")){
                current_time = "01/12/23 10:00";
            }else if (count.equals("29")){
                current_time = "03/01/23 10:00";
            }else if (count.equals("30")){
                current_time = "05/01/23 10:00";
            }else if (count.equals("31")){
                current_time = "07/01/23 10:00";
            }else if (count.equals("32")){
                current_time = "09/01/23 10:00";
            }else if (count.equals("33")){
                current_time = "11/01/23 10:00";
            }else if (count.equals("34")){
                current_time = "13/01/23 10:00";
            }else if (count.equals("35")){
                current_time = "15/01/23 10:00";
            }else{
                Calendar cal;
                cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat();
                current_time = String.valueOf(sdf.format(cal.getTime()));
            }

        }
        else if (sesso_paziente.equals("Female"))
        {
            if (count.equals("1")){
                current_time = "08/12/22 10:00";
            } else if (count.equals("2")){
                current_time = "10/12/22 10:00";
            }else if (count.equals("3")){
                current_time = "12/12/22 10:00";
            }else if (count.equals("4")){
                current_time = "14/12/22 10:00";
            }else if (count.equals("5")){
                current_time = "16/12/22 10:00";
            }else if (count.equals("6")){
                current_time = "18/12/22 10:00";
            }else if (count.equals("7")){
                current_time = "20/12/22 10:00";
            }else if (count.equals("8")){
                current_time = "22/12/22 10:00";
            }else if (count.equals("9")){
                current_time = "24/12/22 10:00";
            }else if (count.equals("10")){
                current_time = "26/12/22 10:00";
            }else if (count.equals("11")){
                current_time = "28/12/22 10:00";
            }else if (count.equals("12")){
                current_time = "30/12/22 10:00";
            }else if (count.equals("13")){
                current_time = "01/01/23 10:00";
            }else if (count.equals("14")){
                current_time = "03/01/23 10:00";
            }else if (count.equals("15")){
                current_time = "05/01/23 10:00";
            }else if (count.equals("16")){
                current_time = "07/01/23 10:00";
            }else if (count.equals("17")){
                current_time = "09/01/23 10:00";
            }else if (count.equals("18")){
                current_time = "11/01/23 10:00";
            }else if (count.equals("19")){
                current_time = "13/01/23 10:00";
            }else if (count.equals("20")){
                current_time = "15/01/23 10:00";
            }else{
                Calendar cal;
                cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat();
                current_time = String.valueOf(sdf.format(cal.getTime()));
            }

        }



        // FINE SEZIONE SIMULAZIONE DEI DATI
       /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        id_op = Arr_Misure[0];
        handgrip_min = Arr_Misure[1];
        handgrip_max = Arr_Misure[2];
        handgrip_postexercise = Arr_Misure[3];
        weight_measured = Arr_Misure[4];
        fat_mass = Arr_Misure[5];
        bone_mass = Arr_Misure[6];
        muscle_mass = Arr_Misure[7];
        step_count = Arr_Misure[8];
        step_frequency = Arr_Misure[9];
        speed = Arr_Misure[10];
        BMI = Arr_Misure[11];

        /*
        Calendar cal;
        cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat();
        current_time = String.valueOf(sdf.format(cal.getTime()));
         */

        if (id_op.equals("1")){

            WriteCSV.write_list_of_patient(patient_name);

        }


        MeasureData MeasureData = new MeasureData(String.valueOf(count), current_time, handgrip_min, handgrip_max, handgrip_postexercise, weight_measured, fat_mass, bone_mass, muscle_mass, step_count, step_frequency, speed, BMI);

        String current_time_reduce = current_time = current_time.substring(3,5) + "/" + current_time.substring(0,3) + "20"+current_time.substring(6,8);

        Dataset_SMI_ML.add(current_time_reduce);
        Dataset_SMI_ML.add(muscle_mass);

        Dataset_Hgrip_ML.add(current_time_reduce);
        Dataset_Hgrip_ML.add(handgrip_max);

        Dataset_Speed_ML.add(current_time_reduce);
        Dataset_Speed_ML.add(speed);

        //CHIAMATA ALLA CLASSE DI CREAZIONE DEL DATASET PER IL MACHINE LEARNING
        WriteCSV.test(Dataset_SMI_ML, path_child, patient_name, 1);
        WriteCSV.test(Dataset_Hgrip_ML, path_child, patient_name, 2);
        WriteCSV.test(Dataset_Speed_ML, path_child, patient_name, 3);

        // AGGIUNGIAMO LA MISURAZIONE EFFETTUATA DAI SENSORI NEL RAMO FIGLIO "LAST MEASURE"
        FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Patient")
                .child(path_child).child("Measure").child("Last Measure")
                .setValue(MeasureData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            if (task.isSuccessful()) {
                                Intent new_page = new Intent(getApplicationContext(), NewMeasureActivity.class);
                                new_page.putExtra("sesso_paziente", sesso_paziente);
                                startActivity(new_page);


                            } else {
                                Toast.makeText(PatientActivity.this, "Misurazione fallita", Toast.LENGTH_SHORT).show();
                            }

                        }

                        // AGGIUNGIAMO LA MISURAZIONE EFFETTUATA DAI SENSORI NEL RAMO FIGLIO DOVE SI ARCHIVIANO TUTTE LE MISURAZIONI
                        FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Patient")
                                .child(path_child).child("Measure").child("ID_" + String.valueOf(count))
                                .setValue(MeasureData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            MeasureData MeasureData = new MeasureData(id_op, current_time, handgrip_min, handgrip_max, handgrip_postexercise, weight_measured, fat_mass, bone_mass, muscle_mass, step_count, step_frequency, speed, BMI);
                                        } else {
                                        Toast.makeText(PatientActivity.this, "Misurazione fallita", Toast.LENGTH_SHORT).show();
                                    }

                                }
                    });
                }
        });
    }

    @Override
    public void onBackPressed() {
        setContentView(R.layout.loading);
        Toast.makeText(PatientActivity.this, "LOG-OUT IN PROGRESS... PLEASE WAIT", Toast.LENGTH_SHORT).show();

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