package PetCareScheduler;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Main application class for the Pet Care Scheduler system.
 * Manages pet registration, appointment scheduling, data persistence, and reporting.
 * This application allows users to register pets, schedule veterinary appointments,
 * store/load data from files, and generate various reports about pets and appointments.
 */
public class PetCareScheduler {
    // Static Scanner for user input across the application
    private static final Scanner scanner = new Scanner(System.in);

    // In-memory collections to store pets and appointments during runtime
    private static final List<Pet> pets = new ArrayList<>();
    private static final List<Apointment> appointments = new ArrayList<>();

    // File names for data persistence
    private static final String PETS_FILE = "pets.txt";
    private static final String APPOINTMENTS_FILE = "appointments.txt";

    // Date and time formatters for consistent serialization/deserialization
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Main entry point of the application.
     * Loads existing data from files, displays a menu loop, processes user choices,
     * and handles exceptions gracefully throughout the session.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Load previously saved pet and appointment data from files
        loadDataFromFiles();

        // Main application loop - continues until user chooses to exit
        boolean running = true;
        while (running) {
            try {
                displayMenu();
                int choice = getIntInput("Enter your choice: ");

                // Process user's menu selection
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
                scanner.nextLine(); // Clear input buffer to prevent infinite loops
            }
        }

        // Clean up resources
        scanner.close();
    }

    /**
     * Displays the main menu with available operations.
     */
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

    /**
     * Registers a new pet in the system.
     * Collects pet information from the user, validates that the pet ID is unique,
     * creates a Pet object, and adds it to the pets list.
     * Registration date is automatically set to the current date.
     */
    private static void registerPet() {
        try {
            System.out.println("\n--- Register New Pet ---");

            // Get pet ID and check for duplicates
            String petID = getStringInput("Enter Pet ID: ");
            if (findPetByID(petID) != null) {
                System.out.println("Error: Pet with ID " + petID + " already exists.");
                return;
            }

            // Collect pet information from user
            String petName = getStringInput("Enter Pet Name: ");
            String specieBreed = getStringInput("Enter Species/Breed: ");
            int petAge = getIntInput("Enter Pet Age: ");
            String ownerName = getStringInput("Enter Owner Name: ");
            String contactInfo = getStringInput("Enter Contact Info: ");
            LocalDate dateOfRegistration = LocalDate.now(); // Current date

            // Create and add new pet to the system
            Pet pet = new Pet(petID, petName, specieBreed, petAge, ownerName, contactInfo, dateOfRegistration);
            pets.add(pet);

            System.out.println("Pet registered successfully!");
        } catch (Exception e) {
            System.out.println("Error registering pet: " + e.getMessage());
        }
    }

    /**
     * Schedules a new appointment for a registered pet.
     * Validates that a pet exists, verifies appointment type, ensures appointment
     * is in the future, and adds the appointment to both the pet's and global appointments list.
     */
    private static void scheduleAppointment() {
        try {
            // Check if any pets are registered
            if (pets.isEmpty()) {
                System.out.println("No pets registered. Please register a pet first.");
                return;
            }

            System.out.println("\n--- Schedule Appointment ---");

            // Find the pet by ID
            String petID = getStringInput("Enter Pet ID: ");
            Pet pet = findPetByID(petID);

            if (pet == null) {
                System.out.println("Error: Pet with ID " + petID + " not found.");
                return;
            }

            // Get and validate appointment type
            String appointmentType = getStringInput("Enter Appointment Type (e.g., Checkup, Vaccination): ");
            List<String> validTypes = Arrays.asList("Checkup", "Vaccination", "Surgery", "Emergency", "Grooming");
            if (!validTypes.contains(appointmentType)) {
                System.out.println("Invalid appointment type. Valid types: " + validTypes);
                return;
            }

            // Get appointment date and time
            LocalDate appointmentDate = getDateInput("Enter Appointment Date (yyyy-MM-dd): ");
            LocalTime appointmentTime = getTimeInput("Enter Appointment Time (HH:mm): ");
            LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentTime);

            // Validate that appointment is scheduled for the future
            if (appointmentDateTime.isBefore(LocalDateTime.now())) {
                System.out.println("Error: Appointment must be scheduled for a future date and time.");
                return;
            }

            // Get optional notes and create appointment
            String notes = getStringInput("Enter Notes (optional): ");
            Apointment appointment = new Apointment(appointmentType, appointmentDate, appointmentTime, notes);

            // Add appointment to both pet's appointment list and global appointments list
            pet.getAppointments().add(appointment);
            appointments.add(appointment);

            System.out.println("Appointment scheduled successfully for " + pet.getPetName() + "!");
        } catch (Exception e) {
            System.out.println("Error scheduling appointment: " + e.getMessage());
        }
    }

    /**
     * Saves all pet and appointment data to files.
     * This persists the current state so data is not lost when the application closes.
     */
    private static void storeData() {
        try {
            savePetsToFile();
            saveAppointmentsToFile();
            System.out.println("Data stored successfully!");
        } catch (IOException e) {
            System.out.println("Error storing data: " + e.getMessage());
        }
    }

    /**
     * Displays a submenu for viewing different types of records.
     * Options include viewing all pets, all appointments, or details for a specific pet.
     */
    private static void displayRecords() {
        System.out.println("\n--- Display Records ---");
        System.out.println("1. Display All Pets");
        System.out.println("2. Display All Appointments");
        System.out.println("3. Display Pet Details");

        int choice = getIntInput("Enter your choice: ");

        // Process user's record display choice
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

    /**
     * Displays a summary list of all registered pets.
     * Shows pet ID, name, species/breed, and owner name for each pet.
     */
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

    /**
     * Displays all scheduled appointments in the system.
     * Shows detailed information for each appointment.
     */
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

    /**
     * Displays comprehensive details for a specific pet.
     * Includes personal information, registration date, and complete appointment history.
     */
    private static void displayPetDetails() {
        String petID = getStringInput("Enter Pet ID: ");
        Pet pet = findPetByID(petID);

        if (pet == null) {
            System.out.println("Pet not found.");
            return;
        }

        // Display all pet information
        System.out.println("\n=== Pet Details ===");
        System.out.println("ID: " + pet.getPetID());
        System.out.println("Name: " + pet.getPetName());
        System.out.println("Species/Breed: " + pet.getSpecieBreed());
        System.out.println("Age: " + pet.getPetAge());
        System.out.println("Owner: " + pet.getOwnerName());
        System.out.println("Contact: " + pet.getContactInfo());
        System.out.println("Registration Date: " + pet.getDateOfRegistration());
        System.out.println("Appointments: " + pet.getAppointments().size());

        // Display appointment history if appointments exist
        if (!pet.getAppointments().isEmpty()) {
            System.out.println("\nAppointment History:");
            for (Apointment apt : pet.getAppointments()) {
                System.out.println("  - " + apt);
            }
        }
    }

    /**
     * Displays a submenu for generating various reports.
     * Options include total pets, upcoming appointments, and appointments by type.
     */
    private static void generateReports() {
        System.out.println("\n--- Generate Reports ---");
        System.out.println("1. Total Pets Report");
        System.out.println("2. Upcoming Appointments Report");
        System.out.println("3. Appointments by Type Report");

        int choice = getIntInput("Enter your choice: ");

        // Process user's report choice
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

    /**
     * Generates a report of pets that haven't had a veterinary visit in the last 6 months.
     * This helps identify pets that may be overdue for checkups.
     */
    private static void generateOverdueVetVisitReport() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        List<Pet> overduePets = new ArrayList<>();

        // Find pets without recent appointments
        for (Pet pet : pets) {
            boolean hasRecentVisit = false;
            for (Apointment apt : pet.getAppointments()) {
                if (apt.getAppointmentDate().isAfter(sixMonthsAgo)) {
                    hasRecentVisit = true;
                    break;
                }
            }
            // Add pet to overdue list if no recent visit and has appointments
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

    /**
     * Generates a summary report showing total number of registered pets
     * and total number of scheduled appointments.
     */
    private static void generateTotalPetsReport() {
        System.out.println("\n=== Total Pets Report ===");
        System.out.println("Total Pets Registered: " + pets.size());
        System.out.println("Total Appointments: " + appointments.size());
    }

    /**
     * Generates a report of all upcoming appointments (today and future dates).
     * Filters appointments and displays them chronologically.
     */
    private static void generateUpcomingAppointmentsReport() {
        LocalDate today = LocalDate.now();
        List<Apointment> upcoming = new ArrayList<>();

        // Filter appointments that are today or in the future
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

    /**
     * Generates a report showing the count of appointments by type.
     * Groups appointments (Checkup, Vaccination, Surgery, etc.) and shows frequency.
     */
    private static void generateAppointmentsByTypeReport() {
        Map<String, Integer> typeCount = new HashMap<>();

        // Count appointments by type
        for (Apointment apt : appointments) {
            String type = apt.getAppointmentType();
            typeCount.put(type, typeCount.getOrDefault(type, 0) + 1);
        }

        System.out.println("\n=== Appointments by Type ===");
        for (Map.Entry<String, Integer> entry : typeCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Loads all persisted data from files at application startup.
     * Delegates to specific loader methods for pets and appointments.
     */
    private static void loadDataFromFiles() {
        loadPetsFromFile();
        loadAppointmentsFromFile();
    }

    /**
     * Loads pet data from the pets file.
     * Parses pipe-delimited format and creates Pet objects.
     * Silently returns if file doesn't exist (first run scenario).
     */
    private static void loadPetsFromFile() {
        File file = new File(PETS_FILE);
        if (!file.exists()) {
            return; // File doesn't exist, skip loading
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse pipe-delimited pet data
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

    /**
     * Saves all pets to the pets file in pipe-delimited format.
     * Creates or overwrites the file with current pet data.
     *
     * @throws IOException if file write operation fails
     */
    private static void savePetsToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PETS_FILE))) {
            for (Pet pet : pets) {
                // Format: petID|petName|specieBreed|age|ownerName|contactInfo|registrationDate
                writer.write(pet.getPetID() + "|" + pet.getPetName() + "|" +
                        pet.getSpecieBreed() + "|" + pet.getPetAge() + "|" +
                        pet.getOwnerName() + "|" + pet.getContactInfo() + "|" +
                        pet.getDateOfRegistration().format(DATE_FORMATTER));
                writer.newLine();
            }
        }
    }

    /**
     * Loads appointment data from the appointments file.
     * Parses pipe-delimited format and creates Appointment objects.
     * Silently returns if file doesn't exist (first run scenario).
     */
    private static void loadAppointmentsFromFile() {
        File file = new File(APPOINTMENTS_FILE);
        if (!file.exists()) {
            return; // File doesn't exist, skip loading
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse pipe-delimited appointment data
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

    /**
     * Saves all appointments to the appointments file in pipe-delimited format.
     * Creates or overwrites the file with current appointment data.
     *
     * @throws IOException if file write operation fails
     */
    private static void saveAppointmentsToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE))) {
            for (Apointment apt : appointments) {
                // Format: appointmentType|date|time|notes
                writer.write(apt.getAppointmentType() + "|" +
                        apt.getAppointmentDate().format(DATE_FORMATTER) + "|" +
                        apt.getAppointmentTime().format(TIME_FORMATTER) + "|" +
                        (apt.getNotes() != null ? apt.getNotes() : ""));
                writer.newLine();
            }
        }
    }

    /**
     * Searches for a pet by its ID in the pets list.
     * Case-insensitive search for convenience.
     *
     * @param petID the ID to search for
     * @return the Pet object if found, null otherwise
     */
    private static Pet findPetByID(String petID) {
        for (Pet pet : pets) {
            if (pet.getPetID().equalsIgnoreCase(petID)) {
                return pet;
            }
        }
        return null;
    }

    /**
     * Prompts user for string input and returns trimmed result.
     *
     * @param prompt the message to display to the user
     * @return the user's input with leading/trailing whitespace removed
     */
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Prompts user for integer input with validation.
     * Continues prompting until valid integer is provided.
     *
     * @param prompt the message to display to the user
     * @return the valid integer entered by the user
     */
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

    /**
     * Prompts user for date input in yyyy-MM-dd format.
     * Continues prompting until valid date is provided.
     *
     * @param prompt the message to display to the user
     * @return the valid LocalDate entered by the user
     */
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

    /**
     * Prompts user for time input in HH:mm format (24-hour).
     * Continues prompting until valid time is provided.
     *
     * @param prompt the message to display to the user
     * @return the valid LocalTime entered by the user
     */
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

