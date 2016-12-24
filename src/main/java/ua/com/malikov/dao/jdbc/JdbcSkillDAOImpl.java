package ua.com.malikov.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.malikov.dao.SkillDAO;
import ua.com.malikov.model.Skill;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcSkillDAOImpl implements SkillDAO {

    private static final String GET_BY_ID = String.format("SELECT * FROM pms.skills WHERE %s=?", Skill.ID);
    private static final String INSERT_ROW = String.format("INSERT INTO pms.skills (%s) VALUES (?)", Skill.NAME);
    private static final String GET_BY_NAME = String.format("SELECT (id) FROM pms.skills WHERE %s = ?", Skill.NAME);
    private static final String UPDATE_ROW = String.format("UPDATE pms.skills SET %s = ? WHERE %s = ?", Skill.NAME, Skill.ID);
    private static final String GET_ALL = "SELECT * FROM pms.skills";
    private static final String DELETE_BY_ID = String.format("DELETE FROM pms.skills WHERE %s = ?", Skill.ID);
    private static final String DELETE_ALL = "DELETE FROM pms.skills";

    private static final Logger LOG = LoggerFactory.getLogger(JdbcSkillDAOImpl.class);
    private DataSource dataSource;

    @Override
    public Skill save(Skill skill) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null ;
        try (Connection connection = dataSource.getConnection()) {
            String sql;
            if (skill.isNew()) {
                sql = INSERT_ROW;
                preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                sql = UPDATE_ROW;
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(2, skill.getId());
            }
            preparedStatement.setString(1, skill.getName());
            preparedStatement.executeUpdate();

            if (skill.isNew()) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    skill.setId(resultSet.getInt(1));
                } else {
                    LOG.error("Something went wrong. Couldn't retrieve generated key from database.");
                }
            }
            LOG.info("Skill was successfully saved.");
            return skill;
        } catch (SQLException e) {
            LOG.error("Exception occurred while saving skill", e);
            return null;
        }
        finally {
            try{
                if( resultSet != null)
                    resultSet.close();
            }
            catch (SQLException e){
                LOG.error("Error while closing result set", e);
            }
            try{
                if( preparedStatement!= null)
                    preparedStatement.close();
            }
            catch (SQLException e){
                LOG.error("Error while closing prepared statement", e);
            }
        }
    }

    @Override
    public void saveAll(List<Skill> skills) {
        skills.forEach(this::save);
    }

    @Override
    public Skill load(int id) {
        try (Connection connection = dataSource.getConnection()) {
            try( PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID)) {
                preparedStatement.setInt(1, id);
                try ( ResultSet resultSet = preparedStatement.executeQuery() ) {
                    Skill skill;
                    if (resultSet.next()) {
                        skill = createSkill(resultSet);
                        LOG.info("Skill " + skill + " was successfully found in database.");
                        return skill;
                    } else {
                        LOG.error("Skill was not found.");
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while loading skill", e);
            throw new RuntimeException(e);
        }
    }

    public Skill getByName(String name){
        try (Connection connection = dataSource.getConnection()) {
            try( PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_NAME)){
                preparedStatement.setString(1, name);
                try ( ResultSet resultSet = preparedStatement.executeQuery() ) {
                    if (resultSet.next()) {
                        Skill skill = new Skill(resultSet.getInt(Skill.ID), name);
                        LOG.info("Skill " + skill + " was successfully found in database.");
                        return skill;
                    } else {
                        LOG.error("Skill was not found.");
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred getting skill by name", e);
            return null;
        }
    }

    @Override
    public List<Skill> findAll() {
        List<Skill> skills = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try( Statement statement = connection.createStatement() ) {
                try( ResultSet resultSet = statement.executeQuery(GET_ALL) ) {
                    while (resultSet.next()) {
                        skills.add(createSkill(resultSet));
                    }
                    LOG.info("All skills were successfully found.");
                    return skills;
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while finding all skills", e);
            return null;
        }
    }

    @Override
    public void delete(Skill skill) {
        deleteById(skill.getId());
    }

    @Override
    public void deleteById(int id) {
        try (Connection connection = dataSource.getConnection()) {
            try( PreparedStatement preparedStatement =
                         connection.prepareStatement(DELETE_BY_ID)) {
                preparedStatement.setInt(1, id);
                preparedStatement.execute();
                LOG.info("Skill was successfully deleted.");
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while deleting skill by id", e);
        }
    }

    @Override
    public void deleteAll() {
        try (Connection connection = dataSource.getConnection()) {
            try( Statement statement = connection.createStatement() ) {
                statement.execute(DELETE_ALL);
                LOG.info("All skills were successfully deleted.");
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred while deleting all skills", e);
        }
    }

    private Skill createSkill(ResultSet resultSet) throws SQLException {
        Skill skill = new Skill();
        skill.setId(resultSet.getInt(Skill.ID));
        skill.setName(resultSet.getString(Skill.NAME));
        return skill;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
