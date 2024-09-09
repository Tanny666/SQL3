package netology.test;

import org.junit.jupiter.api.*;
import netology.data.DataHelper;
import netology.data.SQLHelper;
import netology.page.LoginPage;



import static com.codeborne.selenide.Selenide.open;
import static netology.data.SQLHelper.cleanAuthCodes;
import static netology.data.SQLHelper.cleanDatabase;

public class BankLoginTest {
    LoginPage loginPage;

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @AfterAll
    static void teardown() {
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Should successfully login to dashboard with exist login and password from sut test data")
    void shouldSuccessfulLogin() {
        var authInfo = DataHelper.getAuthInfowithTestData();
        var verificationPage = loginPage.login(authInfo);
        verificationPage.verifycationPageVisiblity();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("Should get error notification if user is not exist in base")
    void shouldGetErrorNotificationIfLoginWithRandomUserWithoutAddingToBase() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.login(authInfo);
        loginPage.verifyErrorNotification("Ошибка! Неверно указан логин или пароль");
    }

    @Test
    @DisplayName("Should get error notification if login with exist in base and active user and random verification code")
    void ShouldGetErrorNotificationIfLoginWithExistUserAndRandomVerificationCode(){
        var authInfo = DataHelper.getAuthInfowithTestData();
        var verificationPage = loginPage.login(authInfo);
        verificationPage.verifycationPageVisiblity();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! Неверно указан код. Попробуйте еще раз.");

    }
}