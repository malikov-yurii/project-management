package ua.com.malikov.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ua.com.malikov.dao.DeveloperDAO;
import ua.com.malikov.model.Developer;
import ua.com.malikov.model.Skill;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeveloperController extends AbstractController<Developer> {

    private static final Logger LOG = LoggerFactory.getLogger(DeveloperController.class);

    private DeveloperDAO developerDAO;

    @Transactional
    public void addSkillsToDeveloper(Set<Skill> skills, Developer developer){
        developer.setSkills(skills);
        developerDAO.save(developer);

    }

    @Override
    @Transactional
    public Developer add(Developer developer) {
        if (isNullThanPrintAndLogErrorMessageFor(developer)) return null;
        Set<Developer> developers = new HashSet<>(developerDAO.findAll());
        if (!developers.contains(developer)) {
            return developerDAO.save(developer);
        } else {
            LOG.error("Developer " + developer + " wasn't saved to database." +
                    " There is an identical developer in the database already.");
            return null;
        }
    }

    @Override
    @Transactional
    public Developer get(int id) throws SQLException {
        return developerDAO.load(id);
    }

    @Override
    @Transactional
    public List<Developer> getAll(){
        return developerDAO.findAll();
    }

    @Override
    @Transactional
    public void update(Developer developer){
        if (isNullThanPrintAndLogErrorMessageFor(developer.getCompany())) return;
        developerDAO.save(developer);
    }

    @Override
    @Transactional
    public void delete(int id){developerDAO.deleteById(id);}

    @Override
    @Transactional
    public void deleteAll(){developerDAO.deleteAll();}

    public void setDeveloperDAO(DeveloperDAO developerDAO) {
        this.developerDAO = developerDAO;
    }
}
