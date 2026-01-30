package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Batch;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Trainer;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;
import java.time.LocalDate;

public class BatchService {

    private final Repository repository;

    public BatchService(Repository repository) {
        this.repository = repository;
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
                    this.searchBatch();
                    break;
                case 3:
                    this.deactivateBatch();
                    break;
                case 4:
                    this.viewAllbatches();
                    break;
                case 5:
                    this.assignTrainerToBatch();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    // 1. Add Batch
    private Batch addBatch() {
        System.out.println("Enter Batch Name: ");
        String name = Utils.getStringInput();

        System.out.println("Enter Start Date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(Utils.getStringInput());

        System.out.println("Enter Course ID: ");
        String courseId = Utils.getStringInput();

        Course selectedCourse = null;
        for (Course c : repository.courses) {
            if (c.getCourse_id().equalsIgnoreCase(courseId)) {
                selectedCourse = c;
                break;
            }
        }

        if (selectedCourse == null) {
            System.out.println("⚠️ Course not found!");
            return null;
        }

        Batch batch = new Batch(name, startDate, selectedCourse);
        repository.batches.add(batch);
        System.out.println("✅ Batch added successfully: " + batch);
        return batch;
    }

    // 2. Search Batch
    private Batch searchBatch() {
        System.out.println("Enter Batch ID: ");
        String id = Utils.getStringInput();

        for (Batch batch : repository.batches) {
            if (batch.getId().equalsIgnoreCase(id)) {
                System.out.println("🔍 Found: " + batch);
                return batch;
            }
        }
        System.out.println("⚠️ Batch not found!");
        return null;
    }

    // 3. Deactivate Batch
    private Batch deactivateBatch() {
        System.out.println("Enter Batch ID to deactivate: ");
        String id = Utils.getStringInput();

        for (Batch batch : repository.batches) {
            if (batch.getId().equalsIgnoreCase(id)) {
                batch.setStatus("CANCELLED");
                System.out.println("🚫 Batch deactivated: " + batch);
                return batch;
            }
        }
        System.out.println("⚠️ Batch not found!");
        return null;
    }

    // 4. View All repository.batches
    private Batch viewAllbatches() {
        if (repository.batches.isEmpty()) {
            System.out.println("📭 No repository.batches available.");
            return null;
        }
        System.out.println("📋 All repository.batches:");
        for (Batch batch : repository.batches) {
            System.out.println(batch);
        }
        return null;
    }

    // 5. Assign Trainer to Batch
    private Batch assignTrainerToBatch() {
        System.out.println("Enter Batch ID: ");
        String batchId = Utils.getStringInput();

        System.out.println("Enter Trainer ID: ");
        String trainerId = Utils.getStringInput();

        Batch selectedBatch = null;
        Trainer selectedTrainer = null;

        for (Batch batch : repository.batches) {
            if (batch.getId().equalsIgnoreCase(batchId)) {
                selectedBatch = batch;
                break;
            }
        }

        for (Trainer trainer : repository.trainers) {
            if (trainer.getTrainer_id().equalsIgnoreCase(trainerId)) { // adjust if Trainer has getTrainer_id()
                selectedTrainer = trainer;
                break;
            }
        }

        if (selectedBatch == null) {
            System.out.println("⚠️ Batch not found!");
            return null;
        }
        if (selectedTrainer == null) {
            System.out.println("⚠️ Trainer not found!");
            return null;
        }

        selectedBatch.setTrainer(selectedTrainer);
        System.out.println("✅ Trainer " + selectedTrainer.getFirst_name() +
                           " assigned to Batch " + selectedBatch.getId());
        return selectedBatch;
    }
}