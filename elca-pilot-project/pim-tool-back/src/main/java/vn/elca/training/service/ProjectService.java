package vn.elca.training.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import vn.elca.training.model.dto.ProjectDto;
import vn.elca.training.model.entity.Group;
import vn.elca.training.model.entity.Project;

import java.time.LocalDate;
import java.util.List;

/**
 * @author vlp
 */
public interface ProjectService{
    Project addProject(ProjectDto dto);

    List<Project> findAllProjects();

    long count();

    public List<Group> findAllGroup();

    Project getProjectById(Long id);

    public List<Project> searchProjects(String searchVal, String statusValue);

    Project updateProject(ProjectDto dto);

    public void deleteProjectById(Long projectId);

    public void deleteProjects(List<Long> projectIds);

}
