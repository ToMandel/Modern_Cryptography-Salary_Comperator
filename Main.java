import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    private static final BigInteger N1 = new BigInteger("1147"); // p = 37, q = 31
    private static final BigInteger E1 = new BigInteger("7");
    private static final BigInteger D1 = new BigInteger("463");

    private static final BigInteger N2 = new BigInteger("1073"); // p = 37, q = 29
    private static final BigInteger E2 = new BigInteger("5");
    private static final BigInteger D2 = new BigInteger("605");

    private static final BigInteger N3 = new BigInteger("1189"); // p = 41, q = 29
    private static final BigInteger E3 = new BigInteger("11");
    private static final BigInteger D3 = new BigInteger("611");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        User user1 = new User("User1", N1, E1, D1);
        User user2 = new User("User2", N2, E2, D2);
        User user3 = new User("User3", N3, E3, D3);

        do {
            createInput(scanner, user1);
            createInput(scanner, user2);
            createInput(scanner, user3);

            System.out.println("\nEncryption process of the share1 of User1 sent to User1: ");
            String encryptedUser1Share1 = EncryptionUtil.encrypt(user1.getShares1Int(), user1.getN(), user1.getE());
            System.out.println("\nEncryption process of the share2 of User1 sent to User2: ");
            String encryptedUser1Share2 = EncryptionUtil.encrypt(user1.getShares2Int(), user2.getN(), user2.getE());
            System.out.println("\nEncryption process of the share3 of User1 sent to User3: ");
            String encryptedUser1Share3 = EncryptionUtil.encrypt(user1.getShares3Int(), user3.getN(), user3.getE());

            System.out.println("\nEncryption process of the share1 of User2 sent to User1: ");
            String encryptedUser2Share1 = EncryptionUtil.encrypt(user2.getShares1Int(), user1.getN(), user1.getE());
            System.out.println("\nEncryption process of the share2 of User2 sent to User2: ");
            String encryptedUser2Share2 = EncryptionUtil.encrypt(user2.getShares2Int(), user2.getN(), user2.getE());
            System.out.println("\nEncryption process of the share3 of User2 sent to User3: ");
            String encryptedUser2Share3 = EncryptionUtil.encrypt(user2.getShares3Int(), user3.getN(), user3.getE());

            System.out.println("\nEncryption process of the share1 of User3 sent to User1: ");
            String encryptedUser3Share1 = EncryptionUtil.encrypt(user3.getShares1Int(), user1.getN(), user1.getE());
            System.out.println("\nEncryption process of the share2 of User3 sent to User2: ");
            String encryptedUser3Share2 = EncryptionUtil.encrypt(user3.getShares2Int(), user2.getN(), user2.getE());
            System.out.println("\nEncryption process of the share3 of User3 sent to User3: ");
            String encryptedUser3Share3 = EncryptionUtil.encrypt(user3.getShares3Int(), user3.getN(), user3.getE());

            System.out.println("");

            // set encrypted salaries to the users
            user1.setEncryptedShares1(encryptedUser1Share1);
            user1.setEncryptedShares2(encryptedUser2Share1);
            user1.setEncryptedShares3(encryptedUser3Share1);

            user2.setEncryptedShares1(encryptedUser1Share2);
            user2.setEncryptedShares2(encryptedUser2Share2);
            user2.setEncryptedShares3(encryptedUser3Share2);

            user3.setEncryptedShares1(encryptedUser1Share3);
            user3.setEncryptedShares2(encryptedUser2Share3);
            user3.setEncryptedShares3(encryptedUser3Share3);
            System.out.println("Sending encrypted shares from users to other users:\n");

            // perform calculation of which user has higher salary
            String encryptedResult1[] = Calculator.performCalculation(user1);
            String encryptedResult2[] = Calculator.performCalculation(user2);
            String encryptedResult3[] = Calculator.performCalculation(user3);

            System.out.println("Decryption process of the results:");
            String decryptedResult1[] = new String[3];
            String decryptedResult2[] = new String[3];
            String decryptedResult3[] = new String[3];

            for (int i = 0; i < encryptedResult1.length; i++) {
                System.out.println("Decryption process of the result User1 sent to User" + (i + 1) + ": ");
                decryptedResult1[i] = EncryptionUtil.decrypt(encryptedResult1[i], user1.getN(), user1.getD());
                System.out.println("Decrypted results of User1: " + decryptedResult1[i]);

            }

            for (int i = 0; i < encryptedResult2.length; i++) {
                System.out.println("Decryption process of the result User3 sent to User" + (i + 1) + ": ");
                decryptedResult2[i] = EncryptionUtil.decrypt(encryptedResult2[i], user2.getN(), user2.getD());
                System.out.println("Decrypted results of User2: " + decryptedResult2[i]);
            }
            for (int i = 0; i < encryptedResult3.length; i++) {
                System.out.println("Decryption process of the result User3 sent to User" + (i + 1) + ": ");
                decryptedResult3[i] = EncryptionUtil.decrypt(encryptedResult3[i], user3.getN(), user3.getD());
                System.out.println("Decrypted results of User3: " + decryptedResult3[i]);
            }

            // Ask if the operator wants to enter new inputs or exit
            System.out.println("Do you want to enter new inputs? (y to continue, any other key to exit)");
            String choice = scanner.next();
            if (!choice.equalsIgnoreCase("y")) {
                exit = true;
            }
        } while (!exit);

        scanner.close();
    }

    public static void createInput(Scanner scanner, User user) {
        int salary = -1;
        while (salary < 0 || salary > 7) {
            System.out.println("Enter your salary between 0 to 7:");
            if (scanner.hasNextInt()) {
                salary = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                if (salary < 0 || salary > 7) {
                    System.out.println("Input must be between 0 and 7");
                }
            } else {
                System.out.println("Invalid input. Please enter an integer between 0 and 7.");
                scanner.next(); // Consume the invalid input
            }
        }
        System.out.println(user.getName() + " salary is: " + Integer.toBinaryString(salary));
        user.setSalary(salary);
        splitSalary(user);
        setSharedSecret(user);
        sharesToInt(user);
    }

    public static void splitSalary(User user) {
        user.setBit1((user.getSalary() >> 2) & 1);
        user.setBit2((user.getSalary() >> 1) & 1);
        user.setBit3(user.getSalary() & 1);
    }

    public static void setSharedSecret(User user) {
        int[] shares1 = EncryptionUtil.sharedSecret(user.getBit1());
        int[] shares2 = EncryptionUtil.sharedSecret(user.getBit2());
        int[] shares3 = EncryptionUtil.sharedSecret(user.getBit3());

        user.setShares1(shares1);
        user.setShares2(shares2);
        user.setShares3(shares3);
    }

    public static void sharesToInt(User user) {
        int res1 = 0;
        int res2 = 0;
        int res3 = 0;
        for (int i = 0; i < user.getShares1().length; i++) {
            res1 = res1 * 10 + user.getShares1()[i];
        }

        for (int i = 0; i < user.getShares2().length; i++) {
            res2 = res2 * 10 + user.getShares2()[i];
        }

        for (int i = 0; i < user.getShares3().length; i++) {
            res3 = res3 * 10 + user.getShares3()[i];
        }
        user.setShares1Int(res1);
        user.setShares2Int(res2);
        user.setShares3Int(res3);
    }
}
