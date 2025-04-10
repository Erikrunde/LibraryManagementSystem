package org.example.libarymgmtsys;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;
import java.io.*;
import static javax.swing.JOptionPane.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Main program for this application
 */
public class HelloApplication extends Application {
    Label prompt;
    TextField input;
    BorderPane root;
    String filename = "books.txt";
    BookList bookList = new BookList();
    VBox centerPane = new VBox(); // Changed to VBox
    VBox listMenu = new VBox();
    File bookFile = new File(filename);

    Button btnAdd = new Button("Add book");
    Button btnShowList = new Button("Show list");
    Button btnShowDelete = new Button("Delete book");
    Button btnShowSearch = new Button("Search for a book");
    Button btnShowReturn = new Button("Return book");
    Button confirm = new Button("Confirm");
    Button borrow = new Button("Borrow");
    Button search = new Button("Search");
    Button returnBook = new Button("Return");

    /**
     * Sets the stage, binds buttons to event handlers
     */

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        showMain();
        root.setCenter(centerPane);
        root.setRight(listMenu);

        btnAdd.setOnMouseClicked(mouseEvent -> showAddMenu());
        btnShowList.setOnMouseClicked(mouseEvent -> showList());
        btnShowDelete.setOnMouseClicked(mouseEvent -> showDelete());
        btnShowSearch.setOnMouseClicked(mouseEvent -> showSearch());
        btnShowReturn.setOnMouseClicked(mouseEvent -> showReturn());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showMain());
        root.setBottom(backButton);

        readFromFile(bookFile);

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Launches the application
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * Loads the main menu
     */
    public void showMain(){
        centerPane.getChildren().clear();
        centerPane.getChildren().addAll(btnAdd, btnShowList, btnShowDelete, btnShowSearch, btnShowReturn);
        root.setRight(listMenu);  // Reset the right side panel to listMenu after returning to the main menu
    }
    /**
     * Loads the add menu, handles user inputs with help of bookList, updates book.txt
     */
    public void showAddMenu(){
        centerPane.getChildren().clear();
        prompt = new Label("Please type in the name of the book you want to add, and press Confirm.");
        input = new TextField("");
        confirm = new Button("Confirm add");
        centerPane.getChildren().addAll(prompt, input, confirm);
        confirm.setOnMouseClicked(mouseEvent -> {
            Book newBook = new Book(bookList.size() + 1, input.getText());
            bookList.addBook(newBook, bookFile);
            if(bookList.getLast().equals(newBook)) {
                showList(); // Show updated list after adding a book
            }
        });
    }
    /**
     * Shows the bookList to the user whenever a change is made, or if they click the button
     */
    public void showList(){
        listMenu.getChildren().clear();
        TextArea listOfBooks = new TextArea(bookList.listToString());
        listOfBooks.setPrefColumnCount(20);
        listMenu.getChildren().add(listOfBooks);
    }
    /**
     * Loads the delete menu, handles user inputs with help of bookList, updates book.txt
     */
    public void showDelete(){
        centerPane.getChildren().clear();
        prompt = new Label("Please type in the number of the book you want to delete, and press Confirm.");
        input = new TextField("");
        confirm = new Button("Confirm delete");
        centerPane.getChildren().addAll(prompt, input, confirm);
        confirm.setOnAction(event -> {
            String userInput = input.getText();
            if(userInput.equals("") || !userInput.matches("\\d+")) {
                showMessageDialog(null, "Invalid input. Please enter a valid book number.");
                showDelete();
                return;
            }
            int bNrInput = Integer.parseInt(userInput);
            if (bNrInput != 0 && bookList.deleteBook(bookList.findBook(bNrInput))){
                generateFile(bookFile);
                showList(); // Show updated list after deleting a book
            }
        });
    }
    /**
     * Loads the search menu, handles user inputs with help of bookList, displays search results, allows users to
     * borrow books,updates book.txt
     */
    public void showSearch() {
        centerPane.getChildren().clear();
        root.setRight(null);  // Remove the listMenu when in search mode

        prompt = new Label("Search for a book using its exact name:");
        input = new TextField();
        search = new Button("Search");
        VBox resultsBox = new VBox();
        resultsBox.setSpacing(10);
        centerPane.getChildren().addAll(prompt, input, search, resultsBox);

        search.setOnAction(event -> {
            resultsBox.getChildren().clear();
            Book[] bookArray = bookList.searchBookByName(input.getText());
            if (bookArray == null || bookArray.length == 0 || bookArray[0] == null) {
                showMessageDialog(null, "No books found.");
                return;
            }
            // Create and apply animations to each book entry
            for (int i = 0; i < bookArray.length; i++) {
                Book book = bookArray[i];
                if (book == null) break;

                // Create a VBox for the book's info and the borrow button
                TextArea bookInfo = new TextArea(book.toString());
                bookInfo.setEditable(false);
                Button borrowButton = new Button("Borrow");
                borrowButton.setOnAction(e -> {
                    String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    if (book.borrow(todayDate)) {
                        showMessageDialog(null, "Book borrowed successfully!");
                        generateFile(bookFile);
                        showList();
                    } else {
                        showMessageDialog(null, "Book is already borrowed.");
                    }
                });
                VBox bookContainer = new VBox(bookInfo, borrowButton);
                bookContainer.setSpacing(5);

                // Add the book container to the results box
                resultsBox.getChildren().add(bookContainer);

                animateBookEntry(bookContainer, i * 300);  // Delay each animation for a smooth effect
            }
        });
    }
    /**
     * Animates each book entry with a fade and slide
     */
    private void animateBookEntry(Node bookNode, int delay) {
        // Fade in animation
        FadeTransition fade = new FadeTransition(Duration.millis(300), bookNode);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setDelay(Duration.millis(delay));
        fade.play();

        TranslateTransition slide = new TranslateTransition(Duration.millis(300), bookNode);
        slide.setFromX(-500);  // Start off-screen (from left)
        slide.setToX(0);  // End at normal position
        slide.setDelay(Duration.millis(delay));
        slide.play();
    }
    /**
     * Loads the return menu, handles user inputs with help of bookList, updates book.txt
     */
    public void showReturn() {
        centerPane.getChildren().clear();
        prompt = new Label("Please enter the Book Number for the book you wish to return:");
        input = new TextField("");
        confirm = new Button("Return");
        centerPane.getChildren().addAll(prompt, input, confirm);
        confirm.setOnAction(event -> {
            String userInput = input.getText();
            if (userInput.equals("") || !userInput.matches("\\d+")) {
                showMessageDialog(null, "Invalid input. Please enter a valid book number.");
                showReturn();
                return;
            }
            int bnr = Integer.parseInt(userInput);
            Book book = bookList.findBook(bnr);
            if (book == null) {
                showMessageDialog(null, "Book not found. Try again.");
                return;
            }
            String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            if (book.returnBook(todayDate)) {
                showMessageDialog(null, "Book returned successfully!");
                generateFile(bookFile);
                showList(); // Show updated list after returning a book
            } else {
                showMessageDialog(null, "Book was not borrowed.");
            }
        });
    }
    /**
     * Updates book.txt with the newest information
     */
    public void generateFile(File file){
        try{
            PrintWriter listToFile = new PrintWriter(new FileOutputStream(file, false));
            int i = 0;
            while(i < bookList.size()){
                listToFile.println(bookList.get(i).toList());
                i++;
            }
            listToFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Reads books.txt whenever the user starts the application
     */
    public void readFromFile(File file){
        try{
            Scanner readBookList = new Scanner(file);

            String[] bookTab;
            while(readBookList.hasNextLine() && readBookList.hasNext()){
                bookTab = readBookList.nextLine().split(";");
                bookList.add(new Book((Integer.parseInt(bookTab[0])), bookTab[1], bookTab[2], bookTab[3], bookTab[4]));
            }
            readBookList.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
