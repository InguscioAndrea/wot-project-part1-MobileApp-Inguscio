package com.example.tabbedapplication.ui.Entities;

import java.io.Serializable;

public class User implements Serializable {

    private String nome, cognome, luogo_nascita, data_nascita, indirizzo, telefono, email, altezza, peso, username, password, role, gender, city;

    public User(String nome, String cognome, String luogo_nascita, String data_nascita, String indirizzo, String telefono, String email, String altezza, String peso, String username, String password, String role, String gender, String city) {
        this.nome = nome;
        this.cognome = cognome;
        this.luogo_nascita = luogo_nascita;
        this.data_nascita = data_nascita;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.email = email;
        this.altezza = altezza;
        this.peso = peso;
        this.username = username;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.city = city;
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

    public String getLuogo_nascita() {
        return luogo_nascita;
    }

    public void setLuogo_nascita(String luogo_nascita) {
        this.luogo_nascita = luogo_nascita;
    }

    public String getData_nascita() {
        return data_nascita;
    }

    public void setData_nascita(String data_nascita) {
        this.data_nascita = data_nascita;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAltezza() {
        return altezza;
    }

    public void setAltezza(String altezza) {
        this.altezza = altezza;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
