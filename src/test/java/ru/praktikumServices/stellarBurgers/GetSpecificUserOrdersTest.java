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
import ru.praktikumServices.stellarBurgers.model.GetSpecificUserOrdersRequest;
import ru.praktikumServices.stellarBurgers.model.RegisterUserRequest;

import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class GetSpecificUserOrdersTest {
    UserOperations userOperations;
    RegisterUserRequest registerUserRequest;
    AllureLifecycle oLifecycle = Allure.getLifecycle();

    String message;
    Boolean authorize;
    Integer orderCount;
    Integer returnedOrderCount;
    Boolean success;
    Integer code;

    GetSpecificUserOrdersRequest getSpecificUserOrdersRequest;


    public GetSpecificUserOrdersTest(String message, Boolean authorize, Integer orderCount, Integer returnedOrderCount,
                                     Boolean success, Integer code) {
        this.message = message;
        this.authorize = authorize;
        this.orderCount = orderCount;
        this.returnedOrderCount = returnedOrderCount;
        this.success = success;
        this.code = code;

    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                { "Authorized user with no orders requests order history", true, 0, 0, true, 200 },
                { "Authorized user with one order requests order history", true, 1, 1, true, 200 },
                { "Authorized user with 49 orders requests order history", true, 49, 49, true, 200 },
                { "Authorized user with 50 orders requests order history", true, 50, 50, true, 200 },
                { "Authorized user with 51 orders requests order history", true, 51, 50, true, 200 },
                { "Unauthorized user with one order requests order history", false, 1, 1, false, 401 }
        };
    }

    @Before
    public void setUp() {
        userOperations = new UserOperations();
        registerUserRequest = RegisterUserRequest.getRandom();
        ValidatableResponse validatableResponse = userOperations.registerNewUserAndReturnResponse(registerUserRequest);
        userOperations.saveUserToken(validatableResponse);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa73"});
        for (int i = 0; i < orderCount; i++) {
            userOperations.createOrderAndReturnResponse(createOrderRequest, true);
        }

        getSpecificUserOrdersRequest = new GetSpecificUserOrdersRequest(registerUserRequest.email);
    }

    @After
    public void tearDown() throws Exception {
       userOperations.deleteUserAndFlushToken();
       Thread.sleep(10);
    }

    @Test
    @Description("Checking if getSpecificUserOrdersAndReturnResponse method returns correct result depending on getSpecificUserOrdersRequest")
    public void testGetSpecificUserOrdersAndReturnResponse() {
        oLifecycle.updateTestCase(testResult -> testResult.setName(message));
        ValidatableResponse validatableResponse = userOperations.getSpecificUserOrdersAndReturnResponse(getSpecificUserOrdersRequest, authorize)
                .assertThat()
                .statusCode(code)
                .body("success", equalTo(this.success));

        if (authorize) {
            validatableResponse
                    .body("orders.size()", is(returnedOrderCount));
        }
    }
}







