package ht.ueh.first.java;

enum Operation {
    DEPOSIT, WITHDRAW, BALANCE
}

class ATMTask implements Runnable {
    private BankAccount account;
    private Operation operation;
    private double amount;
    private String clientName;

    public ATMTask(BankAccount account, Operation operation, double amount, String clientName) {
        this.account = account;
        this.operation = operation;
        this.amount = amount;
        this.clientName = clientName;
    }

    @Override
    public void run() {
        try {
            switch (operation) {
                case DEPOSIT:
                    System.out.println("Client " + clientName + " tente un dépôt de " + amount + " Gourdes");
                    account.deposit(amount);
                    System.out.println("Client " + clientName + " a déposé " + amount + " Gourdes");
                    break;

                case WITHDRAW:
                    System.out.println("Client " + clientName + " tente un retrait de " + amount + " Gourdes");
                    account.withdraw(amount);
                    System.out.println("Client " + clientName + " a retiré " + amount + " Gourdes");
                    break;

                case BALANCE:
                    double balance = account.getBalance();
                    System.out.println("Client " + clientName + " consulte le solde: " + balance + " Gourdes");
                    break;
            }

            // Simuler le temps d'attente à l'ATM
            Thread.sleep((long) (Math.random() * 100));

        } catch (InvalidAmountException | InsufficientFundsException e) {
            System.err.println("Client " + clientName + " - Erreur: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Client " + clientName + " - Opération interrompue");
        } catch (Exception e) {
            System.err.println("Client " + clientName + " - Erreur inattendue: " + e.getMessage());
        }
    }
}
