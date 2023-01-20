module ru.starstreet.particallife {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    opens ru.starstreet.particallife to javafx.fxml;
    exports ru.starstreet.particallife;
}