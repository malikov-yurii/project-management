package ua.com.malikov.dao.jpa;

import org.slf4j.Logger;
import ua.com.malikov.dao.ProjectDAO;
import ua.com.malikov.model.Developer;
import ua.com.malikov.model.Project;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class JpaProjectDAOImpl implements ProjectDAO{

    private static final Logger LOG = getLogger(JpaProjectDAOImpl.class);

    private JpaUtils jpaUtils;

    public void setJpaUtils(JpaUtils jpaUtils) {
        this.jpaUtils = jpaUtils;
    }

    @Override
    public Project save(Project project) {
        return jpaUtils.save(project, LOG);
    }

    @Override
    public void saveAll(List<Project> projects) {
        jpaUtils.saveAll(projects, LOG);
    }

    @Override
    public Project load(int id) {
        return jpaUtils.loadById(Project.class, id, LOG);
    }

    @Override
    public List<Project> findAll() {
        return jpaUtils.findAll(Project.LOAD_ALL, LOG);
    }

    @Override
    public void addDevToProject(Developer developer, Project project) {
        project.getDevelopers().add(developer);
        save(project);
    }

    @Override
    public void deleteById(int id) {
        jpaUtils.deleteById(id, Project.DELETE_BY_ID, LOG);
    }

    @Override
    public void deleteAll() {
        jpaUtils.deleteAll(Project.DELETE_ALL, LOG);
    }
}
