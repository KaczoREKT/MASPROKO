package Model.utils;

import java.io.*;
import java.util.*;

public abstract class ObjectPlus implements Serializable {
    private static Map<Class<? extends ObjectPlus>, List<ObjectPlus>> allExtents = new HashMap<>();

    public ObjectPlus() {
        Class<? extends ObjectPlus> theClass = this.getClass();
        allExtents.computeIfAbsent(theClass, k -> new ArrayList<>()).add(this);
    }

    public static <T> Iterable<T> getExtent(Class<T> type) throws ClassNotFoundException {
        if(allExtents.containsKey(type)) {
            return (Iterable<T>) allExtents.get(type);
        }
        throw new ClassNotFoundException(
                String.format("%s. Stored extents: %s", type.toString(), allExtents.keySet()));
    }

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

    public static void writeExtents(ObjectOutputStream stream) throws IOException {
        stream.writeObject(allExtents);
    }

    public static void readExtents(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        allExtents = (Map<Class<? extends ObjectPlus>, List<ObjectPlus>>) stream.readObject();
    }

    public static void saveToFile(String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            writeExtents(out);
            System.out.println("[INFO] Zapisano ekstensje do pliku " + fileName + ".");
        } catch (IOException e) {
            System.err.println("[BŁĄD] Nie udało się zapisać ekstensji do pliku " + fileName);
            e.printStackTrace();
        }
    }

    public static void loadFromFile(String fileName) {
        File file = new File(fileName);
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            readExtents(in);
            System.out.println("[INFO] Wczytano ekstensje z pliku " + fileName + ".");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[BŁĄD] Nie udało się wczytać ekstensji z pliku " + fileName);
            e.printStackTrace();
        }
    }
    public static void removeFromExtent(ObjectPlus object) {
        if (object == null) return;
        Class<? extends ObjectPlus> theClass = object.getClass();
        List<ObjectPlus> extent = allExtents.get(theClass);
        if (extent != null) {
            extent.remove(object);
        }
    }


    public static String getExtentString(Class<?> cls) throws Exception {
        StringBuilder sb = new StringBuilder();
        Iterable<?> extent = getExtent(cls);
        boolean empty = true;
        for (Object o : extent) {
            sb.append("  ").append(o).append("\n");
            empty = false;
        }
        if (empty) {
            sb.append("  [Brak obiektów]\n");
        }
        return sb.toString();
    }

}