package utils;

import com.github.javafaker.Faker;
import customer.Customer;

public class Generator {
    public static String randomEmail() {
        Faker faker = new Faker();
        return faker.internet().safeEmailAddress();
    }
    public static String randomPassword() {
        Faker faker = new Faker();
        return faker.internet().password(8,9);
    }
    public static String randomName() {
        Faker faker = new Faker();
        return faker.name().fullName();
    }
    public static Customer randomCustomer() {
        return new Customer()
                .withEmail(randomEmail())
                .withPassword(randomPassword())
                .withName(randomName());
        }
}
