module hr.tvz.stefan.project.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;


    exports hr.tvz.stefan.project.passwordmanager.controllers;
    opens hr.tvz.stefan.project.passwordmanager.controllers to javafx.fxml;
    exports hr.tvz.stefan.project.passwordmanager;
    opens hr.tvz.stefan.project.passwordmanager to javafx.fxml;
}