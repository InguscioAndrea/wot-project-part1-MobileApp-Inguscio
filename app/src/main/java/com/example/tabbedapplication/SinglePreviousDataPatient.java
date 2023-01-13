package com.example.tabbedapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.text.DecimalFormat;


public class SinglePreviousDataPatient extends AppCompatActivity {

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
    Double peso;
    String altezza;
    Double BMI;
    Double BMI_precedente;
    Double SMI_1;
    Double SMI_1_precedente;
    Double SMI_2;
    String CHANNEL_ID = "20";
    BarChart mBarChart;
    Double BMIchart;
    Double HG_max_precedente;
    Double HG_min_precedente;
    Double BoneMass_precedente;
    Double Altezza_precedente;
    Double Speed_precedente;

    String sesso_paziente;
    int bool_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String sesso_paziente = getIntent().getExtras().getString("sesso_paziente"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT
        int datopassato = getIntent().getExtras().getInt("id_operazione_scelta"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT
        String dieta = getIntent().getExtras().getString("dieta"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT
        String farmaci = getIntent().getExtras().getString("farmaci"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT
        String scheda_allenamento = getIntent().getExtras().getString("scheda_allenamento"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT
        bool_view = getIntent().getExtras().getInt("bool_view");
        path_child = getIntent().getExtras().getString("id_paziente"); //CODICE PER RICEVERE IL "parametro" DA UN ALTRO INTENT

        System.out.println("IL DATO PASSATO E': " +datopassato);

        //path_child =  getIntent().getExtras().getString("user_id");//RECUPERA L'ID DELLUTENTE CHE SI LOGGA

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.single_previous_data); //si specifica la pagina xml che si vuole stampare a video

        TextView Title;
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
        Button summary;
        TextView tvBoneMass, tvFatMass, tvMuscleMass;
        PieChart pieChart;
        TextView BMI_value;
        TextView BMI_express;
        TextView legend_HandGrip_max;
        TextView legend_HandGrip_min;
        TextView textview_spiegazioneBMI;


        Title = (TextView)findViewById(R.id.idTitle_SingleData);
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
        button_back = (Button)findViewById(R.id.button_back_8);
        summary = (Button)findViewById(R.id.btn_summary);
        pieChart = (PieChart)findViewById(R.id.piechart);
        tvBoneMass = (TextView)findViewById(R.id.tvBoneMass);
        tvFatMass = (TextView)findViewById(R.id.tvFatMass);
        tvMuscleMass = (TextView)findViewById(R.id.tvMuscleMass);
        BMI_value = (TextView)findViewById(R.id.id_BMIvalue3);
        BMI_express = (TextView)findViewById(R.id.id_BMIexpress3);
        legend_HandGrip_max = (TextView)findViewById(R.id.legend_HGmax);
        legend_HandGrip_min = (TextView)findViewById(R.id.legend_HGmin);
        textview_spiegazioneBMI = (TextView)findViewById(R.id.textView21);


        // Inflate the layout for this fragment
        mBarChart = (BarChart)findViewById(R.id.barchart);


        int id_precedente = datopassato - 1;

        if (id_precedente == 0) {
            summary.setVisibility(View.INVISIBLE);
        } else {
            summary.setVisibility(View.VISIBLE);
        }


        summary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Float HG_max = Float.parseFloat(var_hand_grip_max);
                Float HG_min = Float.parseFloat(var_hand_grip_min);
                Float AVG_HG = (HG_max + HG_min) / 2;

                Double BIA_result = SMI_2;

                Float Speed_result = Float.parseFloat(var_speed);

                   DatabaseReference myRefHGMax = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" + id_precedente).child("handgrip_max");
                    myRefHGMax.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String HG_max_precedente_var;
                            try {
                                HG_max_precedente_var = dataSnapshot.getValue(String.class);
                                HG_max_precedente = Double.parseDouble(HG_max_precedente_var);
                            } catch (Exception e) {
                                HG_max_precedente = 0.0;
                            }


                            DatabaseReference myRefHGMin = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" + id_precedente).child("handgrip_min");
                            myRefHGMin.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String HG_min_precedente_var;
                                    try {
                                        HG_min_precedente_var = dataSnapshot.getValue(String.class);
                                        HG_min_precedente = Double.parseDouble(HG_min_precedente_var);
                                    } catch (Exception e) {
                                        HG_min_precedente = 0.0;
                                    }

                                    DatabaseReference myRef2 = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" + id_precedente).child("bone_mass");
                                    myRef2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String BoneMass_precedente_var;
                                            try {
                                                BoneMass_precedente_var = dataSnapshot.getValue(String.class);
                                                BoneMass_precedente = Double.parseDouble(BoneMass_precedente_var);
                                            } catch (Exception e) {
                                                BoneMass_precedente = 0.0;
                                            }

                                            DatabaseReference myRefAltezza = database.getReference("Users").child("Patient").child(path_child).child("Personal_data").child("altezza");
                                            myRefAltezza.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    String Altezza_precedente_var = dataSnapshot.getValue(String.class);
                                                    Altezza_precedente = Double.parseDouble(Altezza_precedente_var);

                                                    DatabaseReference myRef3 = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" + id_precedente).child("speed");
                                                    myRef3.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String Speed_precedente_var;
                                                            try {
                                                                Speed_precedente_var = dataSnapshot.getValue(String.class);
                                                                Speed_precedente = Double.parseDouble(Speed_precedente_var);
                                                            } catch (Exception e) {
                                                                Speed_precedente = 0.0;
                                                            }

                                                            DatabaseReference myRef30 = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" + datopassato).child("muscle_mass");
                                                            myRef30.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    String var_bone_mass_actual = dataSnapshot.getValue(String.class);
                                                                    double actual_muscle_mass = Double.parseDouble(var_bone_mass_actual);

                                                                    DatabaseReference myRefPeso = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" + datopassato).child("weight_measured");
                                                                    myRefPeso.addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            String temp_peso = dataSnapshot.getValue(String.class);
                                                                            peso = Double.parseDouble(temp_peso);

                                                                            DatabaseReference myRefAltezza = database.getReference("Users").child("Patient").child(path_child).child("Personal_data").child("altezza");
                                                                            myRefAltezza.addValueEventListener(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    String temp_altezza = dataSnapshot.getValue(String.class);
                                                                                    int length = temp_altezza.length();
                                                                                    altezza = temp_altezza.substring(0, 1) + "." + temp_altezza.substring(1, length);

                                                                                    //CALCOLIAMO IL BMI [BMI = peso / (altezza)^2 ]
                                                                                    BMI = peso / ((Double.parseDouble(altezza)) * (Double.parseDouble(altezza)));
                                                                                    DecimalFormat round_to_one_decimal = new DecimalFormat("#.#");
                                                                                    String temp = String.valueOf(round_to_one_decimal.format(BMI));
                                                                                    String temp_1 = temp.substring(0, 2);
                                                                                    String temp_2;
                                                                                    try {
                                                                                        //  Block of code to try
                                                                                        temp_2 = temp.substring(3, 4);
                                                                                    } catch (Exception e) {
                                                                                        //  Block of code to handle errors
                                                                                        temp_2 = "0";
                                                                                    }

                                                                                    String BMI_string = temp_1 + "." + temp_2;

                                                                                    BMI = Double.parseDouble(BMI_string);

                                                                                    double SMI = actual_muscle_mass;

                                                                                    // PRELEVIAMO LA BMI RIFERITA ALLA MISURA PRECEDENTE
                                                                                    DatabaseReference myRefBoneMass = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" + id_precedente).child("muscle_mass");
                                                                                    myRefBoneMass.addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            var_muscle_mass = dataSnapshot.getValue(String.class);

                                                                                            DatabaseReference myRefPeso = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" + id_precedente).child("weight_measured");
                                                                                            myRefPeso.addValueEventListener(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                    String temp_peso = dataSnapshot.getValue(String.class);
                                                                                                    try {
                                                                                                        peso = Double.parseDouble(temp_peso);
                                                                                                    } catch (Exception e) {
                                                                                                        DatabaseReference myRefPeso = database.getReference("Users").child("Patient").child(path_child).child("Personal_data").child("peso");
                                                                                                        myRefPeso.addValueEventListener(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                String result = dataSnapshot.getValue(String.class);
                                                                                                                peso = Double.parseDouble(result);


                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                                                            }
                                                                                                        });
                                                                                                    }


                                                                                                    DatabaseReference myRefSMI = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" + id_precedente).child("muscle_mass");
                                                                                                    myRefSMI.addValueEventListener(new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                            String SMI_1_precedente = dataSnapshot.getValue(String.class);



                                                                                                            // RIGHE PER PASSARE IL "parametro" AD UN ALTRO INTENT
                                                                                                            /////////////////////////////////////////
                                                                                                            Intent new_page = new Intent(getApplicationContext(), Summary_resultActivity.class);
                                                                                                            new_page.putExtra("data_misura", current_time);
                                                                                                            new_page.putExtra("HG_result", HG_max);
                                                                                                            new_page.putExtra("BIA_result", SMI);
                                                                                                            new_page.putExtra("Speed_result", Speed_result);
                                                                                                            new_page.putExtra("dieta", dieta);
                                                                                                            new_page.putExtra("farmaci", farmaci);
                                                                                                            new_page.putExtra("scheda_allenamento", scheda_allenamento);
                                                                                                            new_page.putExtra("id_patient", path_child);
                                                                                                            new_page.putExtra("sesso_paziente", sesso_paziente);
                                                                                                            System.out.println("HG MAX PRECEDENTE...." + HG_max_precedente);

                                                                                                            Double AVG_prec_HG = (HG_max_precedente + HG_min_precedente) / 2;


                                                                                                            new_page.putExtra("HG_precedente", HG_max_precedente);
                                                                                                            new_page.putExtra("BIA_precedente", SMI_1_precedente);
                                                                                                            new_page.putExtra("Speed_precedente", Speed_precedente);
                                                                                                            new_page.putExtra("bool_view", bool_view);

                                                                                                            System.out.println("BIA PRECEDENTE SMI VALE....." + SMI_1_precedente);

                                                                                                            /////////////////////////////////////////

                                                                                                            startActivity(new_page);




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
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bool_view == 1) {
                    startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
                } else if (bool_view == 0) {
                    startActivity(new Intent(getApplicationContext(), PatientActivity.class));
                }

            }
        });

        DatabaseReference myRef2 = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("current_time");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_time = String.valueOf(dataSnapshot.getValue(String.class));

                DatabaseReference myRef21 = database.getReference("Users").child("Patient").child(path_child).child("Personal_data").child("cognome");
                myRef21.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String cognome = String.valueOf(dataSnapshot.getValue(String.class));
                        Title.setText((cognome.toUpperCase()) +" data overview \n dated: " + current_time);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


                Title.setText("Data overview \n of: " + current_time);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefId = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("id_op");
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


        DatabaseReference myRefHGMax = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("handgrip_max");
        myRefHGMax.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_hand_grip_max = dataSnapshot.getValue(String.class);
                String stringa_bold = "<b>" + "Max: " + "</b> " + var_hand_grip_max + " kg";
                handgrip_max.setText(Html.fromHtml(stringa_bold));

                Float hgrip_max = Float.parseFloat(var_hand_grip_max);
                legend_HandGrip_max.setText(String.valueOf(hgrip_max));
                legend_HandGrip_max.setTextColor(Color.parseColor("#873F56"));
                hgrip_max = hgrip_max + 10;


                //STAMPIAMO IL CHART A COLONNA DEL HAND GRIP MAX
                mBarChart.addBar(new BarModel("Max", hgrip_max, 0xFF873F56));
                mBarChart.setShowValues(false);




            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference myRefHGMin = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("handgrip_min");
        myRefHGMin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_hand_grip_min = dataSnapshot.getValue(String.class);
                String stringa_bold = "<b>" + "Min: " + "</b> " + var_hand_grip_min + " kg";
                handgrip_min.setText(Html.fromHtml(stringa_bold));

                Float hgrip_min = Float.parseFloat(var_hand_grip_min);
                legend_HandGrip_min.setText(String.valueOf(hgrip_min));
                legend_HandGrip_min.setTextColor(Color.parseColor("#a366a3"));


                //STAMPIAMO IL CHART A COLONNA DEL HAND GRIP MIN
                mBarChart.addBar(new BarModel("Min ", hgrip_min,0xFFa366a3 ));
                mBarChart.setShowValues(false);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        DatabaseReference myRefWeightMeasure = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("weight_measured");
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

        DatabaseReference myRefMuscleMass = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("muscle_mass");
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

        DatabaseReference myRefBoneMass = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("bone_mass");
        myRefBoneMass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_bone_mass = dataSnapshot.getValue(String.class);
                String stringa_bold = "<b>" + "Bone mass: kg" + "</b> " + var_bone_mass;
                bone_mass.setText(Html.fromHtml(stringa_bold));

                DatabaseReference myRefPeso = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("weight_measured");
                myRefPeso.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String temp_peso = dataSnapshot.getValue(String.class);
                        peso = Double.parseDouble(temp_peso);

                        DatabaseReference myRefAltezza = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("bmi");
                        myRefAltezza.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String bmi = dataSnapshot.getValue(String.class);


                                BMI = Double.parseDouble(bmi);

                                System.out.println("IL SESSO PAZIENTE VALEEEEEEEE......." + sesso_paziente);

                                if(sesso_paziente.equals("Male")){
                                    textview_spiegazioneBMI.setText("The body mass index represents the ratio between the ideal total weight (kg) and the square of the height (m). BMI is used to determine if you are underweight (BMI < 18.5), normal weight (18.5 ≤ BMI ≤ 24.9), overweight (25.0 ≤ BMI ≤ 29.9) or obese (BMI ≥ 30.0).");

                                    if(BMI < 18.5){
                                        //sezione sottopeso
                                        BMI_value.setText(String.valueOf(BMI));
                                        BMI_value.setTextColor(Color.parseColor("#e5be01"));
                                        BMI_express.setText("Underweight");
                                        BMI_express.setTextColor(Color.parseColor("#e5be01"));
                                    } else if (BMI >= 18.5 && BMI <= 24.9){
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

                                } else if (sesso_paziente.equals("Female")){
                                    textview_spiegazioneBMI.setText("The body mass index represents the ratio between the ideal total weight (kg) and the square of the height (m). BMI is used to determine if you are underweight (BMI < 17.5), normal weight (17.5 ≤ BMI ≤ 23.90), overweight (23.90 ≤ BMI ≤ 29.0) or obese (BMI ≥ 29.0).");


                                    if(BMI < 17.5){
                                        //sezione sottopeso
                                        BMI_value.setText(String.valueOf(BMI));
                                        BMI_value.setTextColor(Color.parseColor("#e5be01"));
                                        BMI_express.setText("Underweight");
                                        BMI_express.setTextColor(Color.parseColor("#e5be01"));
                                    } else if (BMI >= 17.5 && BMI <= 23.89){
                                        //sezione normopeso
                                        BMI_value.setText(String.valueOf(BMI));
                                        BMI_value.setTextColor(Color.parseColor("#3ea701"));
                                        BMI_express.setText("Normal weight");
                                        BMI_express.setTextColor(Color.parseColor("#3ea701"));
                                    } else if (BMI >= 23.90 && BMI <= 29.0){
                                        //sezione sovrappeso
                                        BMI_value.setText(String.valueOf(BMI));
                                        BMI_value.setTextColor(Color.parseColor("#e5be01"));
                                        BMI_express.setText("Overweight");
                                        BMI_express.setTextColor(Color.parseColor("#e5be01"));
                                    } else if (BMI >= 29.0){
                                        //sezione obeso
                                        BMI_value.setText(String.valueOf(BMI));
                                        BMI_value.setTextColor(Color.parseColor("#B82D01"));
                                        BMI_express.setText("Obese");
                                        BMI_express.setTextColor(Color.parseColor("#B82D01"));
                                    }
                                }



                                BMI_value.setText(String.valueOf(BMI));



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

        DatabaseReference myRefFatMass = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("fat_mass");
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

        DatabaseReference myRefSpeed = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("speed");
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

        DatabaseReference myRefStepCount = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("step_count");
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


        DatabaseReference myRefStepFrequency = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("step_frequency");
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

        DatabaseReference myRefFatMass2 = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("fat_mass");
        myRefFatMass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                var_fat_mass = dataSnapshot.getValue(String.class);


                DatabaseReference myRefBoneMass2 = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("bone_mass");
                myRefBoneMass2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        var_bone_mass = dataSnapshot.getValue(String.class);


                        DatabaseReference myRefMuscleMass2 = database.getReference("Users").child("Patient").child(path_child).child("Measure").child("ID_" +datopassato).child("muscle_mass");
                        myRefMuscleMass2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                var_muscle_mass = dataSnapshot.getValue(String.class);

                                // Set the data and color to the pie chart
                                pieChart.addPieSlice(
                                        new PieModel(
                                                "Bone Mass",
                                                Float.parseFloat(var_bone_mass),
                                                Color.parseColor("#FF0000")));
                                pieChart.addPieSlice(
                                        new PieModel(
                                                "Muscle Mass",
                                                Float.parseFloat(var_muscle_mass),
                                                Color.parseColor("#FF8000")));
                                pieChart.addPieSlice(
                                        new PieModel(
                                                "Fat Mass",
                                                Float.parseFloat(var_fat_mass),
                                                Color.parseColor("#1A237E")));

                                tvBoneMass.setText(var_bone_mass + "%");
                                tvMuscleMass.setText(var_muscle_mass + "%");
                                tvFatMass.setText(var_fat_mass + "%");

                                // To animate the pie chart
                                pieChart.startAnimation();

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


    @Override
    public void onBackPressed() {
        if (bool_view == 1) {
            startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
        } else if (bool_view == 0) {
            startActivity(new Intent(getApplicationContext(), PatientActivity.class));
        }
    }

}
