package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;
import java.util.List;

public class CourseService {
    
    private final Repository repository;

    public CourseService(Repository repository) {
        this.repository = repository;
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
                    this.searchCourse();
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
    private Course addCourse() {
        System.out.println("Enter Course Name: ");
        String name = Utils.getStringInput();

        System.out.println("Enter Duration (e.g., '2 weeks', '3 months'): ");
        String duration = Utils.getStringInput();

        System.out.println("Enter Description (optional): ");
        String description = Utils.getStringInput();

        Course course = new Course(name, duration);
        if (description != null && !description.trim().isEmpty()) {
            course.setDescription(description);
        }
        repository.courses.add(course);

        System.out.println("✅ Course added successfully: " + course);
        return course;
    }

    // 2. Search Course by ID
    private Course searchCourse() {
        System.out.println("Enter Course ID to search: ");
        String id = Utils.getStringInput();

        for (Course course : repository.courses) {
            if (course.getCourse_id().equalsIgnoreCase(id)) {
                System.out.println("🔍 Course found: " + course);
                return course;
            }
        }
        System.out.println("⚠️ Course not found with ID: " + id);
        return null;
    }

    // 3. Deactivate Course
    private Course deactivateCourse() {
        System.out.println("Enter Course ID to deactivate: ");
        String id = Utils.getStringInput();

        for (Course course : repository.courses) {
            if (course.getCourse_id().equalsIgnoreCase(id)) {
                course.setIs_active(false);
                System.out.println("🚫 Course deactivated: " + course);
                return course;
            }
        }
        System.out.println("⚠️ Course not found with ID: " + id);
        return null;
    }

    // 4. View All Courses
    private Course viewAllCourses() {
        if (repository.courses.isEmpty()) {
            System.out.println("📭 No courses available.");
            return null;
        }

        System.out.println("📋 List of all courses:");
        for (Course course : repository.courses) {
            System.out.println(course);
        }
        return null; // Not returning a specific course, just displaying all
    }
}
