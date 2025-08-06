import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;

class IDGenerator {
    private static int currentId = 0;

    public static int generateID() {
        return ++currentId;
    }
}

public class practice {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();


        LocalDate nextweek = today.plusWeeks(1);
        LocalTime now2 = LocalTime.now();
        System.out.println(now2 + " " + nextweek);

        LocalDateTime now1 = LocalDateTime.now();
        System.out.println(now1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String formatted = today.format(formatter);
        System.out.println(formatted);

        System.out.println();
        String id = UUID.randomUUID().toString();
        System.out.println("Generated ID: " + id);

        int newId = IDGenerator.generateID();
        System.out.println("New ID: " + newId);

    }
}
