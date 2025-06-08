package Model;

import Model.Enum.Gender;

import java.io.Serial;

/**
 * Klasa Employee – podklasa Person. Dodaje pole salary.
 * Jest klasą abstrakcyjną, bo bezpośrednio nie tworzymy "Employee" tylko np. Receptionist, Manager lub Librarian.
 */
public abstract class Employee extends Person {
    @Serial
    private static final long serialVersionUID = 1L;

    private double salary;

    public Employee(String firstName, String lastName, Gender gender, double salary) {
        super(firstName, lastName, gender);
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
