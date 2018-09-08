package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.City;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {

    public City insert(City city) {
        if (city.isNew()    ) {
            int id = insertGeneratedId(city);
            city.setId(id);
        } else {
            insertWitId(city);
        }
        return city;
    }

    @SqlQuery("SELECT nextval('city_seq')")
    abstract int getNextVal();

    @Transaction
    public int getSeqAndSkip(int step) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("ALTER SEQUENCE city_seq RESTART WITH " + (id + step)));
        return id;
    }

    @SqlUpdate("INSERT INTO cities (name) VALUES (:name) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean City city);

    @SqlUpdate("INSERT INTO cities (id, name) VALUES (:id, :name) ")
    abstract void insertWitId(@BindBean City city);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("DELETE FROM cities")
    @Override
    public abstract void clean();

//    @SqlQuery("SELECT * from cities GROUP BY id HAVING count() ")
//    abstract int size();
}
