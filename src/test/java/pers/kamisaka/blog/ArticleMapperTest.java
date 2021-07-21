package pers.kamisaka.blog;

import org.h2.tools.Server;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import pers.kamisaka.blog.entity.Article;
import pers.kamisaka.blog.mapper.ArticleMapper;

import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ArticleMapperTest {
    @Autowired
    private ArticleMapper articleMapper;

    @BeforeClass
    public static void setUp() throws SQLException {
        Server server = null;
        try{
             server = Server.createWebServer("-web","-webAllowOthers","-webDaemon","-webPort","8083");
        }catch (Exception e) {
            e.printStackTrace();
        }
        server.start();
    }

    @Test
    public void testInsert(){
        Article article = new Article();
        article.setAid(new Long(2));
        article.setAuthor("Kamisaka");
        article.setTitle("a");
        article.setCreateDate(new Timestamp(System.currentTimeMillis()));
        article.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        System.out.println(articleMapper);
        Long count = articleMapper.addArticle(article);
        assertNotNull(articleMapper);
        assertEquals(new Long(1),count);
    }


}
