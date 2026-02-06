package com.airtribe.learntrack.service.searchservice;

import java.util.ArrayList;
import java.util.List;

import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class StudentSearchService implements Searchable<Student>{

    private final Repository repository;
    
    public StudentSearchService(Repository repository) {
        this.repository = repository;
    }

    public Student search() {
        Student result = null;
        View.studentSearchSubmenuView();
        int userInput = Utils.getUserInput(1, 3);
        switch(userInput) {
            case 1:
                result = this.searchStudentById();
                break;
            case 2:
                result = this.searchStudentByName();
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
        return result;
    }

    private Student searchStudentById() {
        System.out.println("Enter Student ID: ");
        String id = Utils.getStringInput(true);
    
        for (Student student : repository.getStudents()) {
            if (student.getId() == id) {
                System.out.println("Student found: " + student);
                return student;
            }
        }
    
        System.out.println("Student not found.");
        return null;
    }
    
    private Student searchStudentByName() {
        System.out.println("Enter Student Name: ");
        String name = Utils.getStringInput(true).toLowerCase();
    
        List<Student> matchedStudents = new ArrayList<>();
    
        for (Student student : repository.getStudents()) {
            String fullName = (student.getFirstName() + " " + student.getLastName()).toLowerCase();
            if (fullName.contains(name)) {
                matchedStudents.add(student);
            }
        }
    
        if (matchedStudents.isEmpty()) {
            System.out.println("Student not found.");
            return null;
        }
    
        // If only one student found
        if (matchedStudents.size() == 1) {
            System.out.println("Student found: " + matchedStudents.get(0));
            return matchedStudents.get(0);
        }
    
        // Multiple students found
        System.out.println("Multiple students found:");
        for (int i = 0; i < matchedStudents.size(); i++) {
            System.out.println((i + 1) + ". " + matchedStudents.get(i));
        }
    
        System.out.println("Select a student:");
        int choice = Utils.getUserInput(1, matchedStudents.size());
    
        return matchedStudents.get(choice - 1);
    }      
}
