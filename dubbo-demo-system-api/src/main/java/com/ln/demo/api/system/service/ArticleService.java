package com.ln.demo.api.system.service;

import java.util.Map;

import com.ln.demo.api.system.dto.ArticleDTO;
import com.ln.demo.common.data.Page;

/**
 * @ClassName: ArticleService
 * @Description: article服务类
 * @author Administrator
 * @date 2019年3月21日
 *
 */
public interface ArticleService {

	Page<ArticleDTO> listAllArticle(Map<String, Object> parameters, int offset, int limit);
	
	ArticleDTO getById(int id);
	
	int saveArticle(ArticleDTO dto);
	
	int updateArticle(ArticleDTO dto);
	
	int removeArticle(int id);
}
