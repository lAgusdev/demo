module com.java.tp {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.java.tp to javafx.fxml;
    exports com.java.tp;
}
