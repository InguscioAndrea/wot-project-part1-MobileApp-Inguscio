package com.example.tabbedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.tabbedapplication.databinding.ActivityMainBinding;
import com.example.tabbedapplication.ui.Entities.Presciptions;
import com.example.tabbedapplication.ui.Entities.Read_Patient_byDoctor;
import com.example.tabbedapplication.ui.Entities.User;
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

public class NewUserActivity extends AppCompatActivity {

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
    TextView textCity;
    TextView textEmail;
    TextView textHeight;
    TextView textWeight;
    TextView textUsername;
    TextView textPassword;
    Button btnCreateProfile;
    CheckBox checkBox_RoleNewUser;
    RadioGroup radioGroup;
    RadioButton radio_maschio;
    RadioButton radio_femmina;
    String role;
    String index_patient;
    String index_doctor;
    String id;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        path_child = connection.getUser().getUid(); //RECUPERA L'ID DELLUTENTE CHE SI LOGGA

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.new_user_page); //si specifica la pagina xml che si vuole stampare a video

        textName = (TextView) findViewById(R.id.edtText_newName);
        textSurname = (TextView) findViewById(R.id.edtText_newSurname);
        textPlaceofBirth = (TextView) findViewById(R.id.edtText_newPlace_of_birth);
        textDateofBirth = (TextView) findViewById(R.id.edtText_newDate_of_birth);
        textAddress = (TextView) findViewById(R.id.edtText_newAddress);
        textTelephone = (TextView) findViewById(R.id.edtText_newPhone);
        textEmail = (TextView) findViewById(R.id.edtText_newEmail);
        textCity = (TextView) findViewById(R.id.edtText_new_city);
        textHeight = (TextView) findViewById(R.id.edtText_Height);
        textWeight = (TextView) findViewById(R.id.edtText_Weight);
        btnCreateProfile = (Button) findViewById(R.id.btn_CreateProfile);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radio_maschio = (RadioButton)findViewById(R.id.id_radio_maschio);
        radio_femmina = (RadioButton)findViewById(R.id.id_radio_femmina);
        checkBox_RoleNewUser = (CheckBox)findViewById(R.id.checkBoxRoleNewUser2);

        textUsername = (TextView) findViewById(R.id.edtText_newUsername2);
        textPassword = (TextView) findViewById(R.id.edtText_newPassword2);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        DatabaseReference myRef = database.getReference("Users").child("Doctor").child("Patient").child("Last ID");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                index_patient = String.valueOf(dataSnapshot.getValue(String.class));

                if (index_patient.equals("null")){
                    index_patient = "1";

                } else {
                    int temp = Integer.valueOf(index_patient);
                    temp = temp + 1;
                    index_patient = String.valueOf(temp);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRef_2 = database.getReference("Users").child("Admin").child("List_of_doctor").child("Last ID");
        myRef_2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                index_doctor = String.valueOf(dataSnapshot.getValue(String.class));

                if (index_doctor.equals("null")){
                    index_doctor = "1";

                } else {
                    int temp = Integer.valueOf(index_doctor);
                    temp = temp + 1;
                    index_doctor = String.valueOf(temp);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        btnCreateProfile.setOnClickListener(view ->
        validateData());

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
        String username = textUsername.getText().toString().trim();
        String password = textPassword.getText().toString().trim();
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

        if (username.isEmpty()) {
            textUsername.setError("Il campo è obbligatorio!");
            textUsername.requestFocus();
            return;
        }

        if (city.isEmpty()) {
            textCity.setError("Il campo è obbligatorio!");
            textCity.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textUsername.setError("Inserire un'email per username!");
            textUsername.requestFocus();
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

            //code to check if this checkbox is checked!
            if(checkBox_RoleNewUser.isChecked()){
                role = "Doctor";
            }
            else{
                role = "Patient";
            }



        // Se tutto è corretto, si procede con la scrittura sul db
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(nome, cognome,luogo_nascita, data_nascita, indirizzo, telefono, email, altezza, peso, username, password, role, gender, city);

                            if (checkBox_RoleNewUser.isChecked()){

                                FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Doctor").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Personal_data")
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(NewUserActivity.this, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(NewUserActivity.this, "PROFILO " + nome +" " + cognome + " CREATO!", Toast.LENGTH_SHORT).show();

                                                    finish(); //Torna sul login
                                                } else {
                                                    Toast.makeText(NewUserActivity.this, "Registrazione fallita", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                id = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());


                                Read_Patient_byDoctor read_Doctor = new Read_Patient_byDoctor(nome, cognome, id);

                                FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Admin").child("List_of_doctor").child("ID_" + index_doctor)
                                        .setValue(read_Doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {


                                            }
                                        });

                                FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Admin").child("List_of_doctor").child("Last ID")
                                        .setValue(index_doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {


                                            }
                                        });

                            }else{
                            FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Personal_data")
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(NewUserActivity.this, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(NewUserActivity.this, "PROFILO " + nome +" " + cognome + " CREATO!", Toast.LENGTH_SHORT).show();

                                                finish(); //Torna sul login
                                            } else {
                                                Toast.makeText(NewUserActivity.this, "Registrazione fallita", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            if (role == "Patient"){

                                Presciptions prescriptions = new Presciptions("0", "0", "0");
                                FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Prescriptions")
                                        .setValue(prescriptions).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });

                                id = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid());


                                Read_Patient_byDoctor read_patient_byDoctor = new Read_Patient_byDoctor(nome, cognome, id);

                                FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Doctor").child("Patient").child("ID_" + index_patient)
                                        .setValue(read_patient_byDoctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {


                                        }
                                });

                                FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Doctor").child("Patient").child("Last ID")
                                        .setValue(index_patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {


                                        }
                                });


                                FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Admin").child("List_of_patient").child("ID_" + index_patient)
                                        .setValue(read_patient_byDoctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {


                                        }
                                });

                                FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Admin").child("List_of_patient").child("Last ID")
                                        .setValue(index_patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {


                                        }
                                });

                            }
                        }

                        } else
                            Toast.makeText(NewUserActivity.this, "Registrazione fallita", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AdministratorActivity.class));
    }

}