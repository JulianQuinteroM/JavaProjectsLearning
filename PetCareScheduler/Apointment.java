package PetCareScheduler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a pet care appointment with details about the type, date, time, and notes.
 * This class manages appointment information and provides validation for all fields.
 */
public class Apointment {
    private String appointmentType;      // Type of appointment (e.g., vaccination, checkup)
    private LocalDate appointmentDate;   // Date when the appointment is scheduled
    private LocalTime appointmentTime;   // Time when the appointment is scheduled
    private String notes;                // Additional notes or observations about the appointment


    /**
     * Constructor that initializes an appointment with all required details.
     *
     * @param appointmentType the type of appointment
     * @param appointmentDate the date of the appointment
     * @param appointmentTime the time of the appointment
     * @param notes any additional notes for the appointment
     */
    public Apointment(String appointmentType, LocalDate appointmentDate, LocalTime appointmentTime, String notes) {
        this.appointmentType = appointmentType;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.notes = notes;
    }

    /**
     * Default no-argument constructor for creating an empty appointment.
     */
    public Apointment() {
    }

    /**
     * Gets the appointment type.
     *
     * @return the appointment type
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * Sets the appointment type with validation.
     * Throws an exception if the type is null or empty.
     *
     * @param appointmentType the appointment type to set
     * @throws IllegalArgumentException if appointmentType is null or empty
     */
    public void setAppointmentType(String appointmentType) {
        if (appointmentType == null || appointmentType.trim().isEmpty()) {
            throw new IllegalArgumentException("Appointment type cannot be null or empty");
        }
        this.appointmentType = appointmentType;
    }

    /**
     * Gets the appointment date.
     *
     * @return the appointment date
     */
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * Sets the appointment date with validation.
     * Throws an exception if the date is null.
     *
     * @param appointmentDate the appointment date to set
     * @throws IllegalArgumentException if appointmentDate is null
     */
    public void setAppointmentDate(LocalDate appointmentDate) {
        if (appointmentDate == null) {
            throw new IllegalArgumentException("Appointment date cannot be null");
        }
        this.appointmentDate = appointmentDate;
    }

    /**
     * Gets the appointment time.
     *
     * @return the appointment time
     */
    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    /**
     * Sets the appointment time with validation.
     * Throws an exception if the time is null.
     *
     * @param appointmentTime the appointment time to set
     * @throws IllegalArgumentException if appointmentTime is null
     */
    public void setAppointmentTime(LocalTime appointmentTime) {
        if (appointmentTime == null) {
            throw new IllegalArgumentException("Appointment time cannot be null");
        }
        this.appointmentTime = appointmentTime;
    }

    /**
     * Gets the appointment notes.
     *
     * @return the notes, or null if none were provided
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the appointment notes with trimming.
     * Null notes are stored as null; otherwise, leading/trailing whitespace is removed.
     *
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = (notes != null) ? notes.trim() : null;
    }

    /**
     * Returns a string representation of the appointment.
     *
     * @return a formatted string containing all appointment details
     */
    @Override
    public String toString() {
        return "Apointment{" +
                "appointmentType='" + appointmentType + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", notes='" + notes + '\'' +
                '}';
    }

    /**
     * Compares two appointments for equality based on type, date, and time.
     * Notes are not considered in the comparison.
     *
     * @param o the object to compare with
     * @return true if appointments have the same type, date, and time
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apointment that = (Apointment) o;
        return appointmentType.equals(that.appointmentType) &&
                appointmentDate.equals(that.appointmentDate) &&
                appointmentTime.equals(that.appointmentTime);
    }

    /**
     * Generates a hash code for the appointment based on type, date, and time.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(appointmentType, appointmentDate, appointmentTime);
    }
}