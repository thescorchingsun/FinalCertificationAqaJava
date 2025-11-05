package autotests.apiBusiness;

import entities.EmployeeRequest;
import entities.EmployeeResponse;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;


import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
public class GetEmployeesBusinessTest extends BaseTest {

    private final String FIRSTNAME = "Alex";
    private final String SURNAME = "Novikov";
    private final String POSITION = "Architect";
    private final String CITY = "Berlin";

    @BeforeEach
    @Step("Создание сотрудника")
    public void setUp() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME);
        createdEmployeeId = employeeHelperDB.createEmployee(employeeRequest);
    }

    @Test
    @DisplayName("GET. Получение списка всех сотрудников — не пустой список")
    public void getAllEmployeesNotEmptyTest() throws Exception {
        List<EmployeeResponse> employees = employeeHelperDB.getAllEmployees();

        Allure.step("Проверка, что список не приходит как 0", step -> {
            assertNotNull(employees);
        });
        Allure.step("Проверка, что список не приходит пустым", step -> {
            assertFalse(employees.isEmpty());
        });
    }

    @Test
    @DisplayName("GET. Получение всех сотрудников — содержит созданного сотрудника")
    public void getAllEmployeesContainsCreatedTest() throws Exception {
        List<EmployeeResponse> employees = employeeHelperDB.getAllEmployees();

        Allure.step("Проверка, что в полученном списке есть созданный сотрудник", step -> {
            boolean found = employees.stream().anyMatch(e ->
                    e.getId() == createdEmployeeId &&
                            FIRSTNAME.equals(e.getName()) &&
                            SURNAME.equals(e.getSurname()) &&
                            CITY.equals(e.getCity()) &&
                            POSITION.equals(e.getPosition())

            );
            assertTrue(found);
        });
    }

    @Test
    @DisplayName("GET. Удалённый сотрудник отсутствует в списке")
    public void getAllEmployeesAfterDeletionTest() throws Exception {
        Allure.step("Удаление сотрудника из БД", step -> {
            employeeHelperDB.deleteEmployee(createdEmployeeId);
        });

        List<EmployeeResponse> employees = employeeHelperDB.getAllEmployees();

        Allure.step("Проверка, что в полученном списке нет удаленного сотрудника", step -> {
            boolean found = employees.stream().anyMatch(e -> e.getId() == createdEmployeeId);
            assertFalse(found);
        });
    }

    @AfterEach
    @Step("Удаление сотрудника после теста")
    public void tearDown() throws SQLException {
        if (createdEmployeeId != -1) {
            employeeHelperDB.deleteEmployee(createdEmployeeId);
        }

    }

}
