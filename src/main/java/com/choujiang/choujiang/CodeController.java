package com.choujiang.choujiang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.choujiang.choujiang.entity.CodeInfo;
import com.choujiang.choujiang.entity.LjInfo;
import com.choujiang.choujiang.repository.CodeRepository;
import com.choujiang.choujiang.repository.LjINfoRepository;
import com.choujiang.choujiang.resouce.RandomNum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/code")
public class CodeController {
    private final Log logger = LogFactory.getLog(CodeController.class);
    @Autowired
    private CodeRepository codeRepository;
    @Autowired
    private LjINfoRepository ljINfoRepository;

    /**
     * 验证条码是否可用
     *
     * @param code
     * @return
     */
    @RequestMapping("/validationCode")
    public int validationCode(String code) {
        logger.info(code);
        //判断是否有可用奖品
        if((RandomNum.a+RandomNum.b+RandomNum.c+RandomNum.d)>=2000){
            return -1;
        }
        //查询条码是否可用
       int cout= codeRepository.countAllByCode(code);
        //可用 条码信息入库
        if(cout==0){
            CodeInfo codeInfo= new CodeInfo();
            codeInfo.setCode(code);
            codeRepository.save(codeInfo);
            return 0;
        }else {
            return 1;
        }

    }

    /**
     * 进行抽奖操作 0-3 (1-4等奖)
     * @return
     */
    @RequestMapping("/choujiang")
    public int choujiang() {
       Integer success= RandomNum.init();
       if(success!=-1){
           return success;
       }else {
           return -1;
       }
    }

    /**
     * 收集中奖人信息
     * @param name
     * @param phone
     * @param address
     * @return
     */
    @RequestMapping(value = "/insertUserInfo" ,method = RequestMethod.POST)
    public int insertUserInfo(String name, String phone, String address, Integer rewardtype, HttpServletRequest request) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(), "utf-8"));
        br = br;
        StringBuffer sb = new StringBuffer("");
        String temp;
        while ((temp = br.readLine()) != null) {
            sb.append(temp);
        }
        br.close();
        String s = sb.toString();
        JSONObject jsonObject = JSON.parseObject(s);
        name=jsonObject.getString("name");
        phone=jsonObject.getString("phone");
        address=jsonObject.getString("address");
        rewardtype=Integer.parseInt(""+jsonObject.getString("rewardtype"));
        logger.info("----------"+s);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LjInfo ljInfo= new LjInfo();
        ljInfo.setAddress(address);
        ljInfo.setCreatetime(simpleDateFormat.format(new Date()));
        ljInfo.setIsdeal(0);
        ljInfo.setName(name);
        ljInfo.setPhone(phone);
        ljInfo.setRewardtype(rewardtype);
        ljINfoRepository.save(ljInfo);
        return 1;
    }

    /**
     * 数据同步接口
     * @return
     */
    @RequestMapping("/getResultInfo")
    public Map<String,Object> getResultInfo(){
        Map<String,Object> map=new HashMap<>();
        map.put("one",(10-RandomNum.a));
        map.put("two",(100-RandomNum.b));
        map.put("three",(240-RandomNum.c));
        map.put("four",(1650-RandomNum.d));
        return map;
    }

}
