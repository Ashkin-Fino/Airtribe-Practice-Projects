package com.airtribe.learntrack.service.searchservice;

import com.airtribe.learntrack.entity.Student;
import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class StudentSearchService {

    Repository repository;
    
    public StudentSearchService(Repository repository) {
        this.repository = repository;
    }

    public Student searchStudent() {
        Student result = null;
        View.studentSearchSubmenuView();
        int userInput = Utils.getUserInput(0, 3);
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

    private Student searchStudentByName() {
        throw new UnsupportedOperationException("Unimplemented method 'searchSrudentByName'");
    }

    private Student searchStudentById() {
        throw new UnsupportedOperationException("Unimplemented method 'searchStudentById'");
    }
}
