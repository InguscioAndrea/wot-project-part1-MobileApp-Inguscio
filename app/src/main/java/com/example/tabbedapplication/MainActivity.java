package com.example.tabbedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.tabbedapplication.databinding.ActivityMainBinding;
import com.example.tabbedapplication.ui.main.SectionsPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    String path_child;
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);



    int user_login = 0; //VARIABILE BOOLEANA CHE INDICA SE LA CONDIZIONE DI "VERIFICA IDENTITA' UTENTE" E' GIA STATA SODDISFATTA
    String value_role;
    String new_pdf_dieta;
    String new_pdf_farmaci;
    String new_pdf_scheda_allenamento;

    TextView textName; //si dichiarano tuti gli elementi presenti nella pagina xml
    TextView textPassword;
    Button button_login;



    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.login); //si specifica la pagina xml che si vuole stampare a video

        textName = (TextView)findViewById(R.id.username_text);
        textPassword = (TextView)findViewById(R.id.password_text);
        button_login = (Button)findViewById(R.id.button_login);


        button_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                userLogin();

        }
        });




        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }

    private void userLogin(){

        String name = textName.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if (name.isEmpty()) {
            textName.setError("Il campo è obbligatorio!");
            textName.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(name).matches()) {
            textName.setError("Username Errato!");
            textName.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            textPassword.setError("Il campo è obbligatorio!");
            textPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            textPassword.setError("La password deve essere lunga almeno 6 caratteri");
            textPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(name, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    path_child = connection.getUser().getUid(); //RECUPERA L'ID DELLUTENTE CHE SI LOGGA
                    DatabaseReference myRef = database.getReference("Users").child("Patient").child(path_child).child("Personal_data").child("role");
                    myRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            value_role = dataSnapshot.getValue(String.class);

                            try {
                                if (value_role.equals("Patient")){
                                    System.out.println(value_role);
                                    System.out.println("SI STAMPA LA PAGINA XML DEDICATA AL PATIENT");
                                    startActivity(new Intent(getApplicationContext() , PatientActivity.class));}
                                    user_login = 1;
                            } catch(Exception e) {
                                System.out.println("Non è un paziente");
                            }

                            if (user_login == 0){
                                DatabaseReference myRef_2 = database.getReference("Users").child("Doctor").child(path_child).child("Personal_data").child("role");
                                myRef_2.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        value_role = dataSnapshot.getValue(String.class);

                                        try {
                                            if (value_role.equals("Doctor")){
                                                System.out.println(value_role);
                                                System.out.println("SI STAMPA LA PAGINA XML DEDICATA AL DOCTOR");
                                                startActivity(new Intent(getApplicationContext() , DoctorActivity.class));}
                                            user_login = 1;
                                        } catch(Exception e) {
                                            System.out.println(value_role);
                                            System.out.println("SI STAMPA LA PAGINA XML DEDICATA ALL' ADMIN");
                                            startActivity(new Intent(getApplicationContext() , AdministratorActivity.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Failed to read value
                                        System.out.println("YOU LOSE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                    }
                                });
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            System.out.println("YOU LOSE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }
                    });



                }
                else
                    Toast.makeText(MainActivity.this, "Login fallito. I dati non sono corretti", Toast.LENGTH_SHORT).show();
            }



        });



    }


}




























