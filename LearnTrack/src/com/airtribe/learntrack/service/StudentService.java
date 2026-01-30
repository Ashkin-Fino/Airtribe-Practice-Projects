package com.airtribe.learntrack.service;

import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class StudentService {

    private final Repository repository;

    public StudentService(Repository repository) {
        this.repository = repository;
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
                    this.searchStudent();
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

    private Student addStudent() {
        System.out.println("Enter First Name*: ");
        String firstName = Utils.getStringInput(true);

        System.out.println("Enter Last Name*: ");
        String lastName = Utils.getStringInput(true);

        System.out.println("Enter Email*: ");
        String email = Utils.getStringInput(true);

        System.out.println("Enter Age*: ");
        int age = Utils.getIntInput(true);

        Student student = new Student(firstName, lastName, email, age);
        repository.students.add(student);

        System.out.println("Student added successfully: " + student.toString());
        return student;
    }

    private Student searchStudent() {
        System.out.println("Enter Student Id to search: ");
        String id = Utils.getStringInput();

        for (Student student : repository.students) {
            if (student.getStudent_id().equalsIgnoreCase(id)) {
                System.out.println("Student found: " + student.toString());
                return student;
            }
        }
        System.out.println("Student not found with ID: " + id);
        return null;
    }

    private Student deactivateStudent() {
        System.out.println("Enter Student Id to Deactivate:");
        String id = Utils.getStringInput();

        for (Student student : repository.students) {
            if (student.getStudent_id().equalsIgnoreCase(id)) {
                student.setIs_active(false);
                System.out.println("Student deactivated: " + student);
                return student;
            }
        }
        System.out.println("Student not found with ID: " + id);
        return null;
    }

    private void viewAllStudents() {
        if (repository.students.isEmpty()) {
            System.out.println("No students available.");
        } else {
            System.out.println("Found Students:");
            for (Student student : repository.students) {
                System.out.println(student.toString());
            }
        }
    }
}