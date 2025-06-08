package Controller;

import Model.*;
import utils.ObjectPlus;

import java.util.ArrayList;
import java.util.List;

public class EmployeeController {

    public String loginEmployee(String id) {
        for (Employee e : getAllEmployees()) {
            if (String.valueOf(e.getPublicId()).equals(id)) {
                return e.getClass().getSimpleName();
            }
        }
        return null;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> result = new ArrayList<>();
        List<Class<? extends Employee>> subClasses = List.of(Manager.class, Librarian.class, Receptionist.class);
        for (Class<? extends Employee> c : subClasses) {
            try {
                for (Employee e : ObjectPlus.getExtent(c)) {
                    result.add(e);
                }
            } catch (ClassNotFoundException ex) {

            }
        }
        return result;
    }

    public List<Employee> getEmployeeList() {
        List<Employee> employees = new ArrayList<>();
        List<Class<? extends Employee>> subClasses = List.of(Manager.class, Librarian.class, Receptionist.class /*, inne podklasy*/);

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

    public List<String> getEmployeeIds() {
        List<String> ids = new ArrayList<>();
        List<Class<? extends Employee>> subClasses = List.of(Manager.class, Librarian.class, Receptionist.class);
        for (Class<? extends Employee> c : subClasses) {
            try {
                for (Employee e : ObjectPlus.getExtent(c)) {
                    ids.add(String.valueOf(e.getId()));
                }
            } catch (ClassNotFoundException ex) {
                // Brak ekstensji tej klasy - ignorujemy
            }
        }
        return ids;
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
    public Receptionist addReceptionist(String firstName, String lastName, Gender gender, double salary) {
        return new Receptionist(firstName, lastName, gender, salary);
    }
}
