package customer;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class CustomerGenerator {
    @Step("Generator random email")
    public static String randomEmail() {
        Faker faker = new Faker();
        return faker.internet().safeEmailAddress();
    }

    @Step("Generator random password")
    public static String randomPassword() {
        Faker faker = new Faker();
        return faker.internet().password(8, 9);
    }

    @Step("Generator random name")
    public static String randomName() {
        Faker faker = new Faker();
        return faker.name().fullName();
    }

    @Step("Generator random customer for create")
    public static Customer randomCustomer() {
        return new Customer()
                .withEmail(randomEmail())
                .withPassword(randomPassword())
                .withName(randomName());
    }
}
