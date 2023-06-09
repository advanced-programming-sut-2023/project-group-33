package view.GUIController;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.App;
import model.Database;
import model.User;
import utility.CheckFunctions;
import utility.RandomCaptcha;
import utility.RandomGenerators;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class SignUpMenuGUI extends Application {

    @FXML
    private AnchorPane pane;
    @FXML
    private Label messageLabel;
    //level 0
    @FXML
    private TextField captchaField;
    @FXML
    private Hyperlink resetCaptchaHyperLink;
    @FXML
    private ImageView captchaImageViewer;
    //level 1
    @FXML
    private TextField username;
    @FXML
    private Label userLabel;
    //level 2
    @FXML
    private PasswordField passwordPassField;
    @FXML
    private PasswordField passwordConfirmPassField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField passwordConfirmTextField;
    @FXML
    private CheckBox randomPassCheck;
    @FXML
    private CheckBox showPassCheck;
    //level 3
    @FXML
    private TextField nickname;
    @FXML
    private Label nicknameLabel;
    //level 4
    @FXML
    private TextField email;
    @FXML
    private Label emailLabel;
    //level 5
    @FXML
    private TextArea slogan;
    @FXML
    private CheckBox randomSloganCheck;
    @FXML
    private TextArea famousChart;
    //level 6
    @FXML
    private ChoiceBox answerChoice;
    @FXML
    private TextField answerField;
    //level 7
    @FXML
    private Button signButton;
    @FXML
    private Button nextButton;

    private static String captcha;
    private static int signUpLevel = 0;

    ObservableList<String> answerChoiceList = FXCollections.observableArrayList("What was your first job?",
            "What was your first pet’s name?",
            "What is your social security number?");


    @FXML
    public void initialize() {
        //hide every object
        visibilityUpdate();
        //load background
        Image image = new Image(
                Objects.requireNonNull(MarketViewController.class.getResource(
                        "/images/backgrounds/login-menu-background.jpg")).toExternalForm()
        );
        BackgroundImage backgroundFill = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,BackgroundSize.DEFAULT);
        Background background = new Background(backgroundFill);
        pane.setBackground(background);

        //load captcha
        captcha = RandomCaptcha.generateString();
        Image captchaImage = SwingFXUtils.toFXImage(RandomCaptcha.generateImage(captcha), null);
        captchaImageViewer.setImage(captchaImage);

        //add listeners
        listenerUpdate();

        //add choices
        answerChoice.setItems(answerChoiceList);
        answerChoice.setValue(answerChoiceList.get(0));


        famousChart.setText("Most Used Slogans:\n" + Database.top5FamousSlogan());
        famousChart.setEditable(false);
    }

    private void listenerUpdate(){
        switch (signUpLevel){
            case 0:
                captchaField.textProperty().addListener((observable, oldText, newText) -> {
                    if (!newText.equals(captcha)){
                        messageLabel.setText("The captcha is invalid!");
                        nextButton.setVisible(false);
                    }
                    else{
                        messageLabel.setText(null);
                        nextButton.setVisible(true);
                    }
                });
                break;
            case 1:
                username.textProperty().addListener((observable, oldText, newText) -> {
                    if (LoginMenuGUI.getUserFromServer(newText) != null){
                        messageLabel.setText("This Username already exist!");
                        nextButton.setVisible(false);
                    }
                    else if (newText.equals("")) {
                        messageLabel.setText("Username field is empty!");
                        nextButton.setVisible(false);
                    }
                    else if (CheckFunctions.checkUsernameFormat(newText)){
                        messageLabel.setText("This Username format is invalid!");
                        nextButton.setVisible(false);
                    }
                    else{
                        messageLabel.setText(null);
                        nextButton.setVisible(true);
                    }


                });
                break;
            case 2:
                passwordPassField.textProperty().addListener((observable, oldText, newText) -> {
                    passwordTextField.setText(newText);
                    if(newText.length() < 6 && !randomPassCheck.isSelected()){
                        messageLabel.setText("Your Password must be more than 5 characters!");
                        nextButton.setVisible(false);
                    }
                    else if (newText.equals("")) {
                        messageLabel.setText("Password field is empty!");
                        nextButton.setVisible(false);
                    }
                    else if(CheckFunctions.checkPasswordFormat(newText) && !randomPassCheck.isSelected()){
                        messageLabel.setText("This Password format is invalid!");
                        nextButton.setVisible(false);
                    }
                    else if (passwordConfirmPassField.getText().isEmpty()) {
                        messageLabel.setText("Confirm Password field is empty!");
                        nextButton.setVisible(false);
                    }
                    else{
                        messageLabel.setText(null);
                        nextButton.setVisible(true);
                    }

                });
                passwordConfirmPassField.textProperty().addListener((observable, oldText, newText) -> {
                    passwordConfirmTextField.setText(newText);
                    if(passwordPassField.getText().isEmpty() && !randomPassCheck.isSelected()) {
                        messageLabel.setText("The Password field is empty!");
                        nextButton.setVisible(false);
                    }
                    else{
                        if (!newText.equals(passwordPassField.getText()) && !randomPassCheck.isSelected()){
                            messageLabel.setText("The given passwords don't match!");
                            nextButton.setVisible(false);
                        }
                        else{
                            messageLabel.setText(null);
                            nextButton.setVisible(true);
                        }

                    }
                });
                passwordTextField.textProperty().addListener((observable, oldText, newText) -> {
                    passwordPassField.setText(newText);
                    if(newText.length() < 6 && !randomPassCheck.isSelected()){
                        messageLabel.setText("Your Password must be more than 5 characters!");
                        nextButton.setVisible(false);
                    }
                    else if (newText.equals("")) {
                        messageLabel.setText("Password field is empty!");
                        nextButton.setVisible(false);
                    }
                    else if(CheckFunctions.checkPasswordFormat(newText) && !randomPassCheck.isSelected()){
                        messageLabel.setText("This Password format is invalid!");
                        nextButton.setVisible(false);
                    }
                    else if (passwordConfirmTextField.getText().isEmpty()) {
                        messageLabel.setText("Confirm Password field is empty!");
                        nextButton.setVisible(false);
                    }
                    else if (!newText.equals(passwordConfirmTextField.getText()) && !randomPassCheck.isSelected()){
                        messageLabel.setText("The given passwords don't match!");
                        nextButton.setVisible(false);
                    }
                    else{
                        messageLabel.setText(null);
                        nextButton.setVisible(true);
                    }

                });
                passwordConfirmTextField.textProperty().addListener((observable, oldText, newText) -> {
                    passwordConfirmPassField.setText(newText);
                    if (!newText.equals(passwordPassField.getText()) && !randomPassCheck.isSelected()){
                        messageLabel.setText("The given passwords don't match!");
                        nextButton.setVisible(false);
                    }
                    else if(newText.length() < 6 && !randomPassCheck.isSelected()){
                        messageLabel.setText("Your Password must be more than 5 characters!");
                        nextButton.setVisible(false);
                    }
                    else if (newText.equals("")) {
                        messageLabel.setText("Confirm Password field is empty!");
                        nextButton.setVisible(false);
                    }
                    else if(CheckFunctions.checkPasswordFormat(newText) && !randomPassCheck.isSelected()){
                        messageLabel.setText("This Password format is invalid!");
                        nextButton.setVisible(false);
                    }
                    else{
                        messageLabel.setText(null);
                        nextButton.setVisible(true);
                    }


                });
                showPassCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    passwordTextField.setVisible(!(!newValue || randomPassCheck.isSelected()));
                    passwordPassField.setVisible(!(newValue || randomPassCheck.isSelected()));
                    passwordConfirmTextField.setVisible(!(!newValue || randomPassCheck.isSelected()));
                    passwordConfirmPassField.setVisible(!(newValue || randomPassCheck.isSelected()));
                    if (newValue) {
                        passwordTextField.setText(passwordPassField.getText());

                        passwordConfirmTextField.setText(passwordConfirmPassField.getText());
                    }
                    else {
                        passwordPassField.setText(passwordTextField.getText());

                        passwordConfirmPassField.setText(passwordConfirmTextField.getText());
                    }
                });
                break;
            case 3:
                nickname.textProperty().addListener((observable, oldText, newText) -> {
                    if (nickname.getText().isEmpty()){
                        messageLabel.setText("The Nickname is empty!");
                        nextButton.setVisible(false);
                    }
                    else if (newText.equals("")) {
                        messageLabel.setText("Nickname field is empty!");
                        nextButton.setVisible(false);
                    }
                    else{
                        messageLabel.setText(null);
                        nextButton.setVisible(true);
                    }
                });
                break;
            case 4:
                email.textProperty().addListener((observable, oldText, newText) -> {
                    if (CheckFunctions.checkEmailFormat(newText)){
                        messageLabel.setText("The Email format is invalid!");
                        nextButton.setVisible(false);
                    }
                    else if (getUserByEmailFromServer(newText) != null){
                        messageLabel.setText("This Email already exist!");
                        nextButton.setVisible(false);
                    }
                    else if (newText.equals("")) {
                        messageLabel.setText("Email field is empty!");
                        nextButton.setVisible(false);
                    }
                    else{
                        messageLabel.setText(null);
                        nextButton.setVisible(true);
                    }
                });
                break;
            case 5:
                slogan.textProperty().addListener((observable, oldText, newText) -> {
                    messageLabel.setText(null);
                    nextButton.setVisible(true);
                });
                break;
            case 6:
                answerField.textProperty().addListener((observable, oldText, newText) -> {
                    if (answerField.getText().isEmpty()){
                        messageLabel.setText("The Answer is empty!");
                        signButton.setVisible(false);
                    }
                    else if (newText.equals("")) {
                        messageLabel.setText("Answer field is empty!");
                        signButton.setVisible(false);
                    }
                    else{
                        messageLabel.setText(null);
                        signButton.setVisible(true);
                    }
                });
                break;
        }
    }

    private void visibilityUpdate(){
        switch (signUpLevel){
            case 0:
                captchaField.setVisible(true);
                resetCaptchaHyperLink.setVisible(true);
                captchaImageViewer.setVisible(true);
                username.setVisible(false);
                userLabel.setVisible(false);
                passwordPassField.setVisible(false);
                passwordConfirmPassField.setVisible(false);
                passwordTextField.setVisible(false);
                passwordConfirmTextField.setVisible(false);
                randomPassCheck.setVisible(false);
                showPassCheck.setVisible(false);
                nickname.setVisible(false);
                nicknameLabel.setVisible(false);
                email.setVisible(false);
                emailLabel.setVisible(false);
                slogan.setVisible(false);
                randomSloganCheck.setVisible(false);
                famousChart.setVisible(false);
                signButton.setVisible(false);
                nextButton.setVisible(false);
                answerField.setVisible(false);
                answerChoice.setVisible(false);
                break;
            case 1:
                captchaField.setVisible(false);
                resetCaptchaHyperLink.setVisible(false);
                captchaImageViewer.setVisible(false);
                username.setVisible(true);
                userLabel.setVisible(true);
                passwordPassField.setVisible(false);
                passwordConfirmPassField.setVisible(false);
                passwordTextField.setVisible(false);
                passwordConfirmTextField.setVisible(false);
                randomPassCheck.setVisible(false);
                showPassCheck.setVisible(false);
                nickname.setVisible(false);
                nicknameLabel.setVisible(false);
                email.setVisible(false);
                emailLabel.setVisible(false);
                slogan.setVisible(false);
                randomSloganCheck.setVisible(false);
                famousChart.setVisible(false);
                signButton.setVisible(false);
                nextButton.setVisible(false);
                answerField.setVisible(false);
                answerChoice.setVisible(false);
                break;
            case 2:
                captchaField.setVisible(false);
                resetCaptchaHyperLink.setVisible(false);
                captchaImageViewer.setVisible(false);
                username.setVisible(false);
                userLabel.setVisible(false);
                passwordPassField.setVisible(true);
                passwordConfirmPassField.setVisible(true);
                passwordTextField.setVisible(false);
                passwordConfirmTextField.setVisible(false);
                randomPassCheck.setVisible(true);
                showPassCheck.setVisible(true);
                nickname.setVisible(false);
                nicknameLabel.setVisible(false);
                email.setVisible(false);
                emailLabel.setVisible(false);
                slogan.setVisible(false);
                randomSloganCheck.setVisible(false);
                famousChart.setVisible(false);
                signButton.setVisible(false);
                nextButton.setVisible(false);
                answerField.setVisible(false);
                answerChoice.setVisible(false);
                break;
            case 3:
                captchaField.setVisible(false);
                resetCaptchaHyperLink.setVisible(false);
                captchaImageViewer.setVisible(false);
                username.setVisible(false);
                userLabel.setVisible(false);
                passwordPassField.setVisible(false);
                passwordConfirmPassField.setVisible(false);
                passwordTextField.setVisible(false);
                passwordConfirmTextField.setVisible(false);
                randomPassCheck.setVisible(false);
                showPassCheck.setVisible(false);
                nickname.setVisible(true);
                nicknameLabel.setVisible(true);
                email.setVisible(false);
                emailLabel.setVisible(false);
                slogan.setVisible(false);
                randomSloganCheck.setVisible(false);
                famousChart.setVisible(false);
                signButton.setVisible(false);
                nextButton.setVisible(false);
                answerField.setVisible(false);
                answerChoice.setVisible(false);
                break;
            case 4:
                captchaField.setVisible(false);
                resetCaptchaHyperLink.setVisible(false);
                captchaImageViewer.setVisible(false);
                username.setVisible(false);
                userLabel.setVisible(false);
                passwordPassField.setVisible(false);
                passwordConfirmPassField.setVisible(false);
                passwordTextField.setVisible(false);
                passwordConfirmTextField.setVisible(false);
                randomPassCheck.setVisible(false);
                showPassCheck.setVisible(false);
                nickname.setVisible(false);
                nicknameLabel.setVisible(false);
                email.setVisible(true);
                emailLabel.setVisible(true);
                slogan.setVisible(false);
                randomSloganCheck.setVisible(false);
                famousChart.setVisible(false);
                signButton.setVisible(false);
                nextButton.setVisible(false);
                answerField.setVisible(false);
                answerChoice.setVisible(false);
                break;
            case 5:
                captchaField.setVisible(false);
                resetCaptchaHyperLink.setVisible(false);
                captchaImageViewer.setVisible(false);
                username.setVisible(false);
                userLabel.setVisible(false);
                passwordPassField.setVisible(false);
                passwordConfirmPassField.setVisible(false);
                passwordTextField.setVisible(false);
                passwordConfirmTextField.setVisible(false);
                randomPassCheck.setVisible(false);
                showPassCheck.setVisible(false);
                nickname.setVisible(false);
                nicknameLabel.setVisible(false);
                email.setVisible(false);
                emailLabel.setVisible(false);
                slogan.setVisible(true);
                randomSloganCheck.setVisible(true);
                famousChart.setVisible(true);
                signButton.setVisible(false);
                nextButton.setVisible(true);
                answerField.setVisible(false);
                answerChoice.setVisible(false);
                break;
            case 6:
                captchaField.setVisible(false);
                resetCaptchaHyperLink.setVisible(false);
                captchaImageViewer.setVisible(false);
                username.setVisible(false);
                userLabel.setVisible(false);
                passwordPassField.setVisible(false);
                passwordConfirmPassField.setVisible(false);
                passwordTextField.setVisible(false);
                passwordConfirmTextField.setVisible(false);
                randomPassCheck.setVisible(false);
                showPassCheck.setVisible(false);
                nickname.setVisible(false);
                nicknameLabel.setVisible(false);
                email.setVisible(false);
                emailLabel.setVisible(false);
                slogan.setVisible(false);
                randomSloganCheck.setVisible(false);
                famousChart.setVisible(false);
                signButton.setVisible(false);
                nextButton.setVisible(false);
                answerField.setVisible(true);
                answerChoice.setVisible(true);
                break;
        }
    }
    public void randomSloganSetter() {
        slogan.setVisible(!randomSloganCheck.isSelected());
        slogan.setText(null);
        nextButton.setVisible(true);
    }

    public void randomPassSetter() {
        passwordPassField.setVisible(!randomPassCheck.isSelected());
        passwordConfirmPassField.setVisible(!randomPassCheck.isSelected());
        passwordPassField.setText("");
        passwordConfirmPassField.setText("");

        passwordTextField.setVisible(!randomPassCheck.isSelected());
        passwordConfirmTextField.setVisible(!randomPassCheck.isSelected());
        passwordTextField.setText("");
        passwordConfirmTextField.setText("");

        messageLabel.setText(null);
        nextButton.setVisible(true);
    }

    public void back() {
        signUpLevel--;
        if(signUpLevel == -1){
            signUpLevel++;
            try {
                new LoginMenuGUI().start(App.stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        visibilityUpdate();
        listenerUpdate();
    }

    public void nextStep() {
        signUpLevel++;
        visibilityUpdate();
        listenerUpdate();
        nextButton.setVisible(false);
    }

    public void resetCaptcha() {
        captcha = RandomCaptcha.generateString();
        Image captchaImage = SwingFXUtils.toFXImage(RandomCaptcha.generateImage(captcha), null);
        captchaImageViewer.setImage(captchaImage);
        listenerUpdate();
    }

    //made fore phase3 the new User must be deleted
    public HashMap<String,String> extract(){
        HashMap<String,String> output = new HashMap<>();
        String Username = username.getText();
        String Password = randomPassCheck.isSelected() ? RandomGenerators.randomPassword() : passwordPassField.getText();
        String Nickname = nickname.getText();
        String Email = email.getText();
        String Slogan = randomSloganCheck.isSelected() ? RandomGenerators.randomSlogan() : slogan.getText();
        int questionNUmber;
        String Answer = answerField.getText();

        Object value = answerChoice.getValue();
        if (value.equals("What was your first job?")) {
            questionNUmber = 0;
        } else if (value.equals("What was your first pet’s name?")) {
            questionNUmber = 1;
        } else{
            questionNUmber = 2;
        }

        output.put("username", Username);
        output.put("password", Password);
        output.put("nickname", Nickname);
        output.put("email",Email);
        output.put("slogan",Slogan);
        output.put("questionNumber", String.valueOf(questionNUmber));
        output.put("answer",Answer);
        return output;
    }

    public void signUp() {
        HashMap<String,String> userData = extract();
        userData.put("command","signup");

        String Username = userData.get("username");
        String Password = userData.get("password");
        String Email = userData.get("email");
        String Slogan = userData.get("slogan");

        String dataStr = new Gson().toJson(userData);
        try {
            App.writeToServer(dataStr);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Successful!");
            alert.setHeaderText("SignUp Info");
            String context = "\nUsername: " + Username + "\nPassword: " +
                    Password + "\nEmail: " + Email + "\nSlogan: " + Slogan;
            alert.setContentText("Your account was made successfully" + context);
            alert.showAndWait();

            new LoginMenuGUI().start(App.stage);

        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static User getUserByEmailFromServer(String email){
        HashMap<String,String> data = new HashMap<>();
        data.put("command","getUserByEmail");
        data.put("email",email);
        String dataStr = new Gson().toJson(data);
        try {
            App.writeToServer(dataStr);
            dataStr = App.readFromServer();
            User user = new Gson().fromJson(dataStr,User.class);
            return user;
        } catch (Exception e){
            return null;
        }
    }




    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = LoginMenuGUI.class.getResource("/fxml/signup-menu.fxml");
        assert url != null;
        BorderPane borderPane = FXMLLoader.load(url);
        Scene scene = new Scene(borderPane,1000,600);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
