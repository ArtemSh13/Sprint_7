import java.util.Random;

public class DataGenerator {
    private static Random random = new Random();

    private static int randInt(int min, int max) {
        int randomNum = random.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static String getCourierCourierLogin () {
        return "courier" + random.nextInt(999999);
    }

    public static String getRandomPassword () {
        return "p@s$w0RD" + random.nextInt(999999);
    }

    public static String getLoginPasswordJson (String login, String password) {
        return "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
    }

    public static String getRandomFirstName () {
        return "firstName" + random.nextInt(999999);
    }
    public static String getRandomLastName () {
        return "lastName" + random.nextInt(999999);
    }
    public static String getRandomAddress () {
        return "address" + random.nextInt(999999);
    }
    public static String getRandomMetroStation () {
        return "metroStation" + random.nextInt(999999);
    }
    public static String getRandomPhone () {
        return "phone" + random.nextInt(999999);
    }
    public static int getRandomRentTime () {
        return random.nextInt(999);
    }
    public static String getRandomDeliveryDate () {
        return "" + randInt(1900, 2022) + "-" + randInt(1, 12) + "-" + randInt(1, 28);
    }
    public static String getRandomComment () {
        return "comment" + random.nextInt(999999);
    }

}
