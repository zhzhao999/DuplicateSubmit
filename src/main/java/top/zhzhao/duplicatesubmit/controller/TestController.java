/**
 * https://www.zhzhao.top
 */
package top.zhzhao.duplicatesubmit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.zhzhao.duplicatesubmit.annotation.DuplicateSubmitToken;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *@author zhzhao
 *@version $ Id: TestController.java,V 0.1 2018/8/22 16:02 zhzhao Exp $
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @DuplicateSubmitToken
    @GetMapping("/aaa/{appId}")
    public String demo(@PathVariable String appId){

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return "测试接口";
    }

    @GetMapping("/bbb")
    public String demo2(){

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return "测试接口bbb";
    }
}
