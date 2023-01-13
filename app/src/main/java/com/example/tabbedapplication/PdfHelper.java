package com.example.tabbedapplication;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tabbedapplication.ui.Entities.Presciptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PdfHelper extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);


    TextView upload_TextView;
    Button continuebutton;
    Button button_back;
    Button riempimento;
    Uri imageuri = null;
    String name_pdf;
    String id_paziente;
    String id_dottore;
    String cifre_id;
    RadioButton dieta;
    RadioButton farmaci;
    RadioButton fitness;
    String radio_scelto;
    String new_pdf_dieta;
    String new_pdf_farmaci;
    String new_pdf_scheda_allenamento;
    int click_upload = 0; // VARIABILE BOOLEANA CHE ASSOCIA "1" SE VIENE CLICCATA LA SEZIONE DI "Upload" E "0" ALTRIMENTI



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        id_paziente = getIntent().getExtras().getString("id_paziente"); //CODICE PER RICEVERE IL "id_paziente" DA UN ALTRO INTENT
        id_dottore = getIntent().getExtras().getString("id_dottore"); //CODICE PER RICEVERE IL "id_dottore" DA UN ALTRO INTENT
        cifre_id = getIntent().getExtras().getString("cifre_id"); //CODICE PER RICEVERE IL "cifre_id" DA UN ALTRO INTENT



        DatabaseReference myRef = database.getReference("Users").child("Patient").child(id_paziente).child("Prescriptions").child("dieta");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                new_pdf_dieta = String.valueOf(dataSnapshot.getValue(String.class));
                System.out.println("Dieta vale " + new_pdf_dieta);

                DatabaseReference myRef = database.getReference("Users").child("Patient").child(id_paziente).child("Prescriptions").child("farmaci");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        new_pdf_farmaci = String.valueOf(dataSnapshot.getValue(String.class));
                        System.out.println("farmaci vale " + new_pdf_farmaci);

                        DatabaseReference myRef = database.getReference("Users").child("Patient").child(id_paziente).child("Prescriptions").child("scheda_allenamento");
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                new_pdf_scheda_allenamento = String.valueOf(dataSnapshot.getValue(String.class));
                                System.out.println("scheda allenamento vale " + new_pdf_scheda_allenamento);
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_page);
        upload_TextView = findViewById(R.id.id_upload);
        button_back = (Button) findViewById(R.id.button_back_4);
        continuebutton = (Button) findViewById(R.id.buttonediprova);
        dieta = (RadioButton) findViewById(R.id.id_radio_dieta);
        farmaci = (RadioButton) findViewById(R.id.id_radio_farmaci);
        fitness = (RadioButton) findViewById(R.id.id_radio_fitness);
        riempimento = (Button) findViewById(R.id.riempimento);

        riempimento.setVisibility(View.INVISIBLE);

        continuebutton.setOnClickListener(view ->{
            if (click_upload == 1) {
                upload_file();

            }else{
                Toast.makeText(PdfHelper.this, "⚠️UPLOAD A FILE! ⚠️", Toast.LENGTH_SHORT).show();
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {

                name_pdf = radio_scelto;
                Toast.makeText(PdfHelper.this, "FILE RENAMED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
            }
        });

        // SE SI CLICCA SUL TESTO "upload" APPARIRA' IL FILE CHOOSER PER SELEZIONARE IL PDF
        upload_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_upload = 1;
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("application/pdf");
                startActivityForResult(galleryIntent, 1);
                Toast.makeText(PdfHelper.this, "SELECT A PDF FILE", Toast.LENGTH_SHORT).show();
                CountDownTimer countDownTimer = new CountDownTimer(400, 1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        System.out.println("Wait " + millisUntilFinished/1000);
                    }
                    @Override
                    public void onFinish() {
                        upload_TextView.setText("✅ PDF chosen correctly! ✅");
                        upload_TextView.setTextColor(Color.parseColor("#008000"));
                    }
                }.start();


            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            imageuri = data.getData();

        }
    }


    private void upload_file() {


        if (dieta.isChecked()){
            radio_scelto = "Food Plane";
            new_pdf_dieta = "1";
            name_pdf = radio_scelto;
        } else if (farmaci.isChecked()){
            radio_scelto = "Medical Prescription";
            new_pdf_farmaci = "1";
            name_pdf = radio_scelto;
        } else if (fitness.isChecked()){
            radio_scelto = "Training Exercises";
            new_pdf_scheda_allenamento = "1";
            name_pdf = radio_scelto;
        }

        // Here we are uploading the pdf in firebase storage with the name of current time
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference filepath = storageReference.child(id_paziente).child(radio_scelto).child(name_pdf + "." + "pdf");
        Toast.makeText(PdfHelper.this, "YOUR FILE ->   " +filepath.getName() + "\n IT'S UPLOADED", Toast.LENGTH_SHORT).show();

        filepath.putFile(imageuri).continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // After uploading is done it progress
                    // dialog box will be dismissed

                    Uri uri = task.getResult();
                    String myurl;
                    myurl = uri.toString();
                    Toast.makeText(PdfHelper.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                    /////////////////////////////////////////////////////////////
                    Presciptions prescriptions = new Presciptions(new_pdf_dieta, new_pdf_farmaci, new_pdf_scheda_allenamento);
                    FirebaseDatabase.getInstance(STOREF).getReference("Users").child("Patient").child(id_paziente).child("Prescriptions")
                            .setValue(prescriptions).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                    ////////////////////////////////////////////////////////////

                } else {

                    Toast.makeText(PdfHelper.this, "Uploaded Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        startActivity(new Intent(getApplicationContext() , DoctorActivity.class));


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
    }

}