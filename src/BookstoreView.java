import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookstoreView {
    private VBox view;
    private TableView<BookDetails> bookView;
    private Button addBookBtn;
    private Button updateBookBtn;
    private Button removeBookBtn;

    private BookstoreController controller;
    private BookstoreModel model;
    private Stage primaryStage;

    private Boolean isAdmin;

    public BookstoreView(Boolean isAdmin, BookstoreController controller, BookstoreModel model, Stage primaryStage) {

        this.isAdmin = isAdmin;
        this.controller = controller;
        this.model = model;
        this.primaryStage = primaryStage;

        createAndConfigurePane();
        createAndLayoutControls();
        // updateControllerFromListeners();
        // observeModelAndUpdateControls();
    }

    public Parent asParent() {
        return view;
    }

    // private void observeModelAndUpdateControls() {
    // // Not needed here
    // }

    // private void updateControllerFromListeners() {
    // // Not needed here
    // }

    private void createAndLayoutControls() {
        // Setup the TableView
        this.bookView = new TableView<>();
        TableColumn<BookDetails, String> bookNameCol = new TableColumn<>("Book Name");
        bookNameCol.setMinWidth(150);
        bookNameCol.setCellValueFactory(cellData -> cellData.getValue().bookNameProperty());

        TableColumn<BookDetails, String> authorCol = new TableColumn<>("Author");
        authorCol.setMinWidth(150);
        authorCol.setCellValueFactory(cellData -> cellData.getValue().bookAuthorProperty());

        TableColumn<BookDetails, BookStatus> statusCol = new TableColumn<>("Status");
        statusCol.setMinWidth(100);
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        // TableColumn<BookDetails, Genre> genreCol = new TableColumn<>("Genre");
        // genreCol.setMinWidth(120);
        // genreCol.setCellValueFactory(cellData ->
        // cellData.getValue().bookGenreProperty());

        TableColumn<BookDetails, String> priceCol = new TableColumn<>("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(cellData -> cellData.getValue().bookPriceProperty());

        // this.bookView.getColumns().addAll(bookNameCol, authorCol, statusCol,
        // genreCol, priceCol);
        this.bookView.getColumns().addAll(bookNameCol, authorCol, statusCol, priceCol);
        this.bookView.setItems(model.bookListsProperty());

        // Setup Buttons

        this.addBookBtn = new Button("Add Book");
        this.addBookBtn.setOnAction(event -> createAddBookForm());

        this.updateBookBtn = new Button("Edit Book");
        this.updateBookBtn.setOnAction(event -> {
            int index = this.bookView.getSelectionModel().getSelectedIndex();
            if (index != -1) {
                createUpdateBookForm(index);
            }
        });

        this.removeBookBtn = new Button("Remove Book");
        this.removeBookBtn.setOnAction(event -> {
            int index = this.bookView.getSelectionModel().getSelectedIndex();
            if (index != -1) {
                this.controller.removeBook(index);
            }
        });
        HBox buttonRow = new HBox(10, addBookBtn, updateBookBtn, removeBookBtn);
        buttonRow.setAlignment(Pos.CENTER);

        TextField searchField = new TextField();
        searchField.setPromptText("Search By Title");
        HBox searchRow = new HBox(5, new Label("Search (Title):"), searchField);
        searchRow.setAlignment(Pos.BOTTOM_LEFT);

        for (int index = 0; index < model.bookListsProperty().size(); index++) {
            String title = model.bookListsProperty().get(index).getBookName();
            if (searchField.getText().equals(title)) {

            }
        }

        if (isAdmin) {
            view.getChildren().addAll(this.bookView, buttonRow);
        } else {
            view.getChildren().addAll(this.bookView, searchField);
        }

    }

    private void createAndConfigurePane() {
        view = new VBox(5);
        view.setAlignment(Pos.CENTER);
    }

    private void createAddBookForm() {
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        TextField namingField = new TextField();
        namingField.setPromptText("Enter book title");
        HBox namingRow = new HBox(5, new Label("Title:"), namingField);
        namingRow.setAlignment(Pos.CENTER);

        TextField authorField = new TextField();
        authorField.setPromptText("Enter author name");
        HBox authorRow = new HBox(5, new Label("Author:"), authorField);
        authorRow.setAlignment(Pos.CENTER);

        // TextField genreField = new TextField();
        // genreField.setPromptText("Enter book genre");
        // HBox genreRow = new HBox(5, new Label("Genre:"), genreField);
        // genreRow.setAlignment(Pos.CENTER);

        TextField priceField = new TextField();
        priceField.setPromptText("Enter book price");
        HBox priceRow = new HBox(5, new Label("Price:"), priceField);
        priceRow.setAlignment(Pos.CENTER);

        // Toggle group to set status
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton reserverBtn = new RadioButton("RESERVED");
        reserverBtn.setToggleGroup(toggleGroup);

        RadioButton availableBtn = new RadioButton("AVAILABLE");
        availableBtn.setToggleGroup(toggleGroup);

        HBox radioBtnRow = new HBox(5, reserverBtn, availableBtn);
        radioBtnRow.setAlignment(Pos.CENTER);

        Button submitBtn = new Button("Update");
        submitBtn.setOnAction(event -> {
            String name = namingField.getText().trim();
            // String author = authorField.getText().trim();
            // String price = priceField.getText().trim();
            BookStatus status;
            if (reserverBtn.isSelected()) {
                status = BookStatus.RESERVED;
            } else {
                status = BookStatus.AVAILABLE;
            }

            if (!name.isEmpty()) {
                BookDetails bookDetails = new BookDetails(namingField.getText(), status, authorField.getText(),
                        priceField.getText());
                this.controller.addBook(bookDetails);
                stage.close();
            }
        });
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(event -> stage.close());

        HBox buttonRow = new HBox(5, submitBtn, cancelBtn);
        buttonRow.setAlignment(Pos.CENTER);

        // VBox root = new VBox(5, namingRow, authorRow, genreRow, priceRow,
        // radioBtnRow, buttonRow);
        VBox root = new VBox(5, namingRow, authorRow, priceRow, radioBtnRow, buttonRow);
        root.setAlignment(Pos.CENTER);

        Scene addBookScene = new Scene(root, 400, 400);

        stage.setScene(addBookScene);
        stage.show();
    }

    private void createUpdateBookForm(int index) {
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.APPLICATION_MODAL);

        // We access the model's bookDetailsProperty to determine the description of the
        // selected to-do.
        TextField namingField = new TextField();
        String oldName = model.bookListsProperty().get(index).getBookName();
        namingField.setPromptText("Enter new title");
        namingField.setText(oldName);

        HBox namingRow = new HBox(5, new Label("Title:"), namingField);
        namingRow.setAlignment(Pos.CENTER);

        TextField authorField = new TextField();
        String oldAuthor = model.bookListsProperty().get(index).getBookAuthor();
        authorField.setPromptText("Enter new Author");
        authorField.setText(oldAuthor);

        HBox authorRow = new HBox(5, new Label("Author:"), authorField);
        authorRow.setAlignment(Pos.CENTER);

        TextField priceField = new TextField();
        String oldPrice = model.bookListsProperty().get(index).getBookPrice();
        priceField.setPromptText("Enter new Price");
        priceField.setText(oldPrice);

        HBox priceRow = new HBox(5, new Label("Price:"), priceField);
        priceRow.setAlignment(Pos.CENTER);

        // TextField genreField = new TextField();
        // Genre oldGenre = model.bookDetailsProperty().get(index).getBookGenre();
        // genreField.setPromptText("Enter new Genre");
        // genreField.set(oldGenre);

        // HBox genreRow = new HBox(5, new Label("Title:"), genreField);
        // genreRow.setAlignment(Pos.CENTER);

        // Toggle group to set status
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton reserverBtn = new RadioButton("AVAILABLE");
        reserverBtn.setToggleGroup(toggleGroup);

        RadioButton availableBtn = new RadioButton("RESERVED");
        availableBtn.setToggleGroup(toggleGroup);

        HBox radioBtnRow = new HBox(5, reserverBtn, availableBtn);
        radioBtnRow.setAlignment(Pos.CENTER);

        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(event -> {
            String name = namingField.getText().trim();
            String author = authorField.getText().trim();
            String price = priceField.getText().trim();
            BookStatus oldStatus = model.bookListsProperty().get(index).getStatus();
            BookStatus newStatus;
            if (reserverBtn.isSelected()) {
                newStatus = BookStatus.AVAILABLE;
            } else {
                newStatus = BookStatus.RESERVED;
            }
            boolean changedStatus = oldStatus != newStatus;
            boolean newNameIsNonEmptyAndDiffers = !name.isEmpty() && !name.equals(oldName);
            boolean newAuthorIsNonEmptyAndDiffers = !author.isEmpty() || !author.equals(oldAuthor);
            boolean newPriceIsNonEmptyAndDiffers = !price.isEmpty() || !price.equals(oldPrice);
            if (newNameIsNonEmptyAndDiffers || changedStatus || newAuthorIsNonEmptyAndDiffers
                    || newPriceIsNonEmptyAndDiffers) {
                BookDetails bookDetails = new BookDetails(namingField.getText(), newStatus, authorField.getText(),
                        priceField.getText());
                this.controller.updateBook(bookDetails, index);
                stage.close();
            }
        });
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(event -> stage.close());

        HBox buttonRow = new HBox(5, submitBtn, cancelBtn);
        buttonRow.setAlignment(Pos.CENTER);

        VBox root = new VBox(5, namingRow, authorRow, priceRow, radioBtnRow, buttonRow);
        root.setAlignment(Pos.CENTER);

        Scene updateBookScene = new Scene(root, 400, 400);

        stage.setScene(updateBookScene);
        stage.show();
    }

    void getSearchScene(String title, List<BookLists> foundBooks, List<BookLists> books) {
        // Create a single window to display all search results
        Stage searchStage = new Stage();
        searchStage.setTitle("Search Results for Title: " + title);

        VBox searchRoot = new VBox(10);
        searchRoot.setAlignment(Pos.CENTER);

        if (foundBooks.isEmpty()) {
            searchRoot.getChildren().add(new Label("No books found with the title: " + title));

        } else {
            foundBooks.forEach(book -> System.out.println(book));

            for (BookLists book : books) {
                if (book instanceof BookLists && book.getTitle().equals(title)) {

                    searchRoot.getChildren().add(new Label(book.getDetails()));

                }
            }
        }
        Scene scene = new Scene(searchRoot, 600, 300);
        searchStage.setScene(scene);
        searchStage.show();
    }
}