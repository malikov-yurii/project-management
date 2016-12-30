package ua.com.malikov.dao.hibernate;

import org.slf4j.Logger;
import ua.com.malikov.dao.CustomerDAO;
import ua.com.malikov.model.Company;
import ua.com.malikov.model.Customer;

import java.sql.SQLException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HCustomerDAOImpl implements CustomerDAO {

    private HUtils hUtils;

    public void sethUtils(HUtils hUtils) {
        this.hUtils = hUtils;
    }

    private static final Logger LOG = getLogger(HCustomerDAOImpl.class);

    @Override
    public Customer save(Customer customer) {
        return hUtils.save(customer, LOG);
    }

    @Override
    public void saveAll(List<Customer> customers) {
        hUtils.saveAll(customers, LOG);
    }

    @Override
    public Customer load(int id) {
        return hUtils.loadById(Customer.class, id, LOG);
    }

    @Override
    public Customer load(String name) throws SQLException {
        return hUtils.loadByName(name, Customer.LOAD_BY_NAME, LOG);
    }

    @Override
    public List<Customer> findAll() {
        return hUtils.findAll(Customer.LOAD_ALL, LOG);
    }

    @Override
    public void deleteById(int id) {
        hUtils.deleteById(id, Customer.DELETE_BY_ID, LOG);
    }

    @Override
    public void deleteAll() {
        hUtils.deleteAll(Company.DELETE_ALL, LOG);
    }
}
