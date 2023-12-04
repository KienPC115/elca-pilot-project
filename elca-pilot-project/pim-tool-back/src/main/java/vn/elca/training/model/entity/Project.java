package vn.elca.training.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDate;
import java.util.List;

/**
 * @author vlp
 */
@Entity
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
@Table(name = "PROJECT")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GROUP_ID", nullable = false)
    private Group group;

    @Column(name = "PROJECT_NUMBER", nullable = false, unique = true)
    private int projectNumber;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "CUSTOMER", length = 50)
    private String customer;

    @Column(name = "STATUS", length = 3, nullable = false)
    private String status;

    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate;

    @Column(name = "END_DATE", nullable = false)
    @Future
    private LocalDate endDate;

    @ManyToMany
    @JoinTable(name = "project_employee",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<Employee> employees;

    @Version
    @Column(name = "VERSION", nullable = false)
    private int version;
}