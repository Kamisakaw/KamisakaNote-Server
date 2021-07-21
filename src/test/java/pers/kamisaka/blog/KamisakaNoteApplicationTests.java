package pers.kamisaka.blog;

import org.h2.tools.Server;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pers.kamisaka.blog.entity.Article;
import pers.kamisaka.blog.mapper.ArticleMapper;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class KamisakaNoteApplicationTests {
	@Autowired
	private ArticleMapper articleMapper;

	@BeforeClass
	public static void setUp() throws SQLException {
		Server server = Server.createWebServer("-web","-webAllowOthers","-webDaemon","-webPort","8083");
		server.start();
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testInsert(){
		Article article = new Article();
		article.setView(100);
		article.setAuthor("Kamisaka");
		//System.out.println(articleMapper.addArticle(article));
		Long count = articleMapper.addArticle(article);
		assertNotNull(articleMapper);
		assertEquals(new Long(1),count);
	}
}
