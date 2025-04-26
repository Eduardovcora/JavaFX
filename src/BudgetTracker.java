package javaFX;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BudgetTracker extends Application {

    private corefeatures core = new corefeatures();
    private ListView<String> list = new ListView<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy h:mm a");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Budget Tracker");

        Button add = new Button("Add Transaction");
        Button viewSummary = new Button("View Summary");
        Button edit = new Button("Edit Transaction");
        Button delete = new Button("Delete Transaction");
        Button exit = new Button("Exit Program");
        Button categoryFilter = new Button("Filter by Category");
        Button sort = new Button("Sort by Date & Time");

        TextField trans = new TextField();
        TextField title = new TextField();
        TextField category = new TextField();

        RadioButton income = new RadioButton("Income");
        RadioButton expense = new RadioButton("Expense");
        ToggleGroup typeGroup = new ToggleGroup();
        income.setToggleGroup(typeGroup);
        expense.setToggleGroup(typeGroup);
        income.setSelected(true);

        list.setPrefHeight(200);

        add.setOnAction(action -> {
            try {
                double amount = Double.parseDouble(trans.getText());
                if (expense.isSelected() && amount > 0) amount = -amount;

                core.addTransaction(amount, title.getText(), category.getText());
                updateListView();
            } catch (NumberFormatException e) {
                showAlert("Please enter a numeric value for the amount!", Alert.AlertType.ERROR);
            }
        });

        viewSummary.setOnAction(action -> {
            String summary = core.getSummary();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Transaction Summary");
            alert.setHeaderText("Summary of Transactions");
            alert.setContentText(summary);
            alert.showAndWait();
        });

        edit.setOnAction(action -> {
            if (core.getTransactions().isEmpty()) {
                showAlert("No transaction to edit", Alert.AlertType.INFORMATION);
                return;
            }

            TextInputDialog input = new TextInputDialog();
            input.setTitle("Edit Transaction");
            input.setHeaderText("Enter transaction number to edit");
            input.setContentText("Transaction #:");

            input.showAndWait().ifPresent(indexStr -> {
                try {
                    int index = Integer.parseInt(indexStr);
                    if (index < 0 || index >= core.getTransactions().size()) {
                        showAlert("Invalid transaction index.", Alert.AlertType.ERROR);
                        return;
                    }

                    TextInputDialog amountDialog = new TextInputDialog();
                    amountDialog.setTitle("Edit Amount");
                    amountDialog.setHeaderText("Enter new amount");
                    amountDialog.setContentText("Amount:");

                    amountDialog.showAndWait().ifPresent(newAmtStr -> {
                        try {
                            final double[] amount = {Double.parseDouble(newAmtStr)};

                            TextInputDialog typeDialog = new TextInputDialog();
                            typeDialog.setTitle("Edit Type");
                            typeDialog.setHeaderText("Type (income/expense):");

                            typeDialog.showAndWait().ifPresent(type -> {
                                if (type.equalsIgnoreCase("expense") && amount[0] > 0) {
                                    amount[0] = -amount[0];
                                }

                                TextInputDialog titleDialog = new TextInputDialog();
                                titleDialog.setTitle("Edit Title");
                                titleDialog.setHeaderText("New Title:");

                                titleDialog.showAndWait().ifPresent(newTitle -> {
                                    TextInputDialog catDialog = new TextInputDialog();
                                    catDialog.setTitle("Edit Category");
                                    catDialog.setHeaderText("New Category:");

                                    catDialog.showAndWait().ifPresent(newCat -> {
                                        core.editTransaction(index, amount[0], newTitle, newCat);
                                        updateListView();
                                    });
                                });
                            });

                        } catch (NumberFormatException e) {
                            showAlert("Invalid number for amount.", Alert.AlertType.ERROR);
                        }
                    });

                } catch (NumberFormatException e) {
                    showAlert("Invalid transaction number.", Alert.AlertType.ERROR);
                }
            });
        });

        delete.setOnAction(action -> {
            if (core.getTransactions().isEmpty()) {
                showAlert("No transactions to delete.", Alert.AlertType.INFORMATION);
                return;
            }

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Delete Transaction");
            dialog.setHeaderText("Enter transaction number to delete");

            dialog.showAndWait().ifPresent(indexStr -> {
                try {
                    int index = Integer.parseInt(indexStr);
                    if (index < 0 || index >= core.getTransactions().size()) {
                        showAlert("Invalid transaction number.", Alert.AlertType.ERROR);
                        return;
                    }

                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setHeaderText("Are you sure you want to delete this transaction?");
                    confirm.showAndWait().ifPresent(result -> {
                        if (result == ButtonType.OK) {
                            core.deleteTransaction(index);
                            updateListView();
                        }
                    });
                } catch (NumberFormatException e) {
                    showAlert("Invalid transaction number.", Alert.AlertType.ERROR);
                }
            });
        });

        categoryFilter.setOnAction(action -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Filter by Category");
            dialog.setHeaderText("Enter category to filter by");

            dialog.showAndWait().ifPresent(filter -> {
                List<String> filtered = new ArrayList<>();
                for (int i = 0; i < core.getTransactions().size(); i++) {
                    if (core.getCategories().get(i).equalsIgnoreCase(filter)) {
                        filtered.add(formatTransaction(i));
                    }
                }
                list.getItems().setAll(filtered);
            });
        });

        sort.setOnAction(action -> {
            List<Integer> indices = new ArrayList<>();
            for (int i = 0; i < core.getTime().size(); i++) indices.add(i);

            indices.sort(Comparator.comparing(core.getTime()::get));

            List<Double> sortedTrans = new ArrayList<>();
            List<String> sortedTitles = new ArrayList<>();
            List<String> sortedCats = new ArrayList<>();
            List<LocalDateTime> sortedTimes = new ArrayList<>();

            for (int i : indices) {
                sortedTrans.add(core.getTransactions().get(i));
                sortedTitles.add(core.getTitles().get(i));
                sortedCats.add(core.getCategories().get(i));
                sortedTimes.add(core.getTime().get(i));
            }

            core.getTransactions().clear();
            core.getTitles().clear();
            core.getCategories().clear();
            core.getTime().clear();

            core.getTransactions().addAll(sortedTrans);
            core.getTitles().addAll(sortedTitles);
            core.getCategories().addAll(sortedCats);
            core.getTime().addAll(sortedTimes);

            updateListView();
        });

        exit.setOnAction(action -> primaryStage.close());

        VBox vbox = new VBox();
        vbox.getChildren().addAll(title, trans, category, income, expense, add, viewSummary, edit, delete, categoryFilter, sort, exit, list);

        Scene scene = new Scene(vbox, 600, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateListView() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < core.getTransactions().size(); i++) {
            items.add(formatTransaction(i));
        }
        list.getItems().setAll(items);
    }

    private String formatTransaction(int i) {
        return "Transaction number: " + i +" | Title: " + core.getTitles().get(i) +" | Amount: " + core.getTransactions().get(i) +" | Category: " + core.getCategories().get(i) +" | Date & Time: " + core.getTime().get(i).format(formatter);
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
