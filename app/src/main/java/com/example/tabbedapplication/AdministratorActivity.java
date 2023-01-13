package com.example.tabbedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.tabbedapplication.databinding.ActivityMainBinding;
import com.example.tabbedapplication.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
public class AdministratorActivity extends AppCompatActivity {


    Button button_NewUser;
    Button button_ListUser;
    Button btn_logout;



    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.administrator_page); //si specifica la pagina xml che si vuole stampare a video

        button_NewUser = (Button) findViewById(R.id.btn_NewUsers);
        button_ListUser = (Button)findViewById(R.id.btn_ViewUsers);
        btn_logout = (Button)findViewById(R.id.btn_logout_Admin);

        button_NewUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext() , NewUserActivity.class));

            }
        });

        button_ListUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext() , Type_of_usersActivity.class));

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setContentView(R.layout.loading);
                Toast.makeText(AdministratorActivity.this, "LOG-OUT IN PROGRESS... PLEASE WAIT", Toast.LENGTH_SHORT).show();

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
        });



        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        setContentView(R.layout.loading);
        Toast.makeText(AdministratorActivity.this, "LOG-OUT IN PROGRESS... PLEASE WAIT", Toast.LENGTH_SHORT).show();

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



