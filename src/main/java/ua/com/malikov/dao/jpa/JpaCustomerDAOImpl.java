package ua.com.malikov.dao.jpa;

import org.slf4j.Logger;
import ua.com.malikov.dao.CustomerDAO;
import ua.com.malikov.model.Customer;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class JpaCustomerDAOImpl implements CustomerDAO {

    private JpaUtils jpaUtils;

    public void setJpaUtils(JpaUtils jpaUtils) {
        this.jpaUtils = jpaUtils;
    }

    private static final Logger LOG = getLogger(JpaCustomerDAOImpl.class);

    @Override
    public Customer save(Customer customer) {
        return jpaUtils.save(customer, LOG);
    }

    @Override
    public void saveAll(List<Customer> customers) {
        jpaUtils.saveAll(customers, LOG);
    }

    @Override
    public Customer load(int id) {
        return jpaUtils.loadById(Customer.class, id, LOG);
    }

    @Override
    public Customer load(String name) {
        return jpaUtils.loadByName(name, Customer.LOAD_BY_NAME, LOG);
    }

    @Override
    public List<Customer> findAll() {
        return jpaUtils.findAll(Customer.LOAD_ALL, LOG);
    }

    @Override
    public void deleteById(int id) {
        jpaUtils.deleteById(id, Customer.DELETE_BY_ID, LOG);
    }

    @Override
    public void deleteAll() {
        jpaUtils.deleteAll(Customer.DELETE_ALL, LOG);
    }
}
