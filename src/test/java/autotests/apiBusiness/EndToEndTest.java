package autotests.apiBusiness;

import entities.EmployeeRequest;
import entities.EmployeeResponse;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class EndToEndTest extends BaseTest {

    private final String FIRSTNAME = "Elizabeth";
    private final String SURNAME = "Addington";
    private final String POSITION = "Engineer";
    private final String CITY = "Montreal";
    private final String CITY_UPDATE = "Moscow";
    private final String POSITION_UPDATE = "Team Lead";

    @Test
    @DisplayName("Создание, редактирование, удаление сотрудника")
    public void employeeEndToEndTest() throws Exception {
        EmployeeRequest createRequest = new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME);
        employeeId = employeeHelperDB.createEmployee(createRequest);

        EmployeeResponse createdEmployee = employeeHelperDB.getEmployee(employeeId);
        assertEquals(CITY, createdEmployee.getCity());
        assertEquals(FIRSTNAME, createdEmployee.getName());
        assertEquals(POSITION, createdEmployee.getPosition());
        assertEquals(SURNAME, createdEmployee.getSurname());

        EmployeeRequest updateRequest = new EmployeeRequest(CITY_UPDATE, FIRSTNAME, POSITION_UPDATE, SURNAME);
        boolean updateResult = employeeHelperDB.updateEmployee(employeeId, updateRequest);
        assertTrue(updateResult);

        EmployeeResponse updatedEmployee = employeeHelperDB.getEmployee(employeeId);

        step("Проверка, что у сотрудника поменялся город", step -> {
            assertEquals(CITY_UPDATE, updatedEmployee.getCity());
        });
        step("Проверка, что у сотрудника поменялся должность", step -> {
            assertEquals(POSITION_UPDATE, updatedEmployee.getPosition());
        });

        employeeHelperDB.deleteEmployee(employeeId);
        EmployeeResponse deletedEmployee = employeeHelperDB.getEmployee(employeeId);

        step("Проверка по id, что сотрудник удален из БД", step -> {
            assertEquals(0, deletedEmployee.getId());
        });
    }
}
