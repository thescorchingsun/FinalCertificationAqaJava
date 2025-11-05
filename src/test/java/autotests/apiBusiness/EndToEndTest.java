package autotests.apiBusiness;

import entities.EmployeeRequest;
import entities.EmployeeResponse;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        Allure.step("Создание сотрудника.", step -> {
            EmployeeRequest createRequest = new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME);
            employeeId = employeeHelperDB.createEmployee(createRequest);

            EmployeeResponse createdEmployee = employeeHelperDB.getEmployee(employeeId);
            assertEquals(CITY, createdEmployee.getCity());
            assertEquals(FIRSTNAME, createdEmployee.getName());
            assertEquals(POSITION, createdEmployee.getPosition());
            assertEquals(SURNAME, createdEmployee.getSurname());
        });

        Allure.step("Обновление сотрудника", step -> {
            EmployeeRequest updateRequest = new EmployeeRequest(CITY_UPDATE, FIRSTNAME, POSITION_UPDATE, SURNAME);
            boolean updateResult = employeeHelperDB.updateEmployee(employeeId, updateRequest);
            assertTrue(updateResult);
        });

        EmployeeResponse updatedEmployee = employeeHelperDB.getEmployee(employeeId);

        Allure.step("Проверка, что у сотрудника поменялся город", step -> {
            assertEquals(CITY_UPDATE, updatedEmployee.getCity());
        });
        Allure.step("Проверка, что у сотрудника поменялся должность", step -> {
            assertEquals(POSITION_UPDATE, updatedEmployee.getPosition());
        });

        Allure.step("Удаление сотрудника", step -> {
            employeeHelperDB.deleteEmployee(employeeId);
        });

        Allure.step("Проверка по id = 0, что сотрудник удален из БД", step -> {
            EmployeeResponse deletedEmployee = employeeHelperDB.getEmployee(employeeId);
            assertEquals(0, deletedEmployee.getId());
        });
    }
}
