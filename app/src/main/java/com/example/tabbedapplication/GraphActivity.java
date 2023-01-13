package com.example.tabbedapplication;

// Import the required libraries

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DecimalFormat;

public class GraphActivity extends AppCompatActivity {

    // Create the object of TextView
    // and PieChart class
    TextView tvBoneMass, tvFatMass, tvMuscleMass;
    PieChart pieChart;
    TextView BMI_value, BMI_express;
    Double BMI;
    Button button_back;
    int reader_operator;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Double temp_BMI = getIntent().getExtras().getDouble("passaggio_BMI"); //CODICE PER RICEVERE IL "passaggio_BMI" DA UN ALTRO INTENT
        reader_operator = getIntent().getExtras().getInt("reader_operator"); // ASSOCIAMO "1" SE LA VIEW E' LETTA DAL PAZIENTE E "0" SE DAL DOTTORE

        DecimalFormat round_to_one_decimal = new DecimalFormat("#.#");
        String temp = String.valueOf(round_to_one_decimal.format(temp_BMI));
        String temp_1 = temp.substring(0,2);
        String temp_2;
        try {
            //  Block of code to try
            temp_2 = temp.substring(3,4);
        }
        catch(Exception e) {
            //  Block of code to handle errors
            temp_2 = "0";
        }

        String BMI_string = temp_1 + "." + temp_2;

        BMI = Double.parseDouble(BMI_string);





        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        // Link those objects with their
        // respective id's that
        // we have given in .XML file
        tvBoneMass = findViewById(R.id.legend_HGmax);
        tvMuscleMass = findViewById(R.id.legend_HGmin);
        tvFatMass = findViewById(R.id.tvFatMass2);
        pieChart = findViewById(R.id.piechart);
        BMI_value = findViewById(R.id.id_BMIvalue);
        BMI_express = findViewById(R.id.id_BMIexpress);
        button_back = findViewById(R.id.button_back_);

        // Creating a method setData()
        // to set the text in text view and pie chart
        setData();

        button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (reader_operator == 1) { // L'UTENTE E' IL PAZIENTE
                    startActivity(new Intent(getApplicationContext(), PatientActivity.class));
                } else if (reader_operator == 0){ // L'UTENTE E' IL MEDICO
                    startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
                }
            }
        });

    }

    private void setData()
    {

        // Set the percentage of language used
        tvBoneMass.setText("40");
        tvMuscleMass.setText("50");
        tvFatMass.setText("10");

        //double BMI = 25.4;

        if(BMI < 18.0){
            //sezione sottopeso
            BMI_value.setText(String.valueOf(BMI));
            BMI_value.setTextColor(Color.parseColor("#e5be01"));
            BMI_express.setText("Underweight");
            BMI_express.setTextColor(Color.parseColor("#e5be01"));
        } else if (BMI >= 18.0 && BMI <= 24.9){
            //sezione normopeso
            BMI_value.setText(String.valueOf(BMI));
            BMI_value.setTextColor(Color.parseColor("#3ea701"));
            BMI_express.setText("Normal weight");
            BMI_express.setTextColor(Color.parseColor("#3ea701"));
        } else if (BMI >= 25.0 && BMI <= 29.9){
            //sezione sovrappeso
            BMI_value.setText(String.valueOf(BMI));
            BMI_value.setTextColor(Color.parseColor("#e5be01"));
            BMI_express.setText("Overweight");
            BMI_express.setTextColor(Color.parseColor("#e5be01"));
        } else if (BMI >= 30.0){
            //sezione obeso
            BMI_value.setText(String.valueOf(BMI));
            BMI_value.setTextColor(Color.parseColor("#B82D01"));
            BMI_express.setText("Obese");
            BMI_express.setTextColor(Color.parseColor("#B82D01"));
        }

        BMI_value.setText(String.valueOf(BMI));


        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "Bone Mass",
                        Integer.parseInt(tvBoneMass.getText().toString()),
                        Color.parseColor("#FF0000")));
        pieChart.addPieSlice(
                new PieModel(
                        "Muscle Mass",
                        Integer.parseInt(tvMuscleMass.getText().toString()),
                        Color.parseColor("#FF8000")));
        pieChart.addPieSlice(
                new PieModel(
                        "Fat Mass",
                        Integer.parseInt(tvFatMass.getText().toString()),
                        Color.parseColor("#1A237E")));

        // To animate the pie chart
        pieChart.startAnimation();
    }

    @Override
    public void onBackPressed() {
        if (reader_operator == 1) { // L'UTENTE E' IL PAZIENTE
            startActivity(new Intent(getApplicationContext(), PatientActivity.class));
        } else if (reader_operator == 0){ // L'UTENTE E' IL MEDICO
            startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
        }
    }

}
