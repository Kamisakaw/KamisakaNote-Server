package pers.kamisaka.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.kamisaka.blog.entity.Article;

import java.util.List;

@Repository
@Mapper
public interface ArticleMapper {
    Article getArticleById(Long aid);
    List<Article> getArticles(Long index,int pageSize);
    List<Article> getArticlesByCategory(Long cid);
    List<Article> getArticlesByDate(Integer size);
    Long countArticles();
    int addArticle(Article article);
    int deleteArticle(Long aid);
    int updateArticle(Article article);
}
