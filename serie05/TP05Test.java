package serie05;

import util.TPTester;

public final class TP05Test {
    private TP05Test() {
        // rien
    }
    public static void main(String[] args) {
        TPTester t = new TPTester(
                serie05.StdPersistentPhoneBookTest.class
        );
        int exitValue = t.runAll();
        System.exit(exitValue);
    }
}
