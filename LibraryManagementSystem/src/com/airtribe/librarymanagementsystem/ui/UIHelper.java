package com.airtribe.librarymanagementsystem.ui;

public class UIHelper {
    
    public void mainMenu() {
        /*
        This method prints the initial main menu of the application in the console.
        */
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║    LIBRARY MANAGEMENT SYSTEM     ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║ 1. Patron                        ║");
        System.out.println("║ 2. Administrator                 ║");
        System.out.println("║ 3. Exit                          ║");
        System.out.println("╚══════════════════════════════════╝");
    }

    public void patronMenu() {
        /*
        This method prints the menu shown to patrons of the application in the console.
        */
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║         WELCOME PATRON!!         ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║ 1. Search a Book                 ║");
        System.out.println("║ 2. Borrow a Book                 ║");
        System.out.println("║ 3. Return a Book                 ║");
        System.out.println("║ 4. Back to Main Menu             ║");
        System.out.println("╚══════════════════════════════════╝");
    }

    public void adminMenu() {
        /*
        This method prints the menu shwon to admins of the application in the console.
        */
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║         WELCOME ADMIN!!          ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║ 1. Add a new Book                ║");
        System.out.println("║ 2. Restock a Book                ║");
        System.out.println("║ 3. Discard books in stock        ║");
        System.out.println("║ 4. Create a new Branch           ║");
        System.out.println("║ 5. Close a branch                ║");
        System.out.println("║ 6. Generate stock report         ║");
        System.out.println("║ 7. Migrate stock between branches║");
        System.out.println("║ 8. Create Patron                 ║");
        System.out.println("║ 9. Back to Main Menu             ║");
        System.out.println("╚══════════════════════════════════╝");
    }

    public void exitView() {
        /*
        This method prints the thank you view while exiting the app.
        */
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║      Thank You For Visiting      ║");
        System.out.println("╚══════════════════════════════════╝");
    }
}
