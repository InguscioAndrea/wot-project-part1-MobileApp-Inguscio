# WoT-Project

This project aims to design and implement an integrated system for managing and measuring the typical parameters of Sarcopenia, in order to facilitate and simplify continuous monitoring and "self-tests" within one's homes.
The patient will be able, using the technologies provided, to go and perform Bioimpedancemetry (BIA) tests for monitoring the percentage of fat and lean mass, Dynamometry or Handgrip Strength for measuring strength in the hand muscles, Gait Test for measuring speed of walking. These tests can be carried out in total autonomy at the patient's home and on a regular basis, without using machinery or booking any visits from a specialist doctor, reserving the right to carry them out only in limited cases detected by the system. All the data collected by the integrated system will then be forwarded, via bluetooth module, directly to the patient's smartphone and accessible via a special dedicated App. The latter, in addition to allowing a quick overview of previous exams carried out, provides advice and daily exercises to be carried out, aimed at collecting further data in stressful situations. The entire database will then be uploaded to the Cloud and directly accessible, again via the Android App, to the specialist medical staff who will follow the patient.

This repository contains the entire Android project regarding the app, created ad hoc to achieve the goal. It will allow us:

PATIENT

- monitor the state and progress of the pathology, carrying out measurements and obtaining results from the connected sensors (simulated section);
- access a history of all the measurements made, enriching the result with graphs, text and advice. Each result will then be compared with reference to the previous measurement and the (normotypical) target value;
- view a series of graphs describing the trend of the three measured quantities (muscle mass, hand grip, walking speed);
- view the various medical prescriptions (diet, training card and medical prescription) uploaded by the medical staff;
- a special section dedicated to the coinsigli to be applied in case of values lower than the cut-off;
- display of information and contacts regarding the medical staff and the reference clinic;

SPECIALIST MEDICAL STAFF

- access a history of all the measurements made, enriching the result with graphs, text and advice. Each result will then be compared with reference to the previous measurement and the (normotypical) target value;
- view a series of graphs describing the trend of the three measured quantities (muscle mass, hand grip, walking speed);
- forward the various medical prescriptions (diet, training card and medical prescription) to the patient profile;
- view the detailed list of personal data of all patients;

ADMINISTRATOR

- view the detailed list of personal data of all users (medical staff and patients);
- modify one or more fields concerning the profile of one or more selected users (medical personnel and patients);
- create a new user profile (medical staff or patient), assigning access credentials;


IMPORTANT NOTE:
In order to test the mobile app, you will need to log in with your username and password. Below are the login credentials of users already entered in the system:

● ADMINISTRATOR
username -> admin@sarcapp.it
password -> admin_key

● DOCTOR
username -> doctor@sarcapp.it
password -> doctor_key

● PATIENT 1 (MALE)
username -> patient1@sarcapp.it
password -> patient1_key

● PATIENT 2 (FEMALE)
username -> patient2@sarcapp.it
password -> patient2_key
