package ua.com.malikov.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.com.malikov.dao.ProjectDAO;
import ua.com.malikov.model.Developer;
import ua.com.malikov.model.Project;

import java.sql.SQLException;
import java.util.List;

public class ProjectService extends AbstractService<Project> {

    private ProjectDAO projectDAO;
    @Transactional
    public void addDeveloperToProject(Developer developer, Project project){
        projectDAO.addDevToProject(developer,project);
    }

    @Override
    @Transactional
    public Project add(Project project){
        if (isNullThanPrintAndLogErrorMessageFor(project)) return null;
        return projectDAO.save(project);}

    @Transactional(propagation = Propagation.REQUIRED)
    public Project get(int id) throws SQLException {
        return projectDAO.load(id);
    }

    @Transactional
    public List<Project> getAll() throws SQLException {
        return projectDAO.findAll();
    }

    @Override
    @Transactional
    public void update(Project project){
        if (isNullThanPrintAndLogErrorMessageFor(project)) return;
        projectDAO.save(project);
    }

    @Override
    @Transactional
    public void delete(int id){projectDAO.deleteById(id);}

    @Override
    @Transactional
    public void deleteAll(){projectDAO.deleteAll();}

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }
}
