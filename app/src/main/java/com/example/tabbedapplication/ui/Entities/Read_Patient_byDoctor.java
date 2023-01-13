package com.example.tabbedapplication.ui.Entities;

import java.io.Serializable;

public class Read_Patient_byDoctor implements Serializable {

    private String nome, cognome, id;

    public Read_Patient_byDoctor(String nome, String cognome, String id) {
        this.nome = nome;
        this.cognome = cognome;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
