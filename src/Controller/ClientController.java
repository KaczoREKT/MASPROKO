package Controller;

import Model.Client;
import Model.ClientCard;
import Model.Enum.Gender;
import Model.utils.ObjectPlus;

import java.util.List;
import java.util.stream.Collectors;

public class ClientController extends AbstractController<Client> {
    public ClientController() {
        super(Client.class);
    }

    public Client addNewClient(String firstName, String lastName, Gender gender, String email, String phone) throws Exception {
        if (firstName == null || firstName.isEmpty()) throw new Exception("Imię nie może być puste");
        if (lastName == null || lastName.isEmpty()) throw new Exception("Nazwisko nie może być puste");
        if (email == null || email.isEmpty()) throw new Exception("Email nie może być pusty");
        Client client = new Client(firstName, lastName, gender, email, phone);
        new ClientCard(client);
        return client;
    }

    public List<String> getClientsFirstName(){
        return getList().stream()
                .map(Client::getFirstName)
                .collect(Collectors.toList());
    }


    public Client findClientByCardNumber(String cardNumber) {
        return getList().stream()
                .filter(client -> client.getClientCard() != null
                        && cardNumber != null
                        && cardNumber.equals(client.getClientCardId()))
                .findFirst()
                .orElse(null);
    }


    public Client getClientById(long id) {
        return getList().stream()
                .filter(client -> client.getId() == id)
                .findFirst()
                .orElse(null);
    }


    public void updateClient(Client client, String firstName, String lastName, Gender gender, String email, String phone) {
        if (client == null) return;
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setGender(gender);
        client.setEmail(email);
        client.setPhoneNumber(phone);
    }

    public void deleteClient(Client client) throws Exception {
        if (client == null) throw new Exception("Nie podano klienta do usunięcia!");
        ObjectPlus.removeFromExtent(client);
    }
}
