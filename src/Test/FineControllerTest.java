package Test;

import Controller.FineController;
import Model.Client;
import Model.Enum.Gender;
import Model.Fine;
import Model.Enum.FineStatus;
import java.util.List;

public class FineControllerTest {
    public static void runTests() {
        System.out.println("=== FineControllerTest ===");
        FineController fineController = new FineController();

        // Dodaj klienta i mandat
        Client client = new Client("Marek", "Winny", Gender.MAN, "marek@winny.pl", "600-600-600");
        Fine fine = new Fine(50, "Za przetrzymanie książki");
        fine.setClient(client);

        List<Fine> fines = fineController.getList();
        assert fines.contains(fine) : "Mandat nie pojawił się na liście!";
        System.out.println("Dodany mandat: " + fines.getFirst());

        // 1. Zaznacz jako opłacony
        fineController.markFineAsPaid(fine);
        assert fine.getStatus() == FineStatus.PAID : "Status mandatu nie zmienił się na OPLACONO!";
        System.out.println("Mandat po opłaceniu: " + fine);

        // 2. Dodaj jeszcze kilka mandatów (różne daty)
        Fine fine2 = new Fine(25, "Za hałas w bibliotece");
        fine2.setClient(client);
        Fine fine3 = new Fine(80, "Zniszczenie książki");
        fine3.setClient(client);

        // Ustaw daty na przeszłe i bieżące (na potrzeby sumowania)
        fine2.setStatus(FineStatus.UNPAID);
        fine3.setStatus(FineStatus.UNPAID);

        // 3. Test sumowania NIEOPLACONYCH mandatów z ostatnich 30 dni
        // (ustaw datę ręcznie, jeśli masz pole date w klasie Fine, np. fine3.setDate(LocalDate.now().minusDays(5)))
        double totalLast30 = fineController.getTotalFinesInLast30Days();
        System.out.println("Suma NIEOPLACONYCH kar z ostatnich 30 dni: " + totalLast30);

        // 4. Pobierz wszystkie mandaty klienta
        List<Fine> allFines = fineController.getList();
        assert allFines.contains(fine2) && allFines.contains(fine3) : "Nie wszystkie mandaty są na liście!";
        System.out.println("Mandaty klienta: " + allFines);

        // 5. Usuń jeden mandat
        fineController.deleteFine(fine2);
        List<Fine> finesAfterDelete = fineController.getList();
        assert !finesAfterDelete.contains(fine2) : "Mandat nie został usunięty!";
        System.out.println("Lista mandatów po usunięciu: " + finesAfterDelete);

        // 6. Próba opłacenia już opłaconego mandatu
        fineController.markFineAsPaid(fine);
        assert fine.getStatus() == FineStatus.PAID : "Status mandatu zmienił się mimo, że był już opłacony!";
        System.out.println("Mandat już opłacony: " + fine);

        System.out.println("FineControllerTest OK\n");
    }
}
