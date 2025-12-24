package PetCareScheduler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Apointment {
    private String appointmentType;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String notes;


    public Apointment(String appointmentType, LocalDate appointmentDate, LocalTime appointmentTime, String notes) {
        this.appointmentType = appointmentType;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.notes = notes;
    }

    public Apointment() {
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        if (appointmentType == null || appointmentType.trim().isEmpty()) {
            throw new IllegalArgumentException("Appointment type cannot be null or empty");
        }
        this.appointmentType = appointmentType;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        if (appointmentDate == null) {
            throw new IllegalArgumentException("Appointment date cannot be null");
        }
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        if (appointmentTime == null) {
            throw new IllegalArgumentException("Appointment time cannot be null");
        }
        this.appointmentTime = appointmentTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = (notes != null) ? notes.trim() : null;
    }


    @Override
    public String toString() {
        return "Apointment{" +
                "appointmentType='" + appointmentType + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", notes='" + notes + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apointment that = (Apointment) o;
        return appointmentType.equals(that.appointmentType) &&
                appointmentDate.equals(that.appointmentDate) &&
                appointmentTime.equals(that.appointmentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentType, appointmentDate, appointmentTime);
    }
}
