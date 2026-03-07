package com.airtribe.learntrack.service;

import com.airtribe.learntrack.repository.Repository;
import com.airtribe.learntrack.ui.View;
import com.airtribe.learntrack.utils.Utils;

public class LearnTrackService {

    private final Repository repository;
    private final StudentService studentService;
    private final TrainerService trainerService;
    private final CourseService courseService;
    private final BatchService batchService;
    private final EnrollmentService enrollmentService;

    public LearnTrackService() {
        repository = new Repository();
        studentService = new StudentService(repository);
        trainerService = new TrainerService(repository);
        courseService = new CourseService(repository);
        batchService = new BatchService(repository);
        enrollmentService = new EnrollmentService(repository);
    }

    public void mainMenu() {
        /*
        This is the first main menu of the application. Entire method runs in a while loop
        to keep the base application menu running.

        This method first calls the View.mainMenuView() method to print the main menu. Then
        calls the Utils.getUserInput to get the user choice. Based on the choice, Calls the
        menu() method of the respective service class. In case, user selects option six, 
        sets the exit field to true, so the mainmenu while loop breaks and the application 
        ends. In case of invalid input, loop runs again and main menu is shown.
        */
        boolean exit = false;

        while (!exit) {
            View.mainMenuView();
            int userInput = Utils.getUserInput(1, 6);
            switch (userInput) {
                case 1:
                    studentService.menu();
                    break;
                case 2:
                    trainerService.menu();
                    break;
                case 3:
                    courseService.menu();
                    break;
                case 4:
                    batchService.menu();
                    break;
                case 5:
                    enrollmentService.menu();
                    break;
                case 6:
                    System.out.println("Exiting Application...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}
