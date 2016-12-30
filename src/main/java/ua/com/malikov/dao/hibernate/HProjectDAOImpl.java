package ua.com.malikov.dao.hibernate;

import org.slf4j.Logger;
import ua.com.malikov.dao.ProjectDAO;
import ua.com.malikov.model.Developer;
import ua.com.malikov.model.Project;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HProjectDAOImpl implements ProjectDAO{

    private static final Logger LOG = getLogger(HProjectDAOImpl.class);

    private HUtils hUtils;

    public void sethUtils(HUtils hUtils) {
        this.hUtils = hUtils;
    }

    @Override
    public Project save(Project project) {
        return hUtils.save(project, LOG);
    }

    @Override
    public void saveAll(List<Project> projects) {
        hUtils.saveAll(projects, LOG);
    }

    @Override
    public Project load(int id) {
        return hUtils.loadById(Project.class, id, LOG);
    }

    @Override
    public List<Project> findAll() {
        return hUtils.findAll(Project.LOAD_ALL, LOG);
    }

    @Override
    public void addDevToProject(Developer developer, Project project) {
        project.getDevelopers().add(developer);
        save(project);
    }

    @Override
    public void deleteById(int id) {
        hUtils.deleteById(id, Project.DELETE_BY_ID, LOG);
    }

    @Override
    public void deleteAll() {
        hUtils.deleteAll(Project.DELETE_ALL, LOG);
    }
}
