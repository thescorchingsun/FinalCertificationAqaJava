package autotests.apiBusiness;

import entities.EmployeeRequest;
import entities.EmployeeResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Epic("Business Logic")
@Story("Get employee By ID")
public class GetEmployeeByIdBusinessTest extends BaseTest {

    private final String FIRSTNAME = "Margarita";
    private final String SURNAME = "Smirnova";
    private final String POSITION = "Scientist";
    private final String CITY = "Madrid";

    @BeforeEach
    public void setUp() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest(CITY, FIRSTNAME,
                POSITION, SURNAME);
        createdEmployeeId = employeeHelperDB.createEmployee(employeeRequest);
    }

    @Test
    @DisplayName("GET. Успешное получение сотрудника по ID")
    public void getEmployeeByIdSuccessTest() throws Exception {
        EmployeeResponse employee = employeeHelperDB.getEmployee(createdEmployeeId);

        step("Проверка по id, что у сотрудник есть в БД.", step -> {
            assertEquals(createdEmployeeId, employee.getId());
        });
    }

    @Test
    @DisplayName("GET. Получение несуществующего сотрудника по ID")
    public void getNonExistentEmployeeTest() throws Exception {
        int nonExistentId = 53534535;
        EmployeeResponse employee = employeeHelperDB.getEmployee(nonExistentId);

        step("Проверка по id = 0, что сотрудника нет в БД", step -> {
            assertEquals(0, employee.getId());
        });
    }

    @Test
    @DisplayName("GET. Получение сотрудника после удаления")
    public void getDeletedEmployeeTest() throws Exception {
        employeeHelperDB.deleteEmployee(createdEmployeeId);
        EmployeeResponse employee = employeeHelperDB.getEmployee(createdEmployeeId);

        step("Проверка по id = 0, что сотрудника нет в БД", step -> {
            assertEquals(0, employee.getId());
        });
    }

    @Test
    @DisplayName("GET. Получение с некорректным ID")
    public void getWithInvalidIdTest() throws Exception {
        EmployeeResponse employee = employeeHelperDB.getEmployee(-1);

        step("Проверка по id = 0, что сотрудника нет в БД", step -> {
            assertEquals(0, employee.getId());
        });
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (createdEmployeeId != -1) {
            employeeHelperDB.deleteEmployee(createdEmployeeId);
        }
    }
}
