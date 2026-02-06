package com.airtribe.learntrack.service.searchservice;

import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class StudentSearchService implements Searchable<Student>{

    Repository repository;
    
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
    
        for (Student student : repository.getStudents()) {
            String fullName = (student.getFirstName() + " " + student.getLastName()).toLowerCase();
    
            if (fullName.contains(name)) {
                System.out.println("Student found: " + student);
                return student;
            }
        }
    
        System.out.println("Student not found.");
        return null;
    }    
}
