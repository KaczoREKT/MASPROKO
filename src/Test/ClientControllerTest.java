package Test;

import Controller.ClientController;
import Model.Client;
import Model.Enum.Gender;

import java.util.List;

public class ClientControllerTest {
    public static void runTests() throws Exception {
        System.out.println("=== ClientControllerTest ===");
        ClientController clientController = new ClientController();

        // 1. Dodanie klienta
        Client client = clientController.addNewClient("Morda", "Meliniarz", Gender.WOMAN, "ala@kot.pl", "555-123-123");
        assert client != null : "Klient nie został dodany!";
        System.out.println("Dodany klient: " + client);

        // 2. Dodanie drugiego klienta
        Client client2 = clientController.addNewClient("Jan", "Kowalski", Gender.MAN, "jan@kowal.pl", "888-000-111");
        assert client2 != null : "Drugi klient nie został dodany!";
        System.out.println("Dodany klient 2: " + client2);

        // 3. Pobierz listę klientów i sprawdź liczbę
        List<Client> clients = clientController.getList();
        assert clients.size() >= 2 : "Za mało klientów na liście!";
        System.out.println("Wszyscy klienci: " + clients);

        // 4. Sprawdzenie, czy można wyszukać klienta po ID
        Client byId = clientController.getClientById(client.getId());
        assert byId == client : "Nie znaleziono klienta po ID!";
        System.out.println("Znaleziony po ID: " + byId);

        // 5. Sprawdzenie wyszukiwania po numerze karty (dodajemy kartę)
        String cardNumber = client.getClientCard().getPublicId();
        Client byCard = clientController.findClientByCardNumber(cardNumber);
        assert byCard == client : "Nie znaleziono klienta po numerze karty!";
        System.out.println("Znaleziony po numerze karty: " + byCard);

        // 6. Sprawdzenie edycji danych klienta
        clientController.updateClient(client, "Ala", "Koteł", Gender.WOMAN, "ala.kot@kot.pl", "000-999-555");
        assert client.getFirstName().equals("Ala") && client.getLastName().equals("Koteł") : "Nie zaktualizowano imienia lub nazwiska!";
        assert client.getEmail().equals("ala.kot@kot.pl") : "Nie zaktualizowano emaila!";
        System.out.println("Po edycji klient: " + client);

        // 7. Sprawdzenie usuwania klienta
        clientController.deleteClient(client2);
        clients = clientController.getList();
        assert !clients.contains(client2) : "Klient nie został usunięty!";
        System.out.println("Klienci po usunięciu: " + clients);

        // 8. Próba usunięcia null
        boolean exceptionThrown = false;
        try {
            clientController.deleteClient(null);
        } catch (Exception e) {
            exceptionThrown = true;
            System.out.println("Poprawnie złapano wyjątek przy usuwaniu null: " + e.getMessage());
        }
        assert exceptionThrown : "Brak wyjątku przy usuwaniu null!";

        System.out.println("ClientControllerTest OK\n");
    }
}
