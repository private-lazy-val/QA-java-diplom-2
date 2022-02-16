package ru.praktikumServices.stellarBurgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikumServices.stellarBurgers.model.LoginUserRequest;
import ru.praktikumServices.stellarBurgers.model.RegisterUserRequest;

import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {
    UserOperations userOperations;
    RegisterUserRequest registerUserRequest;

    @Before
    public void setUp() {
        userOperations = new UserOperations();
        registerUserRequest = RegisterUserRequest.getRandom();
        userOperations.saveUserToken(userOperations.registerNewUserAndReturnResponse(registerUserRequest));
    }

    @After
    public void tearDown() {
       userOperations.deleteUserAndFlushToken();
    }

    @Test
    @DisplayName("Login user")
    @Description("Checking if \"success\" field has flag \"true\" in the response and status code is 200")
    public void testLoginUserReturn200True() {
        LoginUserRequest loginUserRequest = new LoginUserRequest(registerUserRequest.email, registerUserRequest.password);
        userOperations.loginUserAndReturnResponse(loginUserRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }


    @Test
    @DisplayName("Check login user with wrong password")
    @Description("Checking if \"success\" field has flag \"false\" in the response and status code is 401")
    public void testLoginUserWithWrongPasswordReturn401() {
        LoginUserRequest loginUserRequest = new LoginUserRequest(registerUserRequest.email, registerUserRequest.password + "_wrong");
        userOperations.loginUserAndReturnResponse(loginUserRequest)
                .assertThat().body("success", equalTo(false),
                        "message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Check login user with wrong email")
    @Description("Checking if \"success\" field has flag \"false\" in the response and status code is 401")
    public void testLoginUserWithWrongEmailReturn401() {
        userOperations.deleteUserAndFlushToken();

        LoginUserRequest loginUserRequest = new LoginUserRequest(registerUserRequest.email, registerUserRequest.password);
        userOperations.loginUserAndReturnResponse(loginUserRequest)
                .assertThat().body("success", equalTo(false),
                        "message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Check login user with wrong email and password")
    @Description("Checking if \"success\" field has flag \"false\" in the response and status code is 401")
    public void testLoginUserWithWrongEmailAndPasswordReturn401() {
        userOperations.deleteUserAndFlushToken();

        LoginUserRequest loginUserRequest = new LoginUserRequest(registerUserRequest.email, registerUserRequest.password + "_wrong") ;
        userOperations.loginUserAndReturnResponse(loginUserRequest)
                .assertThat().body("success", equalTo(false),
                        "message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
}






