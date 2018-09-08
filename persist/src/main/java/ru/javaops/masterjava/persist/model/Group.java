package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Group extends BaseEntity {

    private @NonNull String name;
    @Column("type")
    private @NonNull GroupType groupType;

    public Group(Integer id, String name, GroupType groupType) {
        super(id);
        this.name = name;
        this.groupType = groupType;
    }

}
