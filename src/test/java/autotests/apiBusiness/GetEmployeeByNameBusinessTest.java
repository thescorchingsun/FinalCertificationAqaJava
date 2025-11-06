package autotests.apiBusiness;

import entities.EmployeeRequest;
import entities.EmployeeResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Epic("Business Logic")
@Story("Get employee By name")
public class GetEmployeeByNameBusinessTest extends BaseTest {

    private final String FIRSTNAME = "Margarita";
    private final String SURNAME = "Smirnova";
    private final String POSITION = "Scientist";
    private final String CITY = "Madrid";

    @BeforeEach
    public void setUp() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME);
        createdEmployeeId = employeeHelperDB.createEmployee(employeeRequest);
    }

    @Test
    @DisplayName("GET. Успешное получение сотрудника по имени")
    public void getEmployeeByNameTest() throws Exception {
        List<EmployeeResponse> employees = employeeHelperDB.getEmployeesByName(FIRSTNAME);

        step("Проверка, что в БД есть сотрудник с именем " + FIRSTNAME, step -> {
            assertFalse(employees.isEmpty());
        });
        step("Проверка, что у найденного сотрудника правильный город, должность и фамилия", step -> {
            boolean found = employees.stream().anyMatch(e ->
                    CITY.equals(e.getCity()) &&
                            POSITION.equals(e.getPosition()) &&
                            SURNAME.equals(e.getSurname())
            );
            assertTrue(found);
        });
    }

    @Test
    @DisplayName("GET. Получение по имени, которого нет в БД")
    public void getNonExistentNameTest() throws Exception {
        List<EmployeeResponse> employees = employeeHelperDB.getEmployeesByName("nameIsNotInDatabase123");

        step("Проверка, что отсутствующее имя в БД не записывается как 0", step -> {
            assertNotNull(employees);
        });
        step("Проверка, что отсутствующее имя в БД не записывается как пустое поле", step -> {
            assertTrue(employees.isEmpty());
        });
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (createdEmployeeId != -1) {
            employeeHelperDB.deleteEmployee(createdEmployeeId);
        }
    }
}
