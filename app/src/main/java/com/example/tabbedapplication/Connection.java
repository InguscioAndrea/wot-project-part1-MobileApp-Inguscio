package com.example.tabbedapplication;

import com.example.tabbedapplication.ui.Entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Connection {

    private static final String DBREF = "https://progettoandre-495a4-default-rtdb.europe-west1.firebasedatabase.app/";

    private static FirebaseAuth auth;
    private static FirebaseDatabase database;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance(DBREF).getReference();
    private static FirebaseUser user;


    public Connection() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(DBREF);
        user = auth.getCurrentUser();
    }

    /**
     * Permette di recuperare l'autenticazione di Firebase
     * @return FirebaseAuth, l'autenticazione
     */
    public FirebaseAuth getAuth() {
        return auth;
    }

    /**
     * Permette di recuperare un'istanza del database
     * @return FibaseDatabase, istanza del database
     */
    public FirebaseDatabase getDatabase() {
        return database;
    }

    /**
     * Permette di recuperare il riferimento al RealTime Database
     * @return DatabaseReference, il nodo padre
     */
    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    /**
     * Permette di recuperare l'istanza dell'utente connesso
     * @return FirebaseUser, l'utente loggato
     */
    public FirebaseUser getUser() {
        return user;
    }


    /**
     * Permette di recuperare l'indirizzo del RealTimeDatabase
     * @return String, l'indirizzo del db remoto
     */
    public final String getDBREF() {
        return DBREF;
    }

}