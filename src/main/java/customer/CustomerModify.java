package customer;

public class CustomerModify {
    private final String email;
    private final String name;

    public CustomerModify(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public static CustomerPass modifyFrom(Customer customer) {
        return new CustomerPass(customer.getEmail(), customer.getName());
    }
}
