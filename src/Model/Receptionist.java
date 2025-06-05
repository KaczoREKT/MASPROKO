package Model;

/**
 * Klasa Receptionist – dziedziczy po Employee.
 * Nie ma dodatkowych atrybutów (zgodnie z diagramem).
 */
public class Receptionist extends Employee {
    private static final long serialVersionUID = 1L;

    public Receptionist(long id, String firstName, String lastName, Gender gender, double salary) {
        super(id, firstName, lastName, gender, salary);
    }

    @Override
    public String toString() {
        return String.format("Receptionist[id=%d, name=%s %s, salary=%.2f]",
                getId(), getFirstName(), getLastName(), getSalary());
    }
}
