package ua.com.malikov.controller;

import org.springframework.transaction.annotation.Transactional;
import ua.com.malikov.dao.CustomerDAO;
import ua.com.malikov.model.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerController extends AbstractController<Customer> {

    private CustomerDAO customerDAO;

    //if table already have company with same name, than just return entity form table, don't create new one
    @Transactional
    @Override
    public Customer add(Customer customer) throws SQLException {
        if (isNullThanPrintAndLogErrorMessageFor(customer)) return null;

        //search customer by name
        Customer customerRetrievedByName = customerDAO.load(customer.getName());

        //if wasn't found by name add row to db
        if (customerRetrievedByName == null) {
            customerDAO.save(customer);
            return customer;
        } else
            return customerRetrievedByName;
    }

    @Transactional
    @Override
    public Customer get(int id) throws SQLException {
        return customerDAO.load(id);
    }

    @Transactional
    @Override
    public List<Customer> getAll() throws SQLException {
        return customerDAO.findAll();
    }

    @Transactional
    @Override
    public void update(Customer customer) throws SQLException {
        if (isNullThanPrintAndLogErrorMessageFor(customer)) return;
        customerDAO.save(customer);
    }

    @Transactional
    @Override
    public void delete(int id) throws SQLException {
        customerDAO.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() throws SQLException {
        customerDAO.deleteAll();
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }
}
