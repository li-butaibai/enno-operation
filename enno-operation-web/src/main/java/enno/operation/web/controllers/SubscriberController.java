package enno.operation.web.controllers;

import enno.operation.core.model.SubscriberModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/17.
 */
@Controller
@RequestMapping(value = "/subscribers")
public class SubscriberController {
    @RequestMapping(value={"","/","list"}, method = RequestMethod.GET)
    public ModelAndView list(int pageIndex, int pageSize)
    {
        ModelAndView modelAndView = new ModelAndView("/subscribers/list");
        try{
            //TODO: get subscriber list
            modelAndView.addObject("success",true);
        }
        catch (Exception ex){
            modelAndView.addObject("success",false);
        }
        finally {
            return modelAndView;
        }
    }

    @RequestMapping(value="/detail/{subscirberId}", method = RequestMethod.GET)
    public Map<String, Object> detail(String subscirberId)
    {
        Map<String, Object> model = new HashMap<String,Object>();
        try{
            //TODO£º get subscriber detail
            model.put("success", true);
            return model;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            model.put("success", false);
            return model;
        }
        finally {
            return model;
        }
    }

    @RequestMapping(value="/update",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateSubscriber(@RequestBody SubscriberModel subscriberModel){
        Map<String, Object> model = new HashMap<String, Object>();
        try{
            //TODO: update event source
            model.put("success", true);
        }catch (Exception ex){
            ex.printStackTrace();
            model.put("success", false);
        }
        finally {
            return model;
        }
    }

    @RequestMapping(value="/delete/{subscriberId}",method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> deleteSubscriber(String subscriberId){
        Map<String, Object> model = new HashMap<String, Object>();
        try{
            //TODO: delete subscriber;
            model.put("success", true);
        }catch (Exception ex){
            ex.printStackTrace();
            model.put("success", false);
        }
        finally {
            return model;
        }
    }

    @RequestMapping(value="/offline",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> offlineSubscriber(){
        Map<String, Object> model = new HashMap<String, Object>();
        try{
            //TODO: offline subscriber
            model.put("success", true);
        }catch (Exception ex){
            ex.printStackTrace();
            model.put("success", false);
        }
        finally {
            return model;
        }
    }
}
