package com.example.tabbedapplication;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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


public class NewMeasureActivity extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    String path_child;
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);

    private ActivityMainBinding binding;

    String current_time;
    String patient_name;
    String var_id_op;
    String var_hand_grip_min;
    String var_hand_grip_max;
    String var_muscle_mass;
    String var_bone_mass;
    String var_fat_mass;
    String var_weight_measure;
    String var_step_frequency;
    String var_step_count;
    String var_speed;

    Double value_speed;
    Double value_muscle_mass;
    Double value_hand_grip;
    String sesso_paziente = "";

    String CHANNEL_ID = "20";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sesso_paziente = getIntent().getExtras().getString("sesso_paziente");


        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.new_measure_page); //si specifica la pagina xml che si vuole stampare a video


        TextView NewMeasureTitle;
        TextView date_of_measure;
        TextView HandGripTitle;
        TextView BIATitle;
        TextView GaitSpeedTitle;
        TextView id_op;
        TextView handgrip_min;
        TextView handgrip_max;
        TextView weight_measure;
        TextView muscle_mass;
        TextView fat_mass;
        TextView bone_mass;
        TextView step_count;
        TextView step_frequency;
        TextView speed;
        Button button_back;

        NewMeasureTitle = (TextView)findViewById(R.id.id_title_NewMeasure);
        date_of_measure = (TextView)findViewById(R.id.id_current_time);
        id_op = (TextView)findViewById(R.id.id_operation);
        handgrip_min = (TextView)findViewById(R.id.id_hand_grip_min);
        handgrip_max = (TextView)findViewById(R.id.id_hand_grip_max);
        weight_measure = (TextView)findViewById(R.id.id_WeightMeasure);
        muscle_mass = (TextView)findViewById(R.id.id_MuscleMass);
        fat_mass = (TextView)findViewById(R.id.id_FatMass);
        bone_mass = (TextView)findViewById(R.id.id_BoneMass);
        step_count = (TextView)findViewById(R.id.id_StepCount);
        step_frequency = (TextView)findViewById(R.id.id_StepFrequency);
        speed = (TextView)findViewById(R.id.id_Speed);
        button_back = (Button)findViewById(R.id.button_back);

        button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), PatientActivity.class));

            }
        });

        path_child = connection.getUser().getUid(); //RECUPERA L'ID DELLUTENTE CHE SI LOGGA
        DatabaseReference myRef = database.getReference("Users").child("Patient").child(path_child).child("Personal_data").child("cognome");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patient_name = String.valueOf(dataSnapshot.getValue(String.class));
                NewMeasureTitle.setText("Summary of the analysis performed by the patient: " + (patient_name.toUpperCase()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        DatabaseReference myRef2 = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("current_time");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_time = String.valueOf(dataSnapshot.getValue(String.class));
                String stringa_bold = "<b>" + "Date: " + "</b> " + current_time;
                date_of_measure.setText(Html.fromHtml(stringa_bold));


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefId = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("id_op");
        myRefId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_id_op = dataSnapshot.getValue(String.class);
                String stringa_bold = "<b>" + "Id operation: " + "</b> " + var_id_op;
                id_op.setText(Html.fromHtml(stringa_bold));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefHGMin = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("handgrip_min");
        myRefHGMin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_hand_grip_min = dataSnapshot.getValue(String.class);
                String stringa_bold = "<b>" + "Min: " + "</b> " + var_hand_grip_min;
                handgrip_min.setText(Html.fromHtml(stringa_bold));


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefHGMax = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("handgrip_max");
        myRefHGMax.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_hand_grip_max = dataSnapshot.getValue(String.class);
                String stringa_bold = "<b>" + "Max: " + "</b> " + var_hand_grip_max;
                handgrip_max.setText(Html.fromHtml(stringa_bold));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefWeightMeasure = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("weight_measured");
        myRefWeightMeasure.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_weight_measure = dataSnapshot.getValue(String.class);
                String stringa_bold = "<b>" + "Weight: kg" + "</b> " + var_weight_measure;
                weight_measure.setText(Html.fromHtml(stringa_bold));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefMuscleMass = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("muscle_mass");
        myRefMuscleMass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_muscle_mass = dataSnapshot.getValue(String.class);
                String stringa_bold = "<b>" + "Muscle mass: kg" + "</b> " + var_muscle_mass;
                muscle_mass.setText(Html.fromHtml(stringa_bold));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefBoneMass = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("bone_mass");
        myRefBoneMass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_bone_mass = dataSnapshot.getValue(String.class);
                String stringa_bold = "<b>" + "Bone mass: kg" + "</b> " + var_bone_mass;
                bone_mass.setText(Html.fromHtml(stringa_bold));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefFatMass = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("fat_mass");
        myRefFatMass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_fat_mass = dataSnapshot.getValue(String.class);
                String stringa_bold = "<b>" + "Fat mass: kg" + "</b> " + var_fat_mass;
                fat_mass.setText(Html.fromHtml(stringa_bold));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefStepCount = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("step_count");
        myRefStepCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_step_count = dataSnapshot.getValue(String.class);
                String stringa_bold = "<b>" + "Step count: " + "</b> " + var_step_count;
                step_count.setText(Html.fromHtml(stringa_bold));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefStepFrequency = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("step_frequency");
        myRefStepFrequency.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_step_frequency = dataSnapshot.getValue(String.class);
                String stringa_bold = "<b>" + "Step frequency: " + "</b> " + var_step_frequency;
                step_frequency.setText(Html.fromHtml(stringa_bold));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefSpeed = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("speed");
        myRefSpeed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_speed = dataSnapshot.getValue(String.class);
                String stringa_bold = "<b>" + "Speed: " + "</b> " + var_speed;
                speed.setText(Html.fromHtml(stringa_bold));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        DatabaseReference myRefNotification_speed = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("speed");
        myRefNotification_speed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_speed = dataSnapshot.getValue(String.class);
                value_speed = Double.parseDouble(var_speed);

                DatabaseReference myRefNotification_hand_grip = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("handgrip_max");
                myRefNotification_hand_grip.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        var_hand_grip_max = dataSnapshot.getValue(String.class);
                        value_hand_grip = Double.parseDouble(var_hand_grip_max);

                        DatabaseReference myRefNotification_speed = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("Last Measure").child("muscle_mass");
                        myRefNotification_speed.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                var_muscle_mass = dataSnapshot.getValue(String.class);
                                value_muscle_mass = Double.parseDouble(var_muscle_mass);

                                Notification(value_speed, value_hand_grip, value_muscle_mass);

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




        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }

    private void Notification(Double Speed, Double Hand_grip, Double Muscle_mass) {

        String text_notification = "loading...";

        if (sesso_paziente.equals("Male")){
            if ((Speed < 0.8 && Hand_grip > 26.0 && Muscle_mass > 7.9) || (Speed < 0.8 && Hand_grip < 26.0 && Muscle_mass > 7.9) || (Speed > 0.8 && Hand_grip > 26.0 && Muscle_mass > 7.9) || (Speed > 0.8 && Hand_grip < 26.0 && Muscle_mass > 7.9) || (Speed > 0.8 && Hand_grip > 26.0 && Muscle_mass < 7.9)){
                text_notification = "Your results are excellent!\n" +
                        "Great " +String.valueOf(Character.toChars(0x1F60D)) + ", you are in excellent shape. Remember to follow the tips provided and keep it up!\n\nClick here for the tips section!";
            } else if ((Speed < 0.8 && Hand_grip < 26.0 && Muscle_mass < 7.9)){
                text_notification = "Bad news!" + String.valueOf(Character.toChars(0x1F915)) + "\n" +
                        "We found too low values of walking speed, hand grip and muscle mass. View the advice provided, carefully follow the prescriptions provided by the medical staff and contact the clinic if necessary.\n\nClick here for the tips section!";
            } else if ((Speed < 0.8 && Hand_grip > 26.0 && Muscle_mass < 7.9)){
                text_notification = "To improve!" + String.valueOf(Character.toChars(0x1F615)) + "\n" +
                        "We found too low values of walking speed and muscle mass. We advise you to carry out the exercises assigned by the medical staff every day and to carefully follow the personalized diet.\n\nClick here for the tips section!";
            } else if ((Speed > 0.8 && Hand_grip < 26.0 && Muscle_mass < 7.9)){
                text_notification = "To improve!" + String.valueOf(Character.toChars(0x1F615)) + "\n" +
                        "We found too low values of hand grip and muscle mass. We advise you to carry out the exercises assigned by the medical staff every day and to carefully follow the personalized diet.\n\nClick here for the tips section!";
            }
        } else if (sesso_paziente.equals("Female")){
            if ((Speed < 0.8 && Hand_grip > 18.0 && Muscle_mass > 6.0) || (Speed < 0.8 && Hand_grip < 18.0 && Muscle_mass > 6.0) || (Speed > 0.8 && Hand_grip > 18.0 && Muscle_mass > 6.0) || (Speed > 0.8 && Hand_grip < 18.0 && Muscle_mass > 6.0) || (Speed > 0.8 && Hand_grip > 18.0 && Muscle_mass < 6.0)){
                text_notification = "Your results are excellent!\n" +
                        "Great " +String.valueOf(Character.toChars(0x1F60D)) + ", you are in excellent shape. Remember to follow the tips provided and keep it up!\n\nClick here for the tips section!";
            } else if ((Speed < 0.8 && Hand_grip < 18.0 && Muscle_mass < 6.0)){
                text_notification = "Bad news!" + String.valueOf(Character.toChars(0x1F915)) + "\n" +
                        "We found too low values of walking speed, hand grip and muscle mass. View the advice provided, carefully follow the prescriptions provided by the medical staff and contact the clinic if necessary.\n\nClick here for the tips section!";
            } else if ((Speed < 0.8 && Hand_grip > 18.0 && Muscle_mass < 6.0)){
                text_notification = "To improve!" + String.valueOf(Character.toChars(0x1F615)) + "\n" +
                        "We found too low values of walking speed and muscle mass. We advise you to carry out the exercises assigned by the medical staff every day and to carefully follow the personalized diet.\n\nClick here for the tips section!";
            } else if ((Speed > 0.8 && Hand_grip < 18.0 && Muscle_mass < 6.0)){
                text_notification = "To improve!" + String.valueOf(Character.toChars(0x1F615)) + "\n" +
                        "We found too low values of hand grip and muscle mass. We advise you to carry out the exercises assigned by the medical staff every day and to carefully follow the personalized diet.\n\nClick here for the tips section!";
            }
        }



        Intent intent = new Intent(this, Choose_tipsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_icona_sarcapp_senza_fulmine)
                .setContentTitle("Measurement result")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text_notification))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), PatientActivity.class));
    }

}
