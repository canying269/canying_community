package com.cmz.community.controller;

import com.cmz.community.entity.DiscussPost;
import com.cmz.community.entity.Page;
import com.cmz.community.entity.User;
import com.cmz.community.service.ElasticsearchService;
import com.cmz.community.service.LikeService;
import com.cmz.community.service.UserService;
import com.cmz.community.util.CommunityConstant;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController implements CommunityConstant {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model) {

        org.springframework.data.domain.Page<DiscussPost> result =
                elasticsearchService.searchDiscussPost(keyword, page.getCurrent()-1, page.getLimit());

        //聚合数据
        List<Map<String, Object>> discussposts = new ArrayList<>();
        if (result != null) {
            for (DiscussPost post : result) {
                Map<String, Object> map = new HashMap<>();

                //帖子
                map.put("post", post);

                //作者
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);

                //点赞数量
                map.put("like", likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId()));

                discussposts.add(map);

            }
        }
        model.addAttribute("discussPosts", discussposts);
        model.addAttribute("keyword",keyword);

        //分页信息
        page.setPath("/search?keyword="+keyword);
        page.setRows(result == null ? 0 : (int) result.getTotalElements());

        return "site/search";

    }

}
