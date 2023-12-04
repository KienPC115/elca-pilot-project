package vn.elca.training.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


/**
 * @author gtn
 */
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class ProjectDto {
    private Long id;

    private int projectNumber;

    private int group_id;

    private String name;

    private String customer;

    private String status;

//    @JsonFormat(pattern = "d/M/yyyy")
    private LocalDate startDate;

//    @JsonFormat(pattern = "d/M/yyyy")
    private LocalDate endDate;

    private int version;
}