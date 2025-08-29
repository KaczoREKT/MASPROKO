package Controller;

import Model.Sector;
import Model.utils.ObjectPlus;

public class SectorController extends AbstractController<Sector> {
    public SectorController() {
        super(Sector.class);
    }

    public Sector findSectorByRange(char e, char f) {
        try {
            for (Sector s : ObjectPlus.getExtent(Sector.class)) {
                if (s.getStartLetter() == e && s.getEndLetter() == f) {
                    return s;
                }
            }
        } catch (Exception _) {}
        return null;
    }

    public void deleteSector(Sector sectorGH) {
        if (sectorGH == null) return;
        try {
            ObjectPlus.removeFromExtent(sectorGH);
        } catch (Exception ex) {
        }
    }
}
