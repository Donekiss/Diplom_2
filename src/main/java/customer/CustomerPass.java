package customer;

import io.qameta.allure.Step;

public class CustomerPass {
    private final String email;
    private final String password;

    public CustomerPass(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Step("Record email and password of the current customer")
    public static CustomerPass passFrom(Customer customer) {
        return new CustomerPass(customer.getEmail(), customer.getPassword());
    }

    @Step("Record mistake email and password of the current customer")
    public static CustomerPass passFromWithMistakeEmail(String addInEmail, Customer customer) {
        return new CustomerPass(addInEmail + customer.getEmail(), customer.getPassword());
    }

    @Step("Record email and mistake password of the current customer")
    public static CustomerPass passFromWithMistakePassword(String addInPassword, Customer customer) {
        return new CustomerPass(customer.getEmail(), customer.getPassword() + addInPassword);
    }
}
