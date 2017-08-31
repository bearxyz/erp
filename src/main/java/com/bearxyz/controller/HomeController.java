package com.bearxyz.controller;

import com.bearxyz.domain.po.business.Notice;
import com.bearxyz.domain.po.sys.Permission;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.domain.vo.TaskVO;
import com.bearxyz.service.business.NoticeService;
import com.bearxyz.service.sys.SysService;
import com.bearxyz.utility.RelativeDateFormat;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by bearxyz on 2017/5/25.
 */

@Controller
public class HomeController {

    @Autowired
    private SysService sysService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private NoticeService noticeService;

    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public String login() {
        return "/login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginAction(String username, String password, HttpSession session) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return "redirect:/home.html?#dashboard.html";
        } catch (Exception e) {
            return "redirect:/login.html?error";
        }
    }

    @RequestMapping(value = {"/home.html","/"})
    public String home(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Permission> permissions = sysService.getUserPermissions(user.getId());
        model.addAttribute("permissions", permissions);
        return "/home";
    }

    @RequestMapping(value = "/dashboard.html", method = RequestMethod.GET)
    public String dashboard(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Permission> permissions = sysService.getUserPermissions(user.getId());
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(user.getId()).list();
        List<TaskVO> taskVOS = new ArrayList<>();
        List<Notice> notices = noticeService.getTopAndType(5, "NOTICT_TYPE_INTERNAL");
        for (Task task : tasks) {
            TaskVO vo = new TaskVO();
            vo.setTaskId(task.getId());
            if(taskService.getVariable(task.getId(),"name")!=null)
                vo.setName(taskService.getVariable(task.getId(),"name").toString());
            if(taskService.getVariable(task.getId(),"url")!=null)
                vo.setDetailUrl(taskService.getVariable(task.getId(),"url").toString());
            if(taskService.getVariable(task.getId(),"bid")!=null)
                vo.setBussinessId(taskService.getVariable(task.getId(),"bid").toString());
            if(taskService.getVariable(task.getId(),"applyer")!=null) {
                User u = sysService.getUserById(taskService.getVariable(task.getId(), "applyer").toString());
                vo.setApplyer(u.getFirstName()+u.getLastName());
            }
            vo.setAssignee(task.getAssignee());
            vo.setStage(task.getName());
            vo.setProcessInstanceId(task.getProcessInstanceId());
            vo.setCreateDate(RelativeDateFormat.format(task.getCreateTime()));
            vo.setCreateDatetime(task.getCreateTime());
            vo.setTaskDefinitionKey(task.getTaskDefinitionKey());

            taskVOS.add(vo);
        }
        model.addAttribute("tasks", taskVOS);
        model.addAttribute("permissions", permissions);
        model.addAttribute("user", user);
        model.addAttribute("notices", notices);
        return "/dashboard";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String notPermmited() {
        return "/403";
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String notFound() {
        return "/404";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error() {
        return "/500";
    }

}
