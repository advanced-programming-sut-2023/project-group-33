package view;

import controller.ProfileControl;
import model.Database;
import model.User;
import utility.CheckFunctions;
import view.enums.AllMenus;
import view.enums.commands.ProfileCommands;
import view.enums.messages.ProfileMessages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileMenu
{
    private static User currentUser = Database.getCurrentUser();
    public static void run() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String command;
        Matcher matcher;

        while (true){
            command = scanner.nextLine();

            if (ProfileCommands.getMatcher(command,ProfileCommands.BACK) != null) {
                Database.setCurrentMenu(AllMenus.MAIN_MENU);
            }
            else if ( ProfileCommands.getMatcher(command,ProfileCommands.SHOW_MENU) != null) {
                System.out.println("You're in Profile Menu!");
            }
            else if ((matcher = ProfileCommands.getMatcher(command,ProfileCommands.CHANGE_USERNAME)) != null) {
                checkChangeUsername(matcher);
            }
            else if ((matcher = ProfileCommands.getMatcher(command,ProfileCommands.CHANGE_NICKNAME)) != null) {
                checkChangeNickname(matcher);
            }
            else if ((matcher = ProfileCommands.getMatcher(command,ProfileCommands.CHANGE_PASSWORD)) != null) {
                checkChangePassword(command);
            }
            else if ((matcher = ProfileCommands.getMatcher(command,ProfileCommands.CHANGE_EMAIL)) != null) {
                checkChangeEmail(matcher);
            }
            else if ((matcher = ProfileCommands.getMatcher(command,ProfileCommands.CHANGE_SLOGAN)) != null) {
                checkChangeSlogan(matcher);
            }
            else if (ProfileCommands.getMatcher(command,ProfileCommands.REMOVE_SLOGAN) != null) {
                ProfileControl.removeSlogan();
                System.out.println("Slogan removed successfully!");
            }
            else if (ProfileCommands.getMatcher(command,ProfileCommands.DISPLAY_HIGH_SCORE) != null) {
                System.out.println("Your High-Score is :" + Database.getCurrentUser().getHighScore());
            }
            else if (ProfileCommands.getMatcher(command,ProfileCommands.DISPLAY_RANK) != null) {
                System.out.println("Your Rank is :" + Database.getCurrentUser().getRank());
            }
            else if (ProfileCommands.getMatcher(command,ProfileCommands.DISPLAY_SLOGAN) != null) {
                String slogan = Database.getCurrentUser().getSlogan();
                if (slogan != null){
                    System.out.println("Your Slogan is :" + slogan);
                }
                else
                    System.out.println("Slogan is empty!");
            }
            else if (ProfileCommands.getMatcher(command,ProfileCommands.DISPLAY_ALL) != null) {
                System.out.println("Your Username is :" + Database.getCurrentUser().getUsername());
                System.out.println("Your Nickname is :" + Database.getCurrentUser().getNickname());
                System.out.println("Your Email is :" + Database.getCurrentUser().getEmail());
                String slogan = Database.getCurrentUser().getSlogan();
                if (slogan != null){
                    System.out.println("Your Slogan is :" + slogan);
                }
                else
                    System.out.println("Slogan is empty!");
                System.out.println("Your High-Score is :" + Database.getCurrentUser().getHighScore());
                System.out.println("Your Rank is :" + Database.getCurrentUser().getRank());
            }
            else {
                System.out.println("My liege, that's an invalid command!");
            }

            switch (Database.getCurrentMenu()){
                case MAIN_MENU:
                    System.out.println("You're now in view.Main Menu!");
                    return;
                default:
                    break;
            }
        }

    }

    private static void checkChangeUsername(Matcher matcher){
        String username = CheckFunctions.getUserFromMatcher(matcher);
        if (username == null){
            System.out.println("My liege, you must give username!");
            return;
        }

        ProfileMessages messages = ProfileControl.changeUsername(username);

        switch (messages){
            case INVALID_USER_FORMAT:
                System.out.println("That's an invalid format for username!");
                break;
            case USER_EXIST:
                System.out.println("That username is already taken!");
                break;
            case SUCCESS:
                System.out.println("Username changed Successfully!");
                break;
            default:
                break;
        }
    }

    private static void checkChangeNickname(Matcher matcher){
        String nickname = CheckFunctions.getNickFromMatcher(matcher);
        if (nickname == null){
            System.out.println("My liege, you must give nickname!");
            return;
        }

        ProfileMessages messages = ProfileControl.changeNickname(nickname);

        switch (messages){
            case INVALID_NICK_FORMAT:
                System.out.println("That's an invalid format for nickname!");
                break;
            case SUCCESS:
                System.out.println("Nickname changed Successfully!");
                break;
            default:
                break;
        }
    }

    private static void checkChangePassword(String command){
        String oldPassword, newPassword;

        String regexOLD = ProfileCommands.getRegexOLDPASS();
        Matcher matcherOldPass = Pattern.compile(regexOLD).matcher(command);
        if(!matcherOldPass.find()){
            System.out.println("You must give the old password!");
            return;
        }

        oldPassword = CheckFunctions.getPassFromMatcher(matcherOldPass);
        command = command.replaceAll(matcherOldPass.group().toString().trim(),"");

        String regexNEW = ProfileCommands.getRegexNEWPASS();
        Matcher matcherNewPass = Pattern.compile(regexNEW).matcher(command);
        if(!matcherNewPass.find()){
            System.out.println("You must give the new password!");
            return;
        }

        newPassword = CheckFunctions.getPassFromMatcher(matcherNewPass);
        command = command.replaceAll(matcherNewPass.group().toString().trim(),"");

        if (ProfileCommands.getMatcher(command,ProfileCommands.CHANGE_PASSWORD_FINAL_CHECK) == null){
            System.out.println("My liege, there is an invalid argument in change pass command");
            return;
        }

        ProfileMessages messages = ProfileControl.changePassword(oldPassword,newPassword);

        switch (messages){
            case WRONG_PASSWORD:
                System.out.println("Wrong password!");
                break;
            case SAME_PASS:
                System.out.println("Your new password must be different than your previous password!");
                break;
            case INSUFFICIENT_PASS:
                System.out.println("Your password must be at least 6 characters");
                break;
            case INVALID_PASS_FORMAT:
                System.out.println("Your password format is invalid");
                break;
            case FAILED:
                System.out.println("Password change command was terminated!");
                break;
            case SUCCESS:
                System.out.println("Password changed Successfully!");
                break;
            default:
                break;
        }

    }

    private static void checkChangeEmail(Matcher matcher){
        String email = CheckFunctions.getEmailFromMatcher(matcher);
        if (email == null){
            System.out.println("My liege, you must give email!");
            return;
        }

        ProfileMessages messages = ProfileControl.changeEmail(email);

        switch (messages){
            case INVALID_EMAIL:
                System.out.println("That's an invalid format for email!");
                break;
            case EXISTING_EMAIL:
                System.out.println("That email is already taken!");
                break;
            case SUCCESS:
                System.out.println("Email changed Successfully!");
                break;
            default:
                break;
        }
    }

    private static void checkChangeSlogan(Matcher matcher){
        String slogan = CheckFunctions.getSloganFromMatcher(matcher);
        if (slogan == null){
            System.out.println("My liege, you must give slogan!");
            return;
        }

        ProfileMessages messages = ProfileControl.changeSlogan(slogan);

        switch (messages){
            case SUCCESS:
                System.out.println("Slogan changed Successfully!");
                break;
            default:
                break;
        }
    }

    private static void checkRemoveSlogan(){
        Database.getCurrentUser().setSlogan(null);
    }

    private static void displayAll(){
        displayHighScore();
        displayRank();
        displaySlogan();
    }

    private static void displayHighScore(){
        System.out.println("high score: " + currentUser);
    }
    private static void displayRank(){
        System.out.println("rank: " + currentUser);
    }
    private static void displaySlogan(){
        String slogan = currentUser.getSlogan();

        if (slogan == null)
            System.out.println("Slogan is empty!");
        else
            System.out.println(slogan);
    }

    public static boolean getPassConfirmation(String password){
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String command;
        while (true){
            System.out.println("Please enter your new password again or type \"back\" to cancel:");
            command = scanner.nextLine();
            if (command.equals(password)){
                return false;
            }
            else if(ProfileCommands.getMatcher(command, ProfileCommands.BACK) != null){
                System.out.println("Cancelled!");
                return true;
            }
            else
                System.out.println("Incorrect!");
        }
    }
}
