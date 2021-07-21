package pers.kamisaka.blog.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import pers.kamisaka.blog.entity.Article;
import pers.kamisaka.blog.mapper.ArticleMapper;
import pers.kamisaka.blog.mapper.CategoryMapper;
import pers.kamisaka.blog.mapper.TagMapper;
import pers.kamisaka.blog.ratelimit.RateLimit;
import pers.kamisaka.blog.util.RedisUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ArticleService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagMapper tagMapper;
    private static ObjectMapper om = new ObjectMapper();
    private int pageSize = 5;

    @Transactional
    public Article getArticleById(Long aid) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("getArticleById");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        Article article = null;
        String key = "aid:"+aid;
        try {
            Map<Object, Object> cache = redisUtil.hmget(key);
            if(cache.size() != 0){
                article =  om.readValue(om.writeValueAsString(cache),Article.class);
            }
            else{
                article = articleMapper.getArticleById(aid);
                redisUtil.hmset(key,om.readValue(om.writeValueAsString(article), Map.class));
            }
            article.setView(article.getView() + 1);
            updateArticle(article,false);
        } catch (Exception ex) {
            //因为更新点击率被查询嵌套，所以需要手动设置事务状态
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            transactionManager.rollback(status);
        }
        return article;
    }

    @Transactional
    public List<Article> getArticles(Long index) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("getArticles");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        List<Article> res = new LinkedList<>();
        Long begin = (index - 1) * pageSize;
        try{
            res = articleMapper.getArticles(begin, pageSize);
        }catch (Exception ex){
            transactionManager.rollback(status);
        }
        return res;
    }

    @Transactional
    public Long countArticle() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("getArticlesByCategory");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        Long res = 0L;
        try{
            res = articleMapper.countArticles();
        }catch (Exception ex){
            transactionManager.rollback(status);
        }
        return res;
    }

    @Transactional
    public List<Article> getArticlesByCategory(Long cid) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("getArticlesByCategory");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        List<Article> res = new LinkedList<>();
        try {
            res = articleMapper.getArticlesByCategory(cid);
        } catch (Exception ex) {
            transactionManager.rollback(status);
        }
        return res;
    }

    @Transactional
    public List<Article> getArticlesByDate(Integer size) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("getArticlesByDate");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        List<Article> res = new LinkedList<>();
        try {
            res = articleMapper.getArticlesByDate(size);
        } catch (Exception ex) {
            transactionManager.rollback(status);
        }
        return res;
    }

    @Transactional
    public List<Article> getArticlesByTid(Long tid) {
        //定义一个事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("getArticleByTid");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        List<Long> aids;
        List<Article> res = new LinkedList<>();
        try {
            aids = tagMapper.getAidsByTag(tid);
            for (Long aid : aids) {
                res.add(articleMapper.getArticleById(aid));
            }

        } catch (Exception ex) {
            transactionManager.rollback(status);
        }
        return res;
    }

    @Transactional
    public boolean addArticle(Article article) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("addArticle");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        article.setCreateDate(new Timestamp(System.currentTimeMillis()));
        boolean flag = false;
        try {
            flag = articleMapper.addArticle(article) != 0;
            String key = "aid:"+ article.getAid();
        } catch (Exception ex) {
            transactionManager.rollback(status);
        }
        return flag;
    }

    @Transactional
    public boolean deleteArticle(Long aid) {
        String key = "aid:"+aid;
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("deleteArticle");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        boolean flag = false;
        try {
            tagMapper.deleteAllTagsToArticle(aid);
            flag = articleMapper.deleteArticle(aid) != 0;
            redisUtil.del(key);
        } catch (Exception ex) {
            transactionManager.rollback(status);
        }
        return flag;
    }

    @Transactional
    public boolean updateArticle(Article article,boolean cache) {
        String key = "aid:"+article.getAid();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("updateArticle");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        boolean flag = false;
        try {
            article.setUpdateDate(new Timestamp(System.currentTimeMillis()));
            flag = articleMapper.updateArticle(article)!=0;
            if(cache){
                redisUtil.hmset(key,om.readValue(om.writeValueAsString(article), Map.class));
            }
        } catch (Exception ex) {
            //因为更新点击率被查询嵌套，所以需要手动设置事务状态
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            transactionManager.rollback(status);
        }
        return flag;
    }

    @Transactional
    public String uploadPic(Long aid, String url) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("uploadPic");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        int flag = 0;
        try {
            flag = articleMapper.uploadPic(aid, url);
        } catch (Exception ex) {
            transactionManager.rollback(status);
        }
        return flag == 0 ? "Upload failed!" : url;
    }

}
