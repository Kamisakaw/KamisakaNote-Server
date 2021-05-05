package pers.kamisaka.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pers.kamisaka.blog.entity.Category;

import java.util.List;

@Repository
@Mapper
public interface CategoryMapper {
    List<Category> getCategories();
    Category getCategoryById(Long cid);
    Long countCategories();
    int addCategory(Category category);
    int deleteCategory(Long cid);
    int updateCategory(Category category);
}
