package ru.praktikumServices.stellarBurgers;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikumServices.stellarBurgers.model.RegisterUserRequest;
import ru.praktikumServices.stellarBurgers.model.UpdateUserDataRequest;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class UpdateUserDataTest {
    UserOperations userOperations;
    RegisterUserRequest registerUserRequest;
    AllureLifecycle oLifecycle = Allure.getLifecycle();

    String message;
    Boolean authorize;
    UpdateUserDataRequest updateUserDataRequest;
    Boolean success;
    Integer code;

    public UpdateUserDataTest(String message, Boolean authorize, UpdateUserDataRequest updateUserDataRequest, Boolean success,
                              Integer code) {
        this.message = message;
        this.authorize = authorize;
        this.updateUserDataRequest = updateUserDataRequest;
        this.success = success;
        this.code = code;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                { "Authorized user update name and email", true, new UpdateUserDataRequest("308e92c2-8c03-11ec-a8a3-0242ac120002@yandex.ru", "new_name"), true, 200 },
                { "Authorized user update email", true, new UpdateUserDataRequest("308e92c2-8c03-11ec-a8a3-0242ac120002@yandex.ru", null), true, 200 },
                { "Authorized user update name", true, new UpdateUserDataRequest(null, "new_name"), true, 200 },
                { "Unauthorized user update name and email", false, new UpdateUserDataRequest("308e92c2-8c03-11ec-a8a3-0242ac120002@yandex.ru", "new_name"), false, 401 },
                { "Unauthorized user update email", false, new UpdateUserDataRequest("308e92c2-8c03-11ec-a8a3-0242ac120002@yandex.ru", null), false, 401 },
                { "Unauthorized user update name", false, new UpdateUserDataRequest(null, "new_name"), false, 401 }
        };
    }

    @Before
    public void setUp() {
        userOperations = new UserOperations();
        registerUserRequest = RegisterUserRequest.getRandom();
        ValidatableResponse validatableResponse = userOperations.registerNewUserAndReturnResponse(registerUserRequest);
        userOperations.saveUserToken(validatableResponse);
    }

    @After
    public void tearDown() {
       userOperations.deleteUserAndFlushToken();
    }

    @Test
    @Description("Checking if updateUserDataAndReturnResponse method returns correct result depending on the authorization status")
    public void testUpdateUserData() {
        oLifecycle.updateTestCase(testResult -> testResult.setName(message));
        userOperations.updateUserDataAndReturnResponse(updateUserDataRequest, authorize)
                .assertThat().body("success", equalTo(success))
                .and()
                .statusCode(code);
    }
}






