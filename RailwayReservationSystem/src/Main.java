import java.util.*;

public class Main {
    public static void main(String[] args) {
        RailwaySystem app = new RailwaySystem();
        if (args.length > 0 && args[0].equalsIgnoreCase("--demo")) {
            app.demo();
            return;
        }
        app.run();
    }
}
