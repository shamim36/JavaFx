public class BookstoreController {
    private final BookstoreModel model;

    public BookstoreController(BookstoreModel model) {
        this.model = model;
    }

    public void updateBook(BookDetails bookDetails, int index) {
        this.model.updateBook(bookDetails, index);
    }

    public void addBook(BookDetails bookDetails) {
        this.model.addBook(bookDetails);
    }

    public void removeBook(int index) {
        this.model.removeBook(index);
    }
}