package Test;

import Controller.EmployeeController;
import Model.Employee;
import Model.Librarian;
import Model.Manager;
import Model.Enum.Gender;

import java.util.List;

public class EmployeeControllerTest {
    public static void runTests() throws Exception {
        System.out.println("=== EmployeeControllerTest ===");
        EmployeeController employeeController = new EmployeeController();

        // Dodaj bibliotekarza
        Librarian librarian = employeeController.addLibrarian("Zofia", "Bibliotekarka", Gender.WOMAN, 4000, "Dramat");
        assert librarian != null : "Bibliotekarz nie został dodany!";
        System.out.println("Dodany bibliotekarz: " + librarian);

        // Dodaj managera
        Manager manager = employeeController.addManager("Adam", "Zarządzający", Gender.MAN, 7000, 1500);
        assert manager != null : "Manager nie został dodany!";
        System.out.println("Dodany manager: " + manager);

        // Sprawdź listę wszystkich pracowników
        List<Employee> employees = employeeController.getEmployeeList();
        assert employees.size() >= 3 : "Lista pracowników powinna mieć co najmniej 3 osoby!";
        System.out.println("Lista pracowników: " + employees);

        // Aktualizacja pensji (testuj updateSalary)
        double oldSalary = librarian.getSalary();
        employeeController.updateSalary(librarian, 4800);
        assert librarian.getSalary() == 4800 : "Pensja nie została zaktualizowana!";
        System.out.println("Zaktualizowana pensja bibliotekarza: " + librarian.getSalary() + " (było: " + oldSalary + ")");

        // Sprawdź loginEmployee
        String foundType = employeeController.loginEmployee(String.valueOf(librarian.getPublicId()));
        assert foundType.equals("Librarian") : "Niepoprawny typ zwrócony przez loginEmployee!";
        System.out.println("loginEmployee znalazł typ: " + foundType);

        System.out.println("EmployeeControllerTest OK\n");
    }
}
