package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.service.searchservice.CourseSearchService;
import com.airtribe.learntrack.service.searchservice.Searchable;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class CourseService {
    
    private final Repository repository;
    private final Searchable<Course> courseSearchService;

    public CourseService(Repository repository) {
        this.repository = repository;
        this.courseSearchService = new CourseSearchService(repository);
    }

    public void menu() {
        boolean back = false;
        // infinite loop to keep the application running
        while (!back) {
            View.courseSubmenuView();
            int userInput = Utils.getUserInput(1, 5);
            switch (userInput) {
                case 1:
                    this.addCourse();
                    break;
                case 2:
                    this.courseSearchService.search();
                    break;
                case 3:
                    this.deactivateCourse();
                    break;
                case 4:
                    this.viewAllCourses();
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

    // 1. Add Course
    private void addCourse() {
        System.out.println("Enter Course Name*: ");
        String name = Utils.getStringInput(true);

        System.out.println("Enter Duration (e.g., '2 weeks', '3 months')*: ");
        String duration = Utils.getStringInput(true);

        System.out.println("Enter Description: ");
        String description = Utils.getStringInput(false);

        try {
            Course course = new Course(name, duration);
            if (description != null && !description.trim().isEmpty()) {
                course.setDescription(description);
            }
            repository.addCourse(course);
            System.out.println("Course added successfully: " + course);
        } catch (IllegalArgumentException e) {
            System.out.println("Couldn't create Course. Please try again: " + e.toString());
        }

        return;
    }

    // 3. Deactivate Course
    private void deactivateCourse() {
        System.out.println("Identify course to deactivate...");
        Course courseToDeactivate = this.courseSearchService.search();

        if (courseToDeactivate == null) {
            System.out.println("No course selected");
        } else {
            System.out.println("Deactivating course:" + courseToDeactivate);
            courseToDeactivate.setActive(false);
        }
        return;
    }

    // 4. View All Courses
    private void viewAllCourses() {
        
        boolean found = false;

        for (Course course : repository.getCourses()) {
            if (!found) {
                System.out.println("All Courses:");
                found = true;
            }
            System.out.println(course);
        }

        if (!found) {
            System.out.println("No courses found.");
        }
        return;
    }
}
