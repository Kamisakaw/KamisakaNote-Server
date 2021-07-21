package pers.kamisaka.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import pers.kamisaka.blog.entity.Tag;
import pers.kamisaka.blog.mapper.TagMapper;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TagService {
    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Autowired
    TagMapper tagMapper;
    public List<Tag> getTags(){
        return tagMapper.getTags();
    };
    public List<Tag> getTagsByAid(Long aid){
        return tagMapper.getTagsByAid(aid);
    };
    public boolean checkTA(Long aid,Long tid){
        return tagMapper.checkTA(aid, tid) != 0;
    }
    public int addTag(Tag tag){
        //tag.setTid(tagMapper.countTags()+1);
        System.out.println(tag);
        tag.setCreateDate(new Timestamp(System.currentTimeMillis()));
        return tagMapper.addTag(tag);
    };
    public int addTagToArticle(Long aid,Long tid){return tagMapper.addTagToArticle(aid,tid);}
    public int deleteTag(Long tid){
        return tagMapper.deleteTag(tid);
    };
    public int deleteTagToArticle(Long aid,Long tid){return tagMapper.deleteTagToArticle(aid,tid);}
    public int deleteAllTagsToArticle(Long aid){return tagMapper.deleteAllTagsToArticle(aid);}
    public int updateTag(Tag tag){
        return tagMapper.updateTag(tag);
    };

    @Transactional
    public int updateTA(Long aid,Long formerTid,Long newTid){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("updateTA");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        int flag = 0;

        if(formerTid.longValue() == newTid.longValue()){
            return -1;
        }

        try{
            tagMapper.deleteTagToArticle(aid, formerTid);
            flag = tagMapper.addTagToArticle(aid,newTid);
        }catch (Exception ex){
            transactionManager.rollback(status);
        }
        return flag;
    }
}
