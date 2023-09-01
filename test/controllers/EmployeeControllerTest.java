package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Employee;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import services.EmployeeDatabase;
import services.JWTService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;


public class EmployeeControllerTest extends WithApplication {

    @Test
    public void testSum() {
        int a = 1 + 1;
        assertEquals(2, a);
    }

    @Test
    public void testString() {
        String str = "Hello world";
        assertFalse(str.isEmpty());
    }

    @Test
    public void testRoute() {
        Http.RequestBuilder request = fakeRequest().method(GET).uri("/employees");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testBadRoute() {
        Http.RequestBuilder request = fakeRequest().method(GET).uri("/xx/Kiwi");
        Result result = route(app, request);
        assertEquals(NOT_FOUND, result.status());
    }

    public Http.Request createFakeRequest(String method, String uri, String ContentType, String ApplicationType, JsonNode JsonData) {
        Http.RequestBuilder requestBuilder = fakeRequest()
                .method(method)
                .uri(uri)
                .header(ContentType, ApplicationType)
                .bodyJson(JsonData);

        return requestBuilder.build();
    }


    @Test
    public void testCreateEmployeeSuccess() throws ExecutionException, InterruptedException {
        EmployeeDatabase mockDatabase = mock(EmployeeDatabase.class);
        JWTService jwtService = mock(JWTService.class);

        when(mockDatabase.saveEmployee(any(Employee.class))).thenReturn(CompletableFuture.completedFuture(true));

        EmployeeController employeeController = new EmployeeController(mockDatabase, jwtService);

        JsonNode jsonNode = Json.parse("{\"firstName\":\"Siddarood\",\"lastName\":\"Karachuri\",\"gender\":\"Male\"}");

        Http.Request request = createFakeRequest("POST", "/create", "Content-Type", "application/json", jsonNode);

        CompletionStage<Result> resultCompletionStage = employeeController.create(request);

        Result result = resultCompletionStage.toCompletableFuture().get();

        System.out.println(result);
        assertEquals(201, result.status());
        String responseJson = contentAsString(result);
        assertEquals(responseJson, "Employee saved successfully");
        verify(mockDatabase).saveEmployee(argThat(employee -> employee.getFirstName().equals("Siddarood")));
    }

    @Test
    public void testCreateEmployeeFailure() throws ExecutionException, InterruptedException {
        EmployeeDatabase mockDatabase = mock(EmployeeDatabase.class);
        JWTService jwtService = mock(JWTService.class);
        when(mockDatabase.saveEmployee(any(Employee.class))).thenReturn(CompletableFuture.completedFuture(true));

        EmployeeController employeeController = new EmployeeController(mockDatabase, jwtService);

        JsonNode jsonNode = Json.parse("{\"firstame\":\"Siddarood\",\"lastame\":\"Karachuri\"}");

        Http.Request request = createFakeRequest("POST", "/create", "Content-Type", "application/json", jsonNode);

        CompletionStage<Result> resultCompletionStage = employeeController.create(request);
        Result result = resultCompletionStage.toCompletableFuture().get();

        assertEquals(201, result.status());
        String responseJson = contentAsString(result);
        assertEquals(responseJson, "Employee saved successfully");
//        verify(mockDatabase).saveEmployee(argThat(employee -> employee.getFirstName().equals("Siddarood")));
    }

    @Test
    public void testCreateEmployeeFailure1() throws InterruptedException, ExecutionException {
        EmployeeDatabase mockDatabase = mock(EmployeeDatabase.class);
        JWTService jwtService = mock(JWTService.class);

        when(mockDatabase.saveEmployee(any(Employee.class))).thenReturn(CompletableFuture.completedFuture(true));

        EmployeeController employeeController = new EmployeeController(mockDatabase, jwtService);

        Http.Request request = createFakeRequest("POST", "/create", "Content-Type", "application/json", null);

        CompletionStage<Result> resultCompletionStage = employeeController.create(request);
        Result result = resultCompletionStage.toCompletableFuture().get();

        assertEquals(400, result.status());
        String responseJson = contentAsString(result);
        System.out.println(responseJson);
        assertEquals(responseJson, "Expecting Json data");
    }

    @Test
    public void testCreateEmployeeFailure2() throws ExecutionException, InterruptedException {
        EmployeeDatabase mockDatabase = mock(EmployeeDatabase.class);
        JWTService jwtService = mock(JWTService.class);

        when(mockDatabase.saveEmployee(any(Employee.class))).thenReturn(CompletableFuture.completedFuture(false));

        EmployeeController employeeController = new EmployeeController(mockDatabase, jwtService);

        JsonNode jsonNode = Json.parse("{\"firstame\":\"Siddarood\",\"lastame\":\"Karachuri\"}");

        Http.Request request = createFakeRequest("POST", "/create", "Content-Type", "application/json", jsonNode);

        CompletionStage<Result> resultCompletionStage = employeeController.create(request);
        Result result = resultCompletionStage.toCompletableFuture().get();

        assertEquals(500, result.status());
        String responseJson = contentAsString(result);
        assertEquals(responseJson, "Failed to save employee");
    }
}
