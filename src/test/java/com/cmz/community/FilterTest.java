package com.cmz.community;

import com.cmz.CommunityApplication;
import com.cmz.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class FilterTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;


    @Test
    public void testf(){
        String str = "今日开*盘";
        String filter = sensitiveFilter.filter(str);
        System.out.println(filter);
    }

}
