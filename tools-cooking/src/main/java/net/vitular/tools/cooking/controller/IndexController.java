/*
 * -----------------------------------------------------------
 * file name  : IndexController.java
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
 * index controller.
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
public class IndexController extends BaseController {

    /**
     * handle index request.
     *
     * @return ModelAndView
     */
    @RequestMapping(value={"/", "/index"}, method={RequestMethod.GET})
    public ModelAndView denied() {

        ModelAndView mv = new ModelAndView();

        // add model data
        mv.addObject("message", "This is the index page!");

        // set logic viewer name
        mv.setViewName("index");

        return mv;
    }
} // END: IndexController
///:~
