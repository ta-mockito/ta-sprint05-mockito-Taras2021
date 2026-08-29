package database;

import dao.ChildDAO;
import model.Child;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.ChildService;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)

public class ChildServiceMockTest {

    @Mock
    private ChildDAO childDAO;
    @InjectMocks
    private ChildService childService;

    @Test
    @DisplayName("addChild: adding new child")
    void addNewChild() throws IllegalArgumentException, SQLException {

        Child input = new Child(15L, "John", "Wilson", LocalDate.of(1958, 2, 1));

        when(childDAO.addChild(input)).thenReturn(input);
        Child result = childService.addChild(input);

        assertEquals(input, result);
        verify(childDAO).addChild(input);
    }

    @Test
    @DisplayName("updateChild: updating child")
    void tryingUpdateChild() throws IllegalArgumentException, SQLException {
        Child updating = new Child(8L, "Some", "Name", LocalDate.of(2008, 1, 12));
        when(childDAO.updateChild(updating)).thenReturn(true);

        boolean updateDone = childService.updateChild(updating);

        assertTrue(updateDone);

        verify(childDAO).updateChild(updating);


    }

    @Test
    @DisplayName("deleteChild: deleting")
    void deletingExistingChild() throws IllegalArgumentException, SQLException {

        when(childDAO.deleteChild(232L)).thenReturn(true);


        boolean deleted = childService.deleteChild(232L);


        assertTrue(deleted);

        verify(childDAO).deleteChild(232L);

    }

    @Test
    @DisplayName("findByAge: find older children")
    void findOldchildren() throws IllegalArgumentException, SQLException {

        List<Child> children = List.of(
                new Child(5L, "Sone", "Name", LocalDate.of(2016, 8, 27))

        );

        when(childDAO.findChildrenWithMinimumAge(10)).thenReturn(children);

        List<Child> result = childService.findOlderChildren(10);

        assertEquals(children, result);

        verify(childDAO).findChildrenWithMinimumAge(10);


    }

    @Test
    @DisplayName("findByDate: find children without birth date")
    void findChildrenWithoutBirthDate() throws IllegalArgumentException, SQLException {

        List<Child> children = List.of(

                new Child(8L, "Poor", "Baby", null)


        );

        when(childDAO.findChildrenWithoutBirthDate()).thenReturn(children);

        List<Child> noAge = childService.findChildrenMissingBirthDate();

        assertEquals(children,noAge);

        verify(childDAO).findChildrenWithoutBirthDate();


    }


}
