module com.projetodb.projetobancojava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jasperreports;


    opens com.projetodb.projetobancojava to javafx.fxml;
    opens com.projetodb.projetobancojava.model to javafx.base;

    exports com.projetodb.projetobancojava;
}