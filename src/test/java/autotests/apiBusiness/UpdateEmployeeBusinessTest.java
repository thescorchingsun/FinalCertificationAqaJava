package autotests.apiBusiness;

import entities.EmployeeRequest;
import entities.EmployeeResponse;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class UpdateEmployeeBusinessTest extends BaseTest {

    private final String FIRSTNAME = "Rita";
    private final String SURNAME = "Ivanova";
    private final String POSITION = "Manager";
    private final String CITY = "Voronezh";

    private final String FIRSTNAME_2 = "Olga";
    private final String SURNAME_2 = "Bobrova";
    private final String POSITION_2 = "Developer";
    private final String CITY_2 = "Moscow";

    @BeforeEach
    @Step("Создание сотрудника")
    public void setUp() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME);
        createdEmployeeId = employeeHelperDB.createEmployee(employeeRequest);
    }

    @Test
    @DisplayName("PUT. Обновление всех полей сотрудника")
    public void updateAllFieldsTest() throws Exception {
        Allure.step("Обновление сотрудника по всем полям ", step -> {
            EmployeeRequest updatedRequest = new EmployeeRequest(CITY_2, FIRSTNAME_2, POSITION_2, SURNAME_2);
            employeeHelperDB.updateEmployee(createdEmployeeId, updatedRequest);
        });

        EmployeeResponse updatedEmployee = employeeHelperDB.getEmployee(createdEmployeeId);

        Allure.step("Проверка обновленного города", step -> {
            assertEquals(CITY_2, updatedEmployee.getCity());
        });
        Allure.step("Проверка обновленного имени", step -> {
            assertEquals(FIRSTNAME_2, updatedEmployee.getName());
        });
        Allure.step("Проверка обновленной должности", step -> {
            assertEquals(POSITION_2, updatedEmployee.getPosition());
        });
        Allure.step("Проверка обновленной фамилии", step -> {
            assertEquals(SURNAME_2, updatedEmployee.getSurname());
        });
    }

    @Test
    @DisplayName("PUT. Обновление фамилии и города")
    public void updateSurnameCityFieldsTest() throws Exception {
        Allure.step("Обновление сотрудника по фамилии и городу ", step -> {
            EmployeeRequest updatedRequest = new EmployeeRequest(CITY_2, FIRSTNAME, POSITION, SURNAME_2);
            employeeHelperDB.updateEmployee(createdEmployeeId, updatedRequest);
        });

        EmployeeResponse updatedEmployee = employeeHelperDB.getEmployee(createdEmployeeId);

        Allure.step("Проверка обновленного города", step -> {
            assertEquals(CITY_2, updatedEmployee.getCity());
        });
        Allure.step("Проверка, что имя у сотрудника не поменялось", step -> {
            assertEquals(FIRSTNAME, updatedEmployee.getName());
        });
        Allure.step("Проверка, что должность у сотрудника не поменялась", step -> {
            assertEquals(POSITION, updatedEmployee.getPosition());
        });
        Allure.step("Проверка обновленной фамилии", step -> {
            assertEquals(SURNAME_2, updatedEmployee.getSurname());
        });
    }

    @Test
    @DisplayName("PUT. Обновление несуществующего сотрудника")
    public void updateNonExistentEmployeeTest() throws Exception {
        Allure.step("Обновление несуществующего сотрудника по всем полям", step -> {
            int nonExistentId = 999999;
            EmployeeRequest updatedRequest = new EmployeeRequest(CITY_2, FIRSTNAME_2, POSITION_2, SURNAME_2);
            boolean result = employeeHelperDB.updateEmployee(nonExistentId, updatedRequest);

            assertFalse(result);
        });
    }

    @Test
    @DisplayName("PUT. Обновление полей сотрудника на пустые поля")
    public void updateWithEmptyFieldsTest() throws Exception {
        EmployeeRequest emptyRequest = new EmployeeRequest("", "", "", "");

        boolean result = employeeHelperDB.updateEmployee(createdEmployeeId, emptyRequest);
        EmployeeResponse employeeAfterUpdate = employeeHelperDB.getEmployee(createdEmployeeId);

        assertTrue(result);
        Allure.step("Проверка, что город поменялся на пустое значение", step -> {
            assertEquals("", employeeAfterUpdate.getCity());
        });
        Allure.step("Проверка, что имя поменялось на пустое значение", step -> {
            assertEquals("", employeeAfterUpdate.getName());
        });
        Allure.step("Проверка, что должность поменялась на пустое значение", step -> {
            assertEquals("", employeeAfterUpdate.getPosition());
        });
        Allure.step("Проверка, что фамилия поменялась на пустое значение", step -> {
            assertEquals("", employeeAfterUpdate.getSurname());
        });
    }

    @Test
    @DisplayName("PUT. Обновление с теми же самыми данными (идемпотентность)")
    public void updateWithSameDataTest() throws Exception {
        EmployeeRequest originalRequest = new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME);

        boolean result = employeeHelperDB.updateEmployee(createdEmployeeId, originalRequest);
        EmployeeResponse employeeAfterUpdate = employeeHelperDB.getEmployee(createdEmployeeId);

        assertTrue(result);
        Allure.step("Проверка, что город не поменялся", step -> {
            assertEquals(CITY, employeeAfterUpdate.getCity());
        });
        Allure.step("Проверка, что имя не поменялось", step -> {
            assertEquals(FIRSTNAME, employeeAfterUpdate.getName());
        });
        Allure.step("Проверка, что должность не поменялась", step -> {
            assertEquals(POSITION, employeeAfterUpdate.getPosition());
        });
        Allure.step("Проверка, что фамилия не поменялась", step -> {
            assertEquals(SURNAME, employeeAfterUpdate.getSurname());
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
