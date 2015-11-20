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
    private static int pageSize = 10;
    @RequestMapping(value ="/list", method = RequestMethod.GET)
    public ModelAndView list(int pageIndex)
    {
        ModelAndView modelAndView = new ModelAndView("/eventsources/list");
        try {
            eventSourceOperation esOperation = new eventSourceOperation();
            PageDivisionQueryResultModel<EventSourceModel> eventSourceModelPageDivisionQueryResultModel
                    = esOperation.getEventSourceList(pageIndex);
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
    public Map<String, Object> detail(@RequestParam("eventSourceId") int eventSourceId,  int Count)
    {
        Map<String, Object> model = new HashMap<String,Object>();
        try{
            eventSourceOperation esOperation = new eventSourceOperation();
            EventSourceModel esModel = esOperation.GetEventsourceById(eventSourceId);

            eventLogOperation elOperation = new eventLogOperation();
            List<EventLogModel> eventLogModels = elOperation.getEventLogsByEventsourceId(eventSourceId, Count);

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

    @RequestMapping(value="/newEventSourceForm",method = RequestMethod.GET)
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
    public ModelAndView getConnectInfoForm(@RequestParam int templateId){
        ModelAndView modelAndView = new ModelAndView("/eventsources/connectinfo");
        try{
            eventSourceTemplateOperation estOperation = new eventSourceTemplateOperation();
            List<EventSourceTemplateModel> eventSourceModels = estOperation.getEventSourceTemplateList();
            for(EventSourceTemplateModel eventSourceTemplateModel:eventSourceModels) {
                if(eventSourceTemplateModel.getId() == templateId)
                modelAndView.addObject("ESTemplate", eventSourceTemplateModel);
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
                                              String protocol, String[] templateActivityIds,
                                              String[] eaNames,String[] eaValues){
        Map<String, Object> model = new HashMap<String, Object>();
        try{
            EventSourceModel eventSourceModel = new EventSourceModel();
            eventSourceModel.setSourceId(sourceId);
            eventSourceModel.setEventDecoder(eventDecoder);
            eventSourceModel.setEventSourceTemplateId(Integer.parseInt(eventSourceTemplateId));
            eventSourceModel.setComments(comments);
            eventSourceModel.setProtocol(protocol);
            List<EventSourceActivityModel> eventSourceActivityModels = new ArrayList<EventSourceActivityModel>();
            for(int i=0; i<eaNames.length;i++)
            {
                EventSourceActivityModel eventSourceActivityModel= new EventSourceActivityModel();
                eventSourceActivityModel.setName(eaNames[i]);
                eventSourceActivityModel.setValue(eaValues[i]);
                eventSourceActivityModel.setTemplateActivityId(Integer.parseInt(templateActivityIds[i]));
                eventSourceActivityModels.add(eventSourceActivityModel);
            }
            eventSourceModel.setEventSourceActivities(eventSourceActivityModels);
            eventSourceOperation esOperation = new eventSourceOperation();
            esOperation.AddEventsource(eventSourceModel);
            model.put("success", true);
        }catch (Exception ex){
            ex.printStackTrace();
            model.put("success", false);
            model.put("eMessage", ex.getMessage());
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

    @RequestMapping(value="/getAddSubscriberForm",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getAddSubscriberForm(@RequestParam int eventSourceId){
        ModelAndView model = new ModelAndView("/eventsources/addsubscriber");
        try{
            //TODO:get active subscribers, but not in the current event source subscriber list
            List<SubscriberModel> subscriberModels = new ArrayList<SubscriberModel>();
            eventSourceOperation esOperation = new eventSourceOperation();
            EventSourceModel esModel = esOperation.GetEventsourceById(eventSourceId);
            model.addObject("SubscriberList", subscriberModels);
            model.addObject("eventSourceId",eventSourceId);
            model.addObject("success", true);
        }catch (Exception ex){
            ex.printStackTrace();
            model.addObject("success", false);
        }
        finally {
            return model;
        }
    }

    @RequestMapping(value="/addSubscriber",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addSubscriber2EventSource(@RequestParam String eventSourceId, @RequestParam String subscriberId){
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
