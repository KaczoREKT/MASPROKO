package Model;

import Model.Enum.Gender;

public class Accountant extends Employee {

    public Accountant(String firstName, String lastName, Gender gender, double salary) {
        super(firstName, lastName, gender, salary);
    }
}
