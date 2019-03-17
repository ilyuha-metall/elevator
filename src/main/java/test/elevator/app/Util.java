package test.elevator.app;

public class Util {

    private Util() {
    }

    public static void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }
    }
}
