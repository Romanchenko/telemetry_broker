package ru.cshse.project.sources;

import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.cshse.project.settings.FlagsDao;
import ru.cshse.project.settings.SettingsMongoConfiguration;

/**
 * @author apollin
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@Import({SettingsMongoConfiguration.class})
public class MongoTest {
    //@Autowired
    //private FlagsDao flagsDao;

//    @Test
//    @Disabled
//    public void test() {
//        System.out.println(flagsDao.getAllFlags());
//    }
}
