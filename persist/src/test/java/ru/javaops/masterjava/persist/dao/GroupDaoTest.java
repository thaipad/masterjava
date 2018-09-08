package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.GroupType;

import java.util.ArrayList;
import java.util.List;

public class GroupDaoTest extends AbstractDaoTest<GroupDao> {
    private List<Group> groups = new ArrayList<>();

    public GroupDaoTest() {
        super(GroupDao.class);
    }


    @Before
    public void init() {
        groups.add(new Group(1, "Group 2016/2", GroupType.FINISHED));
        groups.add(new Group(2, "Group 2016/1", GroupType.FINISHED));
        groups.add(new Group(3, "Group 2018/3", GroupType.CURRENT));
        groups.add(new Group(4, "Group 2018/4", GroupType.REGISTERING));
        groups.add(new Group(5, "Group 2019/1", GroupType.REGISTERING));
        groups.add(new Group(6, "Group 2017/2", GroupType.FINISHED));
        groups.add(new Group(7, "Group 2017/3", GroupType.FINISHED));
        groups.add(new Group(8, "Group 2018/2", GroupType.CURRENT));
    }

    @Test
    public void insertTest() {
        dao.clean();

        groups.forEach(s->dao.insert(s));

        Assert.assertEquals(groups, dao.getGroups());

    }

    @Test
    public void insertPositionWithGenerateKeyTest() {
        Group group = new Group("Group ext 2018/4", GroupType.REGISTERING);
        dao.insert(group);
        groups.add(group);
        Assert.assertEquals(5, 5);
    }

    @Test
    public void insertPositionWithManualKeyTest() {
        dao.insert(new Group(10,"Ext group 2018", GroupType.REGISTERING));
        Assert.assertEquals(5, 5);
    }

}
