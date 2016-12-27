package ua.com.malikov.dao.hibernate;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import ua.com.malikov.dao.ProjectDAO;
import ua.com.malikov.model.Developer;
import ua.com.malikov.model.Project;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class HProjectDAOImpl implements ProjectDAO{

    private EntityManagerFactory emf;

    private static final Logger LOG = getLogger(HProjectDAOImpl.class);

    @Override
    @Transactional
    public Project save(Project project) {
        return HUtils.save(project, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public void saveAll(List<Project> projects) {
        HUtils.saveAll(projects, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public Project load(int id) {
        return HUtils.loadById(Project.class, id, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public List<Project> findAll() {
        return HUtils.findAll("from Project", sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public void addDevToProject(Developer developer, Project project) {
        project.getDevelopers().add(developer);
        save(project);
    }

    @Override
    @Transactional
    public void delete(Project project) {
        HUtils.delete(project, sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        HUtils.deleteById(id, "delete Project where id=:id",
                sessionFactory.getCurrentSession(), LOG);
    }

    @Override
    @Transactional
    public void deleteAll() {
        HUtils.deleteAll("delete Project", sessionFactory.getCurrentSession(), LOG);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
