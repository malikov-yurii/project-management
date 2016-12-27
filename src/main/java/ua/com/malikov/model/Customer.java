package ua.com.malikov.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pms.customers")
@AttributeOverride(name = "name", column = @Column(name = Customer.NAME))
public class Customer extends NamedEntity {

    public static final String NAME = "customer_name";

    public Customer(String name) {
        super(name);
    }

    public Customer(Integer id, String name) {
        super(id, name);
    }
}
