import java.util.Scanner;

/**
 * A program that encrypts and decrypts messages of a file, and prints the contents of a file.
 * @Author Sarah Shahid
 * @Author Khadija Khan
 */
public class Main {

    public static final Scanner reader = new Scanner(System.in);

    public static void main(String[] args) {

        while(true) {
            Methods.printMenu();
            System.out.println();
            System.out.print("> ");

            String input = reader.nextLine();

            switch (input) {
                case "1":
                    Methods.optionOne();
                    break;
                case "2":
                    Methods.optionTwo();
                    break;
                case "3":
                    Methods.optionThree();
                    break;
                case "4":
                    System.out.println("Thanks for using _______________");
                    break;
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }

            //Exit condition
            if (input.equals("4")) {
                break;
            }
        }
    }
}