package utils;
import Model.*;
import java.util.*;
public class SampleData {
    public static void addSampleData() {
        // ===== 1. Tworzymy sektory =====
        Sector sectorA_C = new Sector('A', 'C');
        Sector sectorD_F = new Sector('D', 'F');

        // ===== 2. Tworzymy kilka książek i przypisujemy je do sektorów =====
        Book book1 = new Book("Alchemik", "Powieść", "Paulo Coelho");
        book1.setSector(sectorA_C);

        Book book2 = new Book("Cień wiatru", "Thriller", "Carlos Ruiz Zafón");
        book2.setSector(sectorA_C);

        Book book3 = new Book("Dotyk Crossa", "Akcja", "James Patterson");
        book3.setSector(sectorD_F);

        Book book4 = new Book("Forteca zatracenia", "Horror", "Stephen King");
        book4.setSector(sectorD_F);

        // ===== 3. Tworzymy bibliotekarza i przypisujemy mu zadanie sortowania =====
        Librarian librarian = new Librarian("Anna", "Nowak", Gender.WOMAN, 3500.00, "Powieść");
        // Zadanie sortowania dla sektora A–C
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2023, Calendar.MARCH, 1);
        Date startSort = cal1.getTime();
        cal1.set(2023, Calendar.MARCH, 5);
        Date endSort = cal1.getTime();
        SortingJob job1 = new SortingJob(startSort, endSort);
        job1.setLibrarian(librarian);
        job1.setSector(sectorA_C);

        // ===== 4. Tworzymy managera i recepcjonistkę =====
        Manager manager = new Manager("Krzysztof", "Wiśniewski", Gender.MAN, 5500.00, 1200.00);
        Receptionist receptionist = new Receptionist("Magdalena", "Kowalczyk", Gender.WOMAN, 3000.00);

        // ===== 5. Tworzymy klienta, kartę klienta, rezerwację i mandat =====
        Client client1 = new Client("Jan", "Kowalski", Gender.MAN,
                "jan.kowalski@example.com", "123-456-789");

        // Karta klienta - ważna do końca 2024.12.31
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2024, Calendar.DECEMBER, 31);
        Date expiry = cal2.getTime();
        ClientCard card1 = new ClientCard(expiry, false);
        card1.setClient(client1);

        // Rezerwacja książki "Cień wiatru" (book2) od 2023.04.10 do 2023.04.20
        Calendar cal3 = Calendar.getInstance();
        cal3.set(2023, Calendar.APRIL, 10);
        Date resStart = cal3.getTime();
        cal3.set(2023, Calendar.APRIL, 20);
        Date resEnd = cal3.getTime();
        Reservation reservation1 = new Reservation(resStart, resEnd);
        reservation1.setBook(book2);
        reservation1.setClient(client1);

        // Mandat dla klienta, np. zwłoka w oddaniu książki (status 0 = nieopłacony)
        Fine fine1 = new Fine(25.50, "Przekroczenie terminu", 0);
        fine1.setClient(client1);

        // ===== 6. Tworzymy dodatkowe przykładowe obiekty (opcjonalnie) =====
        // - Drugi klient z pustą kartą (wygasłą od 2022.01.01)
        Client client2 = new Client( "Alicja", "Zielińska", Gender.WOMAN,
                "alicia.zielinska@example.com", "987-654-321");
        Calendar cal4 = Calendar.getInstance();
        cal4.set(2022, Calendar.JANUARY, 1);
        ClientCard card2 = new ClientCard(cal4.getTime(), true);
        card2.setClient(client2);

        // - Rezerwacja dla Alicji: książka "Forteca zatracenia" (book4) od 2023.05.01 do 2023.05.10
        Calendar cal5 = Calendar.getInstance();
        cal5.set(2023, Calendar.MAY, 1);
        Date resStart2 = cal5.getTime();
        cal5.set(2023, Calendar.MAY, 10);
        Date resEnd2 = cal5.getTime();
        Reservation reservation2 = new Reservation(resStart2, resEnd2);
        reservation2.setBook(book4);
        reservation2.setClient(client2);

        // - Kolejne zadanie sortowania dla sektora D–F przez tego samego bibiotekarza
        Calendar cal6 = Calendar.getInstance();
        cal6.set(2023, Calendar.JUNE, 1);
        Date startSort2 = cal6.getTime();
        cal6.set(2023, Calendar.JUNE, 3);
        Date endSort2 = cal6.getTime();
        SortingJob job2 = new SortingJob(startSort2, endSort2);
        job2.setLibrarian(librarian);
        job2.setSector(sectorD_F);

        // ===== 7. (Opcjonalnie) Przykładowa tabela: wypisz na koniec, co utworzyliśmy =====
        System.out.println("\n[DEBUG] Utworzono następujące obiekty przykładowe:");
        System.out.println(" - Sektory: " + sectorA_C + ", " + sectorD_F);
        System.out.println(" - Książki w sektorze A–C: " + book1 + ", " + book2);
        System.out.println(" - Książki w sektorze D–F: " + book3 + ", " + book4);
        System.out.println(" - Bibliotekarz: " + librarian);
        System.out.println(" - Manager: " + manager);
        System.out.println(" - Recepcjonistka: " + receptionist);
        System.out.println(" - Klient 1: " + client1 + ", karta: " + card1);
        System.out.println("   • Rezerwacja: " + reservation1);
        System.out.println("   • Mandat: " + fine1);
        System.out.println(" - Klient 2: " + client2 + ", karta: " + card2);
        System.out.println("   • Rezerwacja: " + reservation2);
        System.out.println(" - Zadania sortowania: " + job1 + " (A–C), " + job2 + " (D–F)\n");
    }
}
