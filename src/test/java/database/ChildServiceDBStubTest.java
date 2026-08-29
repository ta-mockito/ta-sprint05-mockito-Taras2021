package database;

import dao.ChildDAO;
import model.Child;
import service.CategoryService;
import service.ChildService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Stub demo for child")
public class ChildServiceDBStubTest {

    private ChildDAO childDAO;
    private ChildService childService;

    @BeforeEach
    void setUp() {

        childDAO = new InmemoryChildDAOStub();
        childService = new ChildService(childDAO);

    }

    @Test
    @DisplayName("Stub: creating child")
    void creatingChildFromStub() {

        Child newChild = new Child(null, "Omar", "Jackson", LocalDate.of(2020, 8, 21));

        Child created = childService.addChild(newChild);

        assertEquals(4L, created.id());


    }

    @Test
    @DisplayName("Stub: updating child")
    void updatingChildFromStub() {

        Child update = new Child(3L, "Marito", "Mohito", LocalDate.of(2007, 8, 10));

        boolean updateDone = childService.updateChild(update);

        assertTrue(updateDone);

    }

    @Test
    @DisplayName("Stub: deleting child")
    void deletingChildFromStub() {

        boolean deletingChild = childService.deleteChild(2L);

        assertTrue(deletingChild);

    }

    @Test
    @DisplayName("Stub: find minimum age child ")
    void findChildrenWithMinimumAgeFromStub() {
        List<Child> result = childService.findOlderChildren(20);
        assertEquals(2, result.size());


    }

    @Test
    @DisplayName("Stub: find child with unexisting birth date")

    void findChildrenWithoutBirthDateFromStub(){

        List<Child> result = childService.findChildrenMissingBirthDate();
        assertEquals(1,result.size());

    }


    static class InmemoryChildDAOStub implements ChildDAO {
        private final Map<Long, Child> childMap = new HashMap<>();

        InmemoryChildDAOStub() {

            childMap.put(1L, new Child(1L, "One", "User", null));
            childMap.put(2L, new Child(2L, "Some", "Someone", LocalDate.of(2005, 11, 12)));
            childMap.put(3L, new Child(3L, "Marko", "Polo", LocalDate.of(1579, 12, 13)));

        }


        @Override
        public Child addChild(Child child) throws SQLException {

            Long nextId = childMap.keySet().stream().max(Long::compareTo).orElse(0L) + 1;
            Child created = new Child(nextId, child.firstName(), child.lastName(), child.birthDate());
            childMap.put(nextId, created);

            return created;
        }

        @Override
        public boolean updateChild(Child child) throws SQLException {

            if (!childMap.containsKey(child.id())) {
                return false;

            }
            childMap.put(child.id(), child);
            return true;

        }


        @Override
        public boolean deleteChild(Long id) throws SQLException {
            return childMap.remove(id) != null;
        }

        @Override
        public List<Child> findChildrenWithMinimumAge(int age) throws SQLException {
            List<Child> result = new ArrayList<>();

            LocalDate cutoffDate = LocalDate.now().minusYears(age);

            for (Child child : childMap.values()) {
                if (child.birthDate() != null && !child.birthDate().isAfter(cutoffDate)) {
                    result.add(child);
                }
            }

            return result;
        }


        @Override
        public List<Child> findChildrenWithoutBirthDate() throws SQLException {

            List<Child> result = new ArrayList<>();

            for (Child child : childMap.values()) {
                if (child.birthDate() == null) {
                    result.add(child);
                }
            }

            return result;
        }
    }
}



