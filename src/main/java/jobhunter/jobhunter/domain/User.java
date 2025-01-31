package jobhunter.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jobhunter.jobhunter.enumClass.GenderEnum;
import jobhunter.jobhunter.service.AuthService;
import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String password;

    private String email;

    private int age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String address;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Resume> resumes;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

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
