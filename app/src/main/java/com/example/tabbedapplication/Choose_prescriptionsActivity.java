package com.example.tabbedapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tabbedapplication.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class Choose_prescriptionsActivity extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    String path_child;
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);



    String dieta;
    String farmaci;
    String scheda_allenamento;
    String url;


    TextView txt_new_dieta;
    TextView txt_new_farmaci;
    TextView txt_new_scheda_allenamento;

    Button btn_dieta;
    Button btn_farmaci;
    Button btn_scheda_allenamento;
    Button button_back;


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_read_prescriptions);
        path_child = connection.getUser().getUid(); //RECUPERA L'ID DELLUTENTE CHE SI LOGGA

        dieta = getIntent().getExtras().getString("dieta");
        farmaci = getIntent().getExtras().getString("farmaci");
        scheda_allenamento = getIntent().getExtras().getString("scheda_allenamento");

        button_back = (Button) findViewById(R.id.button_back_9);
        btn_dieta = (Button) findViewById(R.id.btn_read_diet);
        btn_farmaci = (Button) findViewById(R.id.btn_read_medical_prescription);
        btn_scheda_allenamento = (Button) findViewById(R.id.btn_read_training_exercises);
        txt_new_dieta = (TextView) findViewById(R.id.id_txtNew_dieta);
        txt_new_farmaci = (TextView) findViewById(R.id.id_txtNew_farmaci);
        txt_new_scheda_allenamento = (TextView) findViewById(R.id.id_txtNew_scheda_allenamento);

        System.out.println("DIETA VALE "+ dieta);
        System.out.println("FARMACI VALE "+ farmaci);
        System.out.println("SCHEDA ALLENAMENTO VALE "+ scheda_allenamento);

        if (scheda_allenamento.equals("1")){
            txt_new_scheda_allenamento.setVisibility(View.VISIBLE);
        }

        if(dieta.equals("1")){
            txt_new_dieta.setVisibility(View.VISIBLE);
        }

        if(farmaci.equals("1")){
            txt_new_farmaci.setVisibility(View.VISIBLE);
        }



        button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PatientActivity.class));
            }
        });



        btn_dieta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    if (!dieta.equals("0")) {
                        Toast.makeText(Choose_prescriptionsActivity.this, "PDF OPENING IN PROGRESS...", Toast.LENGTH_SHORT).show();
                        // METODO CHE PRELEVA L'URL DI DOWNLOAD DEL FILE CARICATO E LO INSERISCE DENTRO LA STRINGA "url"
                        // Points to the root reference
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path_child).child("Food Plane");
                        StorageReference dateRef = storageRef.child("Food Plane.pdf");
                        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                url = String.valueOf(downloadUrl);
                                System.out.println("la stringa di download è: \n " + downloadUrl);

                                // METODO CHE STAMPA SULL'APP ANDROID IL FILE .PDF
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse(url), "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Intent newIntent = Intent.createChooser(intent, "Open File");
                                try {
                                    startActivity(newIntent);
                                } catch (ActivityNotFoundException e) {
                                    // Instruct the user to install a PDF reader here, or something
                                    Toast.makeText(Choose_prescriptionsActivity.this, "⚠️ERROR! INSTALL A PDF READER ⚠️", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
                                }

                                //Presciptions prescriptions = new Presciptions("0", farmaci, scheda_allenamento );
                                FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Patient").child(path_child).child("Prescriptions").child("dieta")
                                        .setValue("2").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                });

                            }
                        });
                    } else if(dieta.equals("0")){
                        Toast.makeText(Choose_prescriptionsActivity.this, "⚠️THERE ARE NO PDF FILES UPLOADED", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    Toast.makeText(Choose_prescriptionsActivity.this, "⚠️THERE ARE NO PDF FILES UPLOADED", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_farmaci.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!farmaci.equals("0")){
                Toast.makeText(Choose_prescriptionsActivity.this, "PDF OPENING IN PROGRESS...", Toast.LENGTH_SHORT).show();
                // METODO CHE PRELEVA L'URL DI DOWNLOAD DEL FILE CARICATO E LO INSERISCE DENTRO LA STRINGA "url"
                // Points to the root reference
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path_child).child("Medical Prescription");
                StorageReference dateRef = storageRef.child("Medical Prescription.pdf");
                dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        url = String.valueOf(downloadUrl);
                        System.out.println("la stringa di download è: \n " + downloadUrl);

                        // METODO CHE STAMPA SULL'APP ANDROID IL FILE .PDF
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(url), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Intent newIntent = Intent.createChooser(intent, "Open File");
                        try {
                            startActivity(newIntent);
                        } catch (ActivityNotFoundException e) {
                            // Instruct the user to install a PDF reader here, or something
                            Toast.makeText(Choose_prescriptionsActivity.this, "⚠️ERROR! INSTALL A PDF READER ⚠️", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
                        }
                        FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Patient").child(path_child).child("Prescriptions").child("farmaci")
                                .setValue("2").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                    }
                });

            } else if(farmaci.equals("0")){
                Toast.makeText(Choose_prescriptionsActivity.this, "⚠️THERE ARE NO PDF FILES UPLOADED", Toast.LENGTH_SHORT).show();

            }


            }
        });

        btn_scheda_allenamento.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!scheda_allenamento.equals("0")){
                Toast.makeText(Choose_prescriptionsActivity.this, "PDF OPENING IN PROGRESS...", Toast.LENGTH_SHORT).show();
                // METODO CHE PRELEVA L'URL DI DOWNLOAD DEL FILE CARICATO E LO INSERISCE DENTRO LA STRINGA "url"
                // Points to the root reference
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(path_child).child("Training Exercises");
                StorageReference dateRef = storageRef.child("Training Exercises.pdf");
                dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri downloadUrl)
                    {
                        url = String.valueOf(downloadUrl);
                        System.out.println("la stringa di download è: \n " + downloadUrl);

                        // METODO CHE STAMPA SULL'APP ANDROID IL FILE .PDF
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(url), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Intent newIntent = Intent.createChooser(intent, "Open File");
                        try {
                            startActivity(newIntent);
                        } catch (ActivityNotFoundException e) {
                            // Instruct the user to install a PDF reader here, or something
                            Toast.makeText(Choose_prescriptionsActivity.this, "⚠️ERROR! INSTALL A PDF READER ⚠️", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext() , DoctorActivity.class));

                        }

                        FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Patient").child(path_child).child("Prescriptions").child("scheda_allenamento")
                                .setValue("2").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                        }});
                    }
                });

        } else if(scheda_allenamento.equals("0")){
            Toast.makeText(Choose_prescriptionsActivity.this, "⚠️THERE ARE NO PDF FILES UPLOADED", Toast.LENGTH_SHORT).show();

        }


            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), PatientActivity.class));
    }

}