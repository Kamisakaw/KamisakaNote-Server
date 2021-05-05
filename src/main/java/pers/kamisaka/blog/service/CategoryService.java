package pers.kamisaka.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.kamisaka.blog.entity.Category;
import pers.kamisaka.blog.mapper.CategoryMapper;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    public List<Category> getCategories(){
        return categoryMapper.getCategories();
    }
    public Category getCategoryById(Long cid){
        return categoryMapper.getCategoryById(cid);
    }
    public int addCategory(Category category){
        category.setCreateDate(new Timestamp(System.currentTimeMillis()));
        return categoryMapper.addCategory(category);
    }
    public int deleteCategory(Long cid){
        return categoryMapper.deleteCategory(cid);
    }
    public int updateCategory(Category category){
        return categoryMapper.updateCategory(category);
    }
}
