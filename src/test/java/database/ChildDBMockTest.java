package database;

import dao.ChildDB;
import model.Child;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChildDBMockTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private ChildDB childDB;

    @BeforeEach
    void setUp() {
        childDB = new ChildDB(connection);
    }



    @Test
    void addChildShouldReturnGeneratedId() throws SQLException {

        Child child = new Child(
                null,
                "John",
                "Wilson",
                LocalDate.of(2015, 2, 1)
        );

        when(connection.prepareStatement(
                anyString(),
                eq(Statement.RETURN_GENERATED_KEYS)
        )).thenReturn(preparedStatement);

        when(preparedStatement.executeUpdate()).thenReturn(1);

        when(preparedStatement.getGeneratedKeys())
                .thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(15L);

        Child result = childDB.addChild(child);

        assertEquals(15L, result.id());
    }



    @Test
    void addChildShouldThrowExceptionWhenIdWasNotGenerated() throws SQLException {

        Child child = new Child(
                null,
                "John",
                "Wilson",
                LocalDate.of(2015, 2, 1)
        );

        when(connection.prepareStatement(
                anyString(),
                eq(Statement.RETURN_GENERATED_KEYS)
        )).thenReturn(preparedStatement);

        when(preparedStatement.executeUpdate()).thenReturn(1);

        when(preparedStatement.getGeneratedKeys())
                .thenReturn(resultSet);

        when(resultSet.next()).thenReturn(false);

        assertThrows(
                SQLException.class,
                () -> childDB.addChild(child)
        );
    }



    @Test
    void updateChildShouldReturnTrue() throws SQLException {

        Child child = new Child(
                10L,
                "John",
                "Wilson",
                LocalDate.of(2015, 2, 1)
        );

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeUpdate())
                .thenReturn(1);

        boolean result = childDB.updateChild(child);

        assertTrue(result);
    }



    @Test
    void deleteChildShouldReturnFalseWhenChildDoesNotExist() throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeUpdate())
                .thenReturn(0);

        boolean result = childDB.deleteChild(10L);

        assertFalse(result);
    }



    @Test
    void findChildrenShouldMapResultSet() throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenReturn(preparedStatement);

        when(preparedStatement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true, false);

        when(resultSet.getLong("id"))
                .thenReturn(10L);

        when(resultSet.getString("first_name"))
                .thenReturn("John");

        when(resultSet.getString("last_name"))
                .thenReturn("Wilson");

        when(resultSet.getDate("birth_date"))
                .thenReturn(java.sql.Date.valueOf("2015-02-01"));

        List<Child> result =
                childDB.findChildrenWithMinimumAge(5);

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).id());
        assertEquals("John", result.get(0).firstName());
        assertEquals(
                LocalDate.of(2015, 2, 1),
                result.get(0).birthDate()
        );
    }

    @Test
    void deleteChildWithNullIdShouldFail() throws SQLException {

        assertThrows(
                IllegalArgumentException.class,
                () -> childDB.deleteChild(null)
        );

        verify(connection, never()).prepareStatement(anyString());
    }

}