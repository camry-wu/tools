/*
 * -----------------------------------------------------------
 * file name  : MainController.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Fri 12 Jun 2015 04:42:31 PM CST
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

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;

/**
 * main controller.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
@Controller
@RequestMapping("/main")
public class MainController extends BaseController {

    /**
     * show dashboard.
     *
     * @return ModelAndView
     */
    @RequestMapping(value="/dashboard.html")
    public ModelAndView dashboard() {
        ModelAndView mv = new ModelAndView();

        // add model data
        mv.addObject("message", "Show Dashboard!");

        // set logic viewer name
        mv.setViewName("main/dashboard");

        return mv;
    }

    /**
     * show admin page.
     *
     * @return ModelAndView
     */
    @RequestMapping(value="/admin.html")
    public ModelAndView admin() {
        ModelAndView mv = new ModelAndView();

        // add model data
        mv.addObject("message", "Show Admin!");

        // set logic viewer name
        mv.setViewName("main/admin");

        return mv;
    }
} // END: MainController
///:~
