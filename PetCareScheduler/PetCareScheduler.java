package PetCareScheduler;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class PetCareScheduler {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Pet> pets = new ArrayList<>();
    private static final List<Apointment> appointments = new ArrayList<>();
    private static final String PETS_FILE = "pets.txt";
    private static final String APPOINTMENTS_FILE = "appointments.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args) {
        loadDataFromFiles();

        boolean running = true;
        while (running) {
            try {
                displayMenu();
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        registerPet();
                        break;
                    case 2:
                        scheduleAppointment();
                        break;
                    case 3:
                        storeData();
                        break;
                    case 4:
                        displayRecords();
                        break;
                    case 5:
                        generateReports();
                        break;
                    case 6:
                        running = false;
                        System.out.println("Exiting application. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                scanner.nextLine(); // Clear buffer
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n=== Pet Care Scheduler ===");
        System.out.println("1. Register Pet");
        System.out.println("2. Schedule Appointment");
        System.out.println("3. Store Data");
        System.out.println("4. Display Records");
        System.out.println("5. Generate Reports");
        System.out.println("6. Exit");
        System.out.println("==========================");
    }

    private static void registerPet() {
        try {
            System.out.println("\n--- Register New Pet ---");

            String petID = getStringInput("Enter Pet ID: ");
            if (findPetByID(petID) != null) {
                System.out.println("Error: Pet with ID " + petID + " already exists.");
                return;
            }

            String petName = getStringInput("Enter Pet Name: ");
            String specieBreed = getStringInput("Enter Species/Breed: ");
            int petAge = getIntInput("Enter Pet Age: ");
            String ownerName = getStringInput("Enter Owner Name: ");
            String contactInfo = getStringInput("Enter Contact Info: ");
            LocalDate dateOfRegistration = LocalDate.now();

            Pet pet = new Pet(petID, petName, specieBreed, petAge, ownerName, contactInfo, dateOfRegistration);
            pets.add(pet);

            System.out.println("Pet registered successfully!");
        } catch (Exception e) {
            System.out.println("Error registering pet: " + e.getMessage());
        }
    }

    private static void scheduleAppointment() {
        try {
            if (pets.isEmpty()) {
                System.out.println("No pets registered. Please register a pet first.");
                return;
            }

            System.out.println("\n--- Schedule Appointment ---");

            String petID = getStringInput("Enter Pet ID: ");
            Pet pet = findPetByID(petID);

            if (pet == null) {
                System.out.println("Error: Pet with ID " + petID + " not found.");
                return;
            }

            String appointmentType = getStringInput("Enter Appointment Type (e.g., Checkup, Vaccination): ");
            List<String> validTypes = Arrays.asList("Checkup", "Vaccination", "Surgery", "Emergency", "Grooming");
            if (!validTypes.contains(appointmentType)) {
                System.out.println("Invalid appointment type. Valid types: " + validTypes);
                return;
            }
            LocalDate appointmentDate = getDateInput("Enter Appointment Date (yyyy-MM-dd): ");
            LocalTime appointmentTime = getTimeInput("Enter Appointment Time (HH:mm): ");
            LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentTime);
            if (appointmentDateTime.isBefore(LocalDateTime.now())) {
                System.out.println("Error: Appointment must be scheduled for a future date and time.");
                return;
            }

            String notes = getStringInput("Enter Notes (optional): ");
            Apointment appointment = new Apointment(appointmentType, appointmentDate, appointmentTime, notes);
            pet.getAppointments().add(appointment);
            appointments.add(appointment);

            System.out.println("Appointment scheduled successfully for " + pet.getPetName() + "!");
        } catch (Exception e) {
            System.out.println("Error scheduling appointment: " + e.getMessage());
        }
    }

    private static void storeData() {
        try {
            savePetsToFile();
            saveAppointmentsToFile();
            System.out.println("Data stored successfully!");
        } catch (IOException e) {
            System.out.println("Error storing data: " + e.getMessage());
        }
    }

    private static void displayRecords() {
        System.out.println("\n--- Display Records ---");
        System.out.println("1. Display All Pets");
        System.out.println("2. Display All Appointments");
        System.out.println("3. Display Pet Details");

        int choice = getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                displayAllPets();
                break;
            case 2:
                displayAllAppointments();
                break;
            case 3:
                displayPetDetails();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void displayAllPets() {
        if (pets.isEmpty()) {
            System.out.println("No pets registered.");
            return;
        }

        System.out.println("\n=== All Pets ===");
        for (Pet pet : pets) {
            System.out.println("ID: " + pet.getPetID() + " | Name: " + pet.getPetName() +
                    " | Species/Breed: " + pet.getSpecieBreed() +
                    " | Owner: " + pet.getOwnerName());
        }
    }

    private static void displayAllAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments scheduled.");
            return;
        }

        System.out.println("\n=== All Appointments ===");
        for (Apointment apt : appointments) {
            System.out.println(apt);
        }
    }

    private static void displayPetDetails() {
        String petID = getStringInput("Enter Pet ID: ");
        Pet pet = findPetByID(petID);

        if (pet == null) {
            System.out.println("Pet not found.");
            return;
        }

        System.out.println("\n=== Pet Details ===");
        System.out.println("ID: " + pet.getPetID());
        System.out.println("Name: " + pet.getPetName());
        System.out.println("Species/Breed: " + pet.getSpecieBreed());
        System.out.println("Age: " + pet.getPetAge());
        System.out.println("Owner: " + pet.getOwnerName());
        System.out.println("Contact: " + pet.getContactInfo());
        System.out.println("Registration Date: " + pet.getDateOfRegistration());
        System.out.println("Appointments: " + pet.getAppointments().size());

        if (!pet.getAppointments().isEmpty()) {
            System.out.println("\nAppointment History:");
            for (Apointment apt : pet.getAppointments()) {
                System.out.println("  - " + apt);
            }
        }
    }

    private static void generateReports() {
        System.out.println("\n--- Generate Reports ---");
        System.out.println("1. Total Pets Report");
        System.out.println("2. Upcoming Appointments Report");
        System.out.println("3. Appointments by Type Report");

        int choice = getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                generateTotalPetsReport();
                break;
            case 2:
                generateUpcomingAppointmentsReport();
                break;
            case 3:
                generateAppointmentsByTypeReport();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void generateOverdueVetVisitReport() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        List<Pet> overduePets = new ArrayList<>();

        for (Pet pet : pets) {
            boolean hasRecentVisit = false;
            for (Apointment apt : pet.getAppointments()) {
                if (apt.getAppointmentDate().isAfter(sixMonthsAgo)) {
                    hasRecentVisit = true;
                    break;
                }
            }
            if (!hasRecentVisit && !pet.getAppointments().isEmpty()) {
                overduePets.add(pet);
            }
        }

        System.out.println("\n=== Pets Overdue for Vet Visit ===");
        System.out.println("Pets without a visit in the last 6 months: " + overduePets.size());
        for (Pet pet : overduePets) {
            System.out.println("ID: " + pet.getPetID() + " | Name: " + pet.getPetName());
        }
    }

    private static void generateTotalPetsReport() {
        System.out.println("\n=== Total Pets Report ===");
        System.out.println("Total Pets Registered: " + pets.size());
        System.out.println("Total Appointments: " + appointments.size());
    }

    private static void generateUpcomingAppointmentsReport() {
        LocalDate today = LocalDate.now();
        List<Apointment> upcoming = new ArrayList<>();

        for (Apointment apt : appointments) {
            if (!apt.getAppointmentDate().isBefore(today)) {
                upcoming.add(apt);
            }
        }

        System.out.println("\n=== Upcoming Appointments ===");
        System.out.println("Total Upcoming: " + upcoming.size());
        for (Apointment apt : upcoming) {
            System.out.println(apt);
        }
    }

    private static void generateAppointmentsByTypeReport() {
        Map<String, Integer> typeCount = new HashMap<>();

        for (Apointment apt : appointments) {
            String type = apt.getAppointmentType();
            typeCount.put(type, typeCount.getOrDefault(type, 0) + 1);
        }

        System.out.println("\n=== Appointments by Type ===");
        for (Map.Entry<String, Integer> entry : typeCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static void loadDataFromFiles() {
        loadPetsFromFile();
        loadAppointmentsFromFile();
    }

    private static void loadPetsFromFile() {
        File file = new File(PETS_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 7) {
                    Pet pet = new Pet(parts[0], parts[1], parts[2],
                            Integer.parseInt(parts[3]), parts[4],
                            parts[5], LocalDate.parse(parts[6], DATE_FORMATTER));
                    pets.add(pet);
                }
            }
            System.out.println("Loaded " + pets.size() + " pets from file.");
        } catch (IOException | NumberFormatException | DateTimeParseException e) {
            System.out.println("Error loading pets: " + e.getMessage());
        }
    }

    private static void savePetsToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PETS_FILE))) {
            for (Pet pet : pets) {
                writer.write(pet.getPetID() + "|" + pet.getPetName() + "|" +
                        pet.getSpecieBreed() + "|" + pet.getPetAge() + "|" +
                        pet.getOwnerName() + "|" + pet.getContactInfo() + "|" +
                        pet.getDateOfRegistration().format(DATE_FORMATTER));
                writer.newLine();
            }
        }
    }

    private static void loadAppointmentsFromFile() {
        File file = new File(APPOINTMENTS_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    Apointment apt = new Apointment(parts[0],
                            LocalDate.parse(parts[1], DATE_FORMATTER),
                            LocalTime.parse(parts[2], TIME_FORMATTER),
                            parts.length > 3 ? parts[3] : "");
                    appointments.add(apt);
                }
            }
            System.out.println("Loaded " + appointments.size() + " appointments from file.");
        } catch (IOException | DateTimeParseException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
    }

    private static void saveAppointmentsToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE))) {
            for (Apointment apt : appointments) {
                writer.write(apt.getAppointmentType() + "|" +
                        apt.getAppointmentDate().format(DATE_FORMATTER) + "|" +
                        apt.getAppointmentTime().format(TIME_FORMATTER) + "|" +
                        (apt.getNotes() != null ? apt.getNotes() : ""));
                writer.newLine();
            }
        }
    }

    private static Pet findPetByID(String petID) {
        for (Pet pet : pets) {
            if (pet.getPetID().equalsIgnoreCase(petID)) {
                return pet;
            }
        }
        return null;
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    private static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }

    private static LocalTime getTimeInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalTime.parse(scanner.nextLine().trim(), TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use HH:mm.");
            }
        }
    }
}
