import java.math.BigInteger;

public class User {
    private String name;
    private int salary;
    private String encryptedSalary;
    private int bit1;
    private int bit2;
    private int bit3;
    private int[] shares1;
    private int[] shares2;
    private int[] shares3;

    private int shares1Int;
    private int shares2Int;
    private int shares3Int;

    private String encryptedShares1;
    private String encryptedShares2;
    private String encryptedShares3;

    private int[] result; // Changed from int to int[]

    private BigInteger N;
    private BigInteger E;
    private BigInteger D;

    public User(String name, BigInteger N, BigInteger E, BigInteger D) {
        this.name = name;
        this.N = N;
        this.E = E;
        this.D = D;
        this.result = new int[3];
    }

    public int[] getResult() {
        return result;
    }

    public void setResult(int[] result) {
        if (result.length != 3) {
            throw new IllegalArgumentException("Result array must have exactly 3 elements.");
        }
        this.result = result;
    }

    // individual setters for each element of the result array
    public void setResult(int index, int value) {
        if (index < 0 || index >= 3) {
            throw new IllegalArgumentException("Index must be between 0 and 2.");
        }
        this.result[index] = value;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }

    public int getShares1Int() {
        return shares1Int;
    }

    public int getShares2Int() {
        return shares2Int;
    }

    public int getShares3Int() {
        return shares3Int;
    }

    public void setShares1Int(int shares1Int) {
        this.shares1Int = shares1Int;
    }

    public void setShares2Int(int shares2Int) {
        this.shares2Int = shares2Int;
    }

    public void setShares3Int(int shares3Int) {
        this.shares3Int = shares3Int;
    }

    public int[] getShares1() {
        return shares1;
    }

    public int[] getShares2() {
        return shares2;
    }

    public int[] getShares3() {
        return shares3;
    }

    public void setShares1(int[] shares1) {
        this.shares1 = shares1;
    }

    public void setShares2(int[] shares2) {
        this.shares2 = shares2;
    }

    public void setShares3(int[] shares3) {
        this.shares3 = shares3;
    }

    public int getBit1() {
        return bit1;
    }

    public int getBit2() {
        return bit2;
    }

    public int getBit3() {
        return bit3;
    }

    public void setBit1(int bit1) {
        this.bit1 = bit1;
    }

    public void setBit2(int bit2) {
        this.bit2 = bit2;
    }

    public void setBit3(int bit3) {
        this.bit3 = bit3;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public BigInteger getN() {
        return N;
    }

    public BigInteger getE() {
        return E;
    }

    public BigInteger getD() {
        return D;
    }

    public String getEncryptedSalary() {
        return encryptedSalary;
    }

    public void setEncryptedSalary(String encryptedSalary) {
        this.encryptedSalary = encryptedSalary;
    }

    public String getEncryptedShares1() {
        return encryptedShares1;
    }

    public void setEncryptedShares1(String encryptedShares1) {
        this.encryptedShares1 = encryptedShares1;
    }

    public String getEncryptedShares2() {
        return encryptedShares2;
    }

    public void setEncryptedShares2(String encryptedShares2) {
        this.encryptedShares2 = encryptedShares2;
    }

    public String getEncryptedShares3() {
        return encryptedShares3;
    }

    public void setEncryptedShares3(String encryptedShares3) {
        this.encryptedShares3 = encryptedShares3;
    }
}
