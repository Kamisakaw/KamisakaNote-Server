package pers.kamisaka.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.kamisaka.blog.entity.Tag;

import java.util.List;

@Repository
@Mapper
public interface TagMapper {
    List<Tag> getTags();
    List<Tag> getTagsByAid(Long aid);
    List<Long> getAidsByTag(Long tid);
    int checkTA(Long aid,Long tid);
    Long countTags();
    int addTag(Tag tag);
    int addTagToArticle(Long aid,Long tid);
    int deleteTagToArticle(Long aid,Long tid);
    int deleteAllTagsToArticle(Long aid);
    int deleteTag(Long tid);
    int updateTag(Tag tag);
}
