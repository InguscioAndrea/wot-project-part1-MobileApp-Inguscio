package com.example.tabbedapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.tabbedapplication.databinding.ActivityMainBinding;
import com.example.tabbedapplication.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class Specific_tipsActivity extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    String path_child;
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);


    TextView title_tips;
    TextView content_tips;

    String testo_tips;


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        path_child = connection.getUser().getUid(); //RECUPERA L'ID DELLUTENTE CHE SI LOGGA

        String tips_scelto = getIntent().getExtras().getString("tips_scelto"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.specific_tips); //si specifica la pagina xml che si vuole stampare a video

        title_tips = (TextView)findViewById(R.id.id_title_tips);
        content_tips = (TextView)findViewById(R.id.id_content_tips);

//SI SETTA IL TITOLO CORRETTO A SECONDA DEL PULSANTE PREMUTO NELLA VIEW PRECEDENTE

        if (tips_scelto.equals("speed")){
            title_tips.setText("WALKING SPEED \nTIPS");

        } else if (tips_scelto.equals("hand_grip")){
            title_tips.setText("HAND GRIP \nTIPS");

        } else if (tips_scelto.equals("muscle_mass")){
            title_tips.setText("MUSCLE MASS \nTIPS");
        }

//SI SETTA IL CONTENUTO DELLA VARIABILE testo_tips A SECONDA DEL PULSANTE PREMUTO NELLA VIEW PRECEDENTE

        if (tips_scelto.equals("speed")){
            testo_tips =
                    "If the data indicates that your walking speed is below the threshold value, I advise you to:" +
                    "<br>" +
                    "<br>" +
                    "●  make sure you have proper " + "<b>" + "shoes" + "</b>" +". They must be: " + "<u>" + "flexible" + "</u>" + ", " + "<u>" + "light" + "</u>" + " and " + "<u>" + "comfortable" + "</u>" + ";" +
                    "<br>" +
                    "●  include warm-up and cool-down phases in " + "<b>" + "every" + "</b>" + " workout;" +
                    "<br>" +
                    "●  practice constant and regular physical activity;" +
                    "<br>" +
                    "●  maintain good posture (trunk, head, arms and legs);" +
                    "<br>" +
                    "<br>" +
                    "Also pay attention to: " +
                    "<br>" +
                    "●  don't arch your shoulders while standing;" +
                    "<br>" +
                    "●  keep your chin parallel to the ground;" +
                    "<br>" +
                    "●  abs firm but not overly tense;" +
                    "<br>" +
                    "●  elbows at 90° and close to the body;" +
                    "<br>" +
                    "●  arms will follow the opposite trend of the legs;" +
                    "<br>" +
                    "●  touch the ground with the heel and then the rest of the foot" +
                    "<br>" +
                    "<br>" +
                    "<b>" + "IMPORTANT NOTE" + "</b>" +
                    "<br>" +
                    "Don't rush to take long and fast steps, but build your time incrementally. A good starting point would be to practice the technique for 10 minutes a day." +
                    "<br>" +
                    "<i>" + "A good walker takes small rather than long steps!" + "</i>";

            content_tips.setText(Html.fromHtml(testo_tips));


        } else if (tips_scelto.equals("hand_grip")){
            testo_tips =
                    "If the data indicates that your hand grip is below the threshold value, it will be necessary to work and improve both the muscles of the hands and that of the forearms. Here are some exercises to do at home:" +
                            "<br>" +
                            "<br>" +
                            "●  squeeze the two sides of the hand gripper towards the center, overcoming the resistance of the spring. There are different intensities, it is recommended to start with a low load;" +
                            "<br>" +
                            "●  hold a balance wheel in your hand (you can also use dumbbells, sports elastics or a wooden stick) flex the wrist in a palmar direction and then return to a neutral position;" +
                            "<br>" +
                            "●  grab the dumbbell with one hand like a hammer grip and flex the forearms while maintaining the position of the wrist;" +
                            "<br>" +
                            "●  using a disc weight, squeeze it isometrically using only the fingertips, without using the palm;" +
                            "<br>" +
                            "<br>" +
                            "<b>" + "IMPORTANT NOTE" + "</b>" +
                            "<br>" +
                            "Never forget to use loads appropriate to your level. Patience and consistency will repay your sacrifices and allow you to achieve better results.";

            content_tips.setText(Html.fromHtml(testo_tips));

        } else if (tips_scelto.equals("muscle_mass")){
            testo_tips =
                            "Normally, aging leads to a reduction in the ability to synthesize muscle proteins, with a consequent impoverishment of muscle strength. Physical activity plays an " + "<b>" + "anti-aging" + "</b>" + " role on the body of the saropenic patient, by applying a stressful stimulus to the muscle, which induces an increase in its functionality and contractile efficiency. Combined with a good sporting activity, it is essential to associate an excellent diet. People at risk of sarcopenia often make two big " + "<b>" + "dietary mistakes" + "</b>" + ":" +
                            "<br>" +
                            "" +
                            "<br>" +
                            "●  consume few high biological value proteins;" +
                            "<br>" +
                            "●  has an insufficient diet in terms of energy;" +
                            "<br>" +
                            "<br>" +
                            "High biological value proteins are found above all in foods of animal origin (eggs, milk, meat and fish), which should make up " + "<b>" + "at least 1/3 of the total" + " </b>" + ", the rest of the calories go to carbohydrates and fats. Overall energy is very important, because if insufficient, it will lead the body to use amino acids as an energy source, nullifying what has been obtained from training." +                            "<br>" +
                            "<br>" +
                            "<b>" + "IMPORTANT NOTE" + "</b>" +
                            "<br>" +
                            "The combination of physical activity and proper nutrition has demonstrated excellent results in muscle recovery in older adults with sarcopenia.";

            content_tips.setText(Html.fromHtml(testo_tips));
        }


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