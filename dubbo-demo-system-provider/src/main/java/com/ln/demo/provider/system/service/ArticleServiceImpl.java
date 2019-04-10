package com.ln.demo.provider.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ln.demo.api.system.dto.ArticleDTO;
import com.ln.demo.api.system.dto.RouterDTO;
import com.ln.demo.api.system.entity.Article;
import com.ln.demo.api.system.entity.Router;
import com.ln.demo.api.system.service.ArticleService;
import com.ln.demo.common.data.Page;
import com.ln.demo.common.data.PageRequest;
import com.ln.demo.provider.system.dao.ArticleDAO;

/**
 * 文章服务实现类
 * 
 * @author wjp
 * @date 2019/03/21
 */
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDAO articleDAO;

    @Override
    public Page<ArticleDTO> listAllArticle(Map<String, Object> parameters, int offset, int limit) {
    	int total = articleDAO.countArticle(parameters);
        List<Article> articleList = null;
        List<ArticleDTO> dtoList = null;
        if(total > 0) {
            PageRequest pageRequest = new PageRequest(offset, limit, parameters, null);
            articleList = articleDAO.listArticle(pageRequest);
            
            dtoList = new ArrayList<ArticleDTO>(articleList.size());
            for(Article article : articleList) {
            	ArticleDTO dto = new ArticleDTO();
                BeanUtils.copyProperties(article, dto);
                dtoList.add(dto);
            }
        }
        return new Page<ArticleDTO>(total, dtoList);
    }
    
    @Override
    public ArticleDTO getById(int id) {
    	Article article = this.articleDAO.getById(id);
        if(article == null) {
            return null;
        }
        ArticleDTO dto = new ArticleDTO();
        BeanUtils.copyProperties(article, dto);
        return dto;
    }
    
    @Override
    @Transactional
    public int saveArticle(ArticleDTO dto) {
    	Article article = new Article();
        BeanUtils.copyProperties(dto, article);
        return this.articleDAO.saveArticle(article);
    }
    
    @Override
    @Transactional
    public int updateArticle(ArticleDTO dto) {
    	Article article = new Article();
        BeanUtils.copyProperties(dto, article);
        return this.articleDAO.updateArticle(article);
    }
    
    @Override
    @Transactional
    public int removeArticle(int id) {
        return this.articleDAO.removeArticle(id);
    }

}
