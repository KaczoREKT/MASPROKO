package utils;

import java.io.*;
import java.util.*;

public abstract class ObjectPlus implements Serializable {
    private static Map<Class<? extends ObjectPlus>, List<ObjectPlus>> allExtents = new HashMap<>();

    public ObjectPlus() {
        // Dodanie obiektu do odpowiedniej ekstensji
        Class<? extends ObjectPlus> theClass = this.getClass();
        allExtents.computeIfAbsent(theClass, k -> new ArrayList<>()).add(this);
    }

    // Pobieranie ekstensji danej klasy
    public static <T> Iterable<T> getExtent(Class<T> type) throws ClassNotFoundException {
        if(allExtents.containsKey(type)) {
            return (Iterable<T>) allExtents.get(type);
        }
        throw new ClassNotFoundException(
                String.format("%s. Stored extents: %s", type.toString(), allExtents.keySet()));
    }

    // Pokazanie ekstensji dla danej klasy (metoda pomocnicza z wykładu)
    public static void showExtent(Class<?> theClass) throws Exception {
        List<ObjectPlus> extent = allExtents.get(theClass);
        if (extent == null) {
            throw new Exception("Unknown class " + theClass);
        }
        System.out.println("Extent of the class: " + theClass.getSimpleName());
        for(Object obj : extent) {
            System.out.println(obj);
        }
    }

    // Trwałość ekstensji – zapis do pliku
    public static void writeExtents(ObjectOutputStream stream) throws IOException {
        stream.writeObject(allExtents);
    }

    // Trwałość ekstensji – odczyt z pliku
    public static void readExtents(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        allExtents = (Map<Class<? extends ObjectPlus>, List<ObjectPlus>>) stream.readObject();
    }
}