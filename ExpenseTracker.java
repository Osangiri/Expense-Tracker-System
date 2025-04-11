/**
 * ExpenseTracker class
 * It has different functions likr adding, removing, searching
 * and generating reports for expenses along with file handling features
 * 
 */
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.*;  
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class ExpenseTracker {
    private List<Expense> expenses; // Stores all expenses
	private BinaryTree binaryTree;
	
    public ExpenseTracker() {
        this.expenses = new ArrayList<>();
        this.binaryTree = new BinaryTree();
    }
    /**
     * Rebuilds the binary tree for the current list of expenses
     */
    private void rebuildBinaryTree() {
        binaryTree.clear();
        for (Expense expense : expenses) {
            binaryTree.addExpense(expense);
        }
    }

    /**Display expenses using the binary tree
     * 
     * 
     */
    public void displayExpensesAsBinaryTree() {
        rebuildBinaryTree(); // Ensure tree is up to date
        System.out.println("Displaying expenses (in order) using Binary Tree:");
        binaryTree.displayInOrder();//call method in binary tree
    }

    /** Add expense
     * @param String date, String category, double amount
     * 
     */
    public void addExpense(String date, String category, double amount) {
        expenses.add(new Expense(date, category, amount));
    }

    /** Save expenses to a file
     *  Initialize a set to track existing expense based on date and category
     *  Reads the existing expenses from the file into set using streams
     *  Used streams to avoid duplicates
     * @param String filename
     * @throws IOException
     * 
     */
  
	public void saveToFile(String filename) throws IOException {
    
		Set<String> existingExpenses = new HashSet<>();//initialize set

		
		File file = new File(filename);
		if (file.exists()) {
			try (Stream<String> lines = Files.lines(Paths.get(filename))) {
				lines.forEach(line -> {
					String[] parts = line.split(",");
					if (parts.length == 3) {
						String expenseKey = parts[0] + "," + parts[1]; // Date + Category
						existingExpenses.add(expenseKey);  // Add existing expense to the set
					}
				});
			}
		}

		
		try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) { // Avoiding duplicates using streams
			expenses.stream()
				.filter(expense -> {
					String expenseKey = expense.getDate() + "," + expense.getCategory();
					return !existingExpenses.contains(expenseKey); // Filter out existing expenses
				})
				.peek(expense -> existingExpenses.add(expense.getDate() + "," + expense.getCategory())) // Add to the set after writing
				.forEach(expense -> writer.println(expense.getDate() + "," + expense.getCategory() + "," + expense.getAmount()));
		}
	}


    /** Load expenses from a file
     * @param String filename the name of file to load from
     * @throws IOException
     */
   
    public void loadFromFile(String filename) throws IOException {
		try (FileReader reader = new FileReader(filename); Scanner scanner = new Scanner(reader)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (!line.isEmpty()) {  // Skip empty lines
					String[] parts = line.split(",");
					if (parts.length == 3) {  // Ensure there are 3 parts (date, category, amount)
						String date = parts[0].trim();
						String category = parts[1].trim();
						double amount;
						try {
							amount = Double.parseDouble(parts[2].trim());  // Parse amount
							addExpense(date, category, amount);  // Add the expense
						} catch (NumberFormatException e) {
							System.err.println("Invalid amount format: " + parts[2]);
						}
					} else {
						System.err.println("Skipping invalid line: " + line);
					}
				}
			}
		}
	}


    /**Generate report by category
     * @param String filename
     */
   public void generateReportByCategory(String filename) {
    try (Stream<String> lines = Files.lines(Paths.get(filename))) {
        Map<String, Double> categoryTotals = lines
            .map(line -> line.split(",")) // Split each line into an array
            .filter(parts -> parts.length == 3) // Ensure the line has exactly 3 parts (date, category, amount)
            .collect(Collectors.groupingBy(
                parts -> parts[1], // Group by the category
                Collectors.summingDouble(parts -> Double.parseDouble(parts[2])) // Sum up the amounts
            ));

        // Print the report
        categoryTotals.forEach((category, total) -> 
            System.out.printf("Category: %s, Total: $%.2f%n", category, total));
    } catch (IOException e) {
        System.err.println("Error reading the file: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.err.println("Error parsing amount values in the file: " + e.getMessage());
    }
}

	

    /** Sort expenses by date
     * @param String filename
     * @throws IOException
     */
  

	public void sortExpensesByDate(String filename) throws IOException {
		/**Load expenses from the file using Files.readAllLines
		 */
		List<String> lines = Files.readAllLines(Paths.get(filename));
		List<Expense> fileExpenses = new ArrayList<>();

		/**Parse each line into an Expense object
		 */
		for (String line : lines) {
			String[] parts = line.split(",");
			if (parts.length == 3) {
				String date = parts[0];
				String category = parts[1];
				double amount = Double.parseDouble(parts[2]);
				fileExpenses.add(new Expense(date, category, amount));
			}
		}

		// Sort the expenses loaded from the file by date
		fileExpenses.sort(Comparator.naturalOrder());

		// Display the sorted expenses
		System.out.println("Expenses sorted by date:");
		for (Expense expense : fileExpenses) {
			System.out.printf("Date: %s, Category: %s, Amount: $%.2f%n", 
					expense.getDate(), expense.getCategory(), expense.getAmount());
		}
	}

    
    /**Method to search expenses by category
	 * @param String category, String Filename
	 * 
	 */
	public void searchExpensesByCategory(String category, String filename) {
		try (FileReader reader = new FileReader(filename); Scanner scanner = new Scanner(reader)) {
			/** Create a stream of Expense objects by reading each line
			 *  Use streams and lambdas to read and filter data
			 */
			List<Expense> filteredExpenses = new ArrayList<>();
			
			scanner.tokens()
				.map(line -> line.split(","))
				.filter(parts -> parts.length >= 3 && parts[1].equalsIgnoreCase(category)) // Filter by category
				.map(parts -> new Expense(parts[0], parts[1], Double.parseDouble(parts[2]))) // Map to Expense objects
				.forEach(filteredExpenses::add); // Collect the filtered results into the list
			
			if (filteredExpenses.isEmpty()) {
				System.out.println("No expenses found for category: " + category);
			} else {
				filteredExpenses.forEach(System.out::println); // Display matching expenses
			}
		} catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
			System.out.println("Error searching expenses by category: " + e.getMessage());
		}
}

    /** Display all expenses (always loads from the file first)
     * @param String filename the name of file to load from
     * 
     */
	public void displayExpenses(String filename) {
		try {
			loadFromFile(filename); // Load expenses from the file
			expenses.forEach(System.out::println); // Display all expenses
		} catch (IOException e) {
			System.out.println("Failed to load expenses from file: " + e.getMessage());
		}
	}

    /**Calculate total expenses for a specific year
     * @param int year the year to calculate the totals for
     * @param String filename 
     * @return total for that year
     */
	public double calculateTotalForYear(int year, String filename) {
		double total = 0.0;
		try (FileReader reader = new FileReader(filename); Scanner scanner = new Scanner(reader)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split(","); 
				String[] dateParts = parts[0].split("-");
				int expenseYear = Integer.parseInt(dateParts[0]); 
				if (expenseYear == year) {
					total += Double.parseDouble(parts[2]);
				}
			}
		} catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
			System.out.println("Error calculating total for year: " + e.getMessage());
		}
		return total;
	}


}
