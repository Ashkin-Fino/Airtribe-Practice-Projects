package com.airtribe.learntrack.service.searchservice;

import java.util.ArrayList;
import java.util.List;

import com.airtribe.learntrack.entity.Trainer;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class TrainerSearchService implements Searchable<Trainer> {

    private final Repository repository;
    
    public TrainerSearchService(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Trainer search() {
        Trainer result = null;
        View.trainerSearchSubmenuView();
        int userInput = Utils.getUserInput(1, 4);
        switch(userInput) {
            case 1:
                result = this.searchById();
                break;
            case 2:
                result = this.searchByName();
                break;
            case 3:
                result = this.searchBySpecialisation();
                break;
            case 4:
                System.out.println("Returning to Trainer menu.");
                break;
            default:
                System.out.println("Invalid Choice. Returning to Trainer menu.");
                break;
        }
        return result;
    }

    private Trainer searchById() {
        System.out.println("Enter Trainer ID*: ");
        String id = Utils.getStringInput(true);
    
        for (Trainer trainer : repository.getTrainers()) {
            if (trainer.getId().equals(id)) {
                System.out.println("Trainer found: " + trainer);
                return trainer;
            }
        }
    
        System.out.println("Trainer not found.");
        return null;
    }
    
    private Trainer searchByName() {
        System.out.println("Enter Trainer Name*: ");
        String name = Utils.getStringInput(true).toLowerCase();
    
        List<Trainer> matchedTrainers = new ArrayList<>();
    
        for (Trainer trainer : repository.getTrainers()) {
            String fullName = (trainer.getFirstName() + " " + trainer.getLastName()).toLowerCase();
            if (fullName.toLowerCase().contains(name.toLowerCase())) {
                matchedTrainers.add(trainer);
            }
        }
    
        if (matchedTrainers.isEmpty()) {
            System.out.println("Trainer not found.");
            return null;
        }
    
        // If only one student found
        if (matchedTrainers.size() == 1) {
            System.out.println("Trainer found: " + matchedTrainers.get(0));
            return matchedTrainers.get(0);
        }
    
        // Multiple students found
        System.out.println("Multiple trainers found:");
        for (int i = 0; i < matchedTrainers.size(); i++) {
            System.out.println((i + 1) + ". " + matchedTrainers.get(i));
        }
    
        System.out.println("Select a trainer*:");
        int choice = Utils.getUserInput(1, matchedTrainers.size());
        
        System.out.println("Trainer found: " + matchedTrainers.get(choice - 1));
        return matchedTrainers.get(choice - 1);
    }

    private Trainer searchBySpecialisation() {
        System.out.println("Enter Trainer Specialisation*: ");
        String specialisation = Utils.getStringInput(true).toLowerCase();
    
        List<Trainer> matchedTrainers = new ArrayList<>();
    
        for (Trainer trainer : repository.getTrainers()) {
            if (trainer.getSpecialisation().toLowerCase().contains(specialisation.toLowerCase())) {
                matchedTrainers.add(trainer);
            }
        }
    
        if (matchedTrainers.isEmpty()) {
            System.out.println("No trainers found with this specialisation.");
            return null;
        }
    
        if (matchedTrainers.size() == 1) {
            System.out.println("Trainer found: " + matchedTrainers.get(0));
            return matchedTrainers.get(0);
        }
    
        System.out.println("Multiple trainers found:");
        for (int i = 0; i < matchedTrainers.size(); i++) {
            System.out.println((i + 1) + ". " + matchedTrainers.get(i));
        }
    
        System.out.println("Select a trainer:");
        int choice = Utils.getUserInput(1, matchedTrainers.size());
        
        System.out.println("Trainer found: " + matchedTrainers.get(choice - 1));
        return matchedTrainers.get(choice - 1);
    }
    
}
