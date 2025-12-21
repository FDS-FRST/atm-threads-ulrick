import java.util.Random;

public class ATMTask implements Runnable
{
    private BankAccount account;
    private String action; // "DEPOSIT", "WITHDRAW", "BALANCE"
    private double amount;
    private int clientId;
    private Random random = new Random();

    // Constructeur pour actions spécifiques
    public ATMTask(BankAccount account, String action, double amount, int clientId)
    {
        this.account = account;
        this.action = action;
        this.amount = amount;
        this.clientId = clientId;
    }

    // Constructeur pour simulation aléatoire (Partie 5)
    public ATMTask(BankAccount account, int clientId)
    {
        this.account = account;
        this.clientId = clientId;
        this.action = "RANDOM";
    }

    @Override
    public void run()
    {
        try
        {
            switch (action)
            {
                case "DEPOSIT":
                    performDeposit(amount);
                    break;
                case "WITHDRAW":
                    performWithdraw(amount);
                    break;
                case "BALANCE":
                    checkBalance();
                    break;
                case "RANDOM":
                    // Simulation multi-clients avec 5 opérations aléatoires
                    for (int i = 0; i < 5; i++) {
                        performRandomOperation();
                        // Pause aléatoire pour simuler le temps de traitement
                        Thread.sleep(random.nextInt(500));
                    }
                    break;
            }
        }
        catch (InvalidAmountException | InsufficientFundsException e)
        {
            // Gestion des exceptions métier
            System.err.println("Client " + clientId + " (" + Thread.currentThread().getName() +
                    ") - Erreur lors de " + action +
                    " de " + amount + "€ : " + e.getMessage());
        }
        catch (InterruptedException e)
        {
            System.err.println("Thread interrompu : " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            // Capture toute autre exception
            System.err.println("Erreur inattendue pour le client " + clientId +
                    " : " + e.getMessage());
        }
    }

    private void performDeposit(double amount) throws InvalidAmountException {
        account.deposit(amount);
    }

    private void performWithdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
        account.withdraw(amount);
    }

    private void checkBalance() {
        double balance = account.getBalance();
        System.out.println("Client " + clientId + " (" + Thread.currentThread().getName() +
                ") - Consultation solde : " + balance + "€");
    }

    private void performRandomOperation() throws InvalidAmountException, InsufficientFundsException {
        String[] operations = {"DEPOSIT", "WITHDRAW", "BALANCE"};
        String randomOp = operations[random.nextInt(operations.length)];

        switch (randomOp) {
            case "DEPOSIT":
                double depositAmount = 50 + random.nextInt(450); // 50 à 500€
                account.deposit(depositAmount);
                break;
            case "WITHDRAW":
                double withdrawAmount = 20 + random.nextInt(180); // 20 à 200€
                account.withdraw(withdrawAmount);
                break;
            case "BALANCE":
                double balance = account.getBalance();
                System.out.println("Client " + clientId + " (" + Thread.currentThread().getName() +
                        ") - Consultation solde : " + balance + "€");
                break;
        }
    }

    public int getClientId() {
        return clientId;
    }
}