package Model.utils;
import java.util.HashMap;
import java.util.Map;

public abstract class AutoIdEntity extends ObjectPlus {
    private static final Map<Class<? extends AutoIdEntity>, Long> nextIds = new HashMap<>();
    private final long id;

    protected AutoIdEntity() {
        Class<? extends AutoIdEntity> cls = this.getClass();
        long nextId = nextIds.getOrDefault(cls, 1L);
        this.id = nextId;
        nextIds.put(cls, nextId + 1);
    }

    public long getId() {
        return id;
    }

    public String getPrefix() {
        return this.getClass().getSimpleName().toUpperCase().charAt(0) + "";

    }

    public String getPublicId() {
        return getPrefix() + id;
    }

    public static <T extends AutoIdEntity> void recalculateNextId(Class<T> type) {
        long maxId = 0;
        try {
            for (T obj : ObjectPlus.getExtent(type)) {
                if (obj.getId() > maxId) maxId = obj.getId();
            }
        } catch (Exception ignored) {}
        nextIds.put(type, maxId + 1);
    }
}
