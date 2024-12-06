package uz.duol.akfadealerbot.utils;

public class Validator {

    public static boolean isValidFourDigitNumber(String input) {
        return input != null && input.matches("\\d{4}");
    }
}
