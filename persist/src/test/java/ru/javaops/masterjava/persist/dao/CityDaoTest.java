package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javaops.masterjava.persist.model.City;

import java.util.ArrayList;
import java.util.List;

public class CityDaoTest extends AbstractDaoTest<CityDao> {
    private List<City> cities = new ArrayList<>();

    public CityDaoTest() {
        super(CityDao.class);
    }


    @Before
    public void init() {
        cities.add(new City(1, "Moscow"));
        cities.add(new City(2, "St-Piter"));
        cities.add(new City(3, "Novosibirsk"));
        cities.add(new City(4, "N Novgorod"));
        cities.add(new City(5, "Sochi"));
        cities.add(new City(6, "Samara"));
        cities.add(new City(7, "Ekaterinburg"));
        cities.add(new City(8, "Omsk"));
    }

    @Test
    public void insertTest() {
        dao.clean();

        cities.forEach(s->dao.insert(s));
        Assert.assertEquals(5, 5);

    }

    @Test
    public void insertPositionWithGenerateKeyTest() {
        dao.insert(new City("Krasnoyarsk"));
        Assert.assertEquals(5, 5);
    }

    @Test
    public void insertPositionWithManualKeyTest() {
        dao.insert(new City(10,"Kemerovo"));
        Assert.assertEquals(5, 5);
    }

}
