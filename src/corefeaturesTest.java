package javaFX;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreFeaturesTest {

    private corefeatures core;

    @BeforeEach
    void setUp() {
        core = new corefeatures();
    }

    @Test
    void testAddTransaction() {
        core.addTransaction(500.0, "Freelance", "Income");

        assertEquals(1, core.getTransactions().size(), "Transaction was not added correctly.");
        assertEquals(500.0, core.getTransactions().get(0), "Transaction amount does not match.");
        assertEquals("Freelance", core.getTitles().get(0), "Transaction title does not match.");
        assertEquals("Income", core.getCategories().get(0), "Transaction category does not match.");
    }

    @Test
    void testEditTransaction() {
        core.addTransaction(200.0, "Gift", "Other");

        boolean edited = core.editTransaction(0, 300.0, "Bonus", "Income");

        assertEquals(true, edited, "Transaction edit failed.");
        assertEquals(300.0, core.getTransactions().get(0), "Edited transaction amount does not match.");
        assertEquals("Bonus", core.getTitles().get(0), "Edited transaction title does not match.");
        assertEquals("Income", core.getCategories().get(0), "Edited transaction category does not match.");
    }

    @Test
    void testDeleteTransaction() {
        core.addTransaction(100.0, "Payback", "Loan");

        boolean deleted = core.deleteTransaction(0);

        assertEquals(true, deleted, "Transaction deletion failed.");
        assertEquals(0, core.getTransactions().size(), "Transaction list should be empty.");
    }

    @Test
    void testGetSummary() {
        core.addTransaction(1000.0, "Salary", "Income");
        core.addTransaction(-200.0, "Groceries", "Expense");
        core.addTransaction(-150.0, "Transport", "Expense");

        String summary = core.getSummary();

        assertEquals(true, summary.contains("Total Income: $1000.00"), "Summary does not show correct income.");
        assertEquals(true, summary.contains("Total Expenses: $350.00"), "Summary does not show correct expenses.");
        assertEquals(true, summary.contains("Net income: $650.00"), "Summary does not show correct net income.");
    }

    @Test
    void testGetTransactions() {
        core.addTransaction(500.0, "Freelance", "Income");

        assertEquals(1, core.getTransactions().size(), "Transaction list size is incorrect.");
    }

    @Test
    void testGetTitles() {
        core.addTransaction(500.0, "Freelance", "Income");

        assertEquals("Freelance", core.getTitles().get(0), "Title does not match.");
    }

    @Test
    void testGetCategories() {
        core.addTransaction(500.0, "Freelance", "Income");

        assertEquals("Income", core.getCategories().get(0), "Category does not match.");
    }

    @Test
    void testHashCode() {
        core.addTransaction(500.0, "Freelance", "Income");
        core.addTransaction(200.0, "Gift", "Expense");

        assertNotNull(core.hashCode(), "hashCode is not generated correctly.");
    }

    @Test
    void testEquals() {
        core.addTransaction(500.0, "Freelance", "Income");
        core.addTransaction(200.0, "Gift", "Expense");

        assertEquals(core, core, "equals() method failed.");
    }

    @Test
    void testToString() {
        core.addTransaction(500.0, "Freelance", "Income");

        assertNotNull(core.toString(), "toString() method failed.");
    }
}
