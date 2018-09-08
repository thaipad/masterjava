package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Project extends BaseEntity {

    private @NonNull String name;
    private String description;

    public Project(Integer id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }
}
