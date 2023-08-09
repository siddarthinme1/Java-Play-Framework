package services;

import io.swagger.annotations.Api;
import models.Employee;
import play.db.Database;
import models.UserAccount;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class EmployeeDatabase {

    private final Database db;

    @Inject
    public EmployeeDatabase(Database db) {
        this.db = db;
    }

    public CompletionStage<List<Employee>> getEmployees() {
        String sql = "EXEC getAllEmployees";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery();
                List<Employee> employees = new ArrayList<>();
                while (rs.next()) {
                    Employee employee = createEmployeeFromResultSet(rs);
                    employees.add(employee);
                }
                return employees;
            } catch (SQLException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        });
    }

    public CompletionStage<Boolean> saveEmployee(Employee employee) {
        String sql = "EXEC InsertEmployeeWithEmergencyInfo\n" +
                "@Gender = ?,@FirstName = ?,@LastName = ?,@Phone = ?,@Mail = ?,@Birthday =? ,@BloodId = ?,@StreetAddress = ?," +
                "@StreetAddress2 =? ,@City = ?,@State = ?,@Country = ?,@Zipcode = ?,@Status = 'active',@GenderX = ?,@FirstNameX = ?," +
                "@LastNameX = ?,@RelationX = ?,@PhoneX =?,@StreetAddressX = ?,@StreetAddress2X = ?,@CityX = ?,@StateX = ?,@CountryX = ?,@ZipcodeX = ?;";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, employee.getGender());
                statement.setString(2, employee.getFirstName());
                statement.setString(3, employee.getLastName());
                statement.setString(4, employee.getPhone());
                statement.setString(5, employee.getMail());
                statement.setString(6, employee.getBirthday());
                statement.setInt(7, employee.getBloodId());
                statement.setString(8, employee.getStreetAddress());
                statement.setString(9, employee.getStreetAddress2());
                statement.setString(10, employee.getCity());
                statement.setString(11, employee.getState());
                statement.setString(12, employee.getCountry());
                statement.setString(13, employee.getZipcode());
                statement.setString(14, employee.getGenderx());
                statement.setString(15, employee.getFirstNamex());
                statement.setString(16, employee.getLastNamex());
                statement.setInt(17, employee.getRelationxId());
                statement.setString(18, employee.getPhonex());
                statement.setString(19, employee.getStreetAddressx());
                statement.setString(20, employee.getStreetAddress2x());
                statement.setString(21, employee.getCityx());
                statement.setString(22, employee.getStatex());
                statement.setString(23, employee.getCountryx());
                statement.setString(24, employee.getZipcodex());

                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public CompletionStage<Optional<Employee>> getEmployee(int id) {
        String sql = "EXEC GetActiveEmployeeWithEmergencyInfo @EmployeeId = ?";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, id);

                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    Employee employee = createEmployeeFromResultSet(rs);
                    return Optional.of(employee);
                } else {
                    return Optional.empty();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }


    public CompletionStage<Boolean> updateEmployee(Employee employee) {
        String sql = "EXEC UpdateEmployeeWithEmergencyInfo\n" +
                "@Gender = ?,@FirstName = ?,@LastName = ?,@Phone = ?,@Mail = ?,@Birthday =? ,@BloodId = ?,@StreetAddress = ?," +
                "@StreetAddress2 =? ,@City = ?,@State = ?,@Country = ?,@Zipcode = ?,@id = ?,@GenderX = ?,@FirstNameX = ?,@LastNameX = ?," +
                "@RelationX = ?,@PhoneX =?,@StreetAddressX = ?,@StreetAddress2X = ?,@CityX = ?,@StateX = ?,@CountryX = ?,@ZipcodeX = ?, @EmployeeId = ?;";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, employee.getGender());
                statement.setString(2, employee.getFirstName());
                statement.setString(3, employee.getLastName());
                statement.setString(4, employee.getPhone());
                statement.setString(5, employee.getMail());
                statement.setString(6, employee.getBirthday());
                statement.setInt(7, employee.getBloodId());
                statement.setString(8, employee.getStreetAddress());
                statement.setString(9, employee.getStreetAddress2());
                statement.setString(10, employee.getCity());
                statement.setString(11, employee.getState());
                statement.setString(12, employee.getCountry());
                statement.setString(13, employee.getZipcode());
                statement.setInt(14, employee.getId());
                statement.setString(15, employee.getGenderx());
                statement.setString(16, employee.getFirstNamex());
                statement.setString(17, employee.getLastNamex());
                statement.setInt(18, employee.getRelationxId());
                statement.setString(19, employee.getPhonex());
                statement.setString(20, employee.getStreetAddressx());
                statement.setString(21, employee.getStreetAddress2x());
                statement.setString(22, employee.getCityx());
                statement.setString(23, employee.getStatex());
                statement.setString(24, employee.getCountryx());
                statement.setString(25, employee.getZipcodex());
                statement.setInt(26, employee.getId());

                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public CompletionStage<Boolean> deleteEmployee(int id) {
        String sql = "EXEC DeleteEmployee @Id = ?";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, id);

                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public CompletionStage<Boolean> restoreEmployee(int id) {
        String sql = "EXEC RestoreEmployee @Id = ?";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, id);

                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public CompletionStage<Boolean> emptyRecycleBin() {
        String sql = "EXEC emptyRecycleBin";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);

                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public CompletionStage<List<Employee>> getBinEmployees() {
        String sql = "EXEC getBinEmployees";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery();
                List<Employee> employees = new ArrayList<>();
                while (rs.next()) {
                    Employee employee = createEmployeeFromResultSet(rs);
                    employees.add(employee);
                }
                return employees;
            } catch (SQLException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        });
    }


    private Employee createEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getInt("id"));
        employee.setGender(resultSet.getString("gender"));
        employee.setFirstName(resultSet.getString("firstName"));
        employee.setLastName(resultSet.getString("lastName"));
        employee.setPhone(resultSet.getString("phone"));
        employee.setMail(resultSet.getString("mail"));
        employee.setBirthday(resultSet.getString("birthday"));
        employee.setBloodId(resultSet.getInt("bloodId"));
        employee.setStreetAddress(resultSet.getString("streetAddress"));
        employee.setStreetAddress2(resultSet.getString("streetAddress2"));
        employee.setCity(resultSet.getString("city"));
        employee.setState(resultSet.getString("state"));
        employee.setCountry(resultSet.getString("country"));
        employee.setZipcode(resultSet.getString("zipcode"));
        employee.setStatus(resultSet.getString("status"));
        employee.setGenderx(resultSet.getString("genderx"));
        employee.setFirstNamex(resultSet.getString("firstNamex"));
        employee.setLastNamex(resultSet.getString("lastNamex"));
        employee.setRelationxId(resultSet.getInt("relationxId"));
        employee.setPhonex(resultSet.getString("phonex"));
        employee.setStreetAddressx(resultSet.getString("streetAddressx"));
        employee.setStreetAddress2x(resultSet.getString("streetAddress2x"));
        employee.setCityx(resultSet.getString("cityx"));
        employee.setStatex(resultSet.getString("statex"));
        employee.setCountryx(resultSet.getString("countryx"));
        employee.setZipcodex(resultSet.getString("zipcodex"));
        employee.setEmployeeId(resultSet.getInt("employeeId"));
        return employee;
    }

    public CompletionStage<Boolean> saveUserAccount(UserAccount userAccount) {
        String sql = "INSERT INTO userAccount (email, password, firstname, lastname) VALUES (?, ?, ?, ?)";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, userAccount.getEmail());
                statement.setString(2, userAccount.getPassword());
                statement.setString(3, userAccount.getFirstName());
                statement.setString(4, userAccount.getLastName());

                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public CompletionStage<Boolean> checkUserCredentials(String email, String password) {
        String sql = "SELECT COUNT(*) FROM userAccount WHERE email = ? AND password = ?";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = db.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, email);
                statement.setString(2, password);

                ResultSet rs = statement.executeQuery();
                rs.next();
                int count = rs.getInt(1);
                return count > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });


    }
}