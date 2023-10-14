package at.pfeifer.chatapp;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] argsArray) {
        List<String> args = new ArrayList<>(List.of(argsArray));

        boolean help = args.remove("--help");
        boolean noGui = args.remove("--nogui");
        if (!args.isEmpty()) {
            System.err.println("Invalid argument passed: " + args.remove(0));
            return;
        }
        if (help) {
            if (noGui) {
                System.err.println("Invalid combination of arguments: --help && --nogui");
                return;
            }
            System.err.println("How to use this program:");
            System.err.println("--nogui: doesnt start the gui and hosts an server");
            return;
        }

        if (noGui) {

        } else {
            App.main(argsArray);
        }
    }
}
