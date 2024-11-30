import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class BookstoreModel {
    private final ObservableList<BookDetails> bookLists;

    BookstoreModel() {
        this.bookLists = FXCollections.observableArrayList();
    }

    public ObservableList<BookDetails> bookListsProperty() {
        return this.bookLists;
    }

    public void addBook(BookDetails bookDetails) {
        this.bookLists.add(bookDetails);
        BookStoreSystem.addBook(bookLists.getDetails());
    }

    public void updateBook(BookDetails bookDetails, int index) {
        this.bookLists.set(index, bookDetails);
    }

    public void removeBook(int index) {
        this.bookLists.remove(index);
    }

}



class BookDetails {
    // To wrap up enum values within a Property we can use `SimpleObjectProperty`,
    // provided by JavaFX.
    private final SimpleObjectProperty<BookStatus> status;
    private final SimpleStringProperty bookName;
    private final SimpleStringProperty bookAuthor;
    // private final SimpleStringProperty bookGenre;
    private final SimpleStringProperty bookPrice;

    // BookDetails(String bookName, BookStatus status, String bookAuthor, Genre bookGenre, String bookPrice) {
    BookDetails(String bookName, BookStatus status, String bookAuthor,String bookPrice) {
        this.bookName = new SimpleStringProperty(bookName);
        this.status = new SimpleObjectProperty<>(status);
        this.bookAuthor = new SimpleStringProperty(bookAuthor);
        // this.bookGenre = new SimpleObjectProperty<>(bookGenre);
        this.bookPrice = new SimpleStringProperty(bookPrice);
        
    }

    // getters and setters
    public BookStatus getStatus() {
        return status.get();
    }

    public void setStatus(BookStatus status) {
        this.status.set(status);

    }

    public SimpleObjectProperty<BookStatus> statusProperty() {
        return status;
    }

    public String getBookName() {
        return bookName.get();
    }

    public void setBookName(String bookName) {
        this.bookName.set(bookName);
    }

    public SimpleStringProperty bookNameProperty() {
        return bookName;
    }

    public String getBookAuthor() {
        return bookAuthor.get();
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor.set(bookAuthor);
    }

    public SimpleStringProperty bookAuthorProperty() {
        return bookAuthor;
    }


    // public Genre getBookGenre() {
    //     return bookGenre.get();
    // }

    // public void setBookGenre(Genre bookGenre) {
    //     this.bookGenre.set(bookGenre);
    // }

    // public SimpleStringProperty<Genre> bookGenreProperty() {
    //     return bookGenre;
    // }

    public String getBookPrice() {
        return bookPrice.get();
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice.set(bookPrice);
    }

    public SimpleStringProperty bookPriceProperty() {
        return bookPrice;
    }

    public String getDetails() {
        return "Technology Book: " + getBookName().toString() + " by " + getBookAuthor().toString() + " - $" + getBookPrice().toString() + " - Status: " + getStatus().toString();
    }



    
}

enum Genre {
    FICTION, TECHNOLOGY, SCIENCE, ART
}

enum BookStatus {
    AVAILABLE, RESERVED
}



abstract class BookLists {
    protected String title;
    protected String author;
    protected BookStatus status;
    protected double price;



    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public BookStatus getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getDetails();
}



class TechnologyBook extends BookLists {
    public TechnologyBook(String title, String author, BookStatus status, double price) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.price = price;
    }

    @Override
    public String getDetails() {
        return "Technology Book: " + title + " by " + author + " - $" + price + " - Status: " + status;
    }
}

class ArtBook extends BookLists {
    public ArtBook(String title, String author, BookStatus status, double price) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.price = price;
    }

    @Override
    public String getDetails() {
        return "Art Book: " + title + " by " + author + " - $" + price + " - Status: " + status;
    }
}

class ScienceBook extends BookLists {
    public ScienceBook(String title, String author, BookStatus status, double price) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.price = price;
    }

    @Override
    public String getDetails() {
        return "Science Book: " + title + " by " + author + " - $" + price + " - Status: " + status;
    }
}

class BookStoreSystem implements AddRemoveBook {



    private final List<BookLists> books;


    

    // Comparator to sort books by title using method references
    private static final Comparator<BookLists> bookTitleComparator = Comparator.comparing(BookLists::getTitle);

    public BookStoreSystem() {
        books = new ArrayList<>();
        // users = new HashMap<>();
    }

    public void addBook(BookLists book) {
        books.add(book);
        Collections.sort(books, bookTitleComparator); // Sort books by title after adding
    }

    public void removeBook(String title) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equals(title)) {
                books.remove(i);
                break;
            }
        }
    }

    public List<BookLists> getBooks() {
        return books;
    }

    public void searchBooks(String title) {
        List<BookLists> foundBooks = new ArrayList<>();
        for (BookLists book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                foundBooks.add(book);
            }
        }

        BookstoreView searchScene = new BookstoreView();
        searchScene.getSearchScene(title, foundBooks, books);
    }

    


    public void displayAllBooks() {
        
        for (BookLists book : books) {
            book.getDetails();
        }
    }
}

interface AddRemoveBook {
    public void removeBook(String title);

    public void addBook(BookLists book);
}

