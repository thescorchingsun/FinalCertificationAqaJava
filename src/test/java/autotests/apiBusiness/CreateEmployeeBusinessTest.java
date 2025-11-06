package autotests.apiBusiness;

import entities.EmployeeRequest;
import entities.EmployeeResponse;
import helper.EmployeeHelperDB;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Epic("Business Logic")
@Story("Create Employee")
public class CreateEmployeeBusinessTest extends BaseTest {

    private final String FIRSTNAME = "Olga";
    private final String SURNAME = "Lebedev";
    private final String POSITION = "Engineer";
    private final String CITY = "Murmansk";

    private final String FIRSTNAME_2 = "Ivan";
    private final String SURNAME_2 = "Sverchkov";
    private final String POSITION_2 = "Dispatcher";
    private final String CITY_2 = "Petrozavodsk";

    @BeforeEach
    public void setUp() throws SQLException, IOException {
        employeeHelperDB = new EmployeeHelperDB();
        createdEmployeeId = -1;
    }

    @Test
    @DisplayName("POST. Создание сотрудника проверка по id")
    public void createEmployeeTest() throws Exception {
        EmployeeRequest request = (new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME));
        createdEmployeeId = employeeHelperDB.createEmployee(request);

        step("Проверка по id, что сотрудник создался", step -> {
            EmployeeResponse employee = employeeHelperDB.getEmployee(createdEmployeeId);
            assertEquals(createdEmployeeId, employee.getId());
        });
    }

    @Test
    @DisplayName("POST. Создание сотрудника c одинаковыми данными")
    public void createDuplicateEmployeeTest() throws Exception {
        EmployeeRequest request = new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME);
        int firstEmployeeId = employeeHelperDB.createEmployee(request);
        int secondEmployeeId = employeeHelperDB.createEmployee(request);

        EmployeeResponse employee = employeeHelperDB.getEmployee(secondEmployeeId);

        step("Проверка по id, что второй сотрудник создался", step -> {
            assertEquals(secondEmployeeId, employee.getId());
        });
        employeeHelperDB.deleteEmployee(firstEmployeeId);
        createdEmployeeId = secondEmployeeId;
    }

    @Test
    @DisplayName("POST. Создание сотрудника без города")
    public void createEmployeeWithOutCityTest() throws Exception {
        EmployeeRequest request = (new EmployeeRequest("", FIRSTNAME, POSITION, SURNAME));
        createdEmployeeId = employeeHelperDB.createEmployee(request);
        EmployeeResponse employee = employeeHelperDB.getEmployee(createdEmployeeId);

        step("Проверка по id, что сотрудник создался", step -> {
            assertEquals(createdEmployeeId, employee.getId());
        });
        step("Проверка, что у сотрудника нет города", step -> {
            assertEquals("", employee.getCity());
        });
    }

    @Test
    @DisplayName("POST. Создание сотрудника без имени")
    public void createEmployeeWithOutNameTest() throws Exception {
        EmployeeRequest request = (new EmployeeRequest(CITY, "", POSITION, SURNAME));
        createdEmployeeId = employeeHelperDB.createEmployee(request);
        EmployeeResponse employee = employeeHelperDB.getEmployee(createdEmployeeId);

        step("Проверка по id, что сотрудник создался", step -> {
            assertEquals(createdEmployeeId, employee.getId());
        });
        step("Проверка, что у сотрудника нет имени", step -> {
            assertEquals("", employee.getName());
        });
    }

    @Test
    @DisplayName("POST. Создание сотрудника без фамилии")
    public void createEmployeeWithOutSurnameTest() throws Exception {
        EmployeeRequest request = (new EmployeeRequest(CITY_2, FIRSTNAME_2, POSITION_2, ""));
        createdEmployeeId = employeeHelperDB.createEmployee(request);
        EmployeeResponse employee = employeeHelperDB.getEmployee(createdEmployeeId);

        step("Проверка по id, что сотрудник создался.", step -> {
            assertEquals(createdEmployeeId, employee.getId());
        });
        step("Проверка, что у сотрудника нет фамилии.", step -> {
            assertEquals("", employee.getSurname());
        });
    }

    @Test
    @DisplayName("POST. Создание сотрудника без должности")
    public void createEmployeeWithoutPositionTest() throws Exception {
        EmployeeRequest request = (new EmployeeRequest(CITY_2, FIRSTNAME_2, "", SURNAME_2));
        createdEmployeeId = employeeHelperDB.createEmployee(request);
        EmployeeResponse employee = employeeHelperDB.getEmployee(createdEmployeeId);

        step("Проверка по id, что сотрудник создался.", step -> {
            assertEquals(createdEmployeeId, employee.getId());
        });
        step("Проверка, что у сотрудника нет должности.", step -> {
            assertEquals("", employee.getPosition());
        });
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (createdEmployeeId > 0) {
            employeeHelperDB.deleteEmployee(createdEmployeeId);
        }
    }
}
