package ua.com.malikov;

import ua.com.malikov.matcher.ModelMatcher;
import ua.com.malikov.model.Customer;

import java.util.Objects;

public class CustomerTestData {
    public static final Customer CUSTOMER_CITI_BANK      = new Customer(1, "City Bank");
    public static final Customer CUSTOMER_ROZETKA        = new Customer(2, "Rozetka");
    public static final Customer CUSTOMER_UKRZALIZNYTSYA = new Customer(3, "Ukrzaliznytsya");

    public static final ModelMatcher<Customer> MATCHER = ModelMatcher.of(Customer.class,
            (expected, actual) -> expected == actual ||
                    (Objects.equals(expected.getId(), actual.getId())
                            && Objects.equals(expected.getName(), actual.getName())
                    )
    );
}
