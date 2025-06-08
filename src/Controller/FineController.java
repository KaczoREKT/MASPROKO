package Controller;

import Model.Enum.FineStatus;
import Model.Fine;
import java.time.LocalDate;

public class FineController extends AbstractController<Fine> {

    public FineController() {
        super(Fine.class);
    }

    // Oznacz jako opłacony
    public void markFineAsPaid(Fine selected) {
        if (selected != null) {
            selected.setStatus(FineStatus.OPLACONO);
        }
    }

    // Usuń mandat (karę)
    public void deleteFine(Fine selected) {
        if (selected != null) {
            Model.utils.ObjectPlus.removeFromExtent(selected);
        }
    }

    // Suma mandatów z ostatnich 30 dni (opłaconych lub nie)
    public double getTotalFinesInLast30Days() {
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(30);
        double sum = 0;
        for (Fine f : getList()) {
            if (f.getDate() != null && (f.getDate().isAfter(thirtyDaysAgo) || f.getDate().isEqual(thirtyDaysAgo))) {
                sum += f.getPrice();
            }
        }
        return sum;
    }
}
