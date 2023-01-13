package com.example.tabbedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.tabbedapplication.databinding.ActivityMainBinding;
import com.example.tabbedapplication.ui.Entities.Read_Patient_byDoctor;
import com.example.tabbedapplication.ui.Entities.User;
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

public class EditProfileActivity extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    String path_child;
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);

    TextView textName; //si dichiarano tuti gli elementi presenti nella pagina xml
    TextView textSurname;
    TextView textPlaceofBirth;
    TextView textDateofBirth;
    TextView textAddress;
    TextView textTelephone;
    TextView textEmail;
    TextView textHeight;
    TextView textWeight;
    TextView textCity;
    RadioGroup radioGroup;
    RadioButton radio_maschio;
    RadioButton radio_femmina;
    Button btnCreateProfile;
    TextView title;
    String role;

    String ID_utente_scelto;
    String stringa_ID_scelta;

    int bool_editabile;

    String username;
    String password;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        stringa_ID_scelta = getIntent().getExtras().getString("stringa_ID_scelta");
        ID_utente_scelto = getIntent().getExtras().getString("ID_utente_scelto");
        int bool_view = getIntent().getExtras().getInt("bool_view");
        bool_editabile = getIntent().getExtras().getInt("bool_editabile");
        String role_user = "";
        mAuth = FirebaseAuth.getInstance();

        if (bool_view == 0){
            role_user = "Patient";
        } else if (bool_view == 1){
            role_user = "Doctor";
        }

        path_child = stringa_ID_scelta; //RECUPERA L'ID DELLUTENTE CHE SI LOGGA

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.edit_profile); //si specifica la pagina xml che si vuole stampare a video

        title = (TextView) findViewById(R.id.Title_edit_profile);
        textName = (TextView) findViewById(R.id.edtText_newName);
        textSurname = (TextView) findViewById(R.id.edtText_newSurname);
        textPlaceofBirth = (TextView) findViewById(R.id.edtText_newPlace_of_birth);
        textDateofBirth = (TextView) findViewById(R.id.edtText_newDate_of_birth);
        textAddress = (TextView) findViewById(R.id.edtText_newAddress);
        textTelephone = (TextView) findViewById(R.id.edtText_newPhone);
        textEmail = (TextView) findViewById(R.id.edtText_newEmail);
        textHeight = (TextView) findViewById(R.id.edtText_Height);
        textWeight = (TextView) findViewById(R.id.edtText_Weight);
        textCity = (TextView) findViewById(R.id.edtText_new_city);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radio_maschio = (RadioButton)findViewById(R.id.id_radio_maschio);
        radio_femmina = (RadioButton)findViewById(R.id.id_radio_femmina);
        btnCreateProfile = (Button) findViewById(R.id.btn_EditProfile);

        if(bool_editabile == 0){
            btnCreateProfile.setText("BACK");
        }


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);


        DatabaseReference myRefName = database.getReference("Users").child(role_user).child(path_child).child("Personal_data").child("nome");
        myRefName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textName.setText(result);
                if(bool_editabile == 0){
                    //SI SETTANO LE EDITTEXT NON EDITABILI
                    textName.setEnabled(false);
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefGender = database.getReference("Users").child(role_user).child(path_child).child("Personal_data").child("gender");
        myRefGender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                if (result.equals("Male")){
                    radio_maschio.setChecked(true);
                    radio_femmina.setChecked(false);

                } else if (result.equals("Female")){
                    radio_femmina.setChecked(true);
                    radio_maschio.setChecked(false);
                }

                if(bool_editabile == 0){
                    // To disabled the Radio Buttons of radio group.
                    for (int i = 0; i < radioGroup.getChildCount(); i++) {
                        radioGroup.getChildAt(i).setEnabled(false);
                    }
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefCognome = database.getReference("Users").child(role_user).child(path_child).child("Personal_data").child("cognome");
        myRefCognome.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textSurname.setText(result);
                if(bool_editabile == 0){
                    //SI SETTANO LE EDITTEXT NON EDITABILI
                    textSurname.setEnabled(false);
                }

                if (bool_view == 0){
                    title.setText("Profile summary of the patient: " + result);
                } else if (bool_view == 1){
                    title.setText("Profile summary of the doctor: " + result);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefLuogoNascita = database.getReference("Users").child(role_user).child(path_child).child("Personal_data").child("luogo_nascita");
        myRefLuogoNascita.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textPlaceofBirth.setText(result);
                if(bool_editabile == 0){
                    //SI SETTANO LE EDITTEXT NON EDITABILI
                    textPlaceofBirth.setEnabled(false);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefCity = database.getReference("Users").child(role_user).child(path_child).child("Personal_data").child("city");
        myRefCity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textCity.setText(result);
                if(bool_editabile == 0){
                    //SI SETTANO LE EDITTEXT NON EDITABILI
                    textCity.setEnabled(false);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefDataNascita = database.getReference("Users").child(role_user).child(path_child).child("Personal_data").child("data_nascita");
        myRefDataNascita.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textDateofBirth.setText(result);
                if(bool_editabile == 0){
                    //SI SETTANO LE EDITTEXT NON EDITABILI
                    textDateofBirth.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefIndirizzo = database.getReference("Users").child(role_user).child(path_child).child("Personal_data").child("indirizzo");
        myRefIndirizzo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textAddress.setText(result);
                if(bool_editabile == 0){
                    //SI SETTANO LE EDITTEXT NON EDITABILI
                    textAddress.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefTelephone = database.getReference("Users").child(role_user).child(path_child).child("Personal_data").child("telefono");
        myRefTelephone.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textTelephone.setText(result);
                if(bool_editabile == 0){
                    //SI SETTANO LE EDITTEXT NON EDITABILI
                    textTelephone.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefEmail = database.getReference("Users").child(role_user).child(path_child).child("Personal_data").child("email");
        myRefEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textEmail.setText(result);
                if(bool_editabile == 0){
                    //SI SETTANO LE EDITTEXT NON EDITABILI
                    textEmail.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefHeight = database.getReference("Users").child(role_user).child(path_child).child("Personal_data").child("altezza");
        myRefHeight.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textHeight.setText(result);
                if(bool_editabile == 0){
                    //SI SETTANO LE EDITTEXT NON EDITABILI
                    textHeight.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefWeight = database.getReference("Users").child(role_user).child(path_child).child("Personal_data").child("peso");
        myRefWeight.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                textWeight.setText(result);
                if(bool_editabile == 0){
                    //SI SETTANO LE EDITTEXT NON EDITABILI
                    textWeight.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefUsername = database.getReference("Users").child(role_user).child(path_child).child("Personal_data").child("username");
        myRefUsername.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                username = result;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefPassword = database.getReference("Users").child(role_user).child(path_child).child("Personal_data").child("password");
        myRefPassword.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue(String.class);
                password = result;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        role = role_user;

        btnCreateProfile.setOnClickListener(view ->{
            if (bool_editabile == 1){
                validateData();
            } else if (bool_editabile == 0){
                startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
            }
        });



    }

    private void validateData() {
        String nome = textName.getText().toString().trim();
        String cognome = textSurname.getText().toString().trim();
        String luogo_nascita = textPlaceofBirth.getText().toString().trim();
        String data_nascita = textDateofBirth.getText().toString().trim();
        String indirizzo = textAddress.getText().toString().trim();
        String telefono = textTelephone.getText().toString().trim();
        String peso = textWeight.getText().toString().trim();
        String altezza = textHeight.getText().toString().trim();
        String email = textEmail.getText().toString().trim();
        String city = textCity.getText().toString().trim();

        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);

        String gender = (String) radioButton.getText();


        if (nome.isEmpty()) {
            textName.setError("Il campo è obbligatorio!");
            textName.requestFocus();
            return;
        }

        if (cognome.isEmpty()) {
            textSurname.setError("Il campo è obbligatorio!");
            textSurname.requestFocus();
            return;
        }

        if (luogo_nascita.isEmpty()) {
            textPlaceofBirth.setError("Il campo è obbligatorio!");
            textPlaceofBirth.requestFocus();
            return;
        }

        if (data_nascita.isEmpty()) {
            textDateofBirth.setError("Il campo è obbligatorio!");
            textDateofBirth.requestFocus();
            return;
        }

        if (indirizzo.isEmpty()) {
            textAddress.setError("Il campo è obbligatorio!");
            textAddress.requestFocus();
            return;
        }

        if (telefono.isEmpty()) {
            textTelephone.setError("Il campo è obbligatorio!");
            textTelephone.requestFocus();
            return;
        }

        if (peso.isEmpty()) {
            textWeight.setError("Il campo è obbligatorio!");
            textWeight.requestFocus();
            return;
        }

        if (altezza.isEmpty()) {
            textHeight.setError("Il campo è obbligatorio!");
            textHeight.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            textEmail.setError("Il campo è obbligatorio!");
            textEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textEmail.setError("Inserire un'email valida!");
            textEmail.requestFocus();
            return;
        }

        if (city.isEmpty()) {
            textCity.setError("Il campo è obbligatorio!");
            textCity.requestFocus();
            return;
        }


        User user = new User(nome, cognome,luogo_nascita, data_nascita, indirizzo, telefono, email, altezza, peso, username, password, role, gender, city);
        Read_Patient_byDoctor read_Doctor = new Read_Patient_byDoctor(nome, cognome, stringa_ID_scelta);

        if (role == "Doctor"){

            FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Doctor").child(path_child).child("Personal_data")
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this, "Modifica del profilo avvenuta con successo", Toast.LENGTH_SHORT).show();

                                FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Admin").child("List_of_doctor").child(ID_utente_scelto)
                                        .setValue(read_Doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                        }
                                });



                                startActivity(new Intent(getApplicationContext() , AdministratorActivity.class));
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Modifica del profilo fallita", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else{
            FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Patient").child(path_child).child("Personal_data")
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this, "Modifica del profilo avvenuta con successo", Toast.LENGTH_SHORT).show();

                                FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Admin").child("List_of_patient").child(ID_utente_scelto)
                                        .setValue(read_Doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Doctor").child("Patient").child(ID_utente_scelto)
                                                        .setValue(read_Doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                            }
                                                        });

                                            }
                                        });

                                startActivity(new Intent(getApplicationContext() , AdministratorActivity.class));
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Modifica del profilo fallita", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }

    @Override
    public void onBackPressed() {
        if (bool_editabile == 1){
            startActivity(new Intent(getApplicationContext(), AdministratorActivity.class));
        } else if (bool_editabile == 0){
            startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
        }


    }

}