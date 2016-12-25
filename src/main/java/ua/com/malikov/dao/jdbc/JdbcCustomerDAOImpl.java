package ua.com.malikov.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.malikov.model.Customer;
import ua.com.malikov.dao.CustomerDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcCustomerDAOImpl implements CustomerDAO {

    private static final String DELETE_ALL  = "DELETE FROM pms.customers";
    private static final String DELETE_ROW  = String.format("DELETE FROM pms.customers WHERE %s = ?", Customer.ID);
    private static final String GET_ALL     = "SELECT * FROM pms.customers";
    private static final String GET_BY_ID   = String.format("SELECT * FROM pms.customers WHERE %s =?", Customer.ID);
    private static final String GET_BY_NAME = String.format("SELECT * FROM pms.customers WHERE %s =?", Customer.NAME);
    private static final String INSERT_ROW  = String.format("INSERT INTO pms.customers (%s) VALUES (?)", Customer.NAME);
    private static final String UPDATE_ROW  = String.format("UPDATE pms.customers SET %s = ? WHERE %s =?",
            Customer.NAME, Customer.ID);

    private static final Logger LOG = LoggerFactory.getLogger(CustomerDAO.class);

    private DataSource dataSource;

    @Override
    public Customer save(Customer customer) {
        if (!customer.isNew()) {
            return update(customer);
        } else {
            return create(customer);
        }
    }

    private Customer create(Customer customer) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_ROW, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, customer.getName());
                if (ps.executeUpdate() == 0) {
                    LOG.error("Creating customer has failed.");
                    return null;
                }
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        customer.setId(generatedKeys.getInt(1));
                    } else {
                        LOG.error("Creating customer has failed.");
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while creating customer.", e);
            return null;
        }
        LOG.info("Customer has been successfully created.");
        return customer;
    }

    private Customer update(Customer customer) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(UPDATE_ROW)) {
                ps.setString(1, customer.getName());
                ps.setInt(2, customer.getId());
                if (ps.executeUpdate() == 0) {
                    LOG.error("Updating customer has failed.");
                    return null;
                }
                LOG.info("Customer has been successfully updated.");
                return customer;
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while updating customer.", e);
            return null;
        }
    }

    @Override
    public void saveAll(List<Customer> list) {
        try (Connection connection = getConnection()) {
            //insert each customer into BD
            for (Customer customer : list) {
                try (PreparedStatement ps = connection.prepareStatement(INSERT_ROW)) {
                    ps.setString(1, customer.getName());
                    //break method if the customer is  not saved, provided that no commit will be made
                    if (ps.executeUpdate() == 0) {
                        LOG.error("Saving all customers has failed.");
                        return;
                    }
                }
                LOG.info("Customers have been successfully saved.");
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while saving customers.", e);
        }
    }

    @Override
    public Customer load(int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(GET_BY_ID)) {
                ps.setInt(1, id);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (!resultSet.next()) {
                        LOG.info("Loading customer by id has failed.");
                        return null;
                    }
                    LOG.info("Customer has been successfully loaded by id.");
                    return new Customer(resultSet.getInt(Customer.ID), resultSet.getString(Customer.NAME));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while loading customer by id.", e);
            return null;
        }
    }

    @Override
    public List<Customer> findAll() {
        try (Connection connection = getConnection()) {
            try (Statement st = connection.createStatement()) {
                try (ResultSet resultSet = st.executeQuery(GET_ALL)) {
                    List<Customer> customers = new ArrayList<>();
                    while (resultSet.next()) {
                        customers.add(new Customer(resultSet.getInt(Customer.ID), resultSet.getString(Customer.NAME)));
                    }
                    LOG.info("All customers have been successfully found.");
                    return customers;
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while search all customers.", e);
            return null;
        }
    }

    @Override
    public void delete(Customer customer) {
        deleteById(customer.getId());
    }

    @Override
    public void deleteById(int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(DELETE_ROW)) {
                ps.setLong(1, id);
                ps.executeUpdate();
                LOG.info("Customer have been successfully deleted by id.");
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while deleting customer by id.", e);
        }
    }

    @Override
    public void deleteAll() {
        try (Connection connection = getConnection()) {
            try (Statement st = connection.createStatement()) {
                st.executeUpdate(DELETE_ALL);
                LOG.info("All customers have been successfully loaded.");
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while deleting all customers.", e);
        }
    }

    @Override
    public Customer load(String name) throws SQLException {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(GET_BY_NAME)) {
                ps.setString(1, name);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (!resultSet.next()) {
                        LOG.info("Loading customer by name has failed.");
                        return null;
                    }
                    LOG.info("Customer has been successfully loaded by name.");
                    return new Customer(resultSet.getInt(Customer.ID), resultSet.getString(Customer.NAME));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while loading customer by name.", e);
            return null;
        }
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
