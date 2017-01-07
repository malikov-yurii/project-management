package ua.com.malikov.dao.jpa;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import ua.com.malikov.model.NamedEntity;

import javax.persistence.*;
import java.util.List;

public class JpaUtils {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    <T extends NamedEntity> T save(T t, Logger LOG) {
        boolean isNew = t.isNew();
        if (isNew) {
            em.persist(t);
        } else {
            em.merge(t);
        }
        LOG.info(t + " was " + (isNew ? "created" : "updated") + " successfully.");
        return t;
    }

    <T extends NamedEntity> void saveAll(List<T> list, Logger LOG) {
        list.forEach(em::persist);
        LOG.info("All entities of " + list.getClass().getSuperclass().getSimpleName() + " were saved successfully.");
    }

    <T extends NamedEntity> T loadById(Class<T> tClass, int id, Logger LOG) {
        T t = em.find(tClass, id);
        if (t == null) {
            LOG.error("Cannot find " + tClass.getSuperclass().getSimpleName() + " by id ID: " + id);
        } else {
            LOG.info(t + " successfully loaded from DB by ID.");
        }
        return t;
    }


    <T extends NamedEntity> List<T> findAll(String hql, Logger LOG) {
        List<T> resultList = (List<T>) em.createNamedQuery(hql).getResultList();
        if (resultList == null) {
            LOG.error("Search for all entities has failed.");
        } else {
            LOG.info("Search for all entities has been successful.");
        }
        return resultList;
    }

    @Transactional
    <T extends NamedEntity> void deleteById(int id, String hql, Logger LOG) {
        Query query = em.createNamedQuery(hql);
        query.setParameter("id", id);
        if (query.executeUpdate() == 1) {
            LOG.info("Deleting entity by ID has been successful.");
        } else {
            LOG.error("Deleting entity by ID has been failed.");
        }
    }

    public <T extends NamedEntity> void deleteAll(String hql, Logger LOG) {
        Query query = em.createNamedQuery(hql);
        if (query.executeUpdate() != 0) {
            LOG.info("Deleting of all entities has been successful.");
        } else {
            LOG.error("Deleting of all entities has failed.");
        }
    }

    <T extends NamedEntity> T loadByName(String name, String hql, Logger LOG) {
        Query query = em.createNamedQuery(hql);
        query.setParameter("name", name);
        T t = null;
        try {
            if (query.getResultList().size() == 0) {
                LOG.error("Cannot find by name: " + name);
            } else {
                t = (T) query.getSingleResult();
                LOG.info("Successful search by name.");
            }
        } catch (NoResultException e) {
            LOG.error("Cannot find entity by name: " + name);
        } catch (NonUniqueResultException e) {
            LOG.error("Error! Name should be unique! Several entities has been found by name: " + name);
        }
        return t;
    }
}
