package ru.praktikumServices.stellarBurgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikumServices.stellarBurgers.model.RegisterUserRequest;

import static org.hamcrest.Matchers.equalTo;

public class UserRegisterTest {
    UserOperations userOperations;

    @Before
    public void setUp() {
        userOperations = new UserOperations();
    }

    @After
    public void tearDown() {
       userOperations.deleteUserAndFlushToken();
    }

    @Test
    @DisplayName("Check register user")
    @Description("Checking if \"success\" field has flag \"true\" in the response and status code is 200")
    public void testRegisterNewUserReturn200True() {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.getRandom();
        ValidatableResponse validatableResponse = userOperations.registerNewUserAndReturnResponse(registerUserRequest);

        validatableResponse
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);

        userOperations.saveUserToken(validatableResponse);
    }

    @Test
    @DisplayName("Check register existing user")
    @Description("Checking if \"success\" field has flag \"false\" in the response and status code is 403")
    public void testRegisterExistingUserReturn403() {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.getRandom();
        ValidatableResponse validatableResponse = userOperations.registerNewUserAndReturnResponse(registerUserRequest);
        userOperations.registerNewUserAndReturnResponse(registerUserRequest)
                .assertThat().body("success", equalTo(false),
                        "message", equalTo("User already exists"))
                .and()
                .statusCode(403);

        userOperations.saveUserToken(validatableResponse);
    }

    @Test
    @DisplayName("Check register with no email")
    @Description("Checking if \"success\" field has flag \"false\" in the response and status code is 403")
    public void testRegisterUserWithNoEmailReturn403() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(null, "password", "name");
        userOperations.registerNewUserAndReturnResponse(registerUserRequest)
                .assertThat().body("success", equalTo(false),
                        "message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Check register with no name")
    @Description("Checking if \"success\" field has flag \"false\" in the response and status code is 403")
    public void testRegisterUserWithNoNameReturn403() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest("email@yandex.ru", "password", null);
        userOperations.registerNewUserAndReturnResponse(registerUserRequest)
                .assertThat().body("success", equalTo(false),
                        "message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Check register with no password")
    @Description("Checking if \"success\" field has flag \"false\" in the response and status code is 403")
    public void testRegisterUserWithNoPasswordReturn403() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest("email@yandex.ru", null, "name");
        userOperations.registerNewUserAndReturnResponse(registerUserRequest)
                .assertThat().body("success", equalTo(false),
                        "message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
}






