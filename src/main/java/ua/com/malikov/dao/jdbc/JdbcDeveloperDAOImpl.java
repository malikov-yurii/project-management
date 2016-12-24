package ua.com.malikov.dao.jdbc;

import org.slf4j.Logger;
import ua.com.malikov.dao.CompanyDAO;
import ua.com.malikov.dao.DeveloperDAO;
import ua.com.malikov.dao.SkillDAO;
import ua.com.malikov.model.Developer;
import ua.com.malikov.model.Skill;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

public class JdbcDeveloperDAOImpl implements DeveloperDAO {

    private static final String UPDATE_ROW = String.format(
            "UPDATE pms.developers SET %s = ?, %s = ?, %s = ? WHERE %s = ?",
            Developer.FIRST_NAME, Developer.LAST_NAME, Developer.COMPANY_ID, Developer.ID);

    private static final String INSERT_ROW = String.format(
            "INSERT INTO pms.developers (%s, %s, %s) VALUES (?,?,?)",
            Developer.FIRST_NAME, Developer.LAST_NAME, Developer.COMPANY_ID);

    private static final String GET_BY_ID = String.format(
            "SELECT * FROM pms.developers WHERE %s = ?", Developer.ID);

    private static final String GET_ALL = "SELECT * FROM pms.developers";

    private static final String DELETE_BY_ID = String.format(
            "DELETE FROM pms.developers WHERE %s = ?", Developer.ID);;

    private static final String DELETE_SKILLS_FROM_DEVELOPER =
            "DELETE FROM pms.developers_skills WHERE developer_id = ?";

    private static final String ADD_SKILLS_TO_DEVELOPER =
            "INSERT INTO pms.developers_skills(developer_id, skill_id) VALUES (?,?)";

    private static final String GET_SKILLS_OF_DEVELOPER =
            "SELECT skill_id FROM pms.developers_skills WHERE developer_id=?";

    @Override
    public void delete(Developer developer) {
        deleteById(developer.getId());
    }

    private static final String DELETE_ALL = "DELETE FROM pms.developers";

    private static final Logger LOG = getLogger(JdbcDeveloperDAOImpl.class);

    private DataSource dataSource;

    private CompanyDAO companyDAO;

    private SkillDAO skillDAO;

    @Override
    public Developer save(Developer developer) {
        if (!developer.isNew()) {
            update(developer);
            removeSkillsOf(developer);
        } else {
            create(developer);
        }
        addSkillsTo(developer);
        return developer;
    }

    private Developer update(Developer developer) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(UPDATE_ROW)) {
                ps.setString(1, developer.getName());
                ps.setString(2, developer.getLastName());
                if (developer.getCompany() != null)
                    ps.setInt(3, developer.getCompany().getId());
                else
                    ps.setNull(3, Types.INTEGER);
                ps.setInt(4, developer.getId());

                if (ps.executeUpdate() == 0) {
                    LOG.error("Updating developer failed, no rows affected");
                    return null;
                }
                return developer;
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while updating developer.", e);
            return null;
        }
    }

    private Developer create(Developer developer) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(INSERT_ROW, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, developer.getName());
                ps.setString(2, developer.getLastName());
                ps.setInt(3, developer.getCompany().getId());
                if (ps.executeUpdate() == 0) {
                    LOG.error("Creating developer failed, no rows affected.");
                    return null;
                }
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        developer.setId(generatedKeys.getInt(1));
                    } else {
                        LOG.error("Creating developer failed, no ID obtained.");
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while creating developer.", e);
            return null;
        }
        return developer;
    }

    @Override
    public void saveAll(List<Developer> list) {
        list.forEach(this::save);
    }

    @Override
    public Developer load(int id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    Developer developer;
                    if (resultSet.next()) {
                        developer = createDeveloper(resultSet);
                        retrieveSkillsOf(developer);
                        LOG.info("Developer " + developer + " successfully loaded from DB.");
                        return developer;
                    } else {
                        LOG.info("Developer was not found.");
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while loading developer by id.", e);
            return null;
        }
    }

    @Override
    public List<Developer> findAll() {
        List<Developer> developers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(GET_ALL)) {
                    while (resultSet.next()) {
                        Developer developer = createDeveloper(resultSet);
                        retrieveSkillsOf(developer);
                        developers.add(developer);
                    }
                    LOG.info("Finding developers in DB was successful.");
                    return developers;
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while finding all developers.", e);
            return null;
        }
    }

    @Override
    public void deleteById(int id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
                preparedStatement.setInt(1, id);
                preparedStatement.execute();
            }
            LOG.info("Developer was successfully deleted.");
        } catch (SQLException e) {
            LOG.error("Exception occurred while deleting developer by id.", e);
        }
    }

    @Override
    public void deleteAll() {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(DELETE_ALL);
            }
            LOG.info("All developers were successfully deleted.");
        } catch (SQLException e) {
            LOG.error("Exception occurred while deleting all developers.", e);
        }
    }

    private Developer createDeveloper(ResultSet resultSet) throws SQLException {
        Developer developer = new Developer();
        developer.setId(resultSet.getInt("id"));
        developer.setName(resultSet.getString("first_name"));
        developer.setLastName(resultSet.getString("last_name"));
        developer.setCompany(companyDAO.load(resultSet.getInt("company_id")));
        return developer;
    }

    private void removeSkillsOf(Developer developer) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(DELETE_SKILLS_FROM_DEVELOPER);
            preparedStatement.setInt(1, developer.getId());
            preparedStatement.execute();
            LOG.info("Removing skills of developer from DB was successful.");
        } catch (SQLException e) {
            LOG.error("Exception occurred while removing skills of developer.", e);
        }
    }

    private void addSkillsTo(Developer developer) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_SKILLS_TO_DEVELOPER)) {
                for (Skill skill : developer.getSkills()) {
                    preparedStatement.setInt(1, developer.getId());
                    preparedStatement.setInt(2, skill.getId());
                    preparedStatement.execute();
                }
            }
            LOG.info("Adding skills to developer was successful.");
        } catch (SQLException e) {
            LOG.error("Exception occurred while adding skills to developer.", e);
        }
    }

    private void retrieveSkillsOf(Developer developer) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    GET_SKILLS_OF_DEVELOPER)) {
                preparedStatement.setInt(1, developer.getId());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    Set<Skill> skills = new HashSet<>();
                    while (resultSet.next()) {
                        skills.add(skillDAO.load(resultSet.getInt("skill_id")));
                    }
                    developer.setSkills(skills);
                }
            }
            LOG.info("Retrieving skills of developer from DB was successful.");
        } catch (SQLException e) {
            LOG.error("Exception occurred while retrieving skills of developer from DB.", e);
        }
    }

    public void setCompanyDAO(CompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void setSkillDAO(SkillDAO skillDAO) {
        this.skillDAO = skillDAO;
    }
}
