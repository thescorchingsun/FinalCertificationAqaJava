package helper;

import entities.EmployeeRequest;
import entities.EmployeeResponse;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class EmployeeHelperDB extends AbstractHelper {

    private static final Logger log = LoggerFactory.getLogger(EmployeeHelperDB.class);

    public EmployeeHelperDB() throws SQLException, IOException {
        connection = getConnection();
    }

    @Step("Создание сотрудника в БД: {employee}")
    public int createEmployee(EmployeeRequest employee) throws SQLException {
        String INSERT_EMPLOYEE = "INSERT INTO employee(\"name\",\"surname\",\"city\",\"position\") values(?,?,?,?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getSurname());
            preparedStatement.setString(3, employee.getCity());
            preparedStatement.setString(4, employee.getPosition());
            Allure.addAttachment("SQL-запрос", INSERT_EMPLOYEE);
            Allure.addAttachment("Параметры", employee.toString());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                Allure.addAttachment("Созданный ID", String.valueOf(id));
                return id;
            } else {
                throw new SQLException("Не удалось получить ID нового сотрудника");
            }
        } catch (SQLException e) {
            log.error("Ошибка при создании сотрудника: {}", e.getMessage(), e);
            Allure.addAttachment("Ошибка SQL", e.toString());
            throw e;
        }
    }


    @Step("Получение сотрудника из БД по id={id}")
    public EmployeeResponse getEmployee(int id) throws Exception {
        String SELECT_NAME_SURNAME = "SELECT * FROM employee where id = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NAME_SURNAME)) {
            preparedStatement.setInt(1, id);
            Allure.addAttachment("SQL-запрос", SELECT_NAME_SURNAME);
            Allure.addAttachment("Параметр id", String.valueOf(id));

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                EmployeeResponse employee = new EmployeeResponse(
                        resultSet.getString("city"),
                        resultSet.getString("name"),
                        resultSet.getString("position"),
                        resultSet.getString("surname"),
                        resultSet.getInt("id")
                );
                Allure.addAttachment("Результат", employee.toString());
                return employee;
            } else {
                Allure.addAttachment("Результат", "Сотрудник не найден");
                return new EmployeeResponse();
            }
        } catch (Exception e) {
            log.error("Ошибка при получении сотрудника: {}", e.getMessage(), e);
            Allure.addAttachment("Ошибка SQL", e.toString());
            throw e;
        }
    }

    @Step("Обновление сотрудника id={id}")
    public boolean updateEmployee(int id, EmployeeRequest employee) throws SQLException {
        String UPDATE_EMPLOYEE = "UPDATE employee SET name = ?, surname = ?, city = ?, position = ? WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EMPLOYEE)) {
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getSurname());
            preparedStatement.setString(3, employee.getCity());
            preparedStatement.setString(4, employee.getPosition());
            preparedStatement.setInt(5, id);
            Allure.addAttachment("SQL-запрос", UPDATE_EMPLOYEE);
            Allure.addAttachment("Параметры", employee.toString());

            int affectedRows = preparedStatement.executeUpdate();
            Allure.addAttachment("Количество изменённых строк", String.valueOf(affectedRows));
            return affectedRows > 0;
        } catch (SQLException e) {
            log.error("Ошибка при обновлении сотрудника: {}", e.getMessage(), e);
            Allure.addAttachment("Ошибка SQL", e.toString());
            throw e;
        }
    }

    @Step("Удаление сотрудника id={id}")
    public void deleteEmployee(int id) throws SQLException {
        String DELETE_EMPLOYEE = "DELETE FROM employee WHERE id = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EMPLOYEE)) {
            preparedStatement.setInt(1, id);
            Allure.addAttachment("SQL-запрос", DELETE_EMPLOYEE);
            Allure.addAttachment("ID", String.valueOf(id));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Ошибка при удалении сотрудника id={}: {}", id, e.getMessage(), e);
            Allure.addAttachment("Ошибка SQL", e.toString());
            throw e;
        }
    }

    @Step("Поиск сотрудников по имени {name}")
    public List<EmployeeResponse> getEmployeesByName(String name) throws Exception {
        String SELECT_BY_NAME = "SELECT * FROM employee WHERE name = ?;";
        Allure.addAttachment("SQL-запрос", SELECT_BY_NAME);
        Allure.addAttachment("Параметр name", name);

        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_NAME);
        preparedStatement.setString(1, name);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<EmployeeResponse> employees = new ArrayList<>();

        while (resultSet.next()) {
            employees.add(new EmployeeResponse(
                    resultSet.getString("city"),
                    resultSet.getString("name"),
                    resultSet.getString("position"),
                    resultSet.getString("surname"),
                    resultSet.getInt("id")
            ));
        }
        Allure.addAttachment("Количество найденных сотрудников", String.valueOf(employees.size()));
        return employees;
    }

    @Step("Получение всех сотрудников")
    public List<EmployeeResponse> getAllEmployees() throws Exception {
        String sql = "SELECT * FROM employee;";
        Allure.addAttachment("SQL-запрос", sql);

        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        List<EmployeeResponse> employees = new ArrayList<>();
        while (resultSet.next()) {
            employees.add(new EmployeeResponse(
                    resultSet.getString("city"),
                    resultSet.getString("name"),
                    resultSet.getString("position"),
                    resultSet.getString("surname"),
                    resultSet.getInt("id")
            ));
        }
        Allure.addAttachment("Количество сотрудников", String.valueOf(employees.size()));
        return employees;
    }
}
