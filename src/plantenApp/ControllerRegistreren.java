package plantenApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import plantenApp.java.dao.GebruikerDAO;
import plantenApp.java.model.Gebruiker;

import javax.swing.*;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerRegistreren {
    private GebruikerDAO gebruikerDAO;

    public Button btnRegistrerenStudent;
    public Button btnAnnulerenRegistreren;
    public TextField txtVoornaamStudent;
    public TextField txtAchternaamStudent;
    public TextField txtVivesMail;

    public PasswordField pfWachtwoordStudent;
    public PasswordField pfStudentWachtwoordHerhalen;

    public Label lblGelijkeWW;
    public Label lblWachtwoordValidatie;
    public Label lblEmailBoodschap;

    public void initialize(){
        btnRegistrerenStudent.setDisable(true);
    }

    public void loadScreen(MouseEvent event, String screenName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(screenName));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Author Matthias Vancoillie
     *
     * @param
     * @Return overgang en werking Registratie Student
     */

    // Valideren van een e-mail
    public boolean validateEmail(String sVivesEmail) {
        boolean status = false;

        //[a-zA-Z0-9_+&*-]+
        String email_pattern = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9_+&*-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pattern = Pattern.compile(email_pattern);
        Matcher matcher = pattern.matcher(sVivesEmail);

       // if-else aanmaken om te kijken als het ingevoerde veld het pattern opvolgt.
        if (matcher.matches()) {
            status = true;

            //return pattern.matcher(email_pattern).matches();
            lblEmailBoodschap.setText("Dit is een geldig e-mailadres.");
        } else {
            status = false;
            lblEmailBoodschap.setText("Dit is geen geldig e-mailadres");
        }
        return status;

    }


    // Valideren van een wachtwoord
    /*
    Minstens 10 karakters
    Minstens 1 hoofdletter
    Minstens 1 kleine letter
    Minstens 1 nummer
     */
    public boolean validateWachtwoord(String sWachtwoordStudent) {
        if (sWachtwoordStudent.length() > 9) {

            if (checkPass(sWachtwoordStudent)) {
                return true;
            } else {
                return false;
            }

        } else {
            lblWachtwoordValidatie.setText("Wachtwoord is te klein");
            return false;
        }
    }

    public boolean checkPass(String sWachtwoordStudent) {

        boolean hasNum = false;
        boolean hasCap = false;
        boolean hasLow = false;

        char c;

        for (int i = 0; i < sWachtwoordStudent.length(); i++) {
            c = sWachtwoordStudent.charAt(i);
            if (Character.isDigit(c)) {
                hasNum = true;
            } else if (Character.isUpperCase(c)) {
                hasCap = true;
            } else if (Character.isLowerCase(c)) {
                hasLow = true;
            }
            if (hasNum && hasCap && hasLow) {
                return true;
            }

        }
        return false;
    }


    public void clicked_RegistrerenStudent(MouseEvent mouseEvent) throws SQLException {
        // Scherm voor het registreren van een student
            // knop om de aanvraag op registratie in te dienen voor de student.
            // knop om annulatie -> terug naar login
            // De ingevoerde velden binnen het scherm ophalen.
            String sVivesMail = txtVivesMail.getText();
            String sWachtwoordStudent = pfWachtwoordStudent.getText();
            String sWachtwoordHerhalenStudent = pfStudentWachtwoordHerhalen.getText();

            // if else aanmaken voor e-mail - isEmpty controle
        if (sVivesMail.isEmpty()){
            JOptionPane.showMessageDialog(null,"Gelieve een e-mail adres in te vullen");
        } else {

        }
            // if-else aanmaken voor wachtwoord
        if (sWachtwoordStudent.isEmpty() && sWachtwoordHerhalenStudent.isEmpty()) {
            JOptionPane.showMessageDialog(null,"Gelieve iets in te vullen als wachtwoord", "Ongeldige ingave", JOptionPane.INFORMATION_MESSAGE);
        } else {
            validateWachtwoord(sWachtwoordStudent);
        }

        // if-else als wachtwoorden gelijk zijn
        if (sWachtwoordStudent != sWachtwoordHerhalenStudent)
        {
            lblGelijkeWW.setText("De wachtwoorden zijn gelijk");

        } else {
            JOptionPane.showMessageDialog(null, "Wachtwoorden zijn niet gelijk", "Ongeldige invage", JOptionPane.INFORMATION_MESSAGE);
        }

        // enable en disable button "registreren"
       btnRegistrerenStudent.setDisable(true);
        loadScreen(mouseEvent, "view/Inloggen.fxml");

        }

    public void EnableDisable_Button(String sVivesMail)
    {
        boolean isDisabled = (sVivesMail.isEmpty() || sVivesMail.trim().isEmpty()
                /* || (sWachtwoordStudent.isEmpty() || sWachtwoordStudent.trim().isEmpty() || (sWachtwoordHerhalenStudent.isEmpty() || sWachtwoordHerhalenStudent.trim().isEmpty() */);
        btnRegistrerenStudent.setDisable(isDisabled);
}


    public void clicked_AnnulerenRegistreren(MouseEvent mouseEvent) {
        // wanneer de gebruiker de registratie annuleert wilt dit zeggen dat hij / zij al een werkend account in bezig heeft.
        // hiermee worden ze dan terug gestuurd naar het inlogscherm
        loadScreen(mouseEvent,"view/Inloggen.fxml");
    }

    public void click_ValideerMail(MouseEvent mouseEvent) throws SQLException {
        String emailAdres = txtVivesMail.getText();

        if (emailAdres.isEmpty()) {
            JOptionPane.showMessageDialog(null,"Gelieve een geldig e-mail adres in te vullen");

        } else {
            // validatie voor e-mailadres
            validateEmail(emailAdres);
            // na validatie komt de knop registreren weer tevoorschijn.
            btnRegistrerenStudent.setDisable(false);

            /*
            // controleren als de e-mail bestaat in het systeem.
            Gebruiker email = gebruikerDAO.getByEmail(txtVivesMail.getText());
            // e-mail bestaat niet in de database
            if (email == null) {
                int dialogButton = JOptionPane.showConfirmDialog(null, "Het opgegeven e-mail adres wordt niet herkend binnen het systeem. Gelieve eerst uw aanvraag te doen", "Emailadres niet gekend", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                // als de gebruiker op ja kiest wordt hij / zij weer door verwezen naar het aanvraag scherm.
                if (dialogButton == JOptionPane.YES_OPTION) {
                    loadScreen(mouseEvent, "view/AanvraagToegang.fxml");
                }

            } else {
                if (false) {
                    // controleren als het mail adres al gekend is binnen het systeem
                } else {
                    // indien email toch klopt kan de gebruiker zijn wachtwoord kiezen en registreren
                }
            }

             */

        }

    }



}
