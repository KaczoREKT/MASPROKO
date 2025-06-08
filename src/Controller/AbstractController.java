package Controller;

import Model.utils.ObjectPlus;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractController<T> {
    private final Class<T> clazz;

    public AbstractController(Class<T> clazz) {
        this.clazz = clazz;
    }

    public List<T> getList() {
        List<T> list = new ArrayList<>();
        try {
            for (T t : ObjectPlus.getExtent(clazz)) {
                list.add(t);
            }
        } catch (Exception _) {
        }
        return list;
    }
}
