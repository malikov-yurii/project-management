package ua.com.malikov.dao;

import ua.com.malikov.model.Customer;

import java.sql.SQLException;

public interface CustomerDAO extends AbstractDAO<Customer> {

    Customer load(String name)throws SQLException;
}
