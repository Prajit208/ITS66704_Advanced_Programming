
import manager.PersistenceManager;
import model.*;
import java.util.List;

public class ResourceTest {
    public static void main(String[] args) {
        PersistenceManager pm = new PersistenceManager();

        List<Resource> resources = List.of(
                new StudyRoom("R001", "Library 2F", 4, true, List.of("Max 10000")),
                new Lab("R002", "CS Building B12", 20, false, List.of("Requires staff approval"), List.of("PCs", "Projector")),
                new EquipmentResource("R003", "Media Lab", 1, true, List.of("Return within 24hr")),
                new EventSpace("R004", "Main Hall", 100, true, List.of("Book 1 week ahead")),
                new EventSpace("R0099", "Main Hall", 100, true, List.of("Book 99 week ahead"))


        );

        pm.saveResources(resources);
        System.out.println("Seeded " + resources.size() + " resources to resources.dat");
    }
}