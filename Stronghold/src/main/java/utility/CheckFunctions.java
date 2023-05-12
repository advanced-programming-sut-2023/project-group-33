package utility;

import model.Application;
import view.enums.commands.SignUpCommands;
import view.enums.messages.SignUpMessages;

import java.util.HashMap;
import java.util.regex.Matcher;

public class CheckFunctions
{

    public static boolean checkUsernameFormat(String username) {
        return username.matches(".*[^A-Za-z0-9_].*");
    }
    public static boolean checkNicknameFormat(String nickname) {
        return nickname.matches(".*\\s.*");
    }

    public static boolean checkUsernameExits(String username) {
        return Application.getUserByUsername(username) != null;
    }

    public static boolean checkPasswordFormat(String password) {
        if (!password.matches(".*[0-9].*") ) {
            return true;
        }
        if (!password.matches(".*[a-z].*")) {
            return true;
        }
        if (!password.matches(".*[A-Z].*")) {
            return true;
        }
        return !password.matches(".*[^A-Za-z0-9].*");
    }

    public static String getUserFromMatcher(Matcher matcher){
        String username = "";
        String argVal = matcher.group("username");
        String argValSpace = matcher.group("usernameSpace");
        username = (argVal != null) ? argVal : username;
        username = (argValSpace != null) ? argValSpace : username;

        if (username.equals("")){
            return null;
        }

        return username;
    }
    public static String getPassFromMatcher(Matcher matcher){
        String password = "";
        String argVal = matcher.group("password");
        String argValSpace = matcher.group("passwordSpace");
        password = (argVal != null) ? argVal : password;
        password = (argValSpace != null) ? argValSpace : password;

        if (password.equals("")){
            return null;
        }

        return password;
    }
    public static String getNickFromMatcher(Matcher matcher){
        String nickname = "";
        String argVal = matcher.group("nickname");
        String argValSpace = matcher.group("nicknameSpace");
        nickname = (argVal != null) ? argVal : nickname;
        nickname = (argValSpace != null) ? argValSpace : nickname;

        if (nickname.equals("")){
            return null;
        }

        return nickname;
    }
    public static boolean checkEmailExits(String email)
    {
        return Application.getUserByEmail(email) != null;
    }

    public static boolean checkEmailFormat(String email) {
        return !email.matches("([^\\@\\s])+\\@([^\\.\\s])+\\.(\\S+)+");
    }

    public static SignUpMessages questionCommandCheck(HashMap<String, String> questionData) {
        String questionNumber = questionData.get("questionNumber");
        String answer = questionData.get("answer");
        String answerConfirm = questionData.get("answerConfirm");
        Matcher matcher ;

        if ((matcher = SignUpCommands.getMatcher(questionNumber,SignUpCommands.NUMBER_FORMAT)) == null){
            return SignUpMessages.QUESTION_INVALID_NUMBER;
        }
        else if (!answer.equals(answerConfirm)) {
            return SignUpMessages.QUESTION_INVALID_ANSWER;
        }
        else
            return SignUpMessages.SUCCESS;

    }


}
