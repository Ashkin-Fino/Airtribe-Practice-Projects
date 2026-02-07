package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Trainer;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.service.searchservice.Searchable;
import com.airtribe.learntrack.service.searchservice.TrainerSearchService;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class TrainerService {
    
    private final Repository repository;
    private final Searchable<Trainer> trainerSearchService;

    public TrainerService(Repository repository) {
        this.repository = repository;
        this.trainerSearchService = new TrainerSearchService(repository);
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
                    trainerSearchService.search();
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
    private void addTrainer() {
        System.out.println("Enter First Name*: ");
        String firstName = Utils.getStringInput(true);

        System.out.println("Enter Last Name*: ");
        String lastName = Utils.getStringInput(true);

        System.out.println("Enter Email*: ");
        String email = Utils.getStringInput(true);

        System.out.println("Enter Age: ");
        int age = Utils.getIntInput(false);

        System.out.println("Enter Specialisation*: ");
        String specialisation = Utils.getStringInput(true);

        try {
            Trainer trainer = new Trainer(firstName, lastName, email, age, specialisation);
            repository.addTrainer(trainer);
            System.out.println("Trainer added successfully: " + trainer);
        } catch (Exception e) {
            System.out.println("Couldnt add Trainer. Please Try Again:" + e.toString());
        }
        return;
    }

    // 3. Deactivate Trainer
    private void deactivateTrainer() {
        System.out.println("Identify Student to deactivate...");
        Trainer trainerToDeactivate = this.trainerSearchService.search();

        if (trainerToDeactivate == null) {
            System.out.println("No student selected");
        } else {
            System.out.println("Deactivating Trainer:" + trainerToDeactivate);
            trainerToDeactivate.setActive(false);
        }
        return;
    }

    // 4. View All Trainers
    private void viewAllTrainers() {
        if (repository.getTrainers().isEmpty()) {
            System.out.println("No trainers available.");
        } else {
            System.out.println("List of all trainers:");
            for (Trainer trainer : repository.getTrainers()) {
                System.out.println(trainer);
            }
        }
    }
}
