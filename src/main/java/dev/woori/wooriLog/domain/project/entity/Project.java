package dev.woori.wooriLog.domain.project.entity;

import dev.woori.wooriLog.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Project extends BaseEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "project_name")
    private String projectName;

    private String introduce;

    @Column(name = "read_me")
    private String readMe;

    @ElementCollection
    @CollectionTable(name = "tech_stack", joinColumns = @JoinColumn(name = "project_id"))
    private List<String> techStack;

    public static Project create(String projectName, String introduce, String readMe, List<String> techStack) {
        return Project.builder()
                .projectName(projectName)
                .introduce(introduce)
                .readMe(readMe)
                .techStack(techStack)
                .build();
    }
}
