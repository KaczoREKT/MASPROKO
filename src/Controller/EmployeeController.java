package Controller;

import Model.*;
import Model.Enum.Gender;
import Model.utils.ObjectPlus;

import java.util.ArrayList;
import java.util.List;

public class EmployeeController extends AbstractController<Employee> {

    public EmployeeController() {
        super(Employee.class);
    }

    public String loginEmployee(String id) {
        return getEmployeeList().stream()
                .filter(e -> String.valueOf(e.getPublicId()).equals(id))
                .map(e -> e.getClass().getSimpleName())
                .findFirst()
                .orElse(null);
    }


    public List<Employee> getEmployeeList() {
        List<Employee> employees = new ArrayList<>();
        List<Class<? extends Employee>> subClasses = List.of(Accountant.class, Manager.class, Librarian.class);

        for (Class<? extends Employee> c : subClasses) {
            try {
                for (Employee e : ObjectPlus.getExtent(c)) {
                    employees.add(e);
                }
            } catch (ClassNotFoundException ex) {
                // Ekstensja nie istnieje, ignorujemy
            }
        }
        return employees;
    }

    public void deleteEmployee(Employee employee) throws Exception {
        if (employee == null) throw new Exception("Brak pracownika do usunięcia!");
        ObjectPlus.removeFromExtent(employee);
    }
    public Librarian addLibrarian(String firstName, String lastName, Gender gender, double salary, String specialization) {
        return new Librarian(firstName, lastName, gender, salary, specialization);
    }
    public Manager addManager(String firstName, String lastName, Gender gender, double salary, double bonus) {
        return new Manager(firstName, lastName, gender, salary, bonus);
    }
    public Accountant addAccountant(String firstName, String lastName, Gender gender, double salary) {
        return new Accountant(firstName, lastName, gender, salary);
    }

    public void updateSalary(Employee selected, double newSalary) {
        if (selected == null) {
            throw new IllegalArgumentException("Nie wybrano pracownika.");
        }
        if (newSalary <= 0) {
            throw new IllegalArgumentException("Pensja musi być większa od zera.");
        }
        selected.setSalary(newSalary);
    }

    public void setEmployeeSalary(Employee selected, double newSalary) {
        updateSalary(selected, newSalary);
    }

}
