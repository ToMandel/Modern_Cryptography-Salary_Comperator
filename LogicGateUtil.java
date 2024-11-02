import java.math.BigInteger;

public class LogicGateUtil {

    public static int andGate(int bit1, int bit2) {
        return bit1 & bit2;
    }

    public static int orGate(int bit1, int bit2) {
        return bit1 | bit2;
    }

    public static int norGate(int bit1, int bit2) {
        return notGate(orGate(bit1, bit2));
    }

    public static int xorGate(int bit1, int bit2) {
        return bit1 ^ bit2;
    }

    public static int notGate(int bit) {
        return bit == 0 ? 1 : 0;
    }

    public static int muxGate2x1(int bit1, int bit2, int controlBit) {
        return orGate(andGate(bit1, notGate(controlBit)), andGate(bit2, controlBit));
    }

    public static int muxGate4x1(int bit1, int bit2, int bit3, int bit4, int controlBit1, int controlBit2) {
        int mux1 = muxGate2x1(bit1, bit2, controlBit2);
        int mux2 = muxGate2x1(bit3, bit4, controlBit2);
        return muxGate2x1(mux1, mux2, controlBit1);
    }

    public static int muxGate8x1(int bit1, int bit2, int bit3, int bit4, int bit5, int bit6, int bit7, int bit8,
            int controlBit1, int controlBit2, int controlBit3) {
        int mux1 = muxGate4x1(bit1, bit2, bit3, bit4, controlBit2, controlBit3);
        int mux2 = muxGate4x1(bit5, bit6, bit7, bit8, controlBit2, controlBit3);
        return muxGate2x1(mux1, mux2, controlBit1);
    }

    // Secure AND gate for two users
    public static int[] secureAndGateTwoUsers(BigInteger N, BigInteger E1, BigInteger D1, BigInteger E2, BigInteger D2,
            int bit1, int bit2) {
        int[] m = { 0, 0, 0, 1 }; // Possible results of AND gate

        // Perform OT for user 1
        int choice1 = orGate((bit1 << 1), bit2); // Calculate the choice index
        int user1Result = EncryptionUtil.obliviousTransfer(N, E2, D2, m, choice1);

        // Perform OT for user 2
        int choice2 = orGate((bit2 << 1), bit1); // Calculate the choice index
        int user2Result = EncryptionUtil.obliviousTransfer(N, E1, D1, m, choice2);

        return new int[] { user1Result, user2Result };
    }

    // Secure AND gate for three users
    public static int[] secureAndGateThreeUsers(BigInteger N, BigInteger E1, BigInteger D1, BigInteger E2,
            BigInteger D2, BigInteger E3, BigInteger D3, int bit1, int bit2, int bit3) {
        // Calculate AND for the first two users
        int[] result12 = secureAndGateTwoUsers(N, E1, D1, E2, D2, bit1, bit2);

        // Combine the result with the third user
        int[] result123 = secureAndGateTwoUsers(N, E3, D3, E1, D1, result12[0], bit3);

        return result123;
    }
}
