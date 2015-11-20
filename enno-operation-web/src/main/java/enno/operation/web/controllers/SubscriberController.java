package enno.operation.web.controllers;

import enno.operation.core.Operation.eventLogOperation;
import enno.operation.core.Operation.eventSourceOperation;
import enno.operation.core.Operation.subscriberOperation;
import enno.operation.core.model.EventLogModel;
import enno.operation.core.model.EventSourceModel;
import enno.operation.core.model.PageDivisionQueryResultModel;
import enno.operation.core.model.SubscriberModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/17.
 */
@Controller
@RequestMapping(value = "/subscribers")
public class SubscriberController {
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(int pageIndex) {
        ModelAndView modelAndView = new ModelAndView("/subscribers/list");
        try {
            subscriberOperation subOp = new subscriberOperation();
            PageDivisionQueryResultModel<SubscriberModel> subscriberModelPageDivisionQueryResultModel
                    = subOp.getPageDivisonSubscriberList(pageIndex);
            modelAndView.addObject("SubscriberPage", subscriberModelPageDivisionQueryResultModel);
            modelAndView.addObject("success", true);
        } catch (Exception ex) {
            modelAndView.addObject("success", false);
        } finally {
            return modelAndView;
        }
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Map<String, Object> detail(@RequestParam int subscriberId, int Count) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            subscriberOperation subOp = new subscriberOperation();
            SubscriberModel subModel = subOp.getSubscriberById(subscriberId);

            eventLogOperation elOperation = new eventLogOperation();
            List<EventLogModel> eventLogModels = elOperation.getEventLogsBySubscriberId(subscriberId, Count);

            model.put("Subscriber", subModel);
            model.put("EventLogList", eventLogModels);
            model.put("success", true);
            return model;
        } catch (Exception ex) {
            ex.printStackTrace();
            model.put("success", false);
            return model;
        } finally {
            return model;
        }
    }

    @RequestMapping(value = "/getUpdateSubscriberForm", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getUpdateSubscriberForm(@RequestParam int subscriberId) {
        ModelAndView modelAndView = new ModelAndView("updatesubscriber");
        try {
            subscriberOperation subOp = new subscriberOperation();
            SubscriberModel suberModel = new SubscriberModel();
            suberModel = subOp.getSubscriberById(subscriberId);
            modelAndView.addObject("Subscriber", suberModel);
            modelAndView.addObject("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            modelAndView.addObject("success", false);
        } finally {
            return modelAndView;
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateSubscriber(@RequestBody SubscriberModel subscriberModel) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            subscriberOperation subOp = new subscriberOperation();
            subOp.UpdateSubscriber(subscriberModel);
            model.put("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.put("success", false);
        } finally {
            return model;
        }
    }

    @RequestMapping(value = "/delete/{subscriberId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> deleteSubscriber(String subscriberId) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            subscriberOperation subOp = new subscriberOperation();
            subOp.DeleteSubscriber(Integer.parseInt(subscriberId));
            model.put("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.put("success", false);
        } finally {
            return model;
        }
    }

    @RequestMapping(value = "/offline", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> offlineSubscriber() {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            //TODO: offline subscriber
            model.put("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.put("success", false);
        } finally {
            return model;
        }
    }

    @RequestMapping(value = "/getAddEventSourceForm", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getAddEventSourceForm(@RequestParam int subscriberId) {
        ModelAndView model = new ModelAndView("/subscribers/addeventsource");
        try {
            List<EventSourceModel> esModels = new ArrayList<EventSourceModel>();
            eventSourceOperation esOperation = new eventSourceOperation();
            subscriberOperation subOp = new subscriberOperation();
            SubscriberModel subModel = subOp.getSubscriberById(subscriberId);
            esModels = esOperation.getUnSubscribedEventsourceListBySubscriberId(subscriberId);
            model.addObject("EventSourceList", esModels);
            model.addObject("Subscriber", subModel);
            model.addObject("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addObject("success", false);
        } finally {
            return model;
        }
    }
}
