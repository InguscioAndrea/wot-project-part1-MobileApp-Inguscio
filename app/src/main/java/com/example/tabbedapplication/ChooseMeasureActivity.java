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


public class ChooseMeasureActivity extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    String path_child;
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);

    TextView title;
    Spinner spinner;
    Button continue_button;
    Button button_back;

    ArrayList<String> array_spinner = new ArrayList<String>();

    String spinner_selection;
    String value_selected; //VARIABILE CHE CONTIENE IL VALORE SELEZIONATO DALLO SPINNER
    String temp;
    String id_patient_selected;
    int bool_view; //VARIABILE CHE SE "1" RIGUARDA IL DOTTORE, SE "0" IL PAZIENTE
    int last_id_measure;
    int bool_index; //VARIABILE INTERA CHE FUNGE DA BOOLEAN PER FAR SI CHE SI PRENDA I PRIMI 5 CARATTTERI DELLO SPINNER SE VALE 1 O I PRIMI 4 SE VALE 0
    String sesso_paziente;


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sesso_paziente = getIntent().getExtras().getString("sesso_paziente"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT
        id_patient_selected = getIntent().getExtras().getString("id_paziente"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT
        bool_view = getIntent().getExtras().getInt("bool_view");
        String dieta = getIntent().getExtras().getString("dieta"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT
        String farmaci = getIntent().getExtras().getString("farmaci"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT
        String scheda_allenamento = getIntent().getExtras().getString("scheda_allenamento"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT

        System.out.println("L?ID PATIENT SELECTED E..."+ id_patient_selected);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.choose_measure); //si specifica la pagina xml che si vuole stampare a video

        title = (TextView)findViewById(R.id.id_title_DoctorPage_2);
        spinner = (Spinner) findViewById(R.id.id_spinner_measure);
        continue_button = (Button)findViewById(R.id.btn_continue_measure);
        button_back = (Button)findViewById(R.id.button_back_6);

        button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bool_view == 1) {
                    startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
                } else if (bool_view == 0) {
                    startActivity(new Intent(getApplicationContext(), PatientActivity.class));
                }
            }
        });


        DatabaseReference myRef4 = database.getReference("Users").child("Patient").child(id_patient_selected).child("Measure").child("Last Measure").child("id_op");
        myRef4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                temp = String.valueOf(dataSnapshot.getValue(String.class));
                last_id_measure = Integer.parseInt(temp);
                if (last_id_measure < 10){
                    bool_index = 0;
                } else if (last_id_measure >= 10){
                    bool_index = 1;
                }

                for(int i=last_id_measure; i>=1; i--) {
                    int x = i;
                    DatabaseReference myRef6 = database.getReference("Users").child("Patient").child(id_patient_selected).child("Measure").child("ID_"+ i).child("current_time");
                    myRef6.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String result = String.valueOf(dataSnapshot.getValue(String.class));
                            spinner_selection = "#ID_"+ x +" Measure of: " + result;
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

        continue_button.setOnClickListener(view -> {

            value_selected = spinner.getSelectedItem().toString();

            if (bool_index == 0){       //PRENDE GLI ID A 1 CIFRA (es: #ID_2, PRELEVA ID_2)
                temp = value_selected.substring(4,5);
            } else if (bool_index == 1){        //PRENDE GLI ID A 2 CIFRE (es: #ID_12, PRELEVA ID_12)
                if (value_selected.substring(5, 6).equals(" ")){
                    temp = value_selected.substring(4,5);
                } else {
                    temp = value_selected.substring(4,6);
                }
            }

            setContentView(R.layout.loading);
            Toast.makeText(ChooseMeasureActivity.this, "LOADING RESULT  ...", Toast.LENGTH_SHORT).show();

            //FUNZIONE DI "LOADING" CHE STAMPA A VIDEO UNA PROGRESS BAR CIRCOLARE DI CARICAMENTO
            CountDownTimer countDownTimer = new CountDownTimer(500, 1000){

                @Override
                public void onTick(long millisUntilFinished) {
                    System.out.println("Wait " + millisUntilFinished/1000);
                }

                @Override
                public void onFinish() {
                    // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                    /////////////////////////////////////////
                    Intent new_page = new Intent(getApplicationContext(), SinglePreviousDataPatient.class);
                    new_page.putExtra("id_paziente", id_patient_selected);
                    new_page.putExtra("id_operazione_scelta", Integer.parseInt(temp));
                    new_page.putExtra("dieta", dieta);
                    new_page.putExtra("farmaci", farmaci);
                    new_page.putExtra("scheda_allenamento", scheda_allenamento);
                    new_page.putExtra("bool_view", bool_view);
                    new_page.putExtra("sesso_paziente", sesso_paziente);
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
        if (bool_view == 1) {
            startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
        } else if (bool_view == 0) {
            startActivity(new Intent(getApplicationContext(), PatientActivity.class));
        }

    }

}