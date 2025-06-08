package Controller;

import Model.Client;
import Model.ClientCard;
import Model.Enum.Gender;
import Model.utils.ObjectPlus;

import java.util.List;
import java.util.stream.StreamSupport;

public class ClientController {
    public Client addNewClient(String firstName, String lastName, Gender gender, String email, String phone) throws Exception {
        // Walidacja przykładowa
        if (firstName == null || firstName.isEmpty()) throw new Exception("Imię nie może być puste");
        if (lastName == null || lastName.isEmpty()) throw new Exception("Nazwisko nie może być puste");
        if (email == null || email.isEmpty()) throw new Exception("Email nie może być pusty");
        // Możesz dodać walidację formatu e-mail i telefonu

        // Tworzenie klienta automatycznie tworzy też ClientCard (jak ustaliliśmy w konstruktorze Client)
        Client client = new Client(firstName, lastName, gender, email, phone);
        new ClientCard(client);
        return client;
    }
    public List<Client> getClientList() {
        try {
            Iterable<Client> iterable = ObjectPlus.getExtent(Client.class);
            return StreamSupport.stream(iterable.spliterator(), false)
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    public Client findClientByCardNumber(String cardNumber) {
        // Załóżmy, że masz listę wszystkich klientów
        for (Client client : getClientList()) {
            if (client.getClientCard() != null && cardNumber != null
                    && cardNumber.equals(client.getClientCardId())) {
                return client;
            }
        }
        return null;
    }
}
