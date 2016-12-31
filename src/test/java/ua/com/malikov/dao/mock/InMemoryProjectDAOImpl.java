package ua.com.malikov.dao.mock;

import ua.com.malikov.dao.ProjectDAO;
import ua.com.malikov.model.Developer;
import ua.com.malikov.model.Project;

public class InMemoryProjectDAOImpl extends InMemoryAbstractDAOImpl<Project> implements ProjectDAO {
    @Override
    public void addDevToProject(Developer developer, Project project) {
        project.getDevelopers().add(developer);
    }
}
