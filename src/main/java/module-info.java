module com.java.tp {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    opens com.java.tp to javafx.fxml;
    exports com.java.tp;
}
