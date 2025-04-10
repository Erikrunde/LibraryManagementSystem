package org.example.libarymgmtsys;
/**
 * Book object and methods, to handle operations such as borrow and return, as well as get and set methods
 */
public class Book {
    int bNr;
    String name, dateBorrowed, dateReturned, borrowedStatus;
    Book book;
    /**
     * Default constructor sets the date borrowed and returned to N/A because it is a new book
     */
    public Book(int bNr, String name){
        this.bNr = bNr;
        this.name = name;
        this.dateBorrowed = "N/A";
        this.dateReturned = "N/A";
        this.borrowedStatus = "Available";
    }
    /**
     * This constructor is simply used to create objects out of text read by the scanner
     */
    public Book(int bNr, String name, String dateBorrowed, String dateReturned, String borrowedStatus){
        this.bNr = bNr;
        this.name = name;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = dateReturned;
        this.borrowedStatus = borrowedStatus;
    }
    /**
     * Do I need to explain get and set methods?
     */
    public String getName(){
        return name;
    }
    public int getBookNr(){
        return bNr;
    }
    public String getBorrowedStatus(){
        return borrowedStatus;
    }
    public String getDateBorrowed(){
        return dateBorrowed;
    }
    public String getDateReturned(){
        return dateReturned;
    }
    public void setDateBorrowed(String date){
        if(date == null) //Add in more constraints later
            throw new IllegalArgumentException("Please give the date in MM/DD/YYYY format");
        this.dateBorrowed = date;
    }
    public void setDateReturned(String date){
        if(date == null) //Add in more constraints later
            throw new IllegalArgumentException("Please give the date in MM/DD/YYYY format");
        this.dateReturned = date;
    }
    /**
     * Handles the borrowing of books with other methods from this class
     */
    public boolean borrow(String date){
        if(this.borrowedStatus.equals("Available")) {
            this.borrowedStatus = "Borrowed";
            setDateBorrowed(date);
            return true;
        }
        else
            return false;
    }
    /**
     * Handles the returning of books with other methods from this class
     */
    public boolean returnBook(String date){
        if(this.borrowedStatus.equals("Borrowed")) {
            this.borrowedStatus = "Available";
            setDateReturned(date);
            return true;
        }
        else
            return false;
    }

    @Override
    public String toString(){
        return "Book number: " + bNr + "\n" + "Name: " + name + "\n" + "Date borrowed: "  + dateBorrowed + "\n" + "Date returned: " + dateReturned + "\n" + "Availability: " + borrowedStatus;
    }
    /**
     * Puts the given book into the text file format, so that .split(";") will work on it to create future book objects
     */
    public String toList(){
        return bNr + ";" + name + ";" + dateBorrowed + ";" + dateReturned + ";" + borrowedStatus;
    }
}
