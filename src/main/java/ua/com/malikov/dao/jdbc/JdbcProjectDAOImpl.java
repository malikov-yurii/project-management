package ua.com.malikov.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.malikov.dao.CompanyDAO;
import ua.com.malikov.dao.DeveloperDAO;
import ua.com.malikov.dao.ProjectDAO;
import ua.com.malikov.model.Developer;
import ua.com.malikov.model.Project;
import ua.com.malikov.dao.CustomerDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JdbcProjectDAOImpl implements ProjectDAO {

    private static final String GET_ALL = "SELECT * FROM pms.projects ";

    private static final String GET_BY_ID = String.format("SELECT * FROM pms.projects WHERE %s = ?",
            Project.ID);

    private static final String UPDATE_ROW = String.format(
            "UPDATE pms.projects SET %s = ?, %s = ?, %s =? WHERE %s =?",
            Project.PROJECT_NAME, Project.CUSTOMER_ID, Project.COMPANY_ID, Project.ID);

    private static final String INSERT_ROW = String.format(
            "INSERT INTO pms.projects (%s, %s, %s, %s) VALUES (?,?,?,?)",
            Project.PROJECT_NAME, Project.CUSTOMER_ID, Project.COMPANY_ID, Project.COST);

    private static final String REMOVE_DEVELOPER_FROM_PROJECT = "DELETE FROM pms.projects_developers WHERE project_id=?";

    private static final String ADD_DEVELOPERS_TO_PROJECT = "INSERT INTO pms.projects_developers(developer_id, project_id) VALUES (?,?)";

    @Override
    public void delete(Project project) {

    }

    private static final String DELETE_ALL = "DELETE FROM pms.projects CASCADE";

    private static final String GET_DEVS_BY_PROJECT_ID = "SELECT developer_id FROM pms.projects_developers WHERE project_id = ?";

    private static final Logger LOG = LoggerFactory.getLogger(ProjectDAO.class);

    private CompanyDAO companyDAO;

    private CustomerDAO customerDAO;

    private DeveloperDAO developerDAO;

    private DataSource dataSource;

    @Override
    public Project save(Project project) {
        if (!project.isNew()) {
            update(project);
            removeDeveloperFrom(project);
        } else {
            create(project);
        }
        addDevelopersTo(project);
        return project;
    }

    private void removeDeveloperFrom(Project project) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(REMOVE_DEVELOPER_FROM_PROJECT)) {
            ps.setInt(1, project.getId());
            ps.execute();
            LOG.info("Removing developer from project was successful.");
        } catch (SQLException e) {
            LOG.error("Exception occurred while removing developers from project. ", e);
        }
    }

    private void addDevelopersTo(Project project) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_DEVELOPERS_TO_PROJECT)) {
            for (Developer developer : project.getDevelopers()) {
                preparedStatement.setInt(2, project.getId());
                preparedStatement.setInt(1, developer.getId());
                preparedStatement.execute();
                LOG.info("Adding developers to project was successful.");
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while adding developers to project. ", e);
        }
    }

    private Project create(Project project) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ROW, PreparedStatement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, project.getName());
                preparedStatement.setInt(2, project.getCustomer().getId());
                preparedStatement.setInt(3, project.getCompany().getId());
                preparedStatement.setFloat(4, project.getCost());

                if (preparedStatement.executeUpdate() == 0) {
                    LOG.error("Exception occurred while saving project.");
                    return null;
                }
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        project.setId(generatedKeys.getInt(1));
                        LOG.info("Creating project was successful.");
                        return project;
                    } else {
                        LOG.error("Exception occurred while saving project. ");
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while saving project.", e);
            return null;
        }
    }

    public Project update(Project project) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ROW)) {

                preparedStatement.setString(1, project.getName());
                preparedStatement.setInt(2, project.getCustomer().getId());
                preparedStatement.setInt(3, project.getCompany().getId());
                preparedStatement.setInt(4, project.getId());

                if (preparedStatement.executeUpdate() == 0) {
                    LOG.error("No row in projects was affected");
                    return null;
                }
                LOG.info("Updating was successful.");
                return project;
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception occurred: ", e);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(DELETE_ALL);
                LOG.info("Deleting all projects was successful.");
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while deleting all projects.", e);
        }
    }

    @Override
    public void saveAll(List<Project> list) {
        try (Connection connection = getConnection()) {
            //insert each customer into BD
            for (Project project : list) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ROW)) {
                    preparedStatement.setString(1, project.getName());
                    //break method if the customer is  not saved, provided that no commit will be made
                    if (preparedStatement.executeUpdate() == 0) {
                        LOG.error("Project was not saved");
                        return;
                    }
                    LOG.info("Saving all projects was successful.");
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while saving all projects.", e);
        }
    }

    @Override
    public Project load(int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(GET_BY_ID)) {
                ps.setInt(1, id);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (!resultSet.next()) {
                        LOG.error("Project was not loaded by id.");
                        return null;
                    }
                    LOG.info("Loading project by id was successful.");
                    return new Project(
                            resultSet.getInt(Project.ID),
                            resultSet.getString(Project.PROJECT_NAME),
                            companyDAO.load(resultSet.getInt(Project.COMPANY_ID)),
                            customerDAO.load(resultSet.getInt(Project.CUSTOMER_ID)),
                            getDevelopersByProjectId(id),
                            resultSet.getFloat(Project.COST));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while loading project.", e);
            return null;
        }
    }

    private Set<Developer> getDevelopersByProjectId(int id) throws SQLException {
        Set<Developer> developers = new HashSet<>();
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_DEVS_BY_PROJECT_ID)) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    developers.add(developerDAO.load(resultSet.getInt("developer_id")));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while getting developers by project id.", e);
            return null;
        }
        LOG.info("Getting developers by project id was successful.");
        return developers;
    }

    @Override
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(GET_ALL)) {
                while (resultSet.next()) {
                    Project project = new Project();
                    int projectId = resultSet.getInt(Project.ID);
                    project.setId(projectId);
                    project.setName(resultSet.getString(Project.PROJECT_NAME));
                    project.setCompany(companyDAO.load(resultSet.getInt(Project.COMPANY_ID)));
                    project.setCustomer(customerDAO.load(resultSet.getInt(Project.CUSTOMER_ID)));
                    project.setCost(resultSet.getFloat(Project.COST));
                    project.setDevelopers(getDevelopersByProjectId(projectId));
                    projects.add(project);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while finding all projects", e);
            return null;
        }
        LOG.info("Finding all projects was successful.");
        return projects;
    }

    @Override
    public void deleteById(int id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM pms.projects WHERE id=?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            LOG.info("Deleting project by id was successful.");
        } catch (SQLException e) {
            LOG.error("Exception occurred while deleting project by id", e);
        }
    }

    @Override
    public void addDevToProject(Developer developer, Project project) {
        if (developer.isNew() && project.isNew()) {
            LOG.error("Adding developer to project failed.");
        } else {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection
                         .prepareStatement("INSERT INTO pms.projects_developers (project_id, developer_id) VALUES (?,?)")) {
                if (project.getId() != null && developer.getId() != null) {
                    preparedStatement.setInt(1, project.getId());
                    preparedStatement.setInt(2, developer.getId());
                    preparedStatement.execute();
                    LOG.info("Adding developer to project was successful.");
                } else {
                    LOG.error("Can\'t add developer to project, wrong parameters.");
                }
            } catch (SQLException e) {
                LOG.error("Exception occurred while adding developers to project", e);
            }
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setCompanyDAO(CompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void setDeveloperDAO(DeveloperDAO developerDAO) {
        this.developerDAO = developerDAO;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
