package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class GroupDao implements AbstractDao {
    public Group insert(Group group) {
        if (group.isNew()    ) {
            int id = insertGeneratedId(group);
            group.setId(id);
        } else {
            insertWitId(group);
        }
        return group;
    }

    @SqlQuery("SELECT nextval('group_seq')")
    abstract int getNextVal();

    @Transaction
    public int getSeqAndSkip(int step) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("ALTER SEQUENCE group_seq RESTART WITH " + (id + step)));
        return id;
    }

    @SqlUpdate("INSERT INTO groups (name, type) VALUES (:name, CAST(:groupType AS GROUP_TYPE)) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Group group);

    @SqlUpdate("INSERT INTO groups (id, name, type) VALUES (:id, :name, CAST(:groupType AS GROUP_TYPE)) ")
    abstract void insertWitId(@BindBean Group group);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE groups")
    @Override
    public abstract void clean();

    @SqlQuery("SELECT * FROM groups")
    public abstract List<Group> getGroups();


}
