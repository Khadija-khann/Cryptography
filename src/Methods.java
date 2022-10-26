import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

/**
 * A class of methods that manage files, format messages, and encrypt or decrypt messages using a key file.
 * Files are located, read, written to, and printed from.
 * Messages are filtered for letters, capitalized, and organized in groups of five.
 */

public class Methods {

    public static final Scanner reader = new Scanner(System.in);
    public static final String RETURN = "Press return to continue...";


    public static void enterMsg(String fileType) {
        System.out.print("Enter the filename of the " + fileType + ": ");
    }


    //FILE METHODS


    /**
     * Gets the name of an existing src file from the user.
     * @return file path
     */
    public static String getFileName() {
        BufferedReader bReader;

        while (true) {
            try {
                String fileName = reader.nextLine();

                // Check if file exists
                bReader = new BufferedReader(new FileReader("src/" + fileName));

                return "src/" + fileName;

            } catch (FileNotFoundException iox) {
                System.out.println("Invalid file path. Try again.");
            }
        }
    }

    /**
     * Gets the name of a src file where the result will be written.
     * File does not have to exist already.
     * @return file path
     */
    public static String getNewFileName() {
        String fileName = reader.nextLine();
        return "src/" + fileName;
    }

    /**
     * Adds the contents of a file to a new ArrayList.
     * @param file Name of a text file
     * @return ArrayList that was created
     */
    public static ArrayList<Character> readFile(String file) {
        ArrayList<Character> contents = new ArrayList<>();
        int input;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            input = reader.read();

            while (input != -1) {
                contents.add((char)input);
                input = reader.read();
            }

        } catch (IOException iox) {
            System.out.println(iox.getMessage());
        }
        return contents;
    }

    /**
     * Converts arrayList to String in groups of five and writes to a file.
     * Writes a new line to the file for every "0" in the String.
     * @param txt ArrayList with the message being written
     * @param filename Name of file that is written to
     */
    public static void write(ArrayList<Character> txt, String filename) {
        final char NEW_LINE = '0';

        BufferedWriter writer;
        String grouped = groupOfFive(txt);

        try {
            writer = new BufferedWriter(new FileWriter(filename, false));

            for (int i = 0; i < grouped.length(); i++) {

                char letter = grouped.charAt(i);

                if (letter == NEW_LINE) {
                    writer.newLine();
                } else {
                    writer.write(letter);
                }
            }
            writer.close();

        } catch (IOException iox) {
            System.out.println(iox.getMessage());
        }
    }

    /**
     * Prints the contents of a text file.
     * @param file The name of the file being read
     */
    public static void printFile(String file) {

        System.out.println("-+-Begin view-+-");
        System.out.println();
        String line;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException iox) {
            System.out.println(iox.getMessage());
        }

        System.out.println();
        System.out.println("-+-End view-+-");
    }


    //FORMAT METHODS


    /**
     * Filters a message by only adding letters to a new Arraylist
     * @param unfiltered An Arraylist with letters, numbers, symbols or spaces
     * @return A filtered Arraylist with only letters
     */
    public static ArrayList<Character> clean(ArrayList<Character> unfiltered) {
        ArrayList<Character> filtered = new ArrayList<>();

        for (Character characters : unfiltered) {
            if (('a' <= characters && characters <= 'z') || ('A' <= characters && characters <= 'Z')) {
                filtered.add(characters);
            }
        }
        return filtered;
    }

    /**
     * Capitalizes all the letters in an ArrayList.
     * @param plaintxt ArrayList that is being capitalized
     */
    public static void capitalize(ArrayList<Character> plaintxt) {
        for (int i = 0; i < plaintxt.size(); i++) {
            char current = plaintxt.get(i);
            plaintxt.set(i, Character.toUpperCase(current));
        }
    }

    /**
     * Adds random letters to an ArrayList to ensure its size is a multiple of 5.
     * @param plaintxt ArrayList that letters are being added to
     */
    public static void addRandomLetters(ArrayList<Character> plaintxt) {
        int numAdded = 5 - (plaintxt.size() % 5);
        Random random = new Random();
        for (int i = 0; i < numAdded; i++) {
            char randomLetter = (char) (random.nextInt(26) + 'A');
            plaintxt.add(randomLetter);
        }
    }

    /**
     * Adding letters in groups of five to an empty String.
     * For every five groups, a "0" is added to the String so the write method can identify a new line
     * @param input An Arraylist of the message needed to be formatted neatly
     * @return grouped String, with groups of 5 letters
     */
    public static String groupOfFive(ArrayList<Character> input) {

        String grouped = "";
        int numRun = input.size() / 5;
        int counterNewLine = 0;
        int index = 0;

        for (int i = 0; i < numRun; i++) {

            for (int j = 0; j < 4; j++) {
                grouped += input.get(index);
                index++;
            }
            // Prints the last letter in a group with a space afterwards
            grouped += (input.get(index) + " ");
            counterNewLine++;
            index++;

            if (counterNewLine % 5 == 0) {
                grouped += "0";
            }
        }
        return grouped;
    }


    //ENCRYPTION AND DECRYPTION METHODS


    /**
     * Encrypts plaintext using a key.
     * Adds the corresponding number values (26 letters in the alphabet) of the plaintext and key,
     * and return the associated letter.
     * @param plaintextName The name of the file being encrypted
     * @param keyName The name of the key file used to encrypt the message
     * @return An ArrayList of the encrypted plaintext
     */
    public static ArrayList<Character> encrypt(String plaintextName, String keyName) {
        ArrayList<Character> encrypted = new ArrayList<>();

        ArrayList<Character> key = clean(readFile(keyName));
        ArrayList<Character> plaintxt = clean(readFile(plaintextName));

        capitalize(plaintxt);
        addRandomLetters(plaintxt);

        for (int i = 0; i < plaintxt.size(); i++) {

            // Subtract 64 from ASCII decimal value, to get associated number for each letter
            int keyValue = ( (int) key.get(i)) - 64;
            int plainTxtValue = ( (int) plaintxt.get(i)) - 64;

            int encryptedValue = (plainTxtValue + keyValue) + 64;

            // Returns to the beginning of the alphabet, if encrypted value is greater than Z value
            if (encryptedValue > 90) {
                encryptedValue -= 26;
            }
            encrypted.add((char) encryptedValue);
        }

        return encrypted;
    }

    /**
     * Decrypts ciphertext using a key.
     * Subtracts the corresponding number value (26 letters in the alphabet) of the key from the ciphertext,
     * and returns the resulting letter.
     * @param ciphertextName The name of the file being decrypted
     * @param keyName The name of the key file used to decrypt the message
     * @return An ArrayList of the encrypted letters of the plaintext
     */
    public static ArrayList<Character> decrypt(String ciphertextName, String keyName) {
        ArrayList<Character> decrypted = new ArrayList<>();

        ArrayList<Character> key = clean(readFile(keyName));
        ArrayList<Character> ciphertxt = clean(readFile(ciphertextName));

        for (int i = 0; i < ciphertxt.size(); i++) {

            // Subtract 64 from ASCII decimal value, to get associated number for each letter
            int keyValue = ((int) key.get(i)) - 64;
            int cipherValue = ((int) ciphertxt.get(i)) - 64;

            int decryptedValue = (cipherValue - keyValue) + 64;

            // Returns to the beginning of the alphabet, if decrypted value is less than 'A' value
            if (decryptedValue < 65) {
                decryptedValue += 26;
            }
            decrypted.add((char) decryptedValue);
        }

        return decrypted;
    }


    //MENU METHODS


    /**
     * Prints main menu.
     */
    public static void printMenu() {
        System.out.println("---------------------------------");
        System.out.println(" S-E-C-R-E-T-+-M-E-S-S-E-N-G-E-R");
        System.out.println("---------------------------------");
        System.out.println();
        System.out.println("Choose from the following options:");
        System.out.println("1. Encrypt a file");
        System.out.println("2. Decrypt a file");
        System.out.println("3. View the contents of a file");
        System.out.println("4. Exit");
    }

    /**
     * Runs the first menu option, encrypting a file.
     */
    public static void optionOne() {
        System.out.println("-+-+-+-+-+-+-+-");
        System.out.println("FILE ENCRYPTION");
        System.out.println("-+-+-+-+-+-+-+-");
        System.out.println();

        enterMsg("plaintext message");
        String plaintextName = getFileName();
        enterMsg("encryption key");
        String keyName = getFileName();
        enterMsg("ciphertext message");
        String ciphertextName = getNewFileName();

        write(encrypt(plaintextName, keyName), ciphertextName);

        System.out.println("Your file has been encrypted!");
        System.out.println();
        System.out.println(RETURN);
        String next = reader.nextLine();
    }

    /**
     * Runs the second menu option, decrypting a file.
     */
    public static void optionTwo() {
        System.out.println("-+-+-+-+-+-+-+-");
        System.out.println("FILE DECRYPTION");
        System.out.println("-+-+-+-+-+-+-+-");
        System.out.println();

        enterMsg("ciphertext message");
        String ciphertextName = getFileName();
        enterMsg("decryption key");
        String keyName = getFileName();
        enterMsg("plaintext message");
        String plaintextName = getNewFileName();

        write(decrypt(ciphertextName, keyName), plaintextName);

        System.out.println("Your file has been decrypted!");
        System.out.println();
        System.out.println(RETURN);
        String next = reader.nextLine();
    }

    /**
     * Runs the third menu option, viewing the contents of a file.
     */
    public static void optionThree() {
        System.out.println("-+-+-+-+-+-+-+-");
        System.out.println("FILE VIEWER");
        System.out.println("-+-+-+-+-+-+-+-");
        System.out.println();

        enterMsg("file to be viewed");
        String name = getFileName();

        printFile(name);
        System.out.println();
        System.out.println(RETURN);
        String next = reader.nextLine();
    }

}

