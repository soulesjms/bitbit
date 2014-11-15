/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbit;

/**
 *
 * @author adam
 */
public class ColorConsole {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {
        System.out.print(ANSI_BLACK + "Black \n" + ANSI_RED + "Red \n"
                       + ANSI_GREEN + "Green \n" + ANSI_YELLOW + "Yellow \n" +
                         ANSI_BLUE + "Blue \n" + ANSI_PURPLE + "Purple \n" +
                         ANSI_CYAN + "Cyan \n" + ANSI_WHITE + "White \n" +
                         ANSI_RESET + "Reset \n");
    }
}
