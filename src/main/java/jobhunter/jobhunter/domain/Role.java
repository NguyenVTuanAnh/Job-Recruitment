package jobhunter.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jobhunter.jobhunter.service.AuthService;
import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "NOT_BLANK")
    private String name;

    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "permission_role"
            ,joinColumns = @JoinColumn(name = "role_id")
            ,inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions;

    @JsonIgnore
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<User> users;

    @PrePersist
    public void handleBeforeCreate(){
        this.createdAt = Instant.now();
        if (AuthService.getCurrentUserLogin().isPresent()){
            this.createdBy = AuthService.getCurrentUserLogin().get();
        } else {
            this.createdBy = "";
        }

    }
    @PreUpdate
    public void handleBeforeUpdate(){
        this.updatedAt = Instant.now();
        if (AuthService.getCurrentUserLogin().isPresent()){
            this.updatedBy = AuthService.getCurrentUserLogin().get();
        } else {
            this.updatedBy = "";
        }
    }
}
