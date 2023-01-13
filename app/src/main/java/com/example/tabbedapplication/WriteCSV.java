package com.example.tabbedapplication;

import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class WriteCSV {



    public static void test (ArrayList<String> dataset, String ID_paziente, String nome_paziente, int check_valore_passato) {

        int check_exist_file;
        String nome_file_csv = "";
        String nome_file_csv_firebase = "";

        //LA VARIABILE check_valore_passato SERVE A CAPIRE SE IL VALORE CHE VIENE PASSATO ALLA FUNZIONE
        // SE "1" SI PASSA SMI
        // SE "2" SI PASSA HAND GRIP
        // SE "3" SI PASSA SPEED

        System.out.println("LA LISTA VALE..." + dataset);

        //SPECIFICO IL PERCORSO DOVE VOGLIO CHE VENGA CREATO IL FILE .CSV CHE DESIDERO
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);

        //VERIFICO SE ESISTE GIA' UNA CARTELLA CHIAMATA SarcApp
        File folder = new File(path + "/SarcApp");

        boolean var = false;
        if (!folder.exists())
            var = folder.mkdir();


        //VERIFICO SE ESISTE GIA' UNA CARTELLA SOTTO CARTELLA DI SarcApp CHIAMATA nome_paziente
        File folder2 = new File(path + "/SarcApp" +"/"+nome_paziente);

        boolean var2 = false;
        if (!folder2.exists())
            var2 = folder2.mkdir();



        //SI VERIFICA QUALE VALORE VIENE PASSATO E SI CREA IL CORRISPETTIVO FILE .CSV RINOMINATO CORRETTAMENTE
        if(check_valore_passato == 1){
            nome_file_csv = "Dataset_Muscle.csv";
        } else if (check_valore_passato == 2){
            nome_file_csv = "Dataset_HandGrip.csv";
        } else if (check_valore_passato == 3){
            nome_file_csv = "Dataset_Speed.csv";
        }

        //CREO IL MIO FILE ALL'INTERNO DEL PERCORSO E CARTELLA SPECIFICATA, ALL'INTERNO DELLA MEMORIA DEL DEVICE
        File file = new File(path, "/SarcApp/" +nome_paziente+ "/" +nome_file_csv);

        // VERIFICA SE IL FILE "dataset_SMI.csv" ESISTE GIA' NELLA MEMORIA DEL DISPOSITIVO, E SETTA IL VALORE
        // BOOLEANO check_exist_file "1" SE ESISTE E "0" SE NON ESISTE
        if(file.exists()){
            check_exist_file = 1;
        } else{
            check_exist_file = 0;
        }


        int size = dataset.size();


        //final String filename = folder.toString() + "/" + "Test.csv";
        
        try {

            FileWriter pw = new FileWriter(file, true);
            StringBuilder sb=new StringBuilder();

            if(check_exist_file == 0){

                //SE IL FILE NON ESISTE IN MEMORIA, LO SI CREA INSERENDO L'INTESTAZIONE DELLE COLONNE
                sb.append("Date");
                sb.append(",");
                sb.append("\"Total\"");
            }

            sb.append("\r\n");
            sb.append(dataset.get(0));
            sb.append(",");
            sb.append("\""+dataset.get(1)+"\"");

            /*
            for(int i=0; i<size;) {

                sb.append("\r\n");

                for(int j=0; j<2; j++) {
                    sb.append(dataset.get(i));
                    sb.append(",");
                    i++;
                }

            }
             */


            pw.append(sb.toString());
            pw.close();
            System.out.println("IL FILE E' STATO CREATO CON SUCCESSO");


        } catch (Exception e) {
            System.out.println(e);
            System.out.println("C'E' UN PROBLEMA! IL FILE NON E' STATO CREATO...");
        }


        //SI ASSEGNA IL NOME AL FILE .CSV CHE SI CARICHERA' SULLO STORAGE DI FIREBASE
        if(check_valore_passato == 1){
            nome_file_csv_firebase = "Dataset_Muscle_" +nome_paziente+ ".csv";
        } else if (check_valore_passato == 2){
            nome_file_csv_firebase = "Dataset__Hgrip_" +nome_paziente+ ".csv";
        } else if (check_valore_passato == 3){
            nome_file_csv_firebase = "Dataset__Speed_" +nome_paziente+ ".csv";
        }



        Uri percorso = Uri.fromFile(file);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference filepath = storageReference.child("Dataset_ML").child(nome_paziente).child(nome_file_csv_firebase);

        filepath.putFile(percorso).continueWithTask(new Continuation() {
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


                } else {

                }
            }
        });


    }

    public static void write_list_of_patient (String nome_paziente) {

        int check_exist_file_list;

        //SPECIFICO IL PERCORSO DOVE VOGLIO CHE VENGA CREATO IL FILE .CSV CHE DESIDERO
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);

        //VERIFICO SE ESISTE GIA' UNA CARTELLA CHIAMATA SarcApp
        File folder = new File(path + "/SarcApp");

        boolean var = false;
        if (!folder.exists())
            var = folder.mkdir();


        //CREO IL MIO FILE ALL'INTERNO DEL PERCORSO E CARTELLA SPECIFICATA, ALL'INTERNO DELLA MEMORIA DEL DEVICE
        File file = new File(path, "/SarcApp/list_of_patient.csv");

        // VERIFICA SE IL FILE "dataset_SMI.csv" ESISTE GIA' NELLA MEMORIA DEL DISPOSITIVO, E SETTA IL VALORE
        // BOOLEANO check_exist_file "1" SE ESISTE E "0" SE NON ESISTE
        if(file.exists()){
            check_exist_file_list = 1;
        } else{
            check_exist_file_list = 0;
        }

        try {

            FileWriter pw = new FileWriter(file, true);
            StringBuilder sb=new StringBuilder();

            if(check_exist_file_list == 0){

                //SE IL FILE NON ESISTE IN MEMORIA, LO SI CREA INSERENDO L'INTESTAZIONE DELLE COLONNE
                sb.append("list_of_patient");
            }

            sb.append("\r\n");
            sb.append(nome_paziente);


            pw.append(sb.toString());
            pw.close();
            System.out.println("IL FILE E' STATO CREATO CON SUCCESSO");


        } catch (Exception e) {
            System.out.println(e);
            System.out.println("C'E' UN PROBLEMA! IL FILE NON E' STATO CREATO...");
        }


        Uri percorso = Uri.fromFile(file);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference filepath = storageReference.child("Dataset_ML").child("list_of_patient.csv");

        filepath.putFile(percorso).continueWithTask(new Continuation() {
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


                } else {

                }
            }
        });


    }

}