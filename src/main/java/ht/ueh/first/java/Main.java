package ht.ueh.first.java;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount(12345, 1000.0);

        // Opérations
        account.deposit(200.0);
        account.withdraw(150.0);

        System.out.println("No Du Compte: "+ account.getAccountNumber() );
        System.out.println("Solde final: " + account.getBalance() + " Gourdes");

        System.out.println("\n=== Race Condition ===");

        Thread client1 = new Thread(new ATMTask(account, Operation.WITHDRAW, 700.0, "Mike"));
        Thread client2 = new Thread(new ATMTask(account, Operation.WITHDRAW, 700.0, "Dually"));

        client1.start();
        client2.start();

        client1.join();
        client2.join();

        System.out.println("Solde final: " + account.getBalance() + " Gourdes");

    }
}