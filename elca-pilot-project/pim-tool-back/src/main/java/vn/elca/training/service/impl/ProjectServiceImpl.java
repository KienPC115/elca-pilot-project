package vn.elca.training.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.elca.training.model.dto.ProjectDto;
import vn.elca.training.model.entity.Group;
import vn.elca.training.model.entity.Project;
import vn.elca.training.model.entity.QProject;
import vn.elca.training.model.exception.AddExistedProjectException;
import vn.elca.training.repository.GroupRepository;
import vn.elca.training.repository.ProjectRepository;
import vn.elca.training.service.ProjectService;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author gtn
 */
@Component
@Primary
public class ProjectServiceImpl  implements ProjectService {

    private final ProjectRepository projectRepository;

    private final GroupRepository groupRepository;

    @PersistenceContext
    EntityManager em;
    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, GroupRepository groupRepository) {
        this.projectRepository = projectRepository;
        this.groupRepository = groupRepository;
    }

    public Project addProject(ProjectDto dto) throws AddExistedProjectException {
        Project existingProject = projectRepository.findByProjectNumber(dto.getProjectNumber());
        if (existingProject != null) {
            throw new AddExistedProjectException("The project number already existed.");
        }
        Project project = new Project();
        project.setProjectNumber(dto.getProjectNumber());
        project.setGroup(groupRepository.findById(dto.getGroup_id()).orElseThrow(() ->{throw new EntityNotFoundException("Group not found");}));
        project.setName(dto.getName());
        project.setCustomer(dto.getCustomer());
        project.setStatus(dto.getStatus());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setVersion(0);
        return projectRepository.save(project);
    }

    @Override
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public List<Group> findAllGroup() {return groupRepository.findAll();}

    @Override
    public long count() {
        return projectRepository.count();
    }

    public List<Project> searchProjects(String searchValue, String statusValue) {
        QProject project = QProject.project;
        BooleanExpression predicate = null;

        if (!searchValue.equals("null")) {
            String lowerSearchValue = searchValue.toLowerCase();
            predicate = project.projectNumber.stringValue().toLowerCase().likeIgnoreCase("%" + lowerSearchValue + "%")
                    .or(project.name.toLowerCase().likeIgnoreCase("%" + lowerSearchValue + "%"))
                    .or(project.customer.toLowerCase().likeIgnoreCase("%" + lowerSearchValue + "%"));
        }

        if (!statusValue.equals("null")) {
            if (predicate == null) {
                predicate = project.status.stringValue().contains(statusValue);
            } else {
                predicate = predicate.and(project.status.stringValue().contains(statusValue));
            }
        }

        return new JPAQuery<>(em)
                .select(project)
                .from(project)
                .where(predicate)
                .fetch();
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find the project"));
    }

    @Transactional
    @Override
    public Project updateProject(ProjectDto dto) {
        Project project = getProjectById(dto.getId());
        // Apply changes from the DTO
        project.setName(dto.getName());
        project.setCustomer(dto.getCustomer());
        project.setGroup(groupRepository.findById(dto.getGroup_id()).get());
        project.setStatus(dto.getStatus());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        // Save the updated project
        Project updatedProject = projectRepository.save(project);
        if (updatedProject == null) {
            throw new RuntimeException();
        }
        return updatedProject;
    }

    @Transactional
    public void deleteProjects(List<Long> projectIds) {
        for (Long projectId : projectIds) {
            projectRepository.deleteById(projectId);
        }
    }

    @Transactional
    public void deleteProjectById(Long projectId) {
        projectRepository.deleteById(projectId);
    }
}
