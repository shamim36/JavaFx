import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookstoreApp extends Application {

    boolean isAdmin;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bookstore Management System");

        Button bookListMenuBtn = new Button("Book List Menu");

        Button adminMenuBtn = new Button("Admin Menu");


        ///ading few pre existed books to the system
        /// 
        BookDetails bookDetails1 = new BookDetails("Java Programmming", BookStatus.AVAILABLE, "Tom",
                "1170.00");
        BookDetails bookDetails2 = new BookDetails("C Programmming", BookStatus.AVAILABLE, "Anthony",
                "1580.00");
        BookDetails bookDetails3 = new BookDetails("C# Programmming", BookStatus.RESERVED, "Liam",
                "3740.00");
        BookDetails bookDetails4 = new BookDetails("Python Programmming", BookStatus.AVAILABLE, "Sam",
                "4800.00");
        BookDetails bookDetails5 = new BookDetails("C++ Programmming", BookStatus.AVAILABLE, "Stark",
                "7800.00");


        

        bookListMenuBtn.setOnAction(event -> {

            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setTitle("Book List Menu");
            BookstoreModel model = new BookstoreModel();
            BookstoreController controller = new BookstoreController(model);
            addedDemoBooks(bookDetails1, bookDetails2, bookDetails3, bookDetails4, bookDetails5, controller);
            BookstoreView view = new BookstoreView(false, controller, model, primaryStage); /// pass admin value false
                                                                                            /// as parameter

            Scene scene = new Scene(view.asParent(), 600, 300);
            stage.setScene(scene);
            stage.show();

        });
        adminMenuBtn.setOnAction(event -> {

            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setTitle("Admin Menu");
            BookstoreModel model = new BookstoreModel();
            BookstoreController controller = new BookstoreController(model);
            addedDemoBooks(bookDetails1, bookDetails2, bookDetails3, bookDetails4, bookDetails5, controller);
            BookstoreView view = new BookstoreView(true, controller, model, primaryStage); /// pass admin value true as
                                                                                           /// parameter

            Scene scene = new Scene(view.asParent(), 600, 300);
            stage.setScene(scene);
            stage.show();

        });

        VBox root = new VBox(10, bookListMenuBtn, adminMenuBtn);

        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addedDemoBooks(BookDetails bookDetails1, BookDetails bookDetails2, BookDetails bookDetails3,
            BookDetails bookDetails4, BookDetails bookDetails5, BookstoreController controller) {
        controller.addBook(bookDetails1);
        controller.addBook(bookDetails2);
        controller.addBook(bookDetails3);
        controller.addBook(bookDetails4);
        controller.addBook(bookDetails5);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
