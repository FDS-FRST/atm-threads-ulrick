public class Main
{
    public static void main(String[] args)
    {
        System.out.println("=== TP Java - ATM ===\n");

        //PARTIE 1 : Test simple sans threads
        System.out.println("=== Partie 1 : Test simple sans threads ===");
        BankAccount simpleAccount = new BankAccount(12345, 1000.0);
        try
        {
            simpleAccount.deposit(200);
            simpleAccount.withdraw(150);
            System.out.println("Solde final : " + simpleAccount.getBalance() + "€\n");
        }
        catch(Exception e)
        {
            System.err.println("Erreur : " + e.getMessage());
        }

        //PARTIE 3 : Mise en évidence de la Race Condition
        System.out.println("=== Partie 3 : Démonstration de la Race Condition ===");
        demonstrateRaceCondition();

        //PARTIE 4 : Avec synchronisation
        System.out.println("\n=== Partie 4 : Avec synchronisation ===");
        demonstrateWithSynchronization();

        //PARTIE 5 : Simulation multi-clients
        System.out.println("\n=== Partie 5 : Simulation multi-clients ===");
        simulateMultipleClients();

        //PARTIE 6 : Tests avec exceptions
        System.out.println("\n=== Partie 6 : Tests avec gestion d'exceptions ===");
        testWithExceptions();
    }

    private static void demonstrateRaceCondition()
    {
        BankAccount raceAccount = new BankAccount(99999, 1000.0);

        // Création de deux threads qui tentent de retirer 700€ chacun
        Thread thread1 = new Thread(() ->
        {
            try
            {
                raceAccount.withdraw(700);
            }
            catch (Exception e)
            {
                System.err.println("Thread 1 : " + e.getMessage());
            }
        }, "Thread-Race-1");

        Thread thread2 = new Thread(() ->
        {
            try
            {
                raceAccount.withdraw(700);
            }
            catch (Exception e)
            {
                System.err.println("Thread 2 : " + e.getMessage());
            }
        }, "Thread-Race-2");

        // Démarrage des threads
        thread1.start();
        thread2.start();

        // Attente de la fin des threads
        try
        {
            thread1.join();
            thread2.join();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nAnalyse de la Race Condition :");
        System.out.println("- Solde théorique attendu : -400€ (ou erreur pour le second retrait)");
        System.out.println("- Solde observé : " + raceAccount.getBalance() + "€");
        System.out.println("- Problème : Les deux retraits peuvent réussir car ils lisent le solde en même temps");
        System.out.println("- Ce type de problème s'appelle : RACE CONDITION\n");
    }

    private static void demonstrateWithSynchronization()
    {
        BankAccount syncAccount = new BankAccount(88888, 1000.0);

        System.out.println("Avec synchronized, un seul thread peut accéder à la méthode à la fois.");
        System.out.println("Le second retrait sera refusé (fonds insuffisants).\n");

        Thread thread1 = new Thread(() ->
        {
            try
            {
                syncAccount.withdraw(700);
            }
            catch (Exception e)
            {
                System.err.println(Thread.currentThread().getName() + " : " + e.getMessage());
            }
        }, "Thread-Sync-1");

        Thread thread2 = new Thread(() ->
        {
            try
            {
                syncAccount.withdraw(700);
            }
            catch (Exception e)
            {
                System.err.println(Thread.currentThread().getName() + " : " + e.getMessage());
            }
        }, "Thread-Sync-2");

        thread1.start();
        thread2.start();

        try
        {
            thread1.join();
            thread2.join();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        System.out.println("Solde final avec synchronisation : " + syncAccount.getBalance() + "€\n");
    }

    private static void simulateMultipleClients()
    {
        BankAccount multiAccount = new BankAccount(77777, 2000.0);
        Thread[] clients = new Thread[10];

        System.out.println("Création de 10 clients effectuant 5 opérations aléatoires chacun...\n");

        for (int i = 0; i < 10; i++)
        {
            ATMTask task = new ATMTask(multiAccount, i + 1);
            clients[i] = new Thread(task, "Client-" + (i + 1));
            clients[i].start();
        }

        // Attente de la fin de tous les clients
        for (Thread client : clients)
        {
            try
            {
                client.join();
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("\nSimulation terminée. Solde final du compte : " + multiAccount.getBalance() + "€\n");
    }

    private static void testWithExceptions() {
        BankAccount testAccount = new BankAccount(55555, 500.0);

        System.out.println("Tests avec exceptions :");
        System.out.println("1. Retrait valide");
        System.out.println("2. Retrait trop grand (fonds insuffisants)");
        System.out.println("3. Dépôt négatif");
        System.out.println("4. Consultation du solde\n");

        // Création des threads avec différentes opérations
        Thread[] testThreads = new Thread[4];

        // 1. Retrait valide
        testThreads[0] = new Thread(() ->
        {
            try
            {
                testAccount.withdraw(200);
            }
            catch (Exception e)
            {
                System.err.println("Test 1 échoué : " + e.getMessage());
            }
        }, "Test-Retrait-Valide");

        // 2. Retrait trop grand
        testThreads[1] = new Thread(() ->
        {
            try
            {
                testAccount.withdraw(1000); // Solde insuffisant
            }
            catch (Exception e)
            {
                System.err.println("Test 2 - " + Thread.currentThread().getName() +
                        " : Retrait de 1000€ refusé - " + e.getMessage());
            }
        }, "Test-Retrait-Trop-Grand");

        // 3. Dépôt négatif
        testThreads[2] = new Thread(() ->
        {
            try
            {
                testAccount.deposit(-50); // Montant invalide
            }
            catch (Exception e)
            {
                System.err.println("Test 3 - " + Thread.currentThread().getName() +
                        " : Dépôt de -50€ refusé - " + e.getMessage());
            }
        }, "Test-Depot-Negatif");

        // 4. Consultation du solde
        testThreads[3] = new Thread(() ->
        {
            System.out.println("Test 4 - " + Thread.currentThread().getName() +
                    " : Consultation - Solde actuel : " + testAccount.getBalance() + "€");
        }, "Test-Consultation");

        // Démarrage des threads
        for (Thread thread : testThreads)
        {
            thread.start();
        }

        // Attente de la fin des threads
        for (Thread thread : testThreads)
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("\nTous les tests sont terminés. Programme terminé.");
    }
}