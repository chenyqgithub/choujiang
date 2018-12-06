package com.choujiang.choujiang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.choujiang.choujiang.entity.CodeInfo;
import com.choujiang.choujiang.entity.LjInfo;
import com.choujiang.choujiang.repository.CodeLibRepository;
import com.choujiang.choujiang.repository.CodeRepository;
import com.choujiang.choujiang.repository.LjINfoRepository;
import com.choujiang.choujiang.resouce.RandomNum;
import com.choujiang.choujiang.utils.FileUtil;
import com.sun.jndi.toolkit.dir.SearchFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/code")
public class CodeController {
    private final Log logger = LogFactory.getLog(CodeController.class);
    @Autowired
    private CodeRepository codeRepository;
    @Autowired
    private LjINfoRepository ljINfoRepository;
    @Autowired
    private CodeLibRepository codeLibRepository;


    /**
     * 验证条码是否可用
     *
     * @param code
     * @return
     */
    @RequestMapping("/validationCode")
    public int validationCode(String code) throws ParseException {
        logger.info(code);
        //判断条码是否存在条码库
        int i = codeLibRepository.countById(code);
        if (i != 1) {
            return 1;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = simpleDateFormat.parse("2019-03-10 23:59:59");//到期时间
        Date date = new Date();//当前时间
        if (parse.getTime() < date.getTime()) {//监听时间可用
            return -2;
        }
        //判断是否有可用奖品
        if ((RandomNum.a + RandomNum.b + RandomNum.c + RandomNum.d) >= 2000) {
            return -1;
        }
        //查询条码是否可用
        int cout = codeRepository.countAllByCode(code);
        //可用 条码信息入库
        if (cout == 0) {
            CodeInfo codeInfo = new CodeInfo();
            codeInfo.setCode(code);
            codeRepository.save(codeInfo);
            return 0;
        } else {
            return 1;
        }

    }

    /**
     * 进行抽奖操作 0-3 (1-4等奖)
     *
     * @return
     */
    @RequestMapping("/choujiang")
    public int choujiang() {
        Integer success = RandomNum.init();
        if (success != -1) {
            return success;
        } else {
            return -1;
        }
    }

    /**
     * 收集中奖人信息
     *
     * @param name
     * @param phone
     * @param address
     * @return
     */
    @RequestMapping(value = "/insertUserInfo", method = RequestMethod.POST)
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
        logger.info("----------" + s);
        JSONObject jsonObject = JSON.parseObject(s);
        name = jsonObject.getString("name");
        phone = jsonObject.getString("phone");
        address = jsonObject.getString("address");
        rewardtype = Integer.parseInt("" + jsonObject.get("rewardtype"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LjInfo ljInfo = new LjInfo();
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
     *
     * @return
     */
    @RequestMapping("/getResultInfo")
    public Map<String, Object> getResultInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("one", (10 - RandomNum.a));
        map.put("two", (100 - RandomNum.b));
        map.put("three", (240 - RandomNum.c));
        map.put("four", (1650 - RandomNum.d));
        return map;
    }

    @RequestMapping("/getmarqueetext")
    public List<Map<String, String>> getmarqueetext() {
//        StringBuffer sb = new StringBuffer();
        String sb = "";
        //查询获奖名单

        Sort sort = new Sort(Sort.Direction.DESC, "createtime");
        PageRequest pageRequest = this.buildPageRequest(1, 30, sort);
        Page<LjInfo> all = ljINfoRepository.findAll(new Specification<LjInfo>() {//高级查询  支持 like,equals,or,in......
            @Override
            public Predicate toPredicate(Root<LjInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                list.add(criteriaBuilder.notEqual(root.get("rewardtype"), 3));
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        }, pageRequest);
        List<Map<String, String>> mapList = new ArrayList<>();
        if (all != null && all.getContent().size() > 0) {
            List<LjInfo> content = all.getContent();
            for (LjInfo a : content) {
                Map<String, String> map = new HashMap<>();
                String name = a.getName();
                String phone = a.getPhone();
                String aa = "";
                String phoneText = "";
                if (name.length() > 0) {
                    Integer rewardtype = a.getRewardtype();
                    if (rewardtype == 0) {
                        aa = "一等奖         ";
                    } else if (rewardtype == 1) {
                        aa = "二等奖        ";
                    } else if (rewardtype == 2) {
                        aa = "三等奖          ";
                    }
                }
                if (phone.length() == 11) {
                    String substring1 = phone.substring(0, 3);
                    String substring2 = phone.substring(phone.length() - 3, phone.length());
                    phoneText = (substring1 + "*****" + substring2 + "");
                }
                map.put("title", "恭喜 [" + name + " " + phoneText + "]获得" + aa);
                mapList.add(map);
            }
        }
        return mapList;
    }

    @RequestMapping("/img")
    public void renderPicture(HttpServletResponse response) {
        String path = "/hyxt/choujiang/api/back.jpg";
        try {
            byte[] bytes = FileUtil.toByteArray(path);
            response.getOutputStream().write(bytes);
        } catch (Exception e) {
            //如果找不到图片就返回一个默认图片
            try {
                response.sendRedirect("/static/img/head.png");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @RequestMapping("/prizeimg/{num}")
    public void renderPicture(@PathVariable Integer num, HttpServletResponse response) {
        String path = "/hyxt/choujiang/api/back.jpg";

        try {
            if (num != null) {
                if (num == 0) {
                    path = "/hyxt/choujiang/api/back0.jpg";
                } else if (num == 1) {
                    path = "/hyxt/choujiang/api/back1.jpg";
                } else if (num == 2) {
                    path = "/hyxt/choujiang/api/back2.jpg";
                } else if (num == 3) {
                    path = "/hyxt/choujiang/api/back3.jpg";
                }
            }
            byte[] bytes = FileUtil.toByteArray(path);
            response.getOutputStream().write(bytes);
        } catch (Exception e) {
            //如果找不到图片就返回一个默认图片
            try {
                response.sendRedirect("/static/img/head.png");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private PageRequest buildPageRequest(int pageNumber, int pagzSize, Sort sort) {
        return new PageRequest(pageNumber - 1, pagzSize, sort == null ? null : sort);
    }
}
