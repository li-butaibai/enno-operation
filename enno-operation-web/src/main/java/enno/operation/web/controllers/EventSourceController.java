package enno.operation.web.controllers;

import enno.operation.core.Operation.eventLogOperation;
import enno.operation.core.Operation.eventSourceOperation;
import enno.operation.core.Operation.eventSourceTemplateOperation;
import enno.operation.core.model.EventLogModel;
import enno.operation.core.model.EventSourceModel;
import enno.operation.core.model.EventSourceTemplateModel;
import enno.operation.core.model.PageDivisionQueryResultModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EriclLee on 15/11/15.
 */
@RequestMapping(value = "/eventsources")
@Controller
public class EventSourceController {
    @RequestMapping(value = {"","/","/list"}, method = RequestMethod.GET)
    public ModelAndView list(int pageIndex, int pageSize)
    {
        ModelAndView modelAndView = new ModelAndView("/eventsources/list");
        try {
            eventSourceOperation esOperation = new eventSourceOperation();
            PageDivisionQueryResultModel<EventSourceModel> eventSourceModelPageDivisionQueryResultModel
                    = esOperation.getEventSourceList(pageIndex, pageSize);
            modelAndView.addObject("EventSourcePage", eventSourceModelPageDivisionQueryResultModel);
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

    @RequestMapping(value="/detail/{eventSourceId}", method = RequestMethod.GET)
    public Map<String, Object> detail(String eventSourceId)
    {
        Map<String, Object> model = new HashMap<String,Object>();
        try{
            int esId = Integer.parseInt(eventSourceId);
            eventSourceOperation esOperation = new eventSourceOperation();
            EventSourceModel esModel = esOperation.GetEventsourceById(esId);

            eventLogOperation elOperation = new eventLogOperation();
            List<EventLogModel> eventLogModels = elOperation.getEventLogsByEventsourceId(esId);

            model.put("EventSource",esModel);
            model.put("EventLogList",eventLogModels);
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

    @RequestMapping(value="/newEventSourceForm",method = RequestMethod.GET)
    public ModelAndView getESNewForm(){
        ModelAndView modelAndView = new ModelAndView("/eventsources/detail");
        try{
            eventSourceTemplateOperation estOperation = new eventSourceTemplateOperation();
            List<EventSourceTemplateModel> eventSourceModels = estOperation.getEventSourceTemplateList();
            modelAndView.addObject("ESTemplateList", eventSourceModels);
            modelAndView.addObject("success", true);
        }catch (Exception ex){
            ex.printStackTrace();
            modelAndView.addObject("success", false);
        }
        finally {
            return modelAndView;
        }
    }

    @RequestMapping(value="/add",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> newEventSource(@RequestBody EventSourceModel eventSourceModel){
        Map<String, Object> model = new HashMap<String, Object>();
        try{
            //TODO: add new event source
            eventSourceOperation esOperation = new eventSourceOperation();
            esOperation.AddEventsource(eventSourceModel);
            model.put("success", true);
        }catch (Exception ex){
            ex.printStackTrace();
            model.put("success", false);
        }
        finally {
            return model;
        }
    }

    @RequestMapping(value="/update",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateEventSource(@RequestBody EventSourceModel eventSourceModel){
        Map<String, Object> model = new HashMap<String, Object>();
        try{
            //TODO: update event source
            eventSourceOperation esOperation = new eventSourceOperation();
            esOperation.UpdateEventsource(eventSourceModel);
            model.put("success", true);
        }catch (Exception ex){
            ex.printStackTrace();
            model.put("success", false);
        }
        finally {
            return model;
        }
    }

    @RequestMapping(value="/delete/{eventSourceId}",method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> deleteEventSource(String eventSourceId){
        Map<String, Object> model = new HashMap<String, Object>();
        try{
            //TODO: delete event source
            eventSourceOperation esOperation = new eventSourceOperation();
            esOperation.DeleteEventsource(Integer.parseInt(eventSourceId));
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
    public Map<String, Object> offlineEventSource(){
        Map<String, Object> model = new HashMap<String, Object>();
        try{
            //TODO: offline event source
            model.put("success", true);
        }catch (Exception ex){
            ex.printStackTrace();
            model.put("success", false);
        }
        finally {
            return model;
        }
    }

    @RequestMapping(value="/{eventSourceId}/addSubscriber/{subscriberId}",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addSubscriber2EventSource(String eventSourceId, String subscriberId){
        Map<String, Object> model = new HashMap<String, Object>();
        try{
            //TODO: add subscriber for event source
            eventSourceOperation esOperation = new eventSourceOperation();
            esOperation.AssignEventsource(Integer.parseInt(eventSourceId),Integer.parseInt(subscriberId));
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
