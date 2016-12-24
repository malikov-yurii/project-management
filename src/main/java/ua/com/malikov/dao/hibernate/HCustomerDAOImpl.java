package ua.com.malikov.dao.hibernate;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import ua.com.malikov.dao.CustomerDAO;
import ua.com.malikov.model.Customer;

import java.sql.SQLException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HCustomerDAOImpl implements CustomerDAO {

    private SessionFactory sessionFactory;

    private static final Logger LOG = getLogger(HCustomerDAOImpl.class);

    @Override
    public Customer save(Customer customer) {
        return HUtils.save(customer, sessionFactory.getCurrentSession(), LOG);
    }
    @Override
    public void saveAll(List<Customer> customers) {
        HUtils.saveAll(customers, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public Customer load(int id) {
        return HUtils.loadById(Customer.class, id, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public Customer load(String name) throws SQLException {
        return HUtils.loadByName(name, "from Company where name like :name",
                sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public List<Customer> findAll() {
        return HUtils.findAll("from Customer", sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public void delete(Customer customer) {
        HUtils.delete(customer, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public void deleteById(int id) {
        HUtils.deleteById(id, "delete Customer where id=:id", sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    public void deleteAll() {
        HUtils.deleteAll("delete Customer", sessionFactory.getCurrentSession(), LOG);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
