/*
 * -----------------------------------------------------------
 * file name  : LoginController.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Thu 11 Jun 2015 01:53:08 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.cooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.annotation.Validated;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import net.vitular.tools.auth.LoginUser;

/**
 * login controller.
 *
 * RequestMapping:
 *  use for class: indicated the parent path, like /main
 *
 *  use for method:
 *      value="/login"
 *      value="/login/{user}"
 *      value="/login/{user}/pass/{pass}"   use PathVariable to get parameter
 *      value="/login/{name:[a-z-]+}-{version:\d.\d.\d}.{extension:\.[a-z]}"    use regex, PathVariable
 *      value={"/login", "/foo/login"}
 *
 *      method={RequestMethod.POST, RequestMethod.PUT} (GET,PUT,HEAD...)
 *
 *      headers={"key=value", "key2=val2"}
 *
 *      consumes="application/json,text/html"
 *          curl -H "Accept:application/json,text/html" http://ip:8080/Cooking/login/danny
 *
 *      produces="Accept"
 *
 *      params="myParam=myValue"            only handle the request which has this param and value
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
@Controller
@RequestMapping("/auth")
public class LoginController extends BaseController {

    /**
     * show login page.
     *
     * @return ModelAndView
     */
    @RequestMapping(value="/login", method={RequestMethod.GET})
    public ModelAndView loginPage() {
        // 1. validate request parameter
        // 2. invoke model to handle command
        // 3. select the next viewer

        ModelAndView mv = new ModelAndView();

        // add model data

        mv.addObject("loginUser", new LoginUser("danny", "123"));

        // set logic viewer name
        mv.setViewName("login");

        return mv;
    }

    /**
     * handle login request.
     *
     * @param user      login user name
     * @param password  user password
     * @return ModelAndView
     */
    @RequestMapping(value="/login", method={RequestMethod.POST})
    public ModelAndView login(
        @Validated(value=LoginUser.class) LoginUser user,
        RedirectAttributes redirectAttributes) {

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());

        ModelAndView mv = new ModelAndView();

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            mv.addObject("message", "login failure!");
        }

        if (subject.isAuthenticated()) {
            mv.setViewName("main/dashboard");

            //redirectAttributes.addFlashAttribute("", "");
            //redirectAttributes.addFlashAttribute("", "");
            //mv.setViewName("redirect:/main/dashboard");
            //
            //mv.setViewName("forward:/main/dashboard");
        } else {
            mv.setViewName("login");
        }

        return mv;
    }

    /**
     * handle unauthorized request.
     *
     * @param user      login user name
     * @param password  user password
     * @return ModelAndView
     */
    @RequestMapping(value="/unauthorized", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView unauthorized() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("unauthorized");
        return mv;
    }
} // END: LoginController
///:~
