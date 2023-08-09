package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import models.Employee;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.EmployeeDatabase;
import models.UserAccount;

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Api(value = "/employees")
public class EmployeeController extends Controller {

    private final EmployeeDatabase database;

    @Inject
    public EmployeeController(EmployeeDatabase database) {
        this.database = database;
    }


    @ApiOperation(value = "Get all the Employee from the Database", notes = "Make HTTP request to Get all the Employees")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK Response", response = Employee.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "No employees found")
    })

    public CompletionStage<Result> getAllEmployees(Http.Request request) {
        return database.getEmployees()
                .thenApply(employees -> {
                    if (employees.isEmpty()) {
                        return notFound("No employees found.");
                    } else {
                        JsonNode json = Json.toJson(employees);
                        return ok(json);
                    }
                });
    }

    @ApiOperation(value = "Add a new Employee to the Database", notes = "Make HTTP request to add new Employee")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Request body",
                    value = "Employee Object to be added",
                    dataType = "models.EmployeeModel",
                    paramType = "body",
                    required = true
            )
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee saved successfully"),
            @ApiResponse(code = 400, message = "Expecting Json data")
    })
    public CompletionStage<Result> create(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return CompletableFuture.completedFuture(badRequest("Expecting Json data"));
        }

        Employee employee = Json.fromJson(json, Employee.class);
        return database.saveEmployee(employee)
                .thenApply(saved -> {
                    if (saved) {
                        return ok("Employee saved successfully");
                    } else {
                        return internalServerError("Failed to save employee");
                    }
                });
    }

    @ApiOperation(value = "Get a Employee from the Database", notes = "Make HTTP request to get a Employee by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK Response", response = Employee.class),
            @ApiResponse(code = 404, message = "Employee not Found")
    })
    public CompletionStage<Result> retrieve(Http.Request request, int id) {
        return database.getEmployee(id)
                .thenApply(employee -> {
                    if (employee.isPresent()) {
                        JsonNode json = Json.toJson(employee.get());
                        return ok(json);
                    } else {
                        return notFound("Employee not Found");
                    }
                });
    }

    @ApiOperation(value = "Update existing Employee to the Database", notes = "Make HTTP request to Update the Employee", response = Employee.class, httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Employee Object to be updated",
                    dataType = "models.EmployeeModel",
                    paramType = "body",
                    required = true
            )
    })
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Expecting Json data"),
            @ApiResponse(code = 200, message = "Employee updated successfully"),
            @ApiResponse(code = 404, message = "Employee not found")
    })
    public CompletionStage<Result> update(Http.Request request, int id) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return CompletableFuture.completedFuture(badRequest("Expecting Json data"));
        }

        Employee employee = Json.fromJson(json, Employee.class);
        employee.setId(id);

        return database.updateEmployee(employee)
                .thenApply(updated -> {
                    if (updated) {
                        return ok("Employee updated successfully");
                    } else {
                        return notFound("Employee not found");
                    }
                });
    }

    @ApiOperation(value = "Delete a Employee from the Database", notes = "Make HTTP request to Delete the Employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee deleted successfully"),
            @ApiResponse(code = 404, message = "Employee not found")
    })
    public CompletionStage<Result> delete(Http.Request request, int id) {
        return database.deleteEmployee(id)
                .thenApply(deleted -> {
                    if (deleted) {
                        return ok("Employee deleted successfully");
                    } else {
                        return notFound("Employee not found");
                    }
                });
    }

    @ApiOperation(value = "Get all the Deleted employees from Database", notes = "Make HTTP request Get all the Deleted employees")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Recycle bin is Empty"),
            @ApiResponse(code = 200, message = "Successfully")
    })
    public CompletionStage<Result> listBinEmployees(Http.Request request) {
        return database.getBinEmployees()
                .thenApply(employees -> {
                    if (employees.isEmpty()) {
                        return notFound("Recycle bin is Empty");
                    } else {
                        JsonNode json = Json.toJson(employees);
                        return ok(json);
                    }
                });
    }

    @ApiOperation(value = "Restore Deleted Employee from the Database", notes = "Make HTTP request Restore Deleted Employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee restored successfully"),
            @ApiResponse(code = 404, message = "Employee not found")
    })
    public CompletionStage<Result> restore(Http.Request request, int id) {
        return database.restoreEmployee(id)
                .thenApply(deleted -> {
                    if (deleted) {
                        return ok("Employee restored successfully");
                    } else {
                        return notFound("Employee not found");
                    }
                });
    }

    @ApiOperation(value = "Empty the recycle Bin from the Database", notes = "Make HTTP request to Empty the recycle Bin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Recycle bin cleared successfully"),
            @ApiResponse(code = 404, message = "Data not found")
    })
    public CompletionStage<Result> emptyRecycleBin(Http.Request request) {
        return database.emptyRecycleBin()
                .thenApply(deleted -> {
                    if (deleted) {
                        return ok("Recycle bin cleared successfully");
                    } else {
                        return notFound("Data not found");
                    }
                });
    }

    public CompletionStage<Result> saveUserAccount(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return CompletableFuture.completedFuture(badRequest("Expecting Json data"));
        }

        UserAccount userAccount = Json.fromJson(json, UserAccount.class);
        return database.saveUserAccount(userAccount)
                .thenApply(saved -> {
                    if (saved) {
                        return ok("User account saved successfully");
                    } else {
                        return internalServerError("Failed to save user account");
                    }
                });
    }

    public CompletionStage<Result> checkUserCredentials(Http.Request request) {
        Optional<String> emailOptional = request.queryString("email");
        Optional<String> passwordOptional = request.queryString("password");


        if (emailOptional.isPresent() && passwordOptional.isPresent()) {
            String username = emailOptional.get();
            String password = passwordOptional.get();

            return database.checkUserCredentials(username, password)
                    .thenApply(credentialsMatch -> {
                        if (credentialsMatch) {
                            return ok("Credentials found");
                        } else {
                            return notFound("Credentials not found");
                        }
                    });
        } else {
            return CompletableFuture.completedFuture(badRequest("Missing or invalid query parameters"));
        }
    }
}