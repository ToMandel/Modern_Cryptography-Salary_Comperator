public class Calculator {
        public static String[] performCalculation(User user) {
                System.out.println("Performing calculations...");

                System.out.println("Decryption process of the Share1 sent to " + user.getName() + ": ");
                String decryptedShare1 = EncryptionUtil.decrypt(user.getEncryptedShares1(), user.getN(),
                                user.getD());
                System.out.println("Decryption process of the Share2 sent to " + user.getName() + ": ");
                String decryptedShare2 = EncryptionUtil.decrypt(user.getEncryptedShares2(), user.getN(),
                                user.getD());
                System.out.println("Decryption process of the Share3 sent to" + user.getName() + ": ");
                String decryptedShare3 = EncryptionUtil.decrypt(user.getEncryptedShares3(), user.getN(),
                                user.getD());

                String[] retrievedBits = new String[3];
                retrievedBits[0] = EncryptionUtil.recoverOriginalBit(splitShares(decryptedShare1));
                retrievedBits[1] = EncryptionUtil.recoverOriginalBit(splitShares(decryptedShare2));
                retrievedBits[2] = EncryptionUtil.recoverOriginalBit(splitShares(decryptedShare3));

                System.out.println("");

                user.setResult(0, 0);
                user.setResult(1, 0);
                user.setResult(2, 0);

                System.out.println("Calculating Shares outputs...");
                int[] result = compareThreeBinaries(retrievedBits[0], retrievedBits[1], retrievedBits[2]);
                String[] encryptedResult = new String[3];

                System.out.println("Results before encryption:");
                System.out.println("\tShare1 result: " + result[0]);
                System.out.println("\tShare2 result: " + result[1]);
                System.out.println("\tShare3 result: " + result[2]);

                System.out.println("Encryption process of the results:");
                encryptedResult[0] = EncryptionUtil.encrypt(result[0], user.getN(), user.getE());
                encryptedResult[1] = EncryptionUtil.encrypt(result[1], user.getN(), user.getE());
                encryptedResult[2] = EncryptionUtil.encrypt(result[2], user.getN(), user.getE());

                System.out.println("Encrypted results: \n\tShare1: " + encryptedResult[0] +
                                " \n\tShare2: " + encryptedResult[1] + " \n\tShare3: " + encryptedResult[2]);

                return encryptedResult;
        }

        public static int[] compareThreeBinaries(String binary1, String binary2, String binary3) {
                int maxLength = Math.max(binary1.length(), Math.max(binary2.length(), binary3.length()));
                binary1 = padBinaryString(binary1, maxLength);
                binary2 = padBinaryString(binary2, maxLength);
                binary3 = padBinaryString(binary3, maxLength);

                System.out.println("Comparing salaries:");
                System.out.println("\tShare1: " + binary1);
                System.out.println("\tShare2: " + binary2);
                System.out.println("\tShare3: " + binary3);

                int compare12 = compareTwoBinaries(binary1, binary2); // Compare Share1 and Share2
                int compare13 = compareTwoBinaries(binary1, binary3); // Compare Share1 and Share3
                int compare23 = compareTwoBinaries(binary2, binary3); // Compare Share2 and Share3

                // Determine if Share1 has highest salary
                int share1Greater = LogicGateUtil.andGate(
                                LogicGateUtil.muxGate2x1(0, 1, compare12 == 1 ? 1 : 0),
                                LogicGateUtil.muxGate2x1(0, 1, compare13 == 1 ? 1 : 0));

                // Determine if Share2 has highest salary
                int share2Greater = LogicGateUtil.andGate(
                                LogicGateUtil.muxGate2x1(0, 1, compare12 == -1 ? 1 : 0),
                                LogicGateUtil.muxGate2x1(0, 1, compare23 == 1 ? 1 : 0));

                // Determine if Share3 has highest salary
                int share3Greater = LogicGateUtil.andGate(
                                LogicGateUtil.muxGate2x1(0, 1, compare13 == -1 ? 1 : 0),
                                LogicGateUtil.muxGate2x1(0, 1, compare23 == -1 ? 1 : 0));

                // Check if all salaries are equal
                int allEqual = LogicGateUtil.andGate(
                                LogicGateUtil.muxGate2x1(0, 1, compare12 == 0 ? 1 : 0),
                                LogicGateUtil.muxGate2x1(0, 1, compare13 == 0 ? 1 : 0));

                // Check if Share1 and Share2 are equal and greater than Share3
                int share1And2EqualGreater = LogicGateUtil.andGate(
                                LogicGateUtil.muxGate2x1(0, 1, compare12 == 0 ? 1 : 0),
                                LogicGateUtil.muxGate2x1(0, 1, compare13 == 1 ? 1 : 0));

                // Check if Share1 and Share3 are equal and greater than Share2
                int share1And3EqualGreater = LogicGateUtil.andGate(
                                LogicGateUtil.muxGate2x1(0, 1, compare13 == 0 ? 1 : 0),
                                LogicGateUtil.muxGate2x1(0, 1, compare12 == 1 ? 1 : 0));

                // Check if Share2 and Share3 are equal and greater than Share1
                int share2And3EqualGreater = LogicGateUtil.andGate(
                                LogicGateUtil.muxGate2x1(0, 1, compare23 == 0 ? 1 : 0),
                                LogicGateUtil.muxGate2x1(0, 1, compare12 == -1 ? 1 : 0));

                int[] result = new int[3];

                // Set results
                result[0] = LogicGateUtil.orGate(
                                share1Greater,
                                LogicGateUtil.orGate(share1And2EqualGreater, share1And3EqualGreater));

                result[1] = LogicGateUtil.orGate(
                                share2Greater,
                                LogicGateUtil.orGate(share1And2EqualGreater, share2And3EqualGreater));

                result[2] = LogicGateUtil.orGate(
                                share3Greater,
                                LogicGateUtil.orGate(share1And3EqualGreater, share2And3EqualGreater));

                // If all equal, set all results to 1
                result[0] = LogicGateUtil.muxGate2x1(result[0], 1, allEqual);
                result[1] = LogicGateUtil.muxGate2x1(result[1], 1, allEqual);
                result[2] = LogicGateUtil.muxGate2x1(result[2], 1, allEqual);

                return result;
        }

        public static int[] bitCompare(int bit1, int bit2) {
                bit1 -= 48;
                bit2 -= 48;
                int[] result = new int[2];
                result[0] = 0; // comparison result
                result[1] = 0; // greater2 flag

                System.out.println("\t\t\tComparing " + bit1 + " and " + bit2);
                int nBit1 = LogicGateUtil.notGate(bit1);
                int nBit2 = LogicGateUtil.notGate(bit2);

                // Determine if the bits are equal (using NOR gate) retrun 1 if equal
                int equal = LogicGateUtil.norGate(LogicGateUtil.andGate(nBit1, bit2),
                                LogicGateUtil.andGate(nBit2, bit1));
                System.out.println("\t\t\tEqual: " + equal);

                // Determine if bit2 is greater (using AND gate) return 1 if bit2 is greater
                result[1] = LogicGateUtil.andGate(nBit1, bit2);
                System.out.println("\t\t\tBit2 Greater: " + result[1]);

                // Use MUXes to select the final output
                int result1 = LogicGateUtil.muxGate2x1(bit1, bit2, result[1]); // if bit2Greater, return bit2, else
                                                                               // return bit1
                System.out.println("\t\t\tResult1: " + result1);
                result[0] = LogicGateUtil.muxGate2x1(result1, 0, equal); // if equal,return 0, else return result1
                System.out.println("\t\t\tResult2: " + result[0]);

                return result;
        }

        public static int compareTwoBinaries(String binaryA, String binaryB) {
                int length = Math.max(binaryA.length(), binaryB.length());
                binaryA = padBinaryString(binaryA, length);
                binaryB = padBinaryString(binaryB, length);

                for (int i = 0; i < length; i++) {
                        int bitA = binaryA.charAt(i) - '0';
                        int bitB = binaryB.charAt(i) - '0';

                        int bitANot = LogicGateUtil.notGate(bitA);
                        int bitBNot = LogicGateUtil.notGate(bitB);

                        // bitA > bitB
                        int aGreaterB = LogicGateUtil.andGate(bitA, bitBNot);
                        // bitA < bitB
                        int aLessB = LogicGateUtil.andGate(bitANot, bitB);

                        // Determine result using mux
                        int result = LogicGateUtil.muxGate4x1(0, -1, 1, 0, aGreaterB, aLessB);

                        if (result != 0) {
                                return result;
                        }
                }
                return 0; // All bits are equal
        }

        private static String padBinaryString(String binary, int length) {
                int padLength = length - binary.length();
                return "0".repeat(padLength) + binary;
        }

        public static int[] splitShares(String share) {
                int[] shares = new int[3];
                shares[0] = share.charAt(0) - '0';
                shares[1] = share.charAt(1) - '0';
                shares[2] = share.charAt(2) - '0';
                return shares;
        }

}
