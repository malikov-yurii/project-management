package ua.com.malikov.dao;

import ua.com.malikov.model.Developer;
import ua.com.malikov.model.Project;

public interface ProjectDAO extends AbstractDAO<Project> {
   void addDevToProject(Developer developer, Project project);
}
