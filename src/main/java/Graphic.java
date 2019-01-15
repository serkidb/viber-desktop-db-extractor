import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Serkid on 28/6/2018.
 */
public class Graphic extends Application {

    // private Stage primaryStage = new Stage();
    private Button button;
    private Button button1;
    private Button button2;
    private Button hotmailButton;
    private Button vcfButton;
    private TableView myTableView;
    private DatabaseConnection myDB;
    private ObservableList<Contact> data;
    private File file;


    public Graphic() {

    }

    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Viber Data Exporter");


        BorderPane myLayout = new BorderPane();

        this.createButtonViews(myLayout, primaryStage);
        this.createTableView(myLayout);

        //StackPane layout = new StackPane();
        //layout.getChildren().add(button);

        Scene myScene = new Scene(myLayout, 700, 300, Color.AZURE);
        primaryStage.setScene(myScene);
        primaryStage.show();

    }

    //method to initialy create table

    public void createTableView(BorderPane myPane) {

        this.myTableView = new TableView();
        TableColumn myNameColumn = new TableColumn("Name");
        TableColumn myNumberColumn = new TableColumn("Phone Number");
        myTableView.setMinWidth(600);
        myNameColumn.setResizable(false);
        myNumberColumn.setResizable(false);
        myNameColumn.setMinWidth(300);
        myNumberColumn.setMinWidth(300);
        myNameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("name"));
        myNumberColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("number"));


        myTableView.getColumns().addAll(myNameColumn, myNumberColumn);
        //myTableView.refresh();
        myPane.setCenter(myTableView);


    }

    //method to update table

    public int updateTable(File file) {
        myDB = new DatabaseConnection();
        myDB.connect(file);
        ObservableList<Contact> data = FXCollections.observableArrayList();
        ArrayList<ArrayList<String>> contacts = myDB.getNames();
        int count = 0;
        for (int i = 0; i < contacts.size(); i++) {
            Contact cont = new Contact(contacts.get(i).get(0), contacts.get(i).get(1));
            data.add(cont);
            count++;

        }
        myTableView.setItems(data);
        myTableView.refresh();
        return count;
    }

    public void createButtonViews(BorderPane myBorderPane, final Stage primaryStage) {

        button = new Button("Find File");
        button1 = new Button("Read File");
        button2 = new Button("Google CSV");
        hotmailButton = new Button("Export To Hotmail");
        vcfButton = new Button("Export to VCF");
        button1.setDisable(true);
        button2.setDisable(true);
        button2.setMinWidth(10.0);
        hotmailButton.setDisable(true);
        hotmailButton.setMinWidth(10.0);
        vcfButton.setDisable(true);
        vcfButton.setMinWidth(10.0);
        final Label label1 = new Label();

        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                file = fileChooser.showOpenDialog(primaryStage);
                System.out.println(file);
                if (file != null) {
                    label1.setText("File Found");
                }
                System.out.println(file);
                button1.setDisable(false);
            }
        });

        button1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.out.println(file);
                if (file != null) {
                    int i = updateTable(file);
                    label1.setText("Found " + i +" contact(s)");
                    button2.setDisable(false);
                    hotmailButton.setDisable(false);
                    vcfButton.setDisable(false);

                } else {
                    label1.setText("First choose a file");
                }


            }
        });

        button2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (file != null) {
                    myDB.connect(file);
                    myDB.writeCSV();
                    label1.setText("Exported to Google CSV");
                } else {
                    label1.setText("First Choose A File");
                }
            }
        });

        vcfButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                myDB.exportToVCard();
            }
        });
        FlowPane myFlow = new FlowPane();
        GridPane myRightGridPane = new GridPane();
        FlowPane bottomFlow = new FlowPane();

        myFlow.getChildren().add(button);
        myFlow.getChildren().add(button1);
        bottomFlow.getChildren().add(label1);
        myRightGridPane.add(button2,0,0);
        myRightGridPane.add(hotmailButton,0,1);
        myRightGridPane.add(vcfButton,0,2);

        myBorderPane.setRight(myRightGridPane);
        myBorderPane.setBottom(bottomFlow);
        myBorderPane.setTop(myFlow);

    }
}
