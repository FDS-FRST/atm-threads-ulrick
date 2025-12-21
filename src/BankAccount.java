public class BankAccount
{
    private int accountNumber;
    private double balance;

    public BankAccount(int accountNumber, double initialBalance)
    {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public synchronized void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0)
        {
            throw new InvalidAmountException("Montant invalide pour dépôt : " + amount + ". Le montant doit être positif.");
        }
        balance += amount;
        System.out.println(Thread.currentThread().getName() + " - Dépôt de " + amount + "€ effectué. Nouveau solde : " + balance + "€");
    }

    public synchronized void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0)
        {
            throw new InvalidAmountException("Montant invalide pour retrait : " + amount + ". Le montant doit être positif.");
        }
        if (balance < amount)
        {
            throw new InsufficientFundsException("Fonds insuffisants. Solde actuel : " + balance + "€, retrait demandé : " + amount + "€");
        }
        balance -= amount;
        System.out.println(Thread.currentThread().getName() + " - Retrait de " + amount + "€ effectué. Nouveau solde : " + balance + "€");
    }

    public synchronized double getBalance()
    {
        return balance;
    }

    public int getAccountNumber()
    {
        return accountNumber;
    }
}