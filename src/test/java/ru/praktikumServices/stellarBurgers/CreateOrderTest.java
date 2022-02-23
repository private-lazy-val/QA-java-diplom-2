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
import ru.praktikumServices.stellarBurgers.model.CreateOrderRequest;
import ru.praktikumServices.stellarBurgers.model.RegisterUserRequest;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    UserOperations userOperations;
    RegisterUserRequest registerUserRequest;
    AllureLifecycle oLifecycle = Allure.getLifecycle();

    String message;
    Boolean authorize;
    CreateOrderRequest createOrderRequest;
    Boolean success;
    Boolean checkResponseSuccessField;
    Integer code;


    public CreateOrderTest(String message, Boolean authorize, CreateOrderRequest createOrderRequest,
                           Boolean checkResponseSuccessField, Boolean success, Integer code) {
        this.message = message;
        this.authorize = authorize;
        this.createOrderRequest = createOrderRequest;
        this.checkResponseSuccessField = checkResponseSuccessField;
        this.success = success;
        this.code = code;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                { "Authorized user creates order with ingredients", true, new CreateOrderRequest(new String[]{"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa73"}), true, true, 200 },
                { "Unauthorized user creates order with ingredients", false, new CreateOrderRequest(new String[]{"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa73"}), true, true, 200 },
                { "Authorized user creates order without ingredients", true, new CreateOrderRequest(new String[]{}), true, false, 400 },
                { "Unauthorized user creates order without ingredients", false, new CreateOrderRequest(new String[]{}), true, false, 400 },
                { "Authorized user creates order with ingredients with wrong hashes", true, new CreateOrderRequest(new String[]{"_61c0c5a71d1f82001bdaaa6d","_61c0c5a71d1f82001bdaaa6f", "_61c0c5a71d1f82001bdaaa73"}), false, false, 500 },
                { "Unauthorized user creates order with ingredients with wrong hashes", false, new CreateOrderRequest(new String[]{"_61c0c5a71d1f82001bdaaa6d","_61c0c5a71d1f82001bdaaa6f", "_61c0c5a71d1f82001bdaaa73"}), false, false, 500 }
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
    public void tearDown() throws Exception {
       userOperations.deleteUserAndFlushToken();
       Thread.sleep(100);
    }

    @Test
    @Description("Checking if createOrderAndReturnResponse method returns correct result depending on createOrderRequest")
    public void testCreateOrderRequest() {
        oLifecycle.updateTestCase(testResult -> testResult.setName(message));
        ValidatableResponse validatableResponse = userOperations.createOrderAndReturnResponse(createOrderRequest, authorize)
                .assertThat()
                .statusCode(code);

        if (checkResponseSuccessField) {
            validatableResponse.body("success", equalTo(success));
        }
    }
}






