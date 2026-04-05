package com.airtribe.librarymanagementsystem;

import java.util.Scanner;

import com.airtribe.librarymanagementsystem.entity.Book;
import com.airtribe.librarymanagementsystem.entity.Patron;
import com.airtribe.librarymanagementsystem.repository.BookRepository;
import com.airtribe.librarymanagementsystem.repository.BranchRepository;
import com.airtribe.librarymanagementsystem.repository.PatronRepository;
import com.airtribe.librarymanagementsystem.repository.ReservationRepository;
import com.airtribe.librarymanagementsystem.service.BookSearchService;
import com.airtribe.librarymanagementsystem.service.InventoryService;
import com.airtribe.librarymanagementsystem.service.LibraryService;
import com.airtribe.librarymanagementsystem.service.PatronSearchService;
import com.airtribe.librarymanagementsystem.service.Searchable;
import com.airtribe.librarymanagementsystem.ui.UIHelper;

public class Main {

    public static void main(String[] args) {
        /*
        This is the main menu of the application. It has an infinite while loop to 
        keep the app running. It shows two options to users:
            1. Administrator
            2. Patron
        Shows differnt options based on user Input.
        */
       
        UIHelper uiHelper = new UIHelper();
        
        BookRepository bookRepository = new BookRepository();
        PatronRepository patronRepository = new PatronRepository();
        BranchRepository branchRepository = new BranchRepository();
        ReservationRepository reservationRepository = new ReservationRepository();
        
        Scanner scanner = new Scanner(System.in);
        Searchable<Patron> patronSearchService = new PatronSearchService(patronRepository, scanner);
        Searchable<Book> bookSearchService = new BookSearchService(bookRepository, scanner);

        InventoryService inventoryService = new InventoryService(branchRepository);

        LibraryService libraryService = new LibraryService(bookRepository, branchRepository, 
                        reservationRepository, inventoryService, patronRepository, 
                        bookSearchService, scanner);

        while (true) {
            uiHelper.mainMenu();

            int role = scanner.nextInt();

            switch (role) {
                case 1:
                    patronMenu(scanner, patronSearchService, bookSearchService, libraryService, uiHelper);
                    break;
                case 2:
                    adminMenu(scanner, libraryService, uiHelper, inventoryService);
                    break;
                case 3:
                    uiHelper.exitView();
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private static void patronMenu(Scanner scanner, Searchable<Patron> patronSearchService, 
            Searchable<Book> bookSearchService, LibraryService libraryService, UIHelper uiHelper) {
        /*
        The method first identifies the patron who is logged in the system. Then gives the
        following choices:
        1. Search a book
        2. Borrow a book
        3. Return a book
        4. Back
        Calls the respective method for each choice.
        */
        Patron patron = patronSearchService.search();

        if (patron == null) {
            System.out.println("Returning to Main menu");
            return;
        }

        int retryCount = 0;
        while(true) {
            uiHelper.patronMenu();
            int option = scanner.nextInt();
    
            switch (option) {
                case 1:
                    Book book = bookSearchService.search();
                    System.out.println("Found the requested book:");
                    System.out.println(book.toString());
                    retryCount = 0;
                    break;
                case 2:
                    libraryService.lendBook(patron);
                    retryCount = 0;
                    break;
                case 3:
                    libraryService.returnBook(patron);
                    retryCount = 0;
                    break;
                case 4:
                    System.out.println("Returning to Main Menu...");
                    return;
                default:
                    if (retryCount>3) {
                        System.out.println("You entered wrong choice 3 times.");
                        return;
                    }
                    System.out.println("Invalid choice. Try again");
                    retryCount++;
            }
        }
    }

    private static void adminMenu(Scanner scanner, LibraryService libraryService, 
                        UIHelper uiHelper, InventoryService inventoryService) {
        /*
        Shows the following choices to the user:
            1. Add a new book
            2. Restock a book
            3. Discard books in stock
            4. Create a new Branch  
            5. Close a branch
            6. Generate stock report
            7. Migrate stock between branches
            8. Back
        Calls the respective method for each choice.
        */
        
        int retryCount = 0;
        while (true) {
            uiHelper.adminMenu();
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    libraryService.addNewBook();
                    retryCount = 0;
                    break;
                case 2:
                    libraryService.restockBook();
                    retryCount = 0;
                    break;
                case 3:
                    libraryService.discardBook();
                    retryCount = 0;
                    break;
                case 4:
                    libraryService.createBranch();
                    retryCount = 0;
                    break;
                case 5:
                    libraryService.closeBranch();
                    retryCount = 0;
                    break;
                case 6:
                    System.out.println("Enter Branch Id:");
                    String input = scanner.nextLine().toString();
                    inventoryService.generateInventoryReport(input);
                    retryCount = 0;
                    break;
                case 7:
                    libraryService.migrateStocks();
                    retryCount = 0;
                    break;
                case 8:
                    libraryService.createPatron();
                    retryCount = 0;
                    break;
                case 9:
                    System.out.println("Returning to Main Menu...");
                    return;
                default:
                    if (retryCount>3) {
                        System.out.println("You entered wrong choice 3 times.");
                        return;
                    }
                    System.out.println("Invalid choice. Try again");
                    retryCount++;
            }
        }
        
    }
}
