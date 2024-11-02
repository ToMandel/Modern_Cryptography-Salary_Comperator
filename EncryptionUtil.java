import java.math.BigInteger;
import java.security.SecureRandom;

import static java.math.BigInteger.valueOf;

public class EncryptionUtil {
    public static final int nLen = 3;

    public static String encrypt(int message, BigInteger N, BigInteger E) {
        BigInteger m = valueOf(message);

        String binaryMessage = m.toString(2);

        // adding padding so the length of the message will be 3 - the maximum
        if (binaryMessage.length() < nLen) {
            int zerosToPad = nLen - binaryMessage.length();
            StringBuilder paddedBinMessage = new StringBuilder();
            for (int i = 0; i < zerosToPad; i++) {
                paddedBinMessage.append('0');
            }
            paddedBinMessage.append(binaryMessage);
            binaryMessage = paddedBinMessage.toString();
        }

        SecureRandom random = new SecureRandom();
        BigInteger r = // valueOf(37);
                new BigInteger(6, random); // Random value

        // first 2n bits
        BigInteger c = r.modPow(E, N);

        // xor Result:
        BigInteger lsbR = r.mod(BigInteger.valueOf(2).pow(binaryMessage.length()));

        BigInteger xorResult = lsbR.xor(m);
        String xorStr = xorResult.toString(2);

        if (xorStr.length() < binaryMessage.length()) {
            int zerosToPad = binaryMessage.length() - xorStr.length();
            StringBuilder paddedXorStr = new StringBuilder();
            for (int i = 0; i < zerosToPad; i++) {
                paddedXorStr.append('0');
            }
            paddedXorStr.append(xorStr);
            xorStr = paddedXorStr.toString();
        }

        System.out.println("\nEncryption process: E = " + E + ", N = " + N + ", r = " + r + ", r^e mod N = " + c);
        System.out.println("\t\t\t\t\tm = " + m + ", lsb n(r) = " + lsbR + ", m ⊕ lsb n(r) = " + xorStr);
        String first2n = c.toString(2);
        System.out.println("Encrypted message: " + first2n + xorStr);

        return c.toString(2) + xorStr;
    }

    public static String decrypt(String encryptedMessage, BigInteger N, BigInteger D) {
        // part encrypted message:

        // first part:
        BigInteger c = new BigInteger(encryptedMessage, 2);
        c = c.divide(BigInteger.valueOf(2).pow(nLen));

        BigInteger r = c.modPow(D, N);
        // second part
        BigInteger xorResult = new BigInteger(encryptedMessage, 2);
        xorResult = xorResult.mod(BigInteger.valueOf(2).pow(nLen));
        BigInteger rXor = r.mod(BigInteger.valueOf(2).pow(nLen));
        BigInteger decryptedMessage = rXor.xor(xorResult);

        System.out.println("Decryption process: N = " + N + ", D = " + D + ", n = " + nLen);
        System.out.println("Removing n bits from the end of the message - " + xorResult + ", c = " + c);
        System.out.println("\t\t\t\t\tr = c^D mod N  = " + r);
        System.out.println("\t\t\t\t\tThe xor result ,nBit = " + xorResult + ", lsb n(r) = "
                + rXor + ", nBit ⊕ lsb n(r) = " + decryptedMessage);

        return decryptedMessage.toString(2);
    }

    // Function to implement secret sharing where n = k = 3
    public static int[] sharedSecret(int originalBit) {
        SecureRandom random = new SecureRandom();
        int[] shares = new int[3];

        // Generate two random bits
        shares[0] = random.nextInt(2); // Random bit 0 or 1
        shares[1] = random.nextInt(2); // Random bit 0 or 1

        // Calculate the third share such that XOR of all three shares equals the
        // original bit
        shares[2] = LogicGateUtil.xorGate(originalBit, LogicGateUtil.xorGate(shares[0], shares[1]));

        return shares;
    }

    // Function to recover the original bit from the three shares
    public static String recoverOriginalBit(int[] shares) {
        if (shares.length != 3) {
            throw new IllegalArgumentException("There must be exactly 3 shares");
        }

        // XOR all three shares to recover the original bit
        return String.valueOf(LogicGateUtil.xorGate(shares[0], LogicGateUtil.xorGate(shares[1], shares[2])));
    }

    // Oblivious Transfer (OT)
    public static int obliviousTransfer(BigInteger N, BigInteger E, BigInteger D, int[] m, int choice) {
        SecureRandom random = new SecureRandom();

        // Random r
        BigInteger r = new BigInteger(32, random).mod(N);

        // Compute x = r^E mod N (RSA encryption)
        BigInteger x = r.modPow(E, N);

        // Compute the four possible options
        int[] c = new int[4];
        for (int i = 0; i < 4; i++) {
            int xInt = x.intValue();
            c[i] = LogicGateUtil.xorGate(m[i], LogicGateUtil.xorGate(xInt, i & 1));
        }

        // Compute the response for the selected choice
        int rInt = r.modPow(D, N).intValue();
        int response = LogicGateUtil.xorGate(c[choice], rInt & 1);

        return response;
    }

}
