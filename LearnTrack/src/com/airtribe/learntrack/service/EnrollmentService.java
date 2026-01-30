package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Batch;
import com.airtribe.learntrack.entity.Course;
import com.airtribe.learntrack.entity.Enrollment;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;
import java.util.List;

public class EnrollmentService {

    private final Repository repository;

    public EnrollmentService(Repository repository) {
        this.repository = repository;
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
                    this.searchEnrollment();
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

    private Enrollment addEnrollment() {
        System.out.println("Enter Student ID: ");
        String studentId = Utils.getStringInput();

        System.out.println("Enter Course ID: ");
        String courseId = Utils.getStringInput();

        System.out.println("Enter Batch ID: ");
        String batchId = Utils.getStringInput();

        Student selectedStudent = null;
        Course selectedCourse = null;
        Batch selectedBatch = null;

        // Find student
        for (Student s : repository.students) {
            if (s.getStudent_id().equalsIgnoreCase(studentId)) {
                selectedStudent = s;
                break;
            }
        }

        // Find course
        for (Course c : repository.courses) {
            if (c.getCourse_id().equalsIgnoreCase(courseId)) {
                selectedCourse = c;
                break;
            }
        }

        // Find batch
        for (Batch b : repository.batches) {
            if (b.getId().equalsIgnoreCase(batchId)) {
                selectedBatch = b;
                break;
            }
        }

        if (selectedStudent == null) {
            System.out.println("⚠️ Student not found!");
            return null;
        }
        if (selectedCourse == null) {
            System.out.println("⚠️ Course not found!");
            return null;
        }
        if (selectedBatch == null) {
            System.out.println("⚠️ Batch not found!");
            return null;
        }

        Enrollment enrollment = new Enrollment(selectedStudent, selectedCourse, selectedBatch);
        repository.enrollments.add(enrollment);
        System.out.println("✅ Enrollment added successfully: " + enrollment);
        return enrollment;
    }

    // 2. Search Enrollment
    private Enrollment searchEnrollment() {
        System.out.println("Enter Enrollment ID: ");
        String id = Utils.getStringInput();

        for (Enrollment e : repository.enrollments) {
            if (e.getEnrollment_id().equalsIgnoreCase(id)) {
                System.out.println("🔍 Found: " + e);
                return e;
            }
        }
        System.out.println("⚠️ Enrollment not found!");
        return null;
    }

    // 3. Deactivate Enrollment
    private Enrollment deactivateEnrollment() {
        System.out.println("Enter Enrollment ID to deactivate: ");
        String id = Utils.getStringInput();

        for (Enrollment e : repository.enrollments) {
            if (e.getEnrollment_id().equalsIgnoreCase(id)) {
                e.setStatus("CANCELLED");
                System.out.println("🚫 Enrollment deactivated: " + e);
                return e;
            }
        }
        System.out.println("⚠️ Enrollment not found!");
        return null;
    }

}