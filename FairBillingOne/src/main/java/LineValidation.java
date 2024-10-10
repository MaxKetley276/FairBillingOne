import java.time.LocalTime;

public class LineValidation {
  public static boolean isValidTime(String time) {
    try {
      LocalTime lt = LocalTime.parse(time);
      return true;
    } catch (Exception e) {
      System.out.println("Invalid time: " + time);
      return false;
    }
  }

  public static boolean isValid(String line) {
    if ((line == null) || (line.length() == 0)) {
      return false;
    }
    String[] parts = line.split(" ");
    if (parts.length != 3) {
      return false;
    }

    String time = parts[0];
    if (!isValidTime(time)) {
      return false;
    }

    String userName = parts[1];
    if (!userName.matches("[a-zA-Z0-9]+")) {
      return false;
    }
    String action = parts[2];
    if (!action.equals("Start") && !action.equals("End")) {
      return false;
    }

    return true;
  }
}