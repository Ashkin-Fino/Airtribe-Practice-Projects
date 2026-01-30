package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Trainer;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class TrainerService {
    
    private final Repository repository;

    public TrainerService(Repository repository) {
        this.repository = repository;
    }

    public void menu() {
        boolean back = false;
        // infinite loop to keep the application running
        while (!back) {
            View.trainerSubmenuView();
            int userInput = Utils.getUserInput(1, 5);
            switch (userInput) {
                case 1:
                    this.addTrainer();
                    break;
                case 2:
                    this.searchTrainer();
                    break;
                case 3:
                    this.deactivateTrainer();
                    break;
                case 4:
                    this.viewAllTrainers();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    // 1. Add Trainer
    private Trainer addTrainer() {
        System.out.println("Enter First Name: ");
        String firstName = Utils.getStringInput();

        System.out.println("Enter Last Name: ");
        String lastName = Utils.getStringInput();

        System.out.println("Enter Email: ");
        String email = Utils.getStringInput();

        System.out.println("Enter Age: ");
        int age = Utils.getIntInput();

        System.out.println("Enter Specialisation: ");
        String specialisation = Utils.getStringInput();

        Trainer trainer = new Trainer(firstName, lastName, email, age, specialisation);
        repository.trainers.add(trainer);

        System.out.println("✅ Trainer added successfully: " + trainer);
        return trainer;
    }

    // 2. Search Trainer by ID
    private Trainer searchTrainer() {
        System.out.println("Enter Trainer ID to search: ");
        String id = Utils.getStringInput();

        for (Trainer trainer : repository.trainers) {
            if (trainer.getTrainer_id().equalsIgnoreCase(id)) {
                System.out.println("🔍 Trainer found: " + trainer);
                return trainer;
            }
        }
        System.out.println("⚠️ Trainer not found with ID: " + id);
        return null;
    }

    // 3. Deactivate Trainer
    private Trainer deactivateTrainer() {
        System.out.println("Enter Trainer ID to deactivate: ");
        String id = Utils.getStringInput();

        for (Trainer trainer : repository.trainers) {
            if (trainer.getTrainer_id().equalsIgnoreCase(id)) {
                trainer.setIs_active(false);
                System.out.println("🚫 Trainer deactivated: " + trainer);
                return trainer;
            }
        }
        System.out.println("⚠️ Trainer not found with ID: " + id);
        return null;
    }

    // 4. View All Trainers
    private void viewAllTrainers() {
        if (repository.trainers.isEmpty()) {
            System.out.println("📭 No trainers available.");
        } else {
            System.out.println("📋 List of all trainers:");
            for (Trainer trainer : repository.trainers) {
                System.out.println(trainer);
            }
        }
    }
}
