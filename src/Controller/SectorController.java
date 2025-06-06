package Controller;

import Model.Book;
import Model.Sector;
import utils.ObjectPlus;

import java.util.ArrayList;
import java.util.List;

public class SectorController {
    public List<Sector> getAllSectors() {
        List<Sector> sectors = new ArrayList<>();
        try {
            for (Sector s : ObjectPlus.getExtent(Sector.class)) {
                sectors.add(s);
            }
        } catch (Exception e) {}
        return sectors;
    }
}
