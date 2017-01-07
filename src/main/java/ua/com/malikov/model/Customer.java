package ua.com.malikov.model;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = Customer.DELETE_BY_ID, query = "DELETE FROM Customer WHERE id=:id"),
        @NamedQuery(name = Customer.DELETE_ALL, query = "DELETE FROM Customer"),
        @NamedQuery(name = Customer.LOAD_BY_NAME, query = "FROM Customer c WHERE c.name=:name"),
        @NamedQuery(name = Customer.LOAD_ALL, query = "FROM Customer c ORDER BY c.id"),
})
@Entity
@Table(name = "customers")
@AttributeOverride(name = "name", column = @Column(name = Customer.NAME))
public class Customer extends NamedEntity {

    public static final String NAME = "customer_name";

    public static final String LOAD_BY_NAME = "Customer.byName";
    public static final String LOAD_ALL = "Customer.loadAll";
    public static final String DELETE_BY_ID = "Customer.deleteById";
    public static final String DELETE_ALL = "Customer.deleteAll";

    public Customer() {
    }

    public Customer(String name) {
        super(name);
    }

    public Customer(Integer id, String name) {
        super(id, name);
    }

    public Customer(Customer customer) {
        this(customer.getId(), customer.getName());
    }
}
