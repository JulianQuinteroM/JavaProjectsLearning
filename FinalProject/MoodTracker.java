package FinalProject;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;

public class MoodTracker {
    public static void main(String[] args) {
        System.out.println("This is the Mood Tracker application.");
        Scanner scanner = new Scanner(System.in);
        ArrayList<Mood> moodsList = new ArrayList<>();

        while(true) {
            System.out.println("Press 'a' to add mood\n" +
                                "'d' to delete mood(s)\n" +
                                "'e' to edit mood\n" +
                                "'s' to search for moods\n" +
                                "'M' to get all moods\n" +
                                "'w' to write the moods to a file\n" +
                                "Type 'Exit' to exit");
            String menuOption = scanner.nextLine();
            switch(menuOption) {
                case "a":
                    // Code to add mood
                    System.out.println("Enter the mood name");
                    String moodName = scanner.nextLine();
                    System.out.println("Are you tracking the mood for a current day? y/n");
                    String isForCurrentDate = scanner.nextLine();
                    Mood moodToAdd = null;
                    if(isForCurrentDate.equalsIgnoreCase("n")) {
                        try {
                            System.out.println("Input the date in MM/dd/yyyy format:");
                            String moodDateStr = scanner.nextLine();
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                            LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);
                            System.out.println("Input the time in HH:mm:ss format:");
                            String moodTimeStr = scanner.nextLine();
                            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                            LocalTime moodTime = LocalTime.parse(moodTimeStr, timeFormatter);
                            System.out.println("Add notes about this mood");
                            String moodNotes = scanner.nextLine();
                            if(moodNotes.strip().equalsIgnoreCase("")) {
                                moodToAdd = new Mood(moodName, moodDate, moodTime);
                            } else {
                                moodToAdd = new Mood(moodName, moodDate, moodTime, moodNotes);
                            }
                        } catch (DateTimeParseException dfe) {
                            System.out.println("Incorrect format of date or time. Cannot create mood.\n"+dfe);
                            continue;
                        }
                    } else {
                        System.out.println("Add notes about this mood");
                        String moodNotes = scanner.nextLine();
                        if(moodNotes.strip().equalsIgnoreCase("")) {
                            moodToAdd = new Mood(moodName);
                        } else {
                            moodToAdd = new Mood(moodName, moodNotes);
                        }
                    }
                    if (moodToAdd == null) {
                        System.out.println("Unable to create the mood entry");
                        continue;
                    }
                    try {
                        boolean isValid = isMoodValid(moodToAdd, moodsList);
                        if(isValid) {
                            moodsList.add(moodToAdd);
                            System.out.println("The mood has been added to the tracker");
                            continue;
                        }
                    } catch(InvalidMoodException ime) {
                        System.out.println("The mood is not valid");
                    }
                    break;
                case "d":
                    // Code to delete mood(s)
                    System.out.println("Enter '1' to delete all moods by date\n"+
                    "Enter '2' to delete a specific mood");
                    String deleteVariant = scanner.nextLine();
                    if(deleteVariant.equals("1")) {
                        try {
                            System.out.println("Input the date in MM/dd/yyyy format:");
                            String moodDateStr = scanner.nextLine();
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                            LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);
                            boolean areMoodsDeleted = deleteMoods(moodDate, moodsList);
                            if(areMoodsDeleted) {
                                System.out.println("The moods have been deleted");
                            } else {
                                System.out.println("No matching moods found");
                            }
                        } catch (DateTimeParseException dfe) {
                            System.out.println("Incorrect format of date. Cannot delete mood.");
                            continue;
                        }
                    } else if (deleteVariant.equals("2")) {
                        try {
                            System.out.println("Enter the mood name");
                            moodName = scanner.nextLine();
                            System.out.println("Input the date in MM/dd/yyyy format:");
                            String moodDateStr = scanner.nextLine();
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                            LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);
                            System.out.println("Input the time in HH:mm:ss format:");
                            String moodTimeStr = scanner.nextLine();
                            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                            LocalTime moodTime = LocalTime.parse(moodTimeStr, timeFormatter);
                            Mood delMood = new Mood(moodName, moodDate, moodTime);
                            boolean isMoodDeleted = deleteMood(delMood, moodsList);
                            if(isMoodDeleted) {
                                System.out.println("The mood has been deleted");
                            } else {
                                System.out.println("No matching mood found");
                            }
                        } catch (DateTimeParseException dfe) {
                            System.out.println("Incorrect format of date or time. Cannot delete mood.");
                            continue;
                        }
                    }
                    break;
                case "e":
                    // Code to edit mood
                    Mood moodToEdit = null;
                    try {
                        System.out.println("Enter the mood name");
                        moodName = scanner.nextLine();
                        System.out.println("Input the date in MM/dd/yyyy format:");
                        String moodDateStr = scanner.nextLine();
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);
                        System.out.println("Input the time in HH:mm:ss format:");
                        String moodTimeStr = scanner.nextLine();
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        LocalTime moodTime = LocalTime.parse(moodTimeStr, timeFormatter);
                        System.out.println("Add new notes about this mood");
                        String moodNotes = scanner.nextLine();
                        if(moodNotes.strip().equalsIgnoreCase("")) {
                            System.out.println("No notes entered");
                            continue;
                        } else {
                            moodToEdit = new Mood(moodName, moodDate, moodTime, moodNotes);
                            boolean isMoodEdited = editMood(moodToEdit, moodsList);
                            if(isMoodEdited) {
                                System.out.println("The mood has been successfully edited");
                            } else {
                                System.out.println("No matching mood could be found");
                            }
                        }
                    } catch (DateTimeParseException dfe) {
                        System.out.println("Incorrect format of date or time. Cannot create mood.");
                        continue;
                    }
                    break;
                case "s":
                    // Code to search for moods
                    System.out.println("Enter '1' to search for all moods by date\n"+
                                                "Enter '2' to search for a specific mood");
                    String searchVariant = scanner.nextLine();
                    if(searchVariant.equals("1")) {
                        try {
                            System.out.println("Input the date in MM/dd/yyyy format:");
                            String moodDateStr = scanner.nextLine();
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                            LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);
                            searchMoods(moodDate, moodsList);
                        } catch (DateTimeParseException dfe) {
                            System.out.println("Incorrect format of date. Cannot search mood.");
                            continue;
                        }
                    } else if (searchVariant.equals("2")) {
                        try {
                            System.out.println("Enter the mood name");
                            moodName = scanner.nextLine();
                            System.out.println("Input the date in MM/dd/yyyy format:");
                            String moodDateStr = scanner.nextLine();
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                            LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);
                            System.out.println("Input the time in HH:mm:ss format:");
                            String moodTimeStr = scanner.nextLine();
                            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                            LocalTime moodTime = LocalTime.parse(moodTimeStr, timeFormatter);
                            Mood delMood = new Mood(moodName, moodDate, moodTime);
                            searchMood(delMood, moodsList);
                        } catch (DateTimeParseException dfe) {
                            System.out.println("Incorrect format of date or time. Cannot search mood.");
                            continue;
                        }
                    }
                    break;
                case "M":
                    // Code to get all moods
                    for(Mood moodObj: moodsList) {
                        System.out.println(moodObj);
                    }
                    break;
                case "w":
                    // Code to write moods to a file
                    try (PrintWriter writer = new PrintWriter(new FileWriter("FinalProject/Moods.txt"))) {
                    for (Mood mood : moodsList) {
                        writer.println(mood+"\n\n");
                    }
                    System.out.println("The entries are written to a file");
                } catch (IOException e) {
                    System.err.println("Error writing to file: " + e.getMessage());
                }
                    break;
                case "Exit":
                    System.out.println("Exiting Mood Tracker. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        
    }

    public static boolean isMoodValid(Mood mood, ArrayList<Mood> moodsList) throws InvalidMoodException {
        for(Mood tempMood: moodsList) {
            if (tempMood.equals(mood)) {
                throw new InvalidMoodException();
            }
        }
        return true;
    }

    public static boolean deleteMoods(LocalDate moodDate, ArrayList<Mood> moodsList) {
        boolean removed = false;
        java.util.Iterator<Mood> iterator = moodsList.iterator();
        while (iterator.hasNext()) {
            Mood tempMood = iterator.next();
            if (tempMood.getDate().equals(moodDate)) {
                iterator.remove();
                removed = true;
            }
        }
        return removed;
    }

    public static boolean deleteMood(Mood mood, ArrayList<Mood> moodsList) {
        java.util.Iterator<Mood> iterator = moodsList.iterator();
        while (iterator.hasNext()) {
            Mood tempMood = iterator.next();
            if (tempMood.equals(mood)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }


    public static boolean editMood(Mood moodToEdit, ArrayList<Mood> moodsList) {
        for(Mood tempMood: moodsList) {
            if (tempMood.equals(moodToEdit)) {
                tempMood.setNotes(moodToEdit.getNotes());
                return true;
            }
        }
        return false;
    }
    
    public static void searchMoods(LocalDate moodDate, ArrayList<Mood> moodsList) {
        boolean found = false;
        for(Mood tempMood: moodsList) {
            if (tempMood.getDate().equals(moodDate)) {
                found = true;
                System.out.println(tempMood);
            }
        }
        if(!found) {
            System.out.println("No matching records could be found!");
        }
    }

    public static void searchMood(Mood mood, ArrayList<Mood> moodsList) {
        boolean found = false;
        for(Mood tempMood: moodsList) {
            if (tempMood.equals(mood)) {
                found = true;
                System.out.println(tempMood);
            }
        }
        if(!found) {
            System.out.println("No matching records could be found!");
        }
    }
    
}
