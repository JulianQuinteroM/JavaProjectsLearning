package PetCareScheduler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pet {
    private String petID;
    private String petName;
    private String specieBreed;
    private int petAge;
    private String ownerName;
    private String contactInfo;
    private LocalDate dateOfRegistration;
    private List<Apointment> appointments;


    public Pet(String petID, String petName, String specieBreed, int petAge, String ownerName, String contactInfo, LocalDate dateOfRegistration) {
        this.petID = petID;
        this.petName = petName;
        this.specieBreed = specieBreed;
        this.petAge = petAge;
        this.ownerName = ownerName;
        this.contactInfo = contactInfo;
        this.dateOfRegistration = dateOfRegistration;
        this.appointments = new ArrayList<>();
    }

    public Pet() {
    }

    public String getPetID() {
        return petID;
    }

    public void setPetID(String petID) {
        this.petID = petID;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSpecieBreed() {
        return specieBreed;
    }

    public void setSpecieBreed(String specieBreed) {
        this.specieBreed = specieBreed;
    }

    public int getPetAge() {
        return petAge;
    }

    public void setPetAge(int petAge) {
        this.petAge = petAge;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public LocalDate getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDate dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public List<Apointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Apointment> appointments) {
        this.appointments = appointments;
    }


}
