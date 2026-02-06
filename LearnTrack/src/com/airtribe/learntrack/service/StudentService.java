package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.service.searchservice.Searchable;
import com.airtribe.learntrack.service.searchservice.StudentSearchService;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class StudentService {

    private final Repository repository;
    private final Searchable<Student> studentSearchService;

    public StudentService(Repository repository) {
        this.repository = repository;
        this.studentSearchService = new StudentSearchService(repository);
    }

    public void menu() {
        boolean back = false;
        while (!back) {
            View.studentSubmenuView();
            int userInput = Utils.getUserInput(1, 5);
            switch (userInput) {
                case 1:
                    this.addStudent();
                    break;
                case 2:
                    this.studentSearchService.search();
                    break;
                case 3:
                    this.deactivateStudent();
                    break;
                case 4:
                    this.viewAllStudents();
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

    private void addStudent() {
        System.out.println("Enter First Name*: ");
        String firstName = Utils.getStringInput(true);

        System.out.println("Enter Last Name*: ");
        String lastName = Utils.getStringInput(true);

        System.out.println("Enter Email*: ");
        String email = Utils.getStringInput(true);

        System.out.println("Enter Age*: ");
        int age = Utils.getIntInput(true);

        Student student = new Student(firstName, lastName, email, age);
        repository.addStudent(student);

        System.out.println("Student added successfully: " + student.toString());
        return;
    }

    private void deactivateStudent() {
        System.out.println("Identify Student to deactivate...");
        Student studentToDeactivate = this.studentSearchService.search();

        if (studentToDeactivate == null) {
            System.out.println("No student selected");
        }

        System.out.println("Confirm deactivation of student:" + studentToDeactivate);
        studentToDeactivate.setActive(false);
        return;
    }

    private void viewAllStudents() {
        if (repository.getStudents().isEmpty()) {
            System.out.println("No students available.");
        } else {
            System.out.println("Found Students:");
            for (Student student : repository.getStudents()) {
                System.out.println(student.toString());
            }
        }
    }
}