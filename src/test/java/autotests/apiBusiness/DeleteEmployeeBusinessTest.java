package autotests.apiBusiness;

import entities.EmployeeRequest;
import entities.EmployeeResponse;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class DeleteEmployeeBusinessTest extends BaseTest {

    private final String FIRSTNAME = "Dmitry";
    private final String SURNAME = "Lebedev";
    private final String POSITION = "Analyst";
    private final String CITY = "Paris";

    @BeforeEach
    @Step("Создание сотрудника")
    public void setUp() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME);
        createdEmployeeId = employeeHelperDB.createEmployee(employeeRequest);
    }

    @Test
    @DisplayName("DELETE. Успешное удаление сотрудника")
    public void deleteEmployeeSuccessfullyTest() throws Exception {
        employeeHelperDB.deleteEmployee(createdEmployeeId);
        EmployeeResponse employee = employeeHelperDB.getEmployee(createdEmployeeId);

        Allure.step("Проверка по id, что сотрудник удален.", step -> {
            assertEquals(0, employee.getId());
        });
    }

    @Test
    @DisplayName("DELETE. Повторное удаление сотрудника")
    public void deleteEmployeeTwiceTest() throws Exception {
        Allure.step("Первое удаление по id.", step -> {
            employeeHelperDB.deleteEmployee(createdEmployeeId);
        });
        Allure.step("Повторное удаление по id", step -> {
            employeeHelperDB.deleteEmployee(createdEmployeeId);
        });

        Allure.step("Проверка по id = 0, что сотрудник удален", step -> {
            EmployeeResponse employee = employeeHelperDB.getEmployee(createdEmployeeId);
            assertEquals(0, employee.getId());
        });
    }

    @Test
    @DisplayName("DELETE. Удаление несуществующего сотрудника")
    public void deleteNonExistentEmployeeTest() throws Exception {

        int nonExistentId = 474747474;
        employeeHelperDB.deleteEmployee(nonExistentId);

        Allure.step("Проверка по id = 0, что сотрудника нет в БД", step -> {
            EmployeeResponse employee = employeeHelperDB.getEmployee(nonExistentId);
        assertEquals(0, employee.getId());
        });
    }

    @AfterEach
    @Step("Удаление сотрудника после теста")
    public void tearDown() throws SQLException {
        if (createdEmployeeId != -1) {
            employeeHelperDB.deleteEmployee(createdEmployeeId); // Безопасно, т.к. delete идемпотентен
        }
    }
}
