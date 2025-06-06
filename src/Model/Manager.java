package Model;

/**
 * Klasa Manager – dziedziczy po Employee, dodaje pole bonus.
 */
public class Manager extends Employee {
    private static final long serialVersionUID = 1L;

    private double bonus;

    public Manager(String firstName, String lastName, Gender gender, double salary, double bonus) {
        super(firstName, lastName, gender, salary);
        this.bonus = bonus;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    @Override
    public String toString() {
        return String.format("Manager[id=%s, name=%s %s, salary=%.2f, bonus=%.2f]",
                getPublicId(), getFirstName(), getLastName(), getSalary(), bonus);
    }
}
