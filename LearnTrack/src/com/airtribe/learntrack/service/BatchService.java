package com.airtribe.learntrack.service;

import com.airtribe.learntrack.constants.BatchStatus;
import com.airtribe.learntrack.entity.Batch;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Trainer;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.service.searchservice.BatchSearchService;
import com.airtribe.learntrack.service.searchservice.CourseSearchService;
import com.airtribe.learntrack.service.searchservice.Searchable;
import com.airtribe.learntrack.service.searchservice.TrainerSearchService;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;
import java.time.LocalDate;

public class BatchService {

    private final Repository repository;
    private final Searchable<Batch> batchSearchService;
    private final Searchable<Course> courseSearchService;
    private final Searchable<Trainer> trainerSearchService;

    public BatchService(Repository repository) {
        this.repository = repository;
        this.courseSearchService = new CourseSearchService(repository);
        this.batchSearchService = new BatchSearchService(repository, this.courseSearchService);
        this.trainerSearchService = new TrainerSearchService(repository);
        
    }

    public void menu() {
        boolean back = false;
        while (!back) {
            View.batchSubmenuView();
            int userInput = Utils.getUserInput(1, 8);
            switch (userInput) {
                case 1:
                    this.addBatch();
                    break;
                case 2:
                    this.batchSearchService.search();
                    break;
                case 3:
                    this.cancelBatch();
                    break;
                case 4:
                    this.viewOngoingBatches();
                    break;
                case 5:
                    this.viewCompletedBatches();
                    break;
                case 6:
                    this.assignTrainerToBatch();
                    break;
                case 7:
                    this.viewAllStudentsInBatch();
                    break;
                case 8:
                    this.viewAllBatches();
                    break;
                case 9:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    // 1. Add Batch
    private void addBatch() {
        System.out.println("Enter Batch Name: ");
        String name = Utils.getStringInput(true);

        System.out.println("Enter Start Date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(Utils.getStringInput(true));

        Course selectedCourse = this.courseSearchService.search();

        if (selectedCourse == null) {
            System.out.println("Course not found! Try again with a different course.");
            return;
        }

        try {
            Batch batch = new Batch(name, startDate, selectedCourse);
            repository.addBatch(batch);
            System.out.println("Batch added successfully: " + batch);
        } catch (IllegalArgumentException e) {
            System.out.println("Couldnt create batch. Please try again: " + e.toString());
        }
        return;
    }

    // 3. Cancel Batch
    private void cancelBatch() {
        System.out.println("Identify Batch to cancel...");
        Batch batchToCancel = this.batchSearchService.search();

        if (batchToCancel == null) {
            System.out.println("No batch selected");
        } else {
            System.out.println("Cancelling batch:" + batchToCancel);
            batchToCancel.setStatus("CANCELLED");
        }
        return;
    }

    // 4. View Ongoing batches
    private void viewOngoingBatches() {

        boolean found = false;

        for (Batch batch : repository.getBatches()) {
            if (batch.getStatus() == BatchStatus.ONGOING) {
                if (!found) {
                    System.out.println("Ongoing Batches:");
                    found = true;
                }
                System.out.println(batch);
            }
        }

        if (!found) {
            System.out.println("No ongoing batches found.");
        }
        return;
    }

    // 5. View Completed batches
    private void viewCompletedBatches() {
        
        boolean found = false;

        for (Batch batch : repository.getBatches()) {
            if (batch.getStatus() == BatchStatus.COMPLETED) {
                if (!found) {
                    System.out.println("Completed Batches:");
                    found = true;
                }
                System.out.println(batch);
            }
        }

        if (!found) {
            System.out.println("No completed batches found.");
        }
        return;
    }

    // 5. Assign Trainer to Batch
    private void assignTrainerToBatch() {
        System.out.println("Identify Batch to view students...");
        Batch batch = this.batchSearchService.search();

        System.out.println("Select Trainer to add.");
        Trainer trainer = this.trainerSearchService.search();

        batch.setTrainer(trainer);
        System.out.println("Assigned Trainer: " + trainer.getFullName() + 
            " to Batch: " + batch.getName() + ".");
        return;
    }

    // 7. View all students in batch
    private void viewAllStudentsInBatch() {
        System.out.println("Identify Batch to view students...");
        // Batch batchToViewStudents = this.batchSearchService.search();
        
        //search enrollments. filter based on batch.

        return;
    }

    // 8. View All batches
    private void viewAllBatches() {
        
        boolean found = false;

        System.out.println("All Batches:");
        for (Batch batch : repository.getBatches()) {
            if (!found) {
                System.out.println("All Batches:");
                found = true;
            }
            System.out.println(batch);
        }

        if (!found) {
            System.out.println("No completed batches found.");
        }
        return;
    }
}