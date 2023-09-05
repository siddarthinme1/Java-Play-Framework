package controllers;

import actions.AuthenticatedAction;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import models.AuthRequest;
import models.Employee;
import models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import play.libs.Json;
import play.mvc.*;
import services.EmployeeDatabase;
import services.JWTService;

@Api(value = "/employees")
public class EmployeeController extends Controller {

    @Autowired
    private final EmployeeDatabase database;

    @Autowired
    private final JWTService jwtService;

    @Inject
    public EmployeeController(EmployeeDatabase database, JWTService jwtService) {
        this.database = database;
        this.jwtService = jwtService;
    }

    @ApiOperation(
            value = "Get all the Employee from the Database",
            notes = "Make HTTP request to Get all the Employees"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK Response",
                            response = Employee.class,
                            responseContainer = "List"
                    ),
                    @ApiResponse(code = 404, message = "No employees found"),
            }
    )
    @With(AuthenticatedAction.class)
    public CompletionStage<Result> getAllEmployees(Http.Request request) {
        return database
                .getEmployees()
                .thenApply(employees -> {
                    JsonNode json = Json.toJson(employees);
                    return ok(json);
                });
    }

    @ApiOperation(
            value = "Add a new Employee to the Database",
            notes = "Make HTTP request to add new Employee"
    )
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(
                            name = "Request body",
                            value = "Employee Object to be added",
                            dataType = "models.EmployeeModel",
                            paramType = "body",
                            required = true
                    ),
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Employee saved successfully"),
                    @ApiResponse(code = 400, message = "Expecting Json data"),
            }
    )
    @With(AuthenticatedAction.class)
    public CompletionStage<Result> create(Http.Request request) {
        JsonNode json = request.body().asJson();
        System.out.println(json);
        if (json == null) {
            return CompletableFuture.completedFuture(
                    badRequest("Expecting Json data")
            );
        }

        Employee employee = Json.fromJson(json, Employee.class);
        return database
                .saveEmployee(employee)
                .thenApply(saved -> {
                    if (saved) {
                        return ok("Employee saved successfully");
                    } else {
                        return internalServerError("Failed to save employee");
                    }
                });
    }

    @ApiOperation(
            value = "Get a Employee from the Database",
            notes = "Make HTTP request to get a Employee by Id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK Response",
                            response = Employee.class
                    ),
                    @ApiResponse(code = 404, message = "Employee not Found"),
            }
    )
    @With(AuthenticatedAction.class)
    public CompletionStage<Result> retrieve(Http.Request request, int id) {
        return database
                .getEmployee(id)
                .thenApply(employee -> {
                    if (employee.isPresent()) {
                        JsonNode json = Json.toJson(employee.get());
                        return ok(json);
                    } else {
                        return notFound("Employee not Found");
                    }
                });
    }

    @ApiOperation(
            value = "Update existing Employee to the Database",
            notes = "Make HTTP request to Update the Employee",
            response = Employee.class,
            httpMethod = "PUT"
    )
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(
                            name = "body",
                            value = "Employee Object to be updated",
                            dataType = "models.EmployeeModel",
                            paramType = "body",
                            required = true
                    ),
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 400, message = "Expecting Json data"),
                    @ApiResponse(code = 200, message = "Employee updated successfully"),
                    @ApiResponse(code = 404, message = "Employee not found"),
            }
    )
    @With(AuthenticatedAction.class)
    public CompletionStage<Result> update(Http.Request request, int id) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return CompletableFuture.completedFuture(
                    badRequest("Expecting Json data")
            );
        }

        Employee employee = Json.fromJson(json, Employee.class);
        employee.setId(id);

        return database
                .updateEmployee(employee)
                .thenApply(updated -> {
                    if (updated) {
                        return ok("Employee updated successfully");
                    } else {
                        return notFound("Employee not found");
                    }
                });
    }

    @ApiOperation(
            value = "Delete a Employee from the Database",
            notes = "Make HTTP request to Delete the Employee"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Employee deleted successfully"),
                    @ApiResponse(code = 404, message = "Employee not found"),
            }
    )
    @With(AuthenticatedAction.class)
    public CompletionStage<Result> delete(Http.Request request, int id) {
        return database
                .deleteEmployee(id)
                .thenApply(deleted -> {
                    if (deleted) {
                        return ok("Employee deleted successfully");
                    } else {
                        return notFound("Employee not found");
                    }
                });
    }

    @ApiOperation(
            value = "Get all the Deleted employees from Database",
            notes = "Make HTTP request Get all the Deleted employees"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 404, message = "Recycle bin is Empty"),
                    @ApiResponse(code = 200, message = "Successfully"),
            }
    )
    @With(AuthenticatedAction.class)
    public CompletionStage<Result> listBinEmployees(Http.Request request) {
        return database
                .getBinEmployees()
                .thenApply(employees -> {
                    JsonNode json = Json.toJson(employees);
                    return ok(json);
                });
    }

    @ApiOperation(
            value = "Restore Deleted Employee from the Database",
            notes = "Make HTTP request Restore Deleted Employee"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Employee restored successfully"),
                    @ApiResponse(code = 404, message = "Employee not found"),
            }
    )
    @With(AuthenticatedAction.class)
    public CompletionStage<Result> restore(Http.Request request, int id) {
        return database
                .restoreEmployee(id)
                .thenApply(deleted -> {
                    if (deleted) {
                        return ok("Employee restored successfully");
                    } else {
                        return notFound("Employee not found");
                    }
                });
    }

    @ApiOperation(
            value = "Empty the recycle Bin from the Database",
            notes = "Make HTTP request to Empty the recycle Bin"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Recycle bin cleared successfully"),
                    @ApiResponse(code = 404, message = "Data not found"),
            }
    )
    @With(AuthenticatedAction.class)
    public CompletionStage<Result> emptyRecycleBin(Http.Request request) {
        return database
                .emptyRecycleBin()
                .thenApply(deleted -> {
                    if (deleted) {
                        return ok("Recycle bin cleared successfully");
                    } else {
                        return notFound("Data not found");
                    }
                });
    }


    @ApiOperation(
            value = "Get a Employee from the Database",
            notes = "Make HTTP request to get a Employee by Id"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK Response",
                            response = UserAccount.class
                    ),
                    @ApiResponse(code = 404, message = "Employee not Found"),
            }
    )
    public CompletionStage<Result> saveUserAccount(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return CompletableFuture.completedFuture(
                    badRequest("Expecting Json data")
            );
        }
        UserAccount userAccount = Json.fromJson(json, UserAccount.class);
        return database
                .saveUserAccount(userAccount)
                .thenApply(saved -> {
                    if (saved) {
                        return ok("User account saved successfully");
                    } else {
                        return internalServerError("Failed to save user account");
                    }
                });
    }


    @ApiOperation(
            value = "Authenticate the user and return token",
            notes = "Make HTTP request to get a authentication token"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "Bearer Token"
                    ),
                    @ApiResponse(code = 404, message = "Credentials not found"),
                    @ApiResponse(code = 400, message = "Expecting JSON data"),
            }
    )
    public CompletionStage<Result> authenticateAndGetToken(Http.Request request) {
        JsonNode json = request.body().asJson();
        System.out.println(request);
        System.out.println(json);
        if (json == null) {
            return CompletableFuture.completedFuture(
                    badRequest("Expecting JSON data")
            );
        }
        AuthRequest authRequest = Json.fromJson(json, AuthRequest.class);
        String userName = authRequest.getUserName();
        String password = authRequest.getPassword();
        if (userName != null && password != null) {
            return database
                    .checkUserCredentials(userName, password)
                    .thenApply(credentialsMatch -> {
                        if (credentialsMatch) {
                            String token = jwtService.generateToken(userName);
                            return ok(token).as("application/json");
                        } else {
                            return unauthorized("Credentials not found");
                        }
                    });
        } else {
            return CompletableFuture.completedFuture(
                    badRequest("Missing or invalid query parameters")
            );
        }
    }
}
