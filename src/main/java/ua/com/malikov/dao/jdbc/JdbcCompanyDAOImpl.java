package ua.com.malikov.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.malikov.dao.CompanyDAO;
import ua.com.malikov.model.Company;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCompanyDAOImpl implements CompanyDAO {

    private static final String DELETE_ALL  = "DELETE FROM pms.companies";
    private static final String DELETE_ROW  = String.format("DELETE FROM pms.companies WHERE %s = ?", Company.ID);
    private static final String GET_ALL     = "SELECT * FROM pms.companies";
    private static final String GET_BY_ID   = String.format("SELECT * FROM pms.companies WHERE %s =?", Company.ID);
    private static final String GET_BY_NAME = String.format("SELECT * FROM pms.companies WHERE %s =?", Company.NAME);
    private static final String INSERT_ROW  = String.format("INSERT INTO pms.companies (%s) VALUES (?)", Company.NAME);
    private static final String UPDATE_ROW  = String.format("UPDATE pms.companies SET %s = ? WHERE %s =?",
            Company.NAME, Company.ID);

    private static final Logger LOG = LoggerFactory.getLogger(CompanyDAO.class);

    private DataSource dataSource;

    @Override
    public Company save(Company company) {
        if (!company.isNew()) {
            return update(company);
        } else {
            return create(company);
        }
    }

    private Company create(Company company) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_ROW, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, company.getName());
                if (ps.executeUpdate() == 0) {
                    LOG.error("Creating company has failed.");
                    return null;
                }
                // set generated ID
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        company.setId(generatedKeys.getInt(1));
                    } else {
                        LOG.error("Creating company has failed. no ID obtained.");
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while creating company.", e);
            return null;
        }
        LOG.info("Company has been successfully created.");
        return company;
    }

    private Company update(Company company) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(UPDATE_ROW)) {
                ps.setString(1, company.getName());
                ps.setInt(2, company.getId());
                if (ps.executeUpdate() == 0) {
                    LOG.error("Updating company has failed.");
                    return null;
                }
                LOG.info("Company has been successfully updated.");
                return company;
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while updating company.", e);
            return null;
        }
    }

    /**
     * Saves list of companies to DB. Commit only if all objects will be saved.
     *
     * @param list of Company objects
     * @throws RuntimeException on SQLException and the Logger message
     */
    @Override
    public void saveAll(List<Company> list) {
        try (Connection connection = getConnection()) {
            //insert each object into BD
            for (Company object : list) {
                try (PreparedStatement ps = connection.prepareStatement(INSERT_ROW)) {
                    ps.setString(1, object.getName());

                    //break method if the object is  not saved, provided that no commit will be made
                    if (ps.executeUpdate() == 0) {
                        LOG.error("Saving companies has failed.");
                        return;
                    }
                }
            }
            LOG.info("Companies have been successfully saved.");
        } catch (SQLException e) {
            LOG.error("Exception occurred while saving companies.", e);
        }
    }

    /**
     * Deletes companies row by id
     *
     * @param id
     * @throws RuntimeException on SQLException and the Logger message
     */
    @Override
    public void deleteById(int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(DELETE_ROW)) {
                ps.setLong(1, id);
                ps.executeUpdate();
                LOG.info("Company has been successfully deleted by id.");
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while deleting company by id.", e);
        }
    }

    /**
     * Delete all rows from companies table
     * @throws RuntimeException on SQLException and the Logger message
     */
    @Override
    public void deleteAll() {
        try (Connection connection = getConnection()) {
            try (Statement st = connection.createStatement()) {
                st.executeUpdate(DELETE_ALL);
                LOG.info("All companies have been successfully deleted.");
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while deleting companies.", e);
        }
    }

    /**
     * Get company object by id
     *
     * @param id
     * @return company object, null if entry wasn't found
     * @throws RuntimeException on SQLException and the Logger message
     */
    @Override
    public Company load(int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(GET_BY_ID)) {
                ps.setInt(1, id);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (!resultSet.next()) {
                        LOG.error("Loading company has failed.");
                        return null;
                    }
                    LOG.info("Company has been successfully loaded by id.");
                    return new Company(resultSet.getInt(Company.ID), resultSet.getString(Company.NAME));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while loading company by id.", e);
            return null;
        }
    }

    /**
     * Get company object by name
     *
     * @param name
     * @return company object, null if entry wasn't found
     */
    @Override
    public Company load(String name) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(GET_BY_NAME)) {
                ps.setString(1, name);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (!resultSet.next()) {
                        LOG.info("Loading company by name has failed.");
                        return null;
                    }
                    LOG.info("Company has been successfully loaded by name.");
                    return new Company(resultSet.getInt(Company.ID), resultSet.getString(Company.NAME));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while loading company by name.", e);
            return null;
        }
    }

    @Override
    public List<Company> findAll() {
        try (Connection connection = getConnection()) {
            try (Statement st = connection.createStatement()) {
                try (ResultSet resultSet = st.executeQuery(GET_ALL)) {
                    List<Company> companies = new ArrayList<>();
                    while (resultSet.next()) {
                        companies.add(new Company(resultSet.getInt(Company.ID), resultSet.getString(Company.NAME)));
                    }
                    LOG.info("All companies has been successfully retrieved from DB.");
                    return companies;
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while loading companies.", e);
            return null;
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}