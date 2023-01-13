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


public class List_of_usersActivity extends AppCompatActivity {

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

    String value_selected; //VARIABILE CHE CONTIENE IL VALORE SELEZIONATO DALLO SPINNER
    String last_ID_user_scelto;
    String stringa_ID_scelta;


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int bool_view = getIntent().getExtras().getInt("bool_view");
        ArrayList<String> array_lista_utenti = (ArrayList<String>) getIntent().getSerializableExtra("array_lista_utenti");

        System.out.println("STAMPO LA LISTA UTENTI: " +array_lista_utenti);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.list_of_users); //si specifica la pagina xml che si vuole stampare a video

        title = (TextView)findViewById(R.id.id_title_list_of_user);
        spinner = (Spinner) findViewById(R.id.id_spinner_list_of_user);
        continue_button = (Button)findViewById(R.id.btn_continue);
        button_back = (Button)findViewById(R.id.button_back_6);

        button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Type_of_usersActivity.class));
            }
        });

        if (bool_view == 1){
            title.setText("Select the particular doctor profile you want to edit");
        } else if (bool_view == 0){
            title.setText("Select the particular patient profile you want to edit");
        }

        continue_button.setOnClickListener(view -> {

            value_selected = spinner.getSelectedItem().toString();
            String ID_utente_scelto = value_selected.substring(0,4);

            if (bool_view == 0){
                DatabaseReference myRef1 = database.getReference("Users").child("Admin").child("List_of_patient").child(ID_utente_scelto).child("id");
                myRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String result = String.valueOf(dataSnapshot.getValue(String.class));
                        stringa_ID_scelta = result;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } else if (bool_view == 1){
                DatabaseReference myRef1 = database.getReference("Users").child("Admin").child("List_of_doctor").child(ID_utente_scelto).child("id");
                myRef1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String result = String.valueOf(dataSnapshot.getValue(String.class));
                        stringa_ID_scelta = result;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            setContentView(R.layout.loading);
            Toast.makeText(List_of_usersActivity.this, "LOADING RESULT  ...", Toast.LENGTH_SHORT).show();

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


                    Intent new_page = new Intent(getApplicationContext(), EditProfileActivity.class);

                    new_page.putExtra("stringa_ID_scelta", stringa_ID_scelta);
                    new_page.putExtra("bool_view", bool_view);
                    new_page.putExtra("ID_utente_scelto", ID_utente_scelto);
                    new_page.putExtra("bool_editabile", 1);
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


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array_lista_utenti);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Type_of_usersActivity.class));

    }

}