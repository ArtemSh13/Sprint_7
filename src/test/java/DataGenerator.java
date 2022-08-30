import java.util.Random;

public class DataGenerator {
    private static Random random = new Random();

    public static String getCourierCourierLogin () {
        return "courier" + random.nextInt(999999);
    }

    public static String getRandomPassword () {
        return "p@s$w0RD" + random.nextInt(999999);
    }

    public static String getLoginPasswordJson (String login, String password) {
        return "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
    }
}
