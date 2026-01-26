package com.airtribe.learntrack.service;

import com.airtribe.learntrack.utils.Utils;
import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.ui.View;

public class StudentService {

    public StudentService() {
        // Default constructor
    }

    public void menu() {
        boolean back = false;
        // infinite loop to keep the application running
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
        return null;
    }

    private Student searchStudent() {
        return null;
    }

    private Student deactivateStudent() {
        return null;
    }

    private Student viewAllStudents() {
        return null;
    }
}
