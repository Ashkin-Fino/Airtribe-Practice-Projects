package com.airtribe.learntrack.service.searchservice;

import java.util.ArrayList;
import java.util.List;

import com.airtribe.learntrack.entity.Batch;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class BatchSearchService implements Searchable<Batch>{

    private final Repository repository;
    private final Searchable<Course> courseSearchService;

    public BatchSearchService(Repository repository, Searchable<Course> courseSearchService) {
        this.repository = repository;
        this.courseSearchService = courseSearchService;
    }

    @Override
    public Batch search() {
        Batch result = null;
        View.batchSearchSubmenuView();
        int userInput = Utils.getUserInput(1, 4);
        switch(userInput) {
            case 1:
                result = this.searchById();
                break;
            case 2:
                result = this.searchByName();
                break;
            case 3:
                result = this.searchByCourse();
                break;
            case 4:
                System.out.println("Returning to Batch Menu");
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
        return result;
    }

    private Batch searchById() {
        System.out.println("Enter Batch ID: ");
        String id = Utils.getStringInput(true);
    
        for (Batch batch : repository.getBatches()) {
            if (batch.getId().equals(id)) {
                System.out.println("Batch found: " + batch);
                return batch;
            }
        }
    
        System.out.println("Batch not found.");
        return null;
    }
    
    private Batch searchByName() {
        System.out.println("Enter Batch Name: ");
        String searchName = Utils.getStringInput(true).toLowerCase();
    
        List<Batch> matchedBatches = new ArrayList<>();
    
        for (Batch batch : repository.getBatches()) {
            String name = batch.getName();
            if (name.toLowerCase().contains(searchName.toLowerCase())) {
                matchedBatches.add(batch);
            }
        }
    
        if (matchedBatches.isEmpty()) {
            System.out.println("Batch not found.");
            return null;
        }
    
        // If only one Batch found
        if (matchedBatches.size() == 1) {
            System.out.println("Batch found: " + matchedBatches.get(0));
            return matchedBatches.get(0);
        }
    
        // Multiple batches found
        System.out.println("Multiple Batches found:");
        for (int i = 0; i < matchedBatches.size(); i++) {
            System.out.println((i + 1) + ". " + matchedBatches.get(i));
        }
    
        System.out.println("Select a Batch:");
        int choice = Utils.getUserInput(1, matchedBatches.size());
        
        System.out.println("Batch found: " + matchedBatches.get(choice - 1));
        return matchedBatches.get(choice - 1);
    }

    private Batch searchByCourse() {
        System.out.println("Select course to search batches:");
        Course course = courseSearchService.search();

        if(course == null) {
            return null;
        }

        List<Batch> matchedBatches = new ArrayList<>();
    
        for (Batch batch : repository.getBatches()) {
            if (batch.getCourse().getCourse_id().equals(course.getCourse_id())) {
                matchedBatches.add(batch);
            }
        }
    
        if (matchedBatches.isEmpty()) {
            System.out.println("Batch not found.");
            return null;
        }
    
        // If only one Batch found
        if (matchedBatches.size() == 1) {
            System.out.println("Batch found: " + matchedBatches.get(0));
            return matchedBatches.get(0);
        }
    
        // Multiple Batches found
        System.out.println("Multiple Batches found:");
        for (int i = 0; i < matchedBatches.size(); i++) {
            System.out.println((i + 1) + ". " + matchedBatches.get(i));
        }
    
        System.out.println("Select a Batch:");
        int choice = Utils.getUserInput(1, matchedBatches.size());
        
        System.out.println("Batch found: " + matchedBatches.get(choice - 1));
        return matchedBatches.get(choice - 1);
    }
}
