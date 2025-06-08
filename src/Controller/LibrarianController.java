package Controller;

import Model.Librarian;
import Model.utils.ObjectPlus;

import java.util.List;
import java.util.stream.StreamSupport;

public class LibrarianController {

    public List<Librarian> getLibrarianList() {
        try {
            Iterable<Librarian> iterable = ObjectPlus.getExtent(Librarian.class);
            return StreamSupport.stream(iterable.spliterator(), false)
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }
}
