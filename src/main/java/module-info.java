module com.example.traveldiary {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.prefs;
    requires com.opencsv;
    requires net.sourceforge.plantuml;

    opens com.example.traveldiary to javafx.fxml;
    exports com.example.traveldiary;
}