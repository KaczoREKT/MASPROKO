package Test;

public class TestEntity {
    public static void runTests() throws Exception {
        BookControllerTest.runTests();
        System.out.println('\n');
        ClientControllerTest.runTests();
        System.out.println('\n');
        EmployeeControllerTest.runTests();
        System.out.println('\n');
        FineControllerTest.runTests();
        System.out.println('\n');
        LibrarianControllerTest.runTests();
        System.out.println('\n');
        ReservationControllerTest.runTests();
        System.out.println('\n');
        SectorControllerTest.runTests();
        System.out.println('\n');
        SortingJobControllerTest.runTests();
        System.out.println('\n');
    }

    public static void main(String[] args) throws Exception {
        TestEntity.runTests();
    }
}
