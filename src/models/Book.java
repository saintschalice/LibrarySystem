package models;

public class Book {
    private int id;
    private String title;
    private String ISBN;
    private String category;
    private String author;
    private String copyright;
    private String publisher;
    private String status;

    // Constructor
    public Book(int id, String title, String ISBN, String category, String author, String copyright, String publisher, String status) {
        this.id = id;
        this.title = title;
        this.ISBN = ISBN;
        this.category = category;
        this.author = author;
        this.copyright = copyright;
        this.publisher = publisher;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}