package vn.elca.training.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.elca.training.model.dto.ProjectDto;
import vn.elca.training.model.entity.Group;
import vn.elca.training.model.entity.Project;
import vn.elca.training.model.exception.AddExistedProjectException;
import vn.elca.training.service.ProjectService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gtn
 */
@RestController
@RequestMapping("/project")
public class ProjectController extends AbstractApplicationController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // get project
    @GetMapping("/search")
    public List<ProjectDto> search() {
        return projectService.findAllProjects()
                .stream()
                .sorted(Comparator.comparingInt(Project::getProjectNumber))
                .map(mapper::projectToProjectDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/id/{id}")
    public ProjectDto getProjectById(@PathVariable("id") Long id) {
        Project p = projectService.getProjectById(id);
        return mapper.projectToProjectDto(p);
    }

    // get group
    @GetMapping("/group")
    public List<Integer> getListGroupId() {
        List<Group> groupList = projectService.findAllGroup();
        List<Integer> groupIdList = groupList.stream()
                .map(Group::getId)
                .collect(Collectors.toList());
        return groupIdList;
    }


    @GetMapping("/search_project") //localhost:8080/project/search_project?searchVal=""&statusVal=""
    public List<ProjectDto> searchProjects(@RequestParam("searchVal") String searchVal, @RequestParam("statusVal") String statusVal) {
        return projectService.searchProjects(searchVal,statusVal)
                .stream()
                .sorted(Comparator.comparingInt(Project::getProjectNumber))
                .map(mapper::projectToProjectDto)
                .collect(Collectors.toList());
    }


    //create
    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) {
        try {
            Project newProject = projectService.addProject(projectDto);
            ProjectDto createdProjectDto = mapper.projectToProjectDto(newProject);
            return ResponseEntity.ok(createdProjectDto);
        } catch (AddExistedProjectException e) {
            // Handle the AddExistedProjectException
            String errorMessage = "Error creating project: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    //update
    @PostMapping("/update")
    public ResponseEntity<?> updateProject(@RequestBody ProjectDto projectDto) {
        Project existingProject = projectService.getProjectById(projectDto.getId());
        if(existingProject!=null) {
            // check the version - if it not the same throw exception
            // Perform optimistic lock check
            if(existingProject.getVersion()!=projectDto.getVersion()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict detected. Another user has modified the project.");
            }
            //update the project information
            Project projectUpdated = projectService.updateProject(projectDto);
            return ResponseEntity.ok("Project updated successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found the project");
    }

    //delete
    @DeleteMapping("/{id}")
    public void deleteOneProject(@PathVariable("id") Long id) {
        projectService.deleteProjectById(id);
    }

    @PostMapping("/delete")
    public void deleteProjects(@RequestBody List<Long> projectIds) {
        projectService.deleteProjects(projectIds);
    }

}
