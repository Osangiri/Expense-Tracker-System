/**
 * Expense class 
 */
public class Expense implements Comparable<Expense> {
    private String date; // Format has to be: YYYY-MM-DD
    private String category; // E.g., Food, Travel
    private double amount;

	/**
	 * Constructor
	 * @param String date in the format YYYY-MM-DD
	 * @param String Category of expense
	 * @param double amount of expense
	 */
    public Expense(String date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

   /** Getters and setters
    */
    public String getDate() { return date; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }

    /**
     *Compares an expense to another based on date 
     * @param Expense other, the other expense to compare to
     * @return a negative integer , zero or positive integer
     */
    public int compareTo(Expense other) {
        return this.date.compareTo(other.date); // Sort by date
    }

    /**
     * @return a string in the right format
     */
    public String toString() {
        return String.format("Date: %s, Category: %s, Amount: $%.2f", date, category, amount);
    }
}
