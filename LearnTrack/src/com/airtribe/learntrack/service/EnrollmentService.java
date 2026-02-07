package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Batch;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.service.searchservice.BatchSearchService;
import com.airtribe.learntrack.service.searchservice.CourseSearchService;
import com.airtribe.learntrack.service.searchservice.Searchable;
import com.airtribe.learntrack.service.searchservice.StudentSearchService;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentService {

    private final Repository repository;
    private final Searchable<Batch> batchSearchService;
    private final Searchable<Course> courseSearchService;
    private final Searchable<Student> studentSearchService;

    public EnrollmentService(Repository repository) {
        this.repository = repository;
        this.studentSearchService = new StudentSearchService(repository);
        this.courseSearchService = new CourseSearchService(repository);
        this.batchSearchService = new BatchSearchService(repository, courseSearchService);
    }

    public void menu() {
        boolean back = false;
        while (!back) {
            View.enrollmentSubmenuView();
            int userInput = Utils.getUserInput(1, 3);
            switch (userInput) {
                case 1:
                    this.addEnrollment();
                    break;
                case 2:
                    this.cancelEnrollment();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    private void addEnrollment() {
        System.out.println("Identifying student...");
        Student student = this.studentSearchService.search();

        if (student == null) {
            System.out.println("Try again with a different student");
        }

        System.out.println("Identifying batch...");
        Batch batch = this.batchSearchService.search();

        if (batch == null) {
            System.out.println("Try again with a different batch");
        }

        try {
            Enrollment enrollment = new Enrollment(student, batch);
            repository.addEnrollment(enrollment);
            System.out.println("Enrollment added successfully: " + enrollment);
        } catch (IllegalArgumentException e) {
            System.out.println("Couldn't create enrollment: " + e.toString());
        }
        return;
    }

    private void cancelEnrollment() {
        System.out.println("Select student whose enrollment should be cancelled... ");
        Student student = this.studentSearchService.search();

        if (student == null) {
            System.out.println("Try again with a different student");
        }

        List<Enrollment> matchedEnrollments = new ArrayList<>();

        for (Enrollment enrollment: repository.getEnrollments()) {
            if (enrollment.getStudent().getId().equals(student.getId())) {
                matchedEnrollments.add(enrollment);
            }
        }

        if (matchedEnrollments.isEmpty()) {
            System.out.println("Student has no enrollments.");
            return;
        }

        Enrollment enrollmentToCancel;

        // If only one Enrollment found
        if (matchedEnrollments.size() == 1) {
            System.out.println("Enrollment found: " + matchedEnrollments.get(0));
            enrollmentToCancel = matchedEnrollments.get(0);
        }
    
        // Multiple Enrollments found
        System.out.println("Multiple Enrollments found:");
        for (int i = 0; i < matchedEnrollments.size(); i++) {
            System.out.println((i + 1) + ". " + matchedEnrollments.get(i));
        }
        System.out.println("Select a Enrollement");
        int choice = Utils.getUserInput(1, matchedEnrollments.size());
        enrollmentToCancel = matchedEnrollments.get(choice - 1);

        enrollmentToCancel.setStatus("CANCELLED");
        System.out.println("Enrollment cancelled successfully");

        return;
    }
}