package Controller;

import Model.Enum.Gender;
import Model.Librarian;
import Model.utils.ObjectPlus;

public class LibrarianController extends AbstractController<Librarian> {
    public LibrarianController() {
        super(Librarian.class);
    }

    public Librarian addLibrarian(String firstName, String lastName, Gender gender, int salary, String specialization) {
        return new Librarian(firstName, lastName, gender, salary, specialization);
    }

    public void updateSalary(Librarian librarian, int newSalary) {
        if (librarian != null) {
            librarian.setSalary(newSalary);
        }
    }

    public Librarian getLibrarianById(String publicId) {
        try {
            for (Librarian l : ObjectPlus.getExtent(Librarian.class)) {
                if (l.getPublicId().equals(publicId)) {
                    return l;
                }
            }
        } catch (Exception _) {
        }
        return null;
    }

    public void deleteLibrarian(Librarian librarian) {
        if (librarian == null) return;
        try {
            ObjectPlus.removeFromExtent(librarian);
        } catch (Exception e) {
            // Możesz dodać obsługę błędu lub logowanie
        }
    }
}
