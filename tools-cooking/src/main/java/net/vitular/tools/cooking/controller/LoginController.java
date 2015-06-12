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
     * handle login request.
     *
     * @param user      login user name
     * @param password  user password
     * @return ModelAndView
     */
    @RequestMapping(value="/login/{user}", method=RequestMethod.POST)
    public ModelAndView login(@PathVariable String user, @RequestParam String password) {
        // 1. validate request parameter
        // 2. convert parameter to command object
        // 3. invoke model to handle command
        // 4. select the next viewer

        ModelAndView mv = new ModelAndView();

        // add model data
        mv.addObject("username", user);
        mv.addObject("password", password);
        mv.addObject("message", "login success!");

        // set logic viewer name
        mv.setViewName("main");

        return mv;
    }

    /**
     * handle logout request.
     *
     * @return ModelAndView
     */
    @RequestMapping(value="/logout")
    public ModelAndView logout() {
        ModelAndView mv = new ModelAndView();

        // add model data
        mv.addObject("message", "logout success!");

        // set logic viewer name
        mv.setViewName("main");

        return mv;
    }

    /**
     * handle denied request.
     *
     * @return ModelAndView
     */
    @RequestMapping(value="/denied", method={RequestMethod.GET, RequestMethod.POST})
    public ModelAndView denied() {

        ModelAndView mv = new ModelAndView();

        // add model data
        mv.addObject("message", "You has no authentication!");

        // set logic viewer name
        mv.setViewName("denied");

        return mv;
    }
} // END: LoginController
///:~
