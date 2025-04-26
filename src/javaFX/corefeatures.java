package javaFX;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class corefeatures {
    private List<Double> transactions = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private List<LocalDateTime> time = new ArrayList<>();

    public void addTransaction(double amount, String title, String category) {
    	transactions.add(amount);
        titles.add(title);
        categories.add(category);
        time.add(LocalDateTime.now());
    }

    public boolean editTransaction(int position, double amount, String title, String category) {
        if (position < 0 || position >= transactions.size()) {
            return false; 
        }
        transactions.set(position, amount);
        titles.set(position, title);
        categories.set(position, category);
        return true; 
    }

    public boolean deleteTransaction(int pos) {
        if (pos < 0 || pos >= transactions.size()) {
            return false; 
        }
        transactions.remove(pos);
        titles.remove(pos);
        categories.remove(pos);
        time.remove(pos);
        return true; 
    }

    public String getSummary() {
        double totalIncome = 0;
        double totalExpenses = 0;

        for (Double transaction : transactions) {
            if (transaction >= 0) {
                totalIncome += transaction;
            } else {
                totalExpenses += transaction;
            }
        }

        double netIncome = totalIncome + totalExpenses;

        return String.format("Total Income: $%.2f\nTotal Expenses: $%.2f\nNet income: $%.2f",totalIncome, Math.abs(totalExpenses), netIncome);
    }

    public List<Double> getTransactions() {
        return transactions;
    }

    public List<String> getTitles() {
        return titles;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<LocalDateTime> getTime() {
        return time;
    }
}
