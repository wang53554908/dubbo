package com.ln.demo.provider.system.dao;

import java.util.List;
import java.util.Map;

import com.ln.demo.api.system.entity.Article;
import com.ln.demo.common.data.PageRequest;

/**
 * 资源管理DAO接口
 * 
 * @author wjp
 * @date 2019/03/21
 */
public interface ArticleDAO {
	
	/**
     * 统计路由数
     * @param parameters
     * @return
     */
    int countArticle(Map<String, Object> parameters);

	List<Article> listArticle(PageRequest pageRequest);

	int saveArticle(Article article);
	
	int updateArticle(Article article);
	
	int removeArticle(int id);
	
	Article getById(int id);
	

}
