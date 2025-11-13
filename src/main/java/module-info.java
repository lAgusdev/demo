module com.java.tp {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive jakarta.xml.bind;
    exports com.java.tp.guiControllers; 
    opens com.java.tp.guiControllers to javafx.fxml;
    exports com.java.tp;
}
