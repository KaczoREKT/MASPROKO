package Model;

import Model.Enum.Gender;

import java.io.Serial;

public class Manager extends Employee {
    @Serial
    private static final long serialVersionUID = 1L;

    private  double bonus;

    public Manager(String firstName, String lastName, Gender gender, double salary, double bonus) {
        super(firstName, lastName, gender, salary);
        this.bonus = bonus;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        if (bonus > this.getSalary() * 0.1) {
            throw new IllegalArgumentException("Bonus większy niż 10% zysku.");
        }
        this.bonus = bonus;
    }

    @Override
    public String toString() {
        return String.format("Manager[id=%s, name=%s %s, salary=%.2f, bonus=%.2f]",
                getPublicId(), getFirstName(), getLastName(), getSalary(), bonus);
    }
}
