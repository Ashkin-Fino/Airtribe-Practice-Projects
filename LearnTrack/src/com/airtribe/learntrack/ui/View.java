package com.airtribe.learntrack.ui;

public class View {
    
    
    private static void printLogo() {
        System.out.println("                _        _________                ");
        System.out.println("           /   | |      |____ ____|   \\           ");
        System.out.println("          /    | |          | |        \\          ");
        System.out.println("         <     | |          | |         >         ");
        System.out.println("          \\    | |____      | |        /          ");
        System.out.println("           \\   |______|     |_|       /           ");
        System.out.println("--------------------------------------------------");
    }

    public static void welcomeView() {
        printLogo();
        System.out.println("Welcome to Learn Track - Learner Management System");
    }

    public static void exitView() {
        System.out.println("--------------------------------------------------");
        printLogo();
        System.out.println("Thank you for using the Learner Management System!");
    }

    public static void mainMenuView() {
        System.out.println("1) Student Management");
        System.out.println("2) Trainer Management");
        System.out.println("3) Course Management");
        System.out.println("4) Batch Management");
        System.out.println("5) Enrollment Management");
        System.out.println("6) Exit Application");
        System.out.println("Enter your input: ");
    }

    public static void studentSubmenuView() {
        System.out.println("Student Management");
        System.out.println("|-1--Add Student");
        System.out.println("|-2--Search Student");
        System.out.println("|-3--Deactivate Student");
        System.out.println("|-4--View All Students");
        System.out.println("|-5--Back to Main Menu");
        System.out.println("Enter your input: ");
    }

    public static void studentSearchSubmenuView() {
        System.out.println("Student Search");
        System.out.println("|-1--Search by ID");
        System.out.println("|-2--Search by Name");
        System.out.println("|-3--Back to Main Menu");
        System.out.println("Enter your input: ");
    }

    public static void trainerSubmenuView() {
        System.out.println("Trainer Management");
        System.out.println("|-1--Add Trainer");
        System.out.println("|-2--Search Trainer");
        System.out.println("|-3--Deactivate Trainer");
        System.out.println("|-4--View All Trainers");
        System.out.println("|-5--Back to Main Menu");
        System.out.println("Enter your input: ");
    }

    public static void trainerSearchSubmenuView() {
        System.out.println("Trainer Search");
        System.out.println("|-1--Search by ID");
        System.out.println("|-2--Search by Name");
        System.out.println("|-3--Search by specialisation");
        System.out.println("|-4--Back to Main Menu");
        System.out.println("Enter your input: ");
    }

    public static void courseSubmenuView() {
        System.out.println("Course Management");
        System.out.println("|-1-- Add Course");
        System.out.println("|-2-- Search Course");
        System.out.println("|-3-- Deactivate Course");
        System.out.println("|-4-- View All Courses");
        System.out.println("|-5-- Back to Main Menu");
        System.out.println("Enter your input: ");
    }

    public static void courseSearchSubmenuView() {
        System.out.println("Course Search");
        System.out.println("|-1--Search by ID");
        System.out.println("|-2--Search by Name");
        System.out.println("|-3--Back to Main Menu");
        System.out.println("Enter your input: ");
    }

    public static void batchSubmenuView() {
        System.out.println("Batch Management");
        System.out.println("|-1-- Add Batch");
        System.out.println("|-2-- Search Batch");
        System.out.println("|-3-- Cancel batch");
        System.out.println("|-4-- View Ongoing Batches");
        System.out.println("|-5-- View Completed Batches");
        System.out.println("|-6-- Assign Trainer to Batch");
        System.out.println("|-7-- View all students in Batch");
        System.out.println("|-8-- Back to Main Menu");
        System.out.println("Enter your input: ");
    }

    public static void batchSearchSubmenuView() {
        System.out.println("Batch Search");
        System.out.println("|-1--Search by ID");
        System.out.println("|-2--Search by Name");
        System.out.println("|-3--Search by Course");
        System.out.println("|-4--Back to Main Menu");
        System.out.println("Enter your input: ");
    }

    public static void enrollmentSubmenuView() {
        System.out.println("Enrollment Management");
        System.out.println("|-1-- Enroll new Student");
        System.out.println("|-2-- Cancel Enrollment");
        System.out.println("|-3-- Back to Main Menu");
        System.out.println("Enter your input: ");
    }
}
