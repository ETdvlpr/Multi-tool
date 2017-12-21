package com;
/**
 *
 * @author Dave Sami
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class Command {

    private static Process p;

    public static void Run(String command, String dir) {
        try {
            String cmd = dir != null ? "cd \"" + dir + "\" && " + command : command;
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", cmd);

            System.out.println("running command: " + cmd);

            builder.redirectErrorStream(true);
            p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
            System.out.println("command completed");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
