package ht.ueh.first.java;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount(12345, 1000.0);

        System.out.println("\nRÉPONSES des question posées dans le TP");
        System.out.println("sont COMMENTÉES dans le fichier main.java\n");
        // Opérations
        account.deposit(200.0);
        account.withdraw(150.0);

        System.out.println("No Du Compte: "+ account.getAccountNumber() );
        System.out.println("Solde devient: " + account.getBalance() + " Gourdes\n");

        Thread client1 = new Thread(new ATMTask(account, Operation.WITHDRAW, 700.0, "Mike"));
        Thread client2 = new Thread(new ATMTask(account, Operation.WITHDRAW, 700.0, "Dually"));

        client1.start();
        client2.start();

        client1.join();
        client2.join();

        System.out.println("Le solde est: " + account.getBalance() + " Gourdes\n");

        // Avant Synchronized
        // Quel solde attend-on théoriquement ?
        // Réponse: 1050 - 700 - 700 = -450 Gourds ou erreur pour le 2ème retrait

        // Quel solde observez-vous parfois ?
        // Réponse: toujours un seul retrait effectué et c'est le premier.

        // Pourquoi les deux retraits peuvent-ils réussir?
        // Réponse: Les deux threads lisent le solde simultanément (1050 Gourdes),"
        // vérifient que 700 <= 1050 est vrai, puis soustraient 700 chacun.

        // Comment appelle-t-on ce type de problème ?
        // Réponse: Race Condition ou Condition de Course

        // Après Synchronized
        // Pourquoi synchronized empêche-t-il la race condition?
        // Réponse: synchronized crée un verrou (lock) sur l'objet.
        // Un seul thread peut exécuter une méthode synchronisée à la fois.

        // Que se passe-t-il si plusieurs threads veulent entrer?
        // Réponse: Les threads sont mis en attente et exécuter un par un.
        System.out.println("10 THREAD CLIENTS AVEC 5 OPERATIONS ALÉATOIRES");
        String[] clientNames = {
                "Marco", "Bigi", "Valentz", "Kerby", "Ivenson",
                "Ulrick", "black", "Henry", "Edouard", "Dagobert"
        };
        Thread[] clients = new Thread[10];

        for (int i = 0; i < 10; i++) {
            final String clientName = clientNames[i];
            clients[i] = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    try {

                        Operation randomOp = Operation.values()[(int) (Math.random() * 3)];
                        double randomAmount = Math.random() * 500 + 100;

                        ATMTask task = new ATMTask(account, randomOp, randomAmount, clientName);
                        task.run();


                        Thread.sleep((long) (Math.random() * 200));

                    } catch (InterruptedException e) {
                        System.err.println("Client " + clientName + " interrompu");
                    }
                }
            });
        }

        for (Thread client : clients) {
            client.start();
        }

        for (Thread client : clients) {
            client.join();
        }

        System.out.println("\nSolde final: " + account.getBalance() + " Gourdes\n");

        System.out.println("RÉPONSES des question posées dans le TP");
        System.out.println("sont COMMENTÉES dans le fichier main.java");
    }
}