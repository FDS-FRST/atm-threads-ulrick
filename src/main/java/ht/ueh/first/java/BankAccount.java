package ht.ueh.first.java;

class BankAccount {
    private final int accountNumber;
    private double balance;

    public BankAccount(int accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }


    public void deposit(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Montant invalide: " + amount + ". Le montant doit être > 0");
        }
        balance += amount;
    }


    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Montant invalide: " + amount + ". Le montant doit être > 0");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Fonds insuffisants. Solde: " + balance + ", Tentative de retrait: " + amount);
        }
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

}
