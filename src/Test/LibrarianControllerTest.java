package Test;

import Controller.LibrarianController;
import Model.Enum.Gender;
import Model.Librarian;

import java.util.List;

public class LibrarianControllerTest {
    public static void runTests() {
        System.out.println("=== LibrarianControllerTest ===");
        LibrarianController librarianController = new LibrarianController();

        // 1. Dodanie bibliotekarza przez konstruktor
        Librarian librarian1 = new Librarian("Jan", "Biblia", Gender.MAN, 4100, "Fantasy");
        List<Librarian> list = librarianController.getList();
        assert list.contains(librarian1) : "Bibliotekarz nie pojawił się na liście!";
        System.out.println("Bibliotekarze po dodaniu konstruktorem: " + list);

        // 2. Dodanie bibliotekarza przez controller
        Librarian librarian2 = librarianController.addLibrarian("Zofia", "Książkowa", Gender.WOMAN, 4300, "Romans");
        List<Librarian> updatedList = librarianController.getList();
        assert updatedList.contains(librarian2) : "Bibliotekarz nie został dodany przez kontroler!";
        System.out.println("Bibliotekarze po dodaniu przez kontroler: " + updatedList);

        // 3. Aktualizacja wynagrodzenia bibliotekarza
        librarianController.updateSalary(librarian2, 5000);
        assert librarian2.getSalary() == 5000 : "Nie zaktualizowano wynagrodzenia!";
        System.out.println("Bibliotekarz po aktualizacji wynagrodzenia: " + librarian2);

        // 4. Szukanie bibliotekarza po publicId
        Librarian found = librarianController.getLibrarianById(librarian2.getPublicId());
        assert found != null && found.equals(librarian2) : "Nie znaleziono bibliotekarza po publicId!";
        System.out.println("Znaleziony bibliotekarz: " + found);

        // 5. Usuwanie bibliotekarza
        try {
            librarianController.deleteLibrarian(librarian1);
            List<Librarian> afterDelete = librarianController.getList();
            assert !afterDelete.contains(librarian1) : "Bibliotekarz nie został usunięty!";
            System.out.println("Bibliotekarze po usunięciu: " + afterDelete);
        } catch (Exception ex) {
            System.out.println("Błąd przy usuwaniu: " + ex.getMessage());
        }

        // 6. Próba usunięcia bibliotekarza, który już nie istnieje
        boolean exceptionThrown = false;
        try {
            librarianController.deleteLibrarian(librarian1);
        } catch (Exception ex) {
            exceptionThrown = true;
            System.out.println("Poprawnie zgłoszono błąd przy usuwaniu nieistniejącego bibliotekarza: " + ex.getMessage());
        }
        assert exceptionThrown : "Nie zgłoszono wyjątku przy usuwaniu nieistniejącego bibliotekarza!";

        System.out.println("LibrarianControllerTest OK\n");
    }
}
