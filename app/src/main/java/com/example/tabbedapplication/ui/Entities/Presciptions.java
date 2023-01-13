package com.example.tabbedapplication.ui.Entities;

import java.io.Serializable;

public class Presciptions implements Serializable {

    private String dieta, farmaci, scheda_allenamento;

    public Presciptions(String dieta, String farmaci, String scheda_allenamento) {
        this.dieta = dieta;
        this.farmaci = farmaci;
        this.scheda_allenamento = scheda_allenamento;
    }

    public String getDieta() {
        return dieta;
    }

    public void setDieta(String dieta) {
        this.dieta = dieta;
    }

    public String getFarmaci() {
        return farmaci;
    }

    public void setFarmaci(String farmaci) {
        this.farmaci = farmaci;
    }

    public String getScheda_allenamento() {
        return scheda_allenamento;
    }

    public void setScheda_allenamento(String scheda_allenamento) {
        this.scheda_allenamento = scheda_allenamento;
    }

}
