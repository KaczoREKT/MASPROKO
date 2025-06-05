package Model;

/**
 * Klasa Employee – podklasa Person. Dodaje pole salary.
 * Jest klasą abstrakcyjną, bo bezpośrednio nie tworzymy "Employee" tylko np. Receptionist, Manager lub Librarian.
 */
public abstract class Employee extends Person {
    private static final long serialVersionUID = 1L;

    private double salary;

    public Employee(long id, String firstName, String lastName, Gender gender, double salary) {
        super(id, firstName, lastName, gender);
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
