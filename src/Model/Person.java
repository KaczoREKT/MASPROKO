package Model;

import utils.AutoIdEntity;
import utils.ObjectPlus;

/**
 * Klasa abstrakcyjna Person – zawiera wspólne atrybuty WSZYSTKICH osób w systemie (klienci, pracownicy, itd.).
 * Każda instancja Person jest automatycznie rejestrowana w ekstensji Person.
 */
public abstract class Person extends AutoIdEntity {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private Gender gender;

    public Person(String firstName, String lastName, Gender gender) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }


    // Gettery / settery

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
        return String.format("Person[id=%d, name=%s %s, plec=%s]", getId(), firstName, lastName, gender);
    }
}
