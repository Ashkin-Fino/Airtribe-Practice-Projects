package com.airtribe.learntrack.service.searchservice;

import java.util.ArrayList;
import java.util.List;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class CourseSearchService implements Searchable<Course>{

    private final Repository repository;

    public CourseSearchService(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Course search() {
        Course result = null;
        View.courseSearchSubmenuView();
        int userInput = Utils.getUserInput(1, 3);
        switch(userInput) {
            case 1:
                result = this.searchById();
                break;
            case 2:
                result = this.searchByName();
                break;
            case 3:
                System.out.println("Returning to Course Menu");
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
        return result;
    }

    private Course searchById() {
        System.out.println("Enter Course ID: ");
        String id = Utils.getStringInput(true);
    
        for (Course course : repository.getCourses()) {
            if (course.getCourse_id() == id) {
                System.out.println("Course found: " + course);
                return course;
            }
        }
    
        System.out.println("Course not found.");
        return null;
    }
    
    private Course searchByName() {
        System.out.println("Enter Course Name: ");
        String searchName = Utils.getStringInput(true).toLowerCase();
    
        List<Course> matchedCourses = new ArrayList<>();
    
        for (Course course : repository.getCourses()) {
            String name = course.getName();
            if (name.contains(searchName)) {
                matchedCourses.add(course);
            }
        }
    
        if (matchedCourses.isEmpty()) {
            System.out.println("Course not found.");
            return null;
        }
    
        // If only one Course found
        if (matchedCourses.size() == 1) {
            System.out.println("Course found: " + matchedCourses.get(0));
            return matchedCourses.get(0);
        }
    
        // Multiple Courses found
        System.out.println("Multiple Courses found:");
        for (int i = 0; i < matchedCourses.size(); i++) {
            System.out.println((i + 1) + ". " + matchedCourses.get(i));
        }
    
        System.out.println("Select a Course:");
        int choice = Utils.getUserInput(1, matchedCourses.size());
    
        return matchedCourses.get(choice - 1);
    }      
}
