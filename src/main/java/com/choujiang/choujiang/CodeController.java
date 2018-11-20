package com.choujiang.choujiang;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code")
public class CodeController {
    /**
     * 验证条码是否可用
     *
     * @param code
     * @return
     */
    @RequestMapping("/validationCode")
    public int validationCode(String code) {
        //查询条码是否可用
        //可用 条码信息入库

        return 1;
    }

    /**
     * 进行抽奖操作 0-3 (1-4等奖)
     * @return
     */
    @RequestMapping("/choujiang")
    public int choujiang() {
        return 1;
    }

    /**
     * 收集中奖人信息
     * @param name
     * @param phone
     * @param address
     * @return
     */
    @RequestMapping("/insertUserInfo")
    public int insertUserInfo(String name,String phone,String address){
        return 1;
    }

}
