/**
 * @author Dorcas Osangiri
 * @version 1.0
 * Expense Tracker Project
 * This program is used to track your expenses
 * It provides features such as adding expenses, generating reports, and analyzing expenses.
 */
import java.util.Scanner;
import java.io.IOException;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class MainFinals {
	/**
	 * Main Method to run the expense Tracker system
	 */
    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        BinaryTree tree = new BinaryTree();
        Scanner scanner = new Scanner(System.in); 
        
		List<String> validCategories = Arrays.asList("Food","Tuition","Vacation", "Rent", "Utilities", "Transportation", "Entertainment", "Other");
		
        while (true) {
			System.out.println("\nTrack your money with the Expense Tracker!!");
			System.out.println("Select one of the following options");
            System.out.println("\n1. Add Expense");
            System.out.println("2. Display all Expenses");
            System.out.println("3. Generate Report by Category");
            System.out.println("4. Sort and Display Expenses by Date");
            System.out.println("5. Display Expenses in year order(Binary Tree)");
            System.out.println("6. Search Expenses by Category");
            System.out.println("7. compare expenses by year");
            System.out.println("8. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                     try {
                        // Validate date input
                        System.out.print("Enter date (YYYY-MM-DD): ");
                        String date = scanner.nextLine();
                        LocalDate parsedDate = LocalDate.parse(date);//automatically throws exception if date is wrong

                        // Validate category input
                        
                        System.out.println("Enter category (choose from: " + String.join(", ", validCategories) + "): ");
						String category = scanner.nextLine();

						boolean isValid = false;
						for (String validCategory : validCategories) {
							if (validCategory.equalsIgnoreCase(category)) {//added ignore case to take all 
								isValid = true;
								break;
							}
						}

						if (!isValid) {
							throw new IllegalArgumentException("Invalid category. Please select a valid category from the list.");
						}


                        // Validate amount input
                        System.out.print("Enter amount: ");
                        if (!scanner.hasNextDouble()) {
                            scanner.next(); // Consume invalid input
                            throw new IllegalArgumentException("Invalid amount. Make sure you are using numerical values.");
                        }
                        double amount = scanner.nextDouble();
					
                        tracker.addExpense(date, category, amount);
                        tree.addExpense(new Expense(date, category, amount));
                        System.out.println("Expense added successfully.");
                        try {
						tracker.saveToFile("expenses.txt");
						System.out.println("Expenses saved successfully to Expenses file.");
						} catch (IOException e) {
						System.err.println("Error saving file: " + e.getMessage());
					     }
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
				    break;
                case 2:
                    tracker.displayExpenses("Expenses.txt");
                    break;

                
               

                case 3:
                    tracker.generateReportByCategory("Expenses.txt");
                    break;

                case 4:
                    try {
						tracker.sortExpensesByDate("Expenses.txt"); 
						   
					}catch (IOException e) {
						System.err.println("Error: " + e.getMessage());
					}
					break;
                case 5:
                    tracker.displayExpensesAsBinaryTree();
                    break;

				case 6:
					System.out.print("Enter category to search for: ");
					String searchCategory = scanner.nextLine();
					tracker.searchExpensesByCategory(searchCategory, "expenses.txt"); 
					break;
				case 7:
				
					System.out.print("Enter the year to analyze: ");
					int year = scanner.nextInt();
					double totalForYear = tracker.calculateTotalForYear(year, "expenses.txt");
					double totalForPreviousYear = tracker.calculateTotalForYear(year - 1, "expenses.txt");

					System.out.printf("Total for %d: $%.2f%n", year, totalForYear);
					System.out.printf("Total for %d: $%.2f%n", year - 1, totalForPreviousYear);

					if (totalForYear > totalForPreviousYear) {
						System.out.println("You spent more in " + year + " compared to " + (year - 1));
					} else if (totalForYear < totalForPreviousYear) {
						System.out.println("You spent less in " + year + " compared to " + (year - 1));
					} else {
						System.out.println("You spent the same amount in both years.");
					}
					break;
	
                case 8:
                    System.out.println("Exiting...Bye");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
/**
 * 
 * 
 * 
 * https://github.com/bhavesh003/Expense_Income_Tracker 
 * https://stackoverflow.com/questions/49639600/throw-error-for-invalid-localdate-dates
 * 
 */
