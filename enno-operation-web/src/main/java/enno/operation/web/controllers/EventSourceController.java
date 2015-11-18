package enno.operation.web.controllers;

import enno.operation.core.Operation.eventLogOperation;
import enno.operation.core.Operation.eventSourceOperation;
import enno.operation.core.Operation.eventSourceTemplateOperation;
import enno.operation.core.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
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

    @RequestMapping(value="/detail", method = RequestMethod.GET)
    public Map<String, Object> detail(@RequestParam("eventSourceId") int eventSourceId)
    {
        Map<String, Object> model = new HashMap<String,Object>();
        try{
            eventSourceOperation esOperation = new eventSourceOperation();
            EventSourceModel esModel = esOperation.GetEventsourceById(eventSourceId);

            eventLogOperation elOperation = new eventLogOperation();
            List<EventLogModel> eventLogModels = elOperation.getEventLogsByEventsourceId(eventSourceId);

            model.put("EventSource",esModel);
            model.put("EventLogList", eventLogModels);
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

    @RequestMapping(value="/newform",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getESNewForm(){
        ModelAndView modelAndView = new ModelAndView("/eventsources/neweventsource");
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

    @RequestMapping(value="/getconnectinfo",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getConnectInfo(@RequestParam int templateId){
        ModelAndView modelAndView = new ModelAndView("/eventsources/connectinfo");
        try{
            eventSourceTemplateOperation estOperation = new eventSourceTemplateOperation();
            List<EventSourceTemplateModel> eventSourceModels = estOperation.getEventSourceTemplateList();
            for(EventSourceTemplateModel item : eventSourceModels)
            {
                if(item.getId() == templateId)
                {
                    modelAndView.addObject("ESTemplate", item);
                }
            }
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
    public Map<String, Object> newEventSource(String sourceId, String eventDecoder,
                                              String eventSourceTemplateId, String comments,
                                              String protocol, String[] templateActivityId,
                                              String[] eaName, String[] eaValue){
        Map<String, Object> model = new HashMap<String, Object>();
        try{
            EventSourceModel eventSourceModel = new EventSourceModel();
            eventSourceModel.setSourceId(sourceId);
            eventSourceModel.setEventDecoder(eventDecoder);
            eventSourceModel.setEventSourceTemplateId(Integer.parseInt(eventSourceTemplateId));
            eventSourceModel.setComments(comments);
            List<EventSourceActivityModel> eventSourceActivityModels
                    = new ArrayList<EventSourceActivityModel>();
            for(int i=0; i<eaName.length; i++)
            {
                EventSourceActivityModel eventSourceActivityModel = new EventSourceActivityModel();
                eventSourceActivityModel.setName(eaName[i]);
                eventSourceActivityModel.setTemplateActivityId(Integer.parseInt(templateActivityId[i]));
                eventSourceActivityModel.setValue(eaValue[i]);
                eventSourceActivityModels.add(eventSourceActivityModel);
            }
            eventSourceModel.setEventSourceActivities(eventSourceActivityModels);
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

    @RequestMapping(value="/{eventSourceId}/removeSubscriber/{subscriberId}",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> RemoveSubscriber2EventSource(String eventSourceId, String subscriberId){
        Map<String, Object> model = new HashMap<String, Object>();
        try{
            eventSourceOperation esOperation = new eventSourceOperation();
            esOperation.RemoveEventsourceSubscription(Integer.parseInt(eventSourceId), Integer.parseInt(subscriberId));
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
