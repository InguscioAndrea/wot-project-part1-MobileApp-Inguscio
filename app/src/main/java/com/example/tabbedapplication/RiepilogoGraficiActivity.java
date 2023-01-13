package com.example.tabbedapplication;

// Import the required libraries

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;


public class RiepilogoGraficiActivity extends AppCompatActivity {

    // creating a variable
    // for our graph view.
    GraphView graphView_hand_grip;
    GraphView graphView_skeleton_mass_index;
    GraphView graphView_speed;
    Button button_back;

    String sesso_paziente;
    int global_bool_view;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<String> X_axis = (ArrayList<String>) getIntent().getSerializableExtra("X_axis");
        ArrayList<String> Y_axis = (ArrayList<String>) getIntent().getSerializableExtra("Y_axis");
        ArrayList<String> Y_axis_SMI = (ArrayList<String>) getIntent().getSerializableExtra("Y_axis_SMI");
        ArrayList<String> Y_axis_Speed = (ArrayList<String>) getIntent().getSerializableExtra("Y_axis_Speed");
        sesso_paziente = getIntent().getExtras().getString("sesso_paziente");
        int bool_view = getIntent().getExtras().getInt("bool_view");
        global_bool_view = bool_view;

        System.out.println("IL VALORE DI SMI VALE" +Y_axis_SMI );
        System.out.println("IL VALORE DI SPEED VALE" +Y_axis_Speed);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.riepilogo_grafici);


        // on below line we are initializing our graph view.
        graphView_hand_grip = findViewById(R.id.idGraphView);
        graphView_skeleton_mass_index = findViewById(R.id.idGraphView2);
        graphView_skeleton_mass_index = findViewById(R.id.idGraphView2);
        graphView_speed = findViewById(R.id.idGraphView3);
        button_back = findViewById(R.id.button_back_2);


        button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(global_bool_view == 2){
                    startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), PatientActivity.class));
                }

            }
        });


        //initGraph(graphView);

        hand_grip_function(X_axis, Y_axis);
        skeleton_muscle_index_function(X_axis, Y_axis_SMI);
        speed_function(X_axis, Y_axis_Speed);

    }

    private void hand_grip_function(List<String> X_list, List<String> Y_list) {

        int lenght_data_array = X_list.size();

        String[] label_X_axis = new String[lenght_data_array];
        String[] label_Y_axis = new String[lenght_data_array];

        String[] label_X_axis_desc = new String[lenght_data_array]; // prende gli elementi dell'asse X
                                                                    // e li mette in ordine discendente
                                                                    // (dal meno recente al più recente)

        for (int i = 0; i < lenght_data_array; i++) {
            int x = lenght_data_array - i - 1;

            label_X_axis_desc[x] = X_list.get(i);
        }



        for (int i = 0; i < lenght_data_array; i++) {
            int x = i;

            label_X_axis[x] = X_list.get(x);
            label_Y_axis[x] = Y_list.get(x);

        }

    //SEZIONE IN CUI SI INSERISCE LA LINEA DI RIFERIMENTO CORRISPONDENTE AL VALORE SOGLIA PER LA DIAGNOSI DI SARCOPENIA
        DataPoint[] teoricPoints = new DataPoint[lenght_data_array];
        for (int i = 0; i < lenght_data_array; i++) {
            if(sesso_paziente.equals("Male")){
                teoricPoints[i] = new DataPoint(i, 26.0);
            } else if (sesso_paziente.equals("Female")){
                teoricPoints[i] = new DataPoint(i, 18.0);
            }
        }

        LineGraphSeries<DataPoint> teoric_series = new LineGraphSeries<>(teoricPoints);
        graphView_hand_grip.addSeries(teoric_series);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5); //SPESSORE DELLA LINEA TRATTEGGIATA
        paint.setPathEffect(new DashPathEffect(new float[]{20, 8}, 0));
        paint.setColor(Color.rgb(223, 133, 150));

        teoric_series.setCustomPaint(paint);


        ////////////////////////////////////////////////////////////////////////////////////////////////

        DataPoint[] dp = new DataPoint[lenght_data_array];
        for (int i = 0; i < lenght_data_array; i++) {
            int x = lenght_data_array - i - 1;
            String temp_value = Y_list.get(i);
            Double value = Double.parseDouble(temp_value);

            dp[x] = new DataPoint(x, value);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);

        // after adding data to our line graph series.
        // on below line we are setting
        // title for our graph view.
        graphView_hand_grip.setTitle("Hand Grip Trend");

        // on below line we are setting
        // text color to our graph view.
        graphView_hand_grip.setTitleColor(Color.parseColor("#757575"));

        // on below line we are setting
        // our title text size.
        graphView_hand_grip.setTitleTextSize(70);

        // on below line we are adding
        // data series to our graph view.
        graphView_hand_grip.addSeries(series);


        graphView_hand_grip.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graphView_hand_grip.getViewport().setScrollable(true);  // activate horizontal scrolling
        graphView_hand_grip.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graphView_hand_grip.getViewport().setScrollableY(true);  // activate vertical scrolling


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView_hand_grip);
        staticLabelsFormatter.setHorizontalLabels(label_X_axis_desc);
        graphView_hand_grip.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graphView_hand_grip.getGridLabelRenderer().setHorizontalLabelsAngle(115); //ruota il label dell'asse x


    }

    private void skeleton_muscle_index_function(List<String> X_list, List<String> Y_list) {

        int lenght_data_array = X_list.size();

        String[] label_X_axis = new String[lenght_data_array];
        String[] label_Y_axis = new String[lenght_data_array];

        String[] label_X_axis_desc = new String[lenght_data_array]; // prende gli elementi dell'asse X
        // e li mette in ordine discendente
        // (dal meno recente al più recente)

        for (int i = 0; i < lenght_data_array; i++) {
            int x = lenght_data_array - i - 1;

            label_X_axis_desc[x] = X_list.get(i);
        }



        for (int i = 0; i < lenght_data_array; i++) {
            int x = i;

            label_X_axis[x] = X_list.get(x);
            label_Y_axis[x] = Y_list.get(x);

        }

        //SEZIONE IN CUI SI INSERISCE LA LINEA DI RIFERIMENTO CORRISPONDENTE AL VALORE SOGLIA PER LA DIAGNOSI DI SARCOPENIA
        DataPoint[] teoricPoints = new DataPoint[lenght_data_array];
        for (int i = 0; i < lenght_data_array; i++) {
            if(sesso_paziente.equals("Male")){
                teoricPoints[i] = new DataPoint(i, 7.9);
            } else if (sesso_paziente.equals("Female")){
                teoricPoints[i] = new DataPoint(i, 6.0);
            }
        }

        LineGraphSeries<DataPoint> teoric_series = new LineGraphSeries<>(teoricPoints);
        graphView_skeleton_mass_index.addSeries(teoric_series);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5); //SPESSORE DELLA LINEA TRATTEGGIATA
        paint.setPathEffect(new DashPathEffect(new float[]{20, 8}, 0));
        paint.setColor(Color.rgb(223, 133, 150));

        teoric_series.setCustomPaint(paint);


        ////////////////////////////////////////////////////////////////////////////////////////////////

        DataPoint[] dp = new DataPoint[lenght_data_array];
        for (int i = 0; i < lenght_data_array; i++) {
            int x = lenght_data_array - i - 1;
            String temp_value = Y_list.get(i);
            Double value = Double.parseDouble(temp_value);

            dp[x] = new DataPoint(x, value);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);

        // after adding data to our line graph series.
        // on below line we are setting
        // title for our graph view.
        String title_skeleton_graph = "Skeleton Mass Index (SMI) Trend";
        graphView_skeleton_mass_index.setTitle(title_skeleton_graph);


        // on below line we are setting
        // text color to our graph view.
        graphView_skeleton_mass_index.setTitleColor(Color.parseColor("#757575"));

        // on below line we are setting
        // our title text size.
        graphView_skeleton_mass_index.setTitleTextSize(70);

        // on below line we are adding
        // data series to our graph view.
        graphView_skeleton_mass_index.addSeries(series);


        graphView_skeleton_mass_index.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graphView_skeleton_mass_index.getViewport().setScrollable(true);  // activate horizontal scrolling
        graphView_skeleton_mass_index.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graphView_skeleton_mass_index.getViewport().setScrollableY(true);  // activate vertical scrolling


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView_skeleton_mass_index);
        staticLabelsFormatter.setHorizontalLabels(label_X_axis_desc);
        graphView_skeleton_mass_index.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graphView_skeleton_mass_index.getGridLabelRenderer().setHorizontalLabelsAngle(115); //ruota il label dell'asse x


    }

    private void speed_function(List<String> X_list, List<String> Y_list) {

        int lenght_data_array = X_list.size();

        String[] label_X_axis = new String[lenght_data_array];
        String[] label_Y_axis = new String[lenght_data_array];

        String[] label_X_axis_desc = new String[lenght_data_array]; // prende gli elementi dell'asse X
        // e li mette in ordine discendente
        // (dal meno recente al più recente)

        for (int i = 0; i < lenght_data_array; i++) {
            int x = lenght_data_array - i - 1;

            label_X_axis_desc[x] = X_list.get(i);
        }



        for (int i = 0; i < lenght_data_array; i++) {
            int x = i;

            label_X_axis[x] = X_list.get(x);
            label_Y_axis[x] = Y_list.get(x);

        }

        //SEZIONE IN CUI SI INSERISCE LA LINEA DI RIFERIMENTO CORRISPONDENTE AL VALORE SOGLIA PER LA DIAGNOSI DI SARCOPENIA
        DataPoint[] teoricPoints = new DataPoint[lenght_data_array];
        for (int i = 0; i < lenght_data_array; i++) {
            teoricPoints[i] = new DataPoint(i, 0.800);
        }

        LineGraphSeries<DataPoint> teoric_series = new LineGraphSeries<>(teoricPoints);
        graphView_speed.addSeries(teoric_series);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5); //SPESSORE DELLA LINEA TRATTEGGIATA
        paint.setPathEffect(new DashPathEffect(new float[]{20, 8}, 0));
        paint.setColor(Color.rgb(223, 133, 150));

        teoric_series.setCustomPaint(paint);


        ////////////////////////////////////////////////////////////////////////////////////////////////

        DataPoint[] dp = new DataPoint[lenght_data_array];
        for (int i = 0; i < lenght_data_array; i++) {
            int x = lenght_data_array - i - 1;
            String temp_value = Y_list.get(i);
            Double value = Double.parseDouble(temp_value);

            dp[x] = new DataPoint(x, value);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);

        // after adding data to our line graph series.
        // on below line we are setting
        // title for our graph view.
        String title_skeleton_graph = "Walking Speed Trend";
        graphView_speed.setTitle(title_skeleton_graph);


        // on below line we are setting
        // text color to our graph view.
        graphView_speed.setTitleColor(Color.parseColor("#757575"));

        // on below line we are setting
        // our title text size.
        graphView_speed.setTitleTextSize(70);

        // on below line we are adding
        // data series to our graph view.
        graphView_speed.addSeries(series);


        graphView_speed.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
        graphView_speed.getViewport().setScrollable(true);  // activate horizontal scrolling
        graphView_speed.getViewport().setScalableY(true);  // activate horizontal and vertical zooming and scrolling
        graphView_speed.getViewport().setScrollableY(true);  // activate vertical scrolling


        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView_speed);
        staticLabelsFormatter.setHorizontalLabels(label_X_axis_desc);
        graphView_speed.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graphView_speed.getGridLabelRenderer().setHorizontalLabelsAngle(115); //ruota il label dell'asse x


    }

    private void initGraph(GraphView graph) {

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5); //SPESSORE DELLA LINEA TRATTEGGIATA
        paint.setPathEffect(new DashPathEffect(new float[]{30, 8}, 0));
        paint.setColor(Color.RED);

        DataPoint[] minPoints = new DataPoint[30];
        DataPoint[] maxPoints = new DataPoint[30];
        DataPoint[] teoricPoints = new DataPoint[30];
        DataPoint[] basePoint = new DataPoint[1];
        for (int i = 0; i < 30; i++) {
            //points[i] = new DataPoint(i, Math.sin(i*0.5) * 20*(Math.random()*10+1));
            minPoints[i] = new DataPoint(i, 20);
            maxPoints[i] = new DataPoint(i, 45);
            teoricPoints[i] = new DataPoint(i, 30);
        }
        basePoint[0] = new DataPoint(0, 0);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(minPoints);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(maxPoints);
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(teoricPoints);
        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(basePoint);

        graph.getGridLabelRenderer().setGridColor(Color.RED);
        graph.getGridLabelRenderer().setHighlightZeroLines(false);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.GREEN);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.RED);
        graph.getGridLabelRenderer().setVerticalLabelsVAlign(GridLabelRenderer.VerticalLabelsVAlign.ABOVE);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().reloadStyles();


        // styling viewport
        graph.getViewport().setBackgroundColor(Color.argb(255, 222, 222, 222));
        graph.getViewport().setDrawBorder(true);
        graph.getViewport().setBorderColor(Color.BLUE);

        // styling series
        series.setTitle("Random Curve 1");
        series.setColor(Color.GREEN);

        series.setThickness(8);

        series2.setTitle("Random Curve 2");
        series2.setColor(Color.GREEN);

        series2.setThickness(8);

        series3.setTitle("Random Curve 3");
        series3.setColor(Color.GREEN);

        series3.setThickness(8);

        series4.setTitle("Random Curve 4");
        series4.setColor(Color.GREEN);

        series4.setThickness(8);

        graph.getLegendRenderer().setFixedPosition(150, 0);

        series.setCustomPaint(paint);
        series2.setCustomPaint(paint);
        series3.setCustomPaint(paint);
        series4.setCustomPaint(paint);

        graph.addSeries(series);
        graph.addSeries(series2);
        graph.addSeries(series3);
        graph.addSeries(series4);

    }

    @Override
    public void onBackPressed() {
        if(global_bool_view == 2){
            startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), PatientActivity.class));
        }

    }
}