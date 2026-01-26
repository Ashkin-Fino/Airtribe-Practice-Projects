package com.airtribe.learntrack.service;

import com.airtribe.learntrack.utils.Utils;
import com.airtribe.learntrack.entity.Batch;
import com.airtribe.learntrack.ui.View;

public class BatchService {
    
    public BatchService() {
        // Default constructor
    }

    public void menu() {
        boolean back = false;
        // infinite loop to keep the application running
        while (!back) {
            View.batchSubmenuView();
            int userInput = Utils.getUserInput(1, 5);
            switch (userInput) {
                case 1:
                    this.addBatch();
                    break;
                case 2:
                    this.searchBatch();
                    break;
                case 3:
                    this.deactivateBatch();
                    break;
                case 4:
                    this.viewAllBatches();
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

    private Batch addBatch() {
        return null;
    }

    private Batch searchBatch() {
        return null;
    }

    private Batch deactivateBatch() {
        return null;
    }

    private Batch viewAllBatches() {
        return null;
    }
}
