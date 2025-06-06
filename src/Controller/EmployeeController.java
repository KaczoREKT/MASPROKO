package Controller;

import Model.Employee;
import Model.Manager;
import Model.Librarian;
import Model.Receptionist;
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
}
