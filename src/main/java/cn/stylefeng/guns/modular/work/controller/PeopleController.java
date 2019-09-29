package cn.stylefeng.guns.modular.work.controller;

import cn.stylefeng.guns.core.common.annotion.Permission;
import cn.stylefeng.guns.core.common.constant.Const;
import cn.stylefeng.guns.core.common.exception.BizExceptionEnum;
import cn.stylefeng.guns.core.common.page.LayuiPageFactory;
import cn.stylefeng.guns.core.shiro.ShiroKit;
import cn.stylefeng.guns.modular.work.entity.People;
import cn.stylefeng.guns.modular.work.service.PeopleService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

/**
 * 人员控制器
 *
 * @author liangsj
 * @Date 2019年9月28日20:27:22
 */
@Controller
@RequestMapping("/people")
public class PeopleController extends BaseController {

    @Autowired
    PeopleService peopleService;
    private String PREFIX = "/modular/work/people/";

    /**
     * 跳转到人员管理首页
     *
     * @author liangsj
     * @Date 2019年9月29日08:27:22
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "people.html";
    }

    /**
     * 添加人员页面
     *
     * @author liangsj
     * @Date 2019年9月29日08:27:22
     */
    @RequestMapping("/people_add")
    public String addView() {
        return PREFIX + "people_add.html";
    }


    /**
     * 添加人员页面
     *
     * @author liangsj
     * @Date 2019年9月29日08:27:22
     */
    @RequestMapping("/people_import")
    public String importExcle() {
        return PREFIX + "people_import.html";
    }

    /**
     * 查询人员列表
     *
     * @author liangsj
     * @Date 2019年9月29日08:27:22
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String peopleName,
                       @RequestParam(required = false) String peopleDept,
                       @RequestParam(required = false) String peopleType) {


        if (ShiroKit.isAdmin()) {
            Page<Map<String, Object>> peoples = peopleService.selectPeople(null, peopleName, peopleDept, peopleType);
            //Page wrapped = new UserWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(peoples);
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            Page<Map<String, Object>> peoples = peopleService.selectPeople(null, peopleName, peopleDept, peopleType);
            //Page wrapped = new UserWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(peoples);
        }

    }

    /**
     * 添加单个人员
     *
     * @author liangsj
     * @Date 2019年9月29日16:07:22
     */
    @RequestMapping("/add")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public ResponseData add(@Valid People people, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        this.peopleService.peopleAdd(people);
        return SUCCESS_TIP;
    }

    /**
    * 导出excle人员模板
     *
     * @author liangsj
     * @Date 2019年9月29日16:07:22
     */
    @RequestMapping(value = "/export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
    }



}
