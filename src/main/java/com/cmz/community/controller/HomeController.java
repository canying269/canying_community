package com.cmz.community.controller;

import com.cmz.community.entity.DiscussPost;
import com.cmz.community.entity.Page;
import com.cmz.community.entity.User;
import com.cmz.community.service.DiscussPostService;
import com.cmz.community.service.LikeService;
import com.cmz.community.service.MessageService;
import com.cmz.community.service.UserService;
import com.cmz.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model,Page page){

        //userId为0时 mapper.xml里查出全部数据
        //总行数
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        //查询当前页帖子，放入list中
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if(list != null){
            for(DiscussPost post : list){
                Map<String, Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);

                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("like",likeCount);

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);

        return "/index";
    }


    @GetMapping("/error")
    public String getErrorPage(){
        return "/error/500";
    }
}
