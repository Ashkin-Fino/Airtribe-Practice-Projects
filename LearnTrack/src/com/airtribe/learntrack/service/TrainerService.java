package com.airtribe.learntrack.service;

import com.airtribe.learntrack.utils.Utils;
import com.airtribe.learntrack.entity.Trainer;
import com.airtribe.learntrack.ui.View;

public class TrainerService {
    
    public TrainerService() {
        // Default constructor
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

    private Trainer addTrainer() {
        return null;
    }

    private Trainer searchTrainer() {
        return null;
    }

    private Trainer deactivateTrainer() {
        return null;
    }

    private Trainer viewAllTrainers() {
        return null;
    }
}
