package enno.operation.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/17.
 */
@Controller
@RequestMapping(value = "/eventlogs")
public class EventLogController {
    @RequestMapping(value = {"","/","/list"}, method = RequestMethod.GET)
    public ModelAndView list(int pageIndex, int pageSize)
    {
        ModelAndView modelAndView = new ModelAndView("/eventsources/list");
        try {
            //TODO��
            modelAndView.addObject("success", true);
        }
        catch (Exception ex)
        {
            modelAndView.addObject("success",false);
        }
        finally {
            return modelAndView;
        }
    }

    @RequestMapping(value="/detail/{eventLogId}", method = RequestMethod.GET)
    public Map<String, Object> detail(String eventSourceId)
    {
        Map<String, Object> model = new HashMap<String,Object>();
        try{
            //TODO��
            model.put("success",true);
            return model;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            model.put("success",false);
            return model;
        }
        finally {
            return model;
        }
    }
}
