package hr.tvz.stefan.project.passwordmanager.controllers;

import hr.tvz.stefan.project.passwordmanager.util.PasswordGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PasswordGeneratorController {

    @FXML
    private TextField passwordField;

    public void initialize() {

    }

    public void generatePassword() {
        passwordField.setText(PasswordGenerator.generatePassword(16));
    }

}
