module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires exp4j;
    requires java.scripting;


    opens com.example.demo2 to javafx.fxml;
    exports com.example.demo2;
    exports com.example.demo2.controller;
    opens com.example.demo2.controller to javafx.fxml;
    exports com.example.demo2.series.processor;
    opens com.example.demo2.series.processor to javafx.fxml;
}