package DomainLayer;

public class IdGenerator {
    private static Integer counter = 1000;
    public static Integer generate() {
        return counter++;
    }
}