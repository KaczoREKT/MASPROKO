package Model;

import Model.Enum.Gender;

import java.io.Serial;

public class Receptionist extends Employee {
    @Serial
    private static final long serialVersionUID = 1L;

    public Receptionist(String firstName, String lastName, Gender gender, double salary) {
        super(firstName, lastName, gender, salary);
    }

    @Override
    public String toString() {
        return String.format("Receptionist[id=%s, name=%s %s, salary=%.2f]",
                getPublicId(), getFirstName(), getLastName(), getSalary());
    }
    @Override
    public String getPrefix() {
        return "RE";
    }
}
