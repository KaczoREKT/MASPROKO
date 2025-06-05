package Model;

import utils.ObjectPlus;

/**
 * Klasa abstrakcyjna Person – zawiera wspólne atrybuty WSZYSTKICH osób w systemie (klienci, pracownicy, itd.).
 * Każda instancja Person jest automatycznie rejestrowana w ekstensji Person.
 */
public abstract class Person extends ObjectPlus {
    private static final long serialVersionUID = 1L;

    private long id;
    private String firstName;
    private String lastName;
    private Gender gender;

    public Person(long id, String firstName, String lastName, Gender gender) {
        super();  // zapis do ekstensji
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    // Gettery / settery

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return String.format("Person[id=%d, name=%s %s, plec=%s]", id, firstName, lastName, gender);
    }
}
