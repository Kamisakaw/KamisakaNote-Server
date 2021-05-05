package pers.kamisaka.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import pers.kamisaka.blog.entity.Article;
import pers.kamisaka.blog.mapper.ArticleMapper;
import pers.kamisaka.blog.mapper.CategoryMapper;
import pers.kamisaka.blog.mapper.TagMapper;
import pers.kamisaka.blog.ratelimit.RateLimit;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagMapper tagMapper;
    private int pageSize = 5;

    @Transactional
    @Cacheable(value = "article",keyGenerator = "keyGenerator")
    public Article getArticleById(Long aid){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("getArticleById");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        Article article = null;

        try{
            article = articleMapper.getArticleById(aid);
            article.setView(article.getView()+1);
            articleMapper.updateArticle(article);
        }catch (Exception ex){
            transactionManager.rollback(status);
        }
        return article;
    }

    public List<Article> getArticles(Long index){
        Long begin = (index-1)*pageSize;
        return articleMapper.getArticles(begin,pageSize);}

    public Long countArticlePage(){
        //+1是因为即使文章数目不足以填满一页时，至少也应该有一页
        Long res = (articleMapper.countArticles()/pageSize)+1;
        return res;}

    public List<Article> getArticlesByCategory(Long cid){return articleMapper.getArticlesByCategory(cid);}

    public List<Article> getArticlesByDate(Integer size){return articleMapper.getArticlesByDate(size);}

    @Transactional
    public List<Article> getArticlesByTid(Long tid){
        //定义一个事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("getArticleByTid");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        List<Long> aids;
        List<Article> res = new LinkedList<>();
        try{
            aids = tagMapper.getAidsByTag(tid);
            for(Long aid : aids){
                res.add(articleMapper.getArticleById(aid));
            }

        }catch (Exception ex){
            transactionManager.rollback(status);
        }
        return res;
    }

    public int addArticle(Article article){
        article.setCreateDate(new Timestamp(System.currentTimeMillis()));
        return articleMapper.addArticle(article);}

    @Transactional
    public int deleteArticle(Long aid){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("deleteArticle");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        int flag = 0;
        try{
            tagMapper.deleteAllTagsToArticle(aid);
            flag = articleMapper.deleteArticle(aid);
        }catch (Exception ex){
            transactionManager.rollback(status);
        }
        return flag;
    }

    public int updateArticle(Article article){
        article.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        return articleMapper.updateArticle(article);}
}
