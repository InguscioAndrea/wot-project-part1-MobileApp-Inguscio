package com.example.tabbedapplication.ui.Entities;

import java.io.Serializable;

public class MeasureData implements Serializable {

    private String id_op, current_time, handgrip_min, handgrip_max, handgrip_postexercise, weight_measured, fat_mass, bone_mass, muscle_mass, step_count, step_frequency, speed, bmi;

    public MeasureData(String id_op, String current_time, String handgrip_min, String handgrip_max, String handgrip_postexercise, String weight_measured, String fat_mass, String bone_mass, String muscle_mass, String step_count, String step_frequency, String speed, String bmi) {
        this.id_op = id_op;
        this.current_time = current_time;
        this.handgrip_min = handgrip_min;
        this.handgrip_max = handgrip_max;
        this.handgrip_postexercise = handgrip_postexercise;
        this.weight_measured = weight_measured;
        this.fat_mass = fat_mass;
        this.bone_mass = bone_mass;
        this.muscle_mass = muscle_mass;
        this.step_count = step_count;
        this.step_frequency = step_frequency;
        this.speed = speed;
        this.bmi = bmi;
    }


    public String getId_op() {
        return id_op;
    }

    public void setId_op(String id_op) {
        this.id_op = id_op;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public String getHandgrip_min() {
        return handgrip_min;
    }

    public void setHandgrip_min(String handgrip_min) {
        this.handgrip_min = handgrip_min;
    }

    public String getHandgrip_max() {
        return handgrip_max;
    }

    public void setHandgrip_max(String handgrip_max) {
        this.handgrip_max = handgrip_max;
    }

    public String getHandgrip_postexercise() {
        return handgrip_postexercise;
    }

    public void setHandgrip_postexercise(String handgrip_postexercise) {
        this.handgrip_postexercise = handgrip_postexercise;
    }

    public String getWeight_measured() {
        return weight_measured;
    }

    public void setWeight_measured(String weight_measured) {
        this.weight_measured = weight_measured;
    }

    public String getFat_mass() {
        return fat_mass;
    }

    public void setFat_mass(String fat_mass) {
        this.fat_mass = fat_mass;
    }

    public String getBone_mass() {
        return bone_mass;
    }

    public void setBone_mass(String bone_mass) {
        this.bone_mass = bone_mass;
    }

    public String getMuscle_mass() {
        return muscle_mass;
    }

    public void setMuscle_mass(String muscle_mass) {
        this.muscle_mass = muscle_mass;
    }

    public String getStep_count() {
        return step_count;
    }

    public void setStep_count(String step_count) {
        this.step_count = step_count;
    }

    public String getStep_frequency() {
        return step_frequency;
    }

    public void setStep_frequency(String step_frequency) {
        this.step_frequency = step_frequency;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

}




