package helper;

import entities.EmployeeRequest;
import entities.EmployeeResponse;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeHelperDB extends AbstractHelper {

    public EmployeeHelperDB() throws SQLException, IOException {
        connection = getConnection();
    }

    public int createEmployee(EmployeeRequest employee) throws SQLException {
        String INSERT_EMPLOYEE = "INSERT INTO employee(\"name\",\"surname\",\"city\",\"position\") values(?,?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, employee.getName());
        preparedStatement.setString(2, employee.getSurname());
        preparedStatement.setString(3, employee.getCity());
        preparedStatement.setString(4, employee.getPosition());
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        } else {
            return -1;
        }
    }

    public EmployeeResponse getEmployee(int id) throws Exception {
        String SELECT_NAME_SURNAME = "SELECT * FROM employee where id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NAME_SURNAME);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new EmployeeResponse(
                    resultSet.getString("city"),
                    resultSet.getString("name"),
                    resultSet.getString("position"),
                    resultSet.getString("surname"),
                    resultSet.getInt("id")
            );
        } else {
            return new EmployeeResponse();
        }
    }

    public boolean updateEmployee(int id, EmployeeRequest employee) throws SQLException {
        String UPDATE_EMPLOYEE = "UPDATE employee SET name = ?, surname = ?, city = ?, position = ? WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EMPLOYEE);
        preparedStatement.setString(1, employee.getName());
        preparedStatement.setString(2, employee.getSurname());
        preparedStatement.setString(3, employee.getCity());
        preparedStatement.setString(4, employee.getPosition());
        preparedStatement.setInt(5, id);

        int affectedRows = preparedStatement.executeUpdate();
        return affectedRows > 0;
    }

    public void deleteEmployee(int id) throws SQLException {
        String DELETE_EMPLOYEE = "DELETE FROM employee WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EMPLOYEE);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public List<EmployeeResponse> getEmployeesByName(String name) throws Exception {
        String SELECT_BY_NAME = "SELECT * FROM employee WHERE name = ?;";
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
        return employees;
    }

    public List<EmployeeResponse> getAllEmployees() throws Exception {
        String query = "SELECT * FROM employee;";
        PreparedStatement statement = connection.prepareStatement(query);
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
        return employees;
    }


}
