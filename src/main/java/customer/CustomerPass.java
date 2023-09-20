package customer;

public class CustomerPass {
    private final String email;
    private final String password;

    public CustomerPass(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static CustomerPass passFrom(Customer customer) {
        return new CustomerPass(customer.getEmail(), customer.getPassword());
    }
}
