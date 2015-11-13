package enno.operation.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by v-zoli on 2015/11/13.
 */
@Controller
@RequestMapping(value = "/home")
public class HomeController {
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index(){
        return "/home/index";
    }
}
