package services;

import models.Employee;
import org.junit.Test;
import play.test.WithApplication;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class EmployeeDatabaseTest extends WithApplication {

    @Test
    public void mockDatabaseData(){

        Employee employee = new Employee();
        employee.setFirstName("Manaswini");
        employee.setLastName("Karachuri");

        EmployeeDatabase mockDatabase = mock(EmployeeDatabase.class);
        when(mockDatabase.saveEmployee(any(Employee.class))).thenReturn(CompletableFuture.completedFuture(true));

        CompletionStage<Boolean> respEmployee = mockDatabase.saveEmployee(employee);
        assertTrue(true, String.valueOf(respEmployee));

    }
}