package com.example.tabbedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.tabbedapplication.databinding.ActivityMainBinding;
import com.example.tabbedapplication.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class Choose_tipsActivity extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    String path_child;
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);


    Button btn_speed_tips;
    Button btn_hand_grip_tips;
    Button btn_muscle_mass_tips;
    TextView icona_help;
    TextView text_help;

    String tips_scelto;


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        path_child = connection.getUser().getUid(); //RECUPERA L'ID DELLUTENTE CHE SI LOGGA

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.choose_tips); //si specifica la pagina xml che si vuole stampare a video

        btn_speed_tips = (Button)findViewById(R.id.btn_speed_tips);
        btn_hand_grip_tips = (Button) findViewById(R.id.btn_hand_grip_tips);
        btn_muscle_mass_tips = (Button) findViewById(R.id.btn_muscle_mass_tips);
        icona_help = (TextView)findViewById(R.id.id_icona_aiuto);
        text_help = (TextView)findViewById(R.id.id_text_help);


        btn_speed_tips.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                tips_scelto = "speed";

                Intent new_page = new Intent(getApplicationContext(), Specific_tipsActivity.class);
                new_page.putExtra("tips_scelto", tips_scelto);
                startActivity(new_page);

            }
        });

        btn_hand_grip_tips.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                tips_scelto = "hand_grip";

                Intent new_page = new Intent(getApplicationContext(), Specific_tipsActivity.class);
                new_page.putExtra("tips_scelto", tips_scelto);
                startActivity(new_page);


            }
        });

        btn_muscle_mass_tips.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                tips_scelto = "muscle_mass";

                Intent new_page = new Intent(getApplicationContext(), Specific_tipsActivity.class);
                new_page.putExtra("tips_scelto", tips_scelto);
                startActivity(new_page);

            }
        });

        text_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Hospital_informationActivity.class));

            }
        });

        icona_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Hospital_informationActivity.class));

            }
        });


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }




    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), PatientActivity.class));
    }

}