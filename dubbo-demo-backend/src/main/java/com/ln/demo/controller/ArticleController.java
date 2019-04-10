package com.ln.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.ln.demo.api.system.dto.ArticleDTO;
import com.ln.demo.api.system.service.ArticleService;
import com.ln.demo.common.data.Page;
import com.ln.demo.util.JwtUtils;
import com.ln.demo.vo.ArticleDetailVO;
import com.ln.demo.vo.ArticleListVO;

/**
 * 资源控制器
 * 
 * @author Lining
 * @date 2018/2/11
 */
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @GetMapping("")
    public ResponseEntity<Page<ArticleListVO>> listArticle(@RequestParam(required = false) String name, int offset, int limit) {
        Map<String, Object> paremeters = new HashMap<String, Object>();
        if (!StringUtils.isBlank(name)) {
            paremeters.put("name", name);
        }
        Page<ArticleDTO> result = articleService.listAllArticle(paremeters, offset, limit);
        List<ArticleListVO> voList = new ArrayList<ArticleListVO>(result.getRows().size());
        if(CollectionUtils.isNotEmpty(result.getRows())){
	        for(ArticleDTO dto : result.getRows()) {
	        	ArticleListVO vo = new ArticleListVO();
	            BeanUtils.copyProperties(dto, vo);
	            voList.add(vo);
	        }
        }
        return ResponseEntity.ok(new Page<ArticleListVO>(result.getTotal(), voList));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDetailVO> getById(@PathVariable("id") int id) {
    	ArticleDTO dto = this.articleService.getById(id);
        if(dto == null) {
            return ResponseEntity.notFound().build();
        }
        ArticleDetailVO vo = new ArticleDetailVO();
        BeanUtils.copyProperties(dto, vo);
        return ResponseEntity.ok(vo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateArticle(@RequestBody ArticleDetailVO vo) {
    	ArticleDTO dto = new ArticleDTO();
        BeanUtils.copyProperties(vo, dto);
        int rows = this.articleService.updateArticle(dto);
        return rows == 0 ? ResponseEntity.notFound().build() : 
            ResponseEntity.status(HttpStatus.CREATED).body(rows);
    }
    
    @PostMapping("")
    public ResponseEntity<ArticleDetailVO> saveArticle(@RequestBody ArticleDetailVO vo) {
    	ArticleDTO dto = new ArticleDTO();
        BeanUtils.copyProperties(vo, dto);
        int id = this.articleService.saveArticle(dto);
        vo.setId(id);
        return ResponseEntity.ok(vo);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeArticle(@PathVariable("id") int id) {
        int rows = this.articleService.removeArticle(id);
        return rows == 0 ? ResponseEntity.notFound().build() : 
            ResponseEntity.noContent().build();
    }

}
