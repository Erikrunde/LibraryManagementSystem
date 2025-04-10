package org.example.libarymgmtsys;
import java.util.*;
import java.io.*;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;
/**
 * Modified ArrayList for the purposes of this program
 */
public class BookList extends ArrayList<Book> {
    File file;
    ArrayList<Book> bookList;
    Book book;
    /**
     * Empty constructor
     */
    public BookList(){
        bookList = new ArrayList<>();
    }
    /**
     * Handles the deletion of books with .remove()
     */
    public boolean deleteBook(Book book){

        if(this.contains(book)){
            this.remove(book);
            System.out.println("Book removed");
            return true;
        }
        else
            return false;
    }
    /**
     * Handles the addition of books with .add()
     */
    public void addBook(Book book, File file){
        this.add(book);
        try{
            PrintWriter appendBookFile = new PrintWriter(new FileOutputStream(file, true));
            appendBookFile.println(book.toList());
            appendBookFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Turns the entire list of books into a string
     */
    public String listToString(){
        StringBuilder listOfBooks = new StringBuilder();
        for(Book book: this){
            listOfBooks.append(book.toString() + "\n");
        }
        return listOfBooks.toString();
    }
    /**
     * Returns the book with the given Book Number to help with deletion and potential future operations
     */
    public Book findBook(int bNr) {
        for (Book book : this) {
            if (book.getBookNr() == bNr) {
                return book;
            }
        }
        return null; // No book found
    }
    /**
     * Returns a simple array of books with the given name, to be used for search results and borrowing
     */
    public Book[] searchBookByName(String name) {
        if(name.equals(""))
            return null;
        final int RESULTSIZE = 10;
        Book[] bookArray = new Book[RESULTSIZE];
        int count = 0;
        for (Book book : this) {
            if (book.getName().equalsIgnoreCase(name)) {
                bookArray[count++] = book;
            }
        }
        if(count == 0)
            return null;
        else
            return bookArray;
    }


}

