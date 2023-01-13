package com.example.tabbedapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tabbedapplication.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;


public class Summary_resultActivity extends AppCompatActivity {

    private static final String STOREF = "https://sarc-app-default-rtdb.europe-west1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    String path_child;
    private Connection connection = new Connection();
    FirebaseDatabase database = FirebaseDatabase.getInstance(STOREF);

    private ActivityMainBinding binding;

    int bool_view;
    String dieta, farmaci, scheda_allenamento;

    Double ref_HG;
    Double ref_BIA;
    Double ref_Speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String sesso_paziente = getIntent().getExtras().getString("sesso_paziente"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT


        String date_misura = getIntent().getExtras().getString("data_misura"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT
        Float HG_result = getIntent().getExtras().getFloat("HG_result");
        Double BIA_result = getIntent().getExtras().getDouble("BIA_result");
        Float Speed_result = getIntent().getExtras().getFloat("Speed_result");

        Double HG_precedente = getIntent().getExtras().getDouble("HG_precedente");
        Double BIA_precedente = Double.parseDouble(getIntent().getExtras().getString("BIA_precedente"));
        Double Speed_precedente = getIntent().getExtras().getDouble("Speed_precedente");

        dieta = getIntent().getExtras().getString("dieta");
        farmaci = getIntent().getExtras().getString("farmaci");
        scheda_allenamento = getIntent().getExtras().getString("scheda_allenamento");
        bool_view = getIntent().getExtras().getInt("bool_view");

        path_child = getIntent().getExtras().getString("user_id");//RECUPERA L'ID DELLUTENTE CHE SI LOGGA

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.riepilogo_result); //si specifica la pagina xml che si vuole stampare a video

        TextView date_of_summary;
        TextView result_HG;
        TextView result_BIA;
        TextView result_Speed;
        TextView goal_HG_view;
        TextView goal_BIA_view;
        TextView goal_Speed_view;
        TextView past_HG_view;
        TextView past_BIA_view;
        TextView past_Speed_view;
        TextView consult_textView;
        Button button_back;
        ImageView freccia_giu;


        date_of_summary = (TextView) findViewById(R.id.data_of_summary);
        result_HG = (TextView) findViewById(R.id.result_HG);
        result_BIA = (TextView) findViewById(R.id.result_BIA);
        result_Speed = (TextView) findViewById(R.id.result_Speed);
        goal_HG_view = (TextView) findViewById(R.id.goal_HG);
        goal_BIA_view = (TextView) findViewById(R.id.goal_BIA);
        goal_Speed_view = (TextView) findViewById(R.id.goal_speed);
        past_HG_view = (TextView) findViewById(R.id.past_HG);
        past_BIA_view = (TextView) findViewById(R.id.past_BIA);
        past_Speed_view = (TextView) findViewById(R.id.past_speed);
        consult_textView = (TextView) findViewById(R.id.consult_textview);
        button_back = (Button) findViewById(R.id.button_back_10);
        freccia_giu = (ImageView) findViewById(R.id.id_freccia_giu);

//NASCONDE IL TESTO ALL'INTERNO DELLA TEXTVIEW "consult_textView" SE L'UTENTE E' "DOTTORE"
        if (bool_view == 1){
            consult_textView.setVisibility(View.INVISIBLE);
            freccia_giu.setVisibility(View.INVISIBLE);
        }


// PARAMETRI DI RIFERIMENTO (GOAL) PER LE 3 GRANDEZZE MISURATE
        if(sesso_paziente.equals("Male")){
            ref_HG = 26.0;
            ref_BIA = 7.9;
            ref_Speed = 0.800;
        } else if (sesso_paziente.equals("Female")){
            ref_HG = 18.0;
            ref_BIA = 6.0;
            ref_Speed = 0.800;
        }

//PERCENTUALE IN ECCESSO/DIFETTO RISPETTO AI PARAMETRI DI RIFERIMENTO
        int goal_HG;
        int goal_BIA;
        int goal_Speed;

//PERCENTUALE IN ECCESSO/DIFETTO RISPETTO ALLA MISURA PRECEDENTE
        int past_HG;
        int past_BIA;
        int past_Speed;



// FUNZIONE CHE CALCOLA LA DIFFERENZA IN PERCENTUALE TRA VALORE MISURATO E VALORE DI RIFERIMENTO

        //SEZIONE HAND GRIP
        double x;
        x = (HG_result * 100)/ref_HG;
        double decimal_goal_HG = 100 - x;
        goal_HG = (int) Math.round(decimal_goal_HG);

        //SEZIONE BIA
        double y;
        y = (BIA_result * 100)/ref_BIA;
        double decimal_goal_BIA = 100 - y;
        goal_BIA = (int) Math.round(decimal_goal_BIA);

        //SEZIONE SPEED
        double z;
        z = (Speed_result * 100)/ref_Speed;
        double decimal_goal_Speed = 100 - z;
        goal_Speed = (int) Math.round(decimal_goal_Speed);

        System.out.println(HG_result + "\n"+ BIA_result + "\n" + Speed_result);
        System.out.println("100 - "+y);

        // SEZIONE DI STAMPA PERCENTUALE DI "HAND GRIP GOAL"
        if (goal_HG > 0){
         goal_HG_view.setText(ref_HG +" (-" +goal_HG+ "%)");
            goal_HG_view.setTextColor(Color.parseColor("#ff0000"));
        } else if (goal_HG < 0){
            goal_HG_view.setText(ref_HG+ " (+" +(- goal_HG)+ "%)");
            goal_HG_view.setTextColor(Color.parseColor("#008000"));
        } else if (goal_HG == 0){
            goal_HG_view.setText("Perfect");
            goal_HG_view.setTextColor(Color.parseColor("#959595"));
        }

        // SEZIONE DI STAMPA PERCENTUALE DI "SKELETAL MUSCLE INDEX GOAL"
        if (goal_BIA > 0){
            goal_BIA_view.setText(ref_BIA+ " (-" +goal_BIA+ "%)");
            goal_BIA_view.setTextColor(Color.parseColor("#ff0000"));
        } else if (goal_BIA < 0){
            goal_BIA_view.setText(ref_BIA+" (+" +(- goal_BIA)+ "%)");
            goal_BIA_view.setTextColor(Color.parseColor("#008000"));
        } else if (goal_BIA == 0){
            goal_BIA_view.setText("Perfect");
            goal_BIA_view.setTextColor(Color.parseColor("#959595"));
        }

        // SEZIONE DI STAMPA PERCENTUALE DI "SPEED GOAL"
        if (goal_Speed > 0){
            goal_Speed_view.setText(ref_Speed+ " (-" +goal_Speed+ "%)");
            goal_Speed_view.setTextColor(Color.parseColor("#ff0000"));
        } else if (goal_Speed < 0){
            goal_Speed_view.setText(ref_Speed+ " (+" +(- goal_Speed)+ "%)");
            goal_Speed_view.setTextColor(Color.parseColor("#008000"));
        } else if (goal_Speed == 0){
            goal_Speed_view.setText("Perfect");
            goal_Speed_view.setTextColor(Color.parseColor("#959595"));
        }


// FUNZIONE CHE CALCOLA LA DIFFERENZA IN PERCENTUALE TRA IL VALORE DELLA MISURA ATTUALE E LA PRECEDENTE

        //SEZIONE HAND GRIP
        double a;
        a = (HG_precedente * 100)/HG_result;
        double decimal_past_HG = 100 - a;
        //ARROTONDIAMO A 2 CIFRE DECIMALI IL VALORE DI HG PRECEDENTE
        DecimalFormat round_to_three_decimal_HG = new DecimalFormat("#.##");
        String temp_HG = String.valueOf(round_to_three_decimal_HG.format(HG_precedente));

        String temp_HG_1;
        String temp_HG_2;

        if(temp_HG.substring(1,2).equals(",")){
            temp_HG_1 = temp_HG.substring(0, 1);
            try {
                temp_HG_2 = temp_HG.substring(2, 3);
            } catch (Exception e) {
                temp_HG_2 = "0";
            }
        }
        else{
            temp_HG_1 = temp_HG.substring(0, 2);
            try {
                temp_HG_2 = temp_HG.substring(3, 4);
            } catch (Exception e) {
                temp_HG_2 = "0";
            }
        }

        String HG_string = temp_HG_1 + "." + temp_HG_2;
        HG_precedente = Double.parseDouble(HG_string);
        //ARROTONDIAMO AD 1 NUMERO INTERO (SENZA DECIMALI) IL VALORE IN PERCENTUALE
        past_HG = (int) Math.round(decimal_past_HG);

        //SEZIONE BIA
        double b;
        b = (BIA_precedente * 100)/BIA_result;
        double decimal_past_BIA = 100 - b;
        //ARROTONDIAMO A 2 CIFRE DECIMALI IL VALORE DI SMI PRECEDENTE
        DecimalFormat round_to_three_decimal_SMI = new DecimalFormat("#.##");
        String temp_SMI = String.valueOf(round_to_three_decimal_SMI.format(BIA_precedente));
        System.out.println("TEMP SMI VALE....." + temp_SMI);

        String temp_SMI_1;
        String temp_SMI_2;

        if(temp_SMI.substring(1,2).equals(",")){
            temp_SMI_1 = temp_SMI.substring(0, 1);
            try {
                temp_SMI_2 = temp_SMI.substring(2, 4);
            } catch (Exception e) {
                temp_SMI_2 = "0";
            }
        }
        else{
            temp_SMI_1 = temp_SMI.substring(0, 2);
            try {
                temp_SMI_2 = temp_SMI.substring(3, 4);
            } catch (Exception e) {
                temp_SMI_2 = "0";
            }
        }

        String SMI_string = temp_SMI_1 + "." + temp_SMI_2;
        BIA_precedente = Double.parseDouble(SMI_string);
        //ARROTONDIAMO AD 1 NUMERO INTERO (SENZA DECIMALI) IL VALORE IN PERCENTUALE DELL'SMI
        past_BIA = (int) Math.round(decimal_past_BIA);


        //SEZIONE SPEED
        double c;
        c = (Speed_precedente * 100)/Speed_result;
        double decimal_past_Speed = 100 - c;
        //ARROTONDIAMO A 2 CIFRE DECIMALI IL VALORE DI SPEED PRECEDENTE
        DecimalFormat round_to_three_decimal_SPEED = new DecimalFormat("#.##");
        String temp_SPEED = String.valueOf(round_to_three_decimal_SPEED.format(Speed_precedente));
        String temp_SPEED_1 = temp_SPEED.substring(0, 1);
        String temp_SPEED_2;
        try {
            temp_SPEED_2 = temp_SPEED.substring(2, 4);
        } catch (Exception e) {
            temp_SPEED_2 = "0";
        }
        String SPEED_string = temp_SPEED_1 + "." + temp_SPEED_2;
        Speed_precedente = Double.parseDouble(SPEED_string);
        //ARROTONDIAMO AD 1 NUMERO INTERO (SENZA DECIMALI) IL VALORE IN PERCENTUALE DELLO SPEED
        past_Speed = (int) Math.round(decimal_past_Speed);


        // SEZIONE DI STAMPA PERCENTUALE DI "HAND GRIP PRECEDENTE"
        if (past_HG < 0){
            past_HG_view.setText(HG_precedente+ " (" +past_HG+ "%)");
            past_HG_view.setTextColor(Color.parseColor("#ff0000"));
        } else if (past_HG > 0){
            past_HG_view.setText(HG_precedente+ " (+" +(past_HG)+ "%)");
            past_HG_view.setTextColor(Color.parseColor("#008000"));
        } else if (past_HG == 0){
            past_HG_view.setText("no change");
            past_HG_view.setTextColor(Color.parseColor("#959595"));
        }

        // SEZIONE DI STAMPA PERCENTUALE DI "SKELETAL MUSCLE INDEX PRECEDENTE"
        if (past_BIA < 0){
            past_BIA_view.setText(BIA_precedente+ " (" +past_BIA+ "%)");
            past_BIA_view.setTextColor(Color.parseColor("#ff0000"));
        } else if (past_BIA > 0){
            past_BIA_view.setText(BIA_precedente+ " (+" +(past_BIA)+ "%)");
            past_BIA_view.setTextColor(Color.parseColor("#008000"));
        } else if (past_BIA == 0){
            past_BIA_view.setText("no change");
            past_BIA_view.setTextColor(Color.parseColor("#959595"));
        }

        // SEZIONE DI STAMPA PERCENTUALE DI "SPEED PRECEDENTE"
        if (past_Speed < 0){
            past_Speed_view.setText(Speed_precedente+ " (" +past_Speed+ "%)");
            past_Speed_view.setTextColor(Color.parseColor("#ff0000"));
        } else if (past_Speed > 0){
            past_Speed_view.setText(Speed_precedente+ " (+" +(past_Speed)+ "%)");
            past_Speed_view.setTextColor(Color.parseColor("#008000"));
        } else if (past_Speed == 0){
            past_Speed_view.setText("no change");
            past_Speed_view.setTextColor(Color.parseColor("#959595"));
        }


        date_of_summary.setText("Measure of " +date_misura);
        result_HG.setText(String.valueOf(HG_result));
        result_BIA.setText(String.valueOf(BIA_result));
        result_Speed.setText(String.valueOf(Speed_result));


        button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (bool_view == 1) {
                    startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
                } else if (bool_view == 0) {
                    // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                    /////////////////////////////////////////
                    Intent new_page = new Intent(getApplicationContext(), PatientActivity.class);
                    new_page.putExtra("dieta", dieta);
                    new_page.putExtra("farmaci", farmaci);
                    new_page.putExtra("scheda_allenamento", scheda_allenamento);
                    /////////////////////////////////////////
                    startActivity(new_page);
                }


            }
        });

// SE SI E' PAZIENTI E SI CLICCA SUL TESTO IN BASSO, SI APRIRA' IL RIEPILOGO PRESCRIZIONI
        if (bool_view == 0) {
            consult_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                    /////////////////////////////////////////
                    Intent new_page = new Intent(getApplicationContext(), Choose_prescriptionsActivity.class);
                    new_page.putExtra("dieta", dieta);
                    new_page.putExtra("farmaci", farmaci);
                    new_page.putExtra("scheda_allenamento", scheda_allenamento);
                    /////////////////////////////////////////
                    startActivity(new_page);
                }
            });
        }

        // SE SI CLICCA SULL'IMMAGINE FRECCETTA IN BASSO SI APRIRA' IL RIEPILOGO PRESCRIZIONI
        freccia_giu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                /////////////////////////////////////////
                Intent new_page = new Intent(getApplicationContext(), Choose_prescriptionsActivity.class);
                new_page.putExtra("dieta", dieta);
                new_page.putExtra("farmaci", farmaci);
                new_page.putExtra("scheda_allenamento", scheda_allenamento);
                /////////////////////////////////////////
                startActivity(new_page);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (bool_view == 1) {
            startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
        } else if (bool_view == 0) {
            // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
            /////////////////////////////////////////
            Intent new_page = new Intent(getApplicationContext(), PatientActivity.class);
            new_page.putExtra("dieta", dieta);
            new_page.putExtra("farmaci", farmaci);
            new_page.putExtra("scheda_allenamento", scheda_allenamento);
            /////////////////////////////////////////
            startActivity(new_page);
        }
    }

}
