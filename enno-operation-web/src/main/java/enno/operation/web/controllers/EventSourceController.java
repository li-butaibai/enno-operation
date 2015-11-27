package enno.operation.web.controllers;

import enno.operation.core.Operation.eventLogOperation;
import enno.operation.core.Operation.eventSourceOperation;
import enno.operation.core.Operation.eventSourceTemplateOperation;
import enno.operation.core.Operation.subscriberOperation;
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

    //region event source list and detail
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(int pageIndex) {
        ModelAndView modelAndView = new ModelAndView("/eventsources/list");
        try {
            eventSourceOperation esOperation = new eventSourceOperation();
            PageDivisionQueryResultModel<EventSourceModel> eventSourceModelPageDivisionQueryResultModel
                    = esOperation.getEventSourceList(pageIndex);
            modelAndView.addObject("EventSourcePage", eventSourceModelPageDivisionQueryResultModel);
            modelAndView.addObject("success", true);
        } catch (Exception ex) {
            modelAndView.addObject("success", false);
        } finally {
            return modelAndView;
        }
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Map<String, Object> detail(@RequestParam("eventSourceId") int eventSourceId, int Count) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            eventSourceOperation esOperation = new eventSourceOperation();
            EventSourceModel esModel = esOperation.GetEventsourceById(eventSourceId);

            eventLogOperation elOperation = new eventLogOperation();
            List<EventLogModel> eventLogModels = elOperation.getEventLogsByEventsourceId(eventSourceId, Count);

            model.put("EventSource", esModel);
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
    //endregion

    //region new event source
    @RequestMapping(value = "/newEventSourceForm", method = RequestMethod.GET)
    public ModelAndView getESNewForm() {
        ModelAndView modelAndView = new ModelAndView("/eventsources/neweventsource");
        try {
            eventSourceTemplateOperation estOperation = new eventSourceTemplateOperation();
            List<EventSourceTemplateModel> eventSourceModels = estOperation.getEventSourceTemplateList();
            modelAndView.addObject("ESTemplateList", eventSourceModels);
            modelAndView.addObject("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            modelAndView.addObject("success", false);
        } finally {
            return modelAndView;
        }
    }

    @RequestMapping(value = "/getconnectinfo", method = RequestMethod.GET)
    public ModelAndView getConnectInfoForm(@RequestParam int templateId) {
        ModelAndView modelAndView = new ModelAndView("/eventsources/connectinfo");
        try {
            eventSourceTemplateOperation estOperation = new eventSourceTemplateOperation();
            List<EventSourceTemplateModel> eventSourceModels = estOperation.getEventSourceTemplateList();
            for (EventSourceTemplateModel eventSourceTemplateModel : eventSourceModels) {
                if (eventSourceTemplateModel.getId() == templateId)
                    modelAndView.addObject("ESTemplate", eventSourceTemplateModel);
            }
            modelAndView.addObject("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            modelAndView.addObject("success", false);
        } finally {
            return modelAndView;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> newEventSource(String sourceId, String eventDecoder,
                                              String eventSourceTemplateId, String comments,
                                              String protocol, String[] templateActivityIds,
                                              String[] eaNames, String[] eaValues) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            EventSourceModel eventSourceModel = new EventSourceModel();
            eventSourceModel.setSourceId(sourceId);
            eventSourceModel.setEventDecoder(eventDecoder);
            eventSourceModel.setEventSourceTemplateId(Integer.parseInt(eventSourceTemplateId));
            eventSourceModel.setComments(comments);
            eventSourceModel.setProtocol(protocol);
            List<EventSourceActivityModel> eventSourceActivityModels = new ArrayList<EventSourceActivityModel>();
            for (int i = 0; i < eaNames.length; i++) {
                EventSourceActivityModel eventSourceActivityModel = new EventSourceActivityModel();
                eventSourceActivityModel.setName(eaNames[i]);
                eventSourceActivityModel.setValue(eaValues[i]);
                eventSourceActivityModel.setTemplateActivityId(Integer.parseInt(templateActivityIds[i]));
                eventSourceActivityModels.add(eventSourceActivityModel);
            }
            eventSourceModel.setEventSourceActivities(eventSourceActivityModels);
            eventSourceOperation esOperation = new eventSourceOperation();
            esOperation.AddEventsource(eventSourceModel);
            model.put("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.put("success", false);
            model.put("eMessage", ex.getMessage());
        } finally {
            return model;
        }
    }
    //endregion

    //region update event source
    @RequestMapping(value = "/getUpdateEventSourceForm", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getUpdateEventSourceForm(@RequestParam int eventSourceId) {
        ModelAndView modelAndView = new ModelAndView("/eventsources/updateeventsource");
        try {
            eventSourceOperation esOperation = new eventSourceOperation();
            EventSourceModel eventSourceModel = new EventSourceModel();
            eventSourceModel = esOperation.GetEventsourceById(eventSourceId);
            modelAndView.addObject("EventSource", eventSourceModel);
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
    public Map<String, Object> updateEventSource(String id, String sourceId, String eventDecoder,
                                                 String comments, String eventSourceTemplateId,
                                                 String protocol, String[] templateActivityIds,
                                                 String[] eaNames, String[] eaValues) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            EventSourceModel eventSourceModel = new EventSourceModel();
            eventSourceModel.setId(Integer.parseInt(id));
            eventSourceModel.setSourceId(sourceId);
            eventSourceModel.setEventDecoder(eventDecoder);
            eventSourceModel.setComments(comments);
            eventSourceModel.setProtocol(protocol);
            eventSourceModel.setEventSourceTemplateId(Integer.parseInt(eventSourceTemplateId));
            List<EventSourceActivityModel> eventSourceActivityModels = new ArrayList<EventSourceActivityModel>();
            for (int i = 0; i < eaNames.length; i++) {
                EventSourceActivityModel eventSourceActivityModel = new EventSourceActivityModel();
                eventSourceActivityModel.setTemplateActivityId(Integer.parseInt(templateActivityIds[i]));
                eventSourceActivityModel.setName(eaNames[i]);
                eventSourceActivityModel.setValue(eaValues[i]);
                eventSourceActivityModels.add(eventSourceActivityModel);
            }
            eventSourceModel.setEventSourceActivities(eventSourceActivityModels);
            eventSourceOperation esOperation = new eventSourceOperation();
            esOperation.UpdateEventsource(eventSourceModel);
            model.put("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.put("success", false);
        } finally {
            return model;
        }
    }
    //endregion

    //region offline or delete event source
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> deleteEventSource(@RequestParam String eventSourceId) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            eventSourceOperation esOperation = new eventSourceOperation();
            esOperation.DeleteEventsource(Integer.parseInt(eventSourceId));
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
    public Map<String, Object> offlineEventSource(@RequestParam String eventSourceId) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            eventSourceOperation esOperation = new eventSourceOperation();
            esOperation.OfflineEventsource(Integer.parseInt(eventSourceId));
            model.put("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.put("success", false);
        } finally {
            return model;
        }
    }
    //endregion

    //region add or remove subscriber for event source
    @RequestMapping(value = "/getAddSubscriberForm", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getAddSubscriberForm(@RequestParam int eventSourceId) {
        ModelAndView model = new ModelAndView("/eventsources/addsubscriber");
        try {
            //TODO:get active subscribers, but not in the current event source subscriber list
            List<SubscriberModel> subscriberModels = new ArrayList<SubscriberModel>();
            eventSourceOperation esOperation = new eventSourceOperation();
            subscriberOperation subOp = new subscriberOperation();
            EventSourceModel esModel = esOperation.GetEventsourceById(eventSourceId);
            subscriberModels = subOp.getUnSubscribeListByEventSourceId(eventSourceId);
            model.addObject("SubscriberList", subscriberModels);
            model.addObject("EventSource", esModel);
            model.addObject("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addObject("success", false);
        } finally {
            return model;
        }
    }

    @RequestMapping(value = "/addSubscriber", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addSubscriber2EventSource(@RequestParam String eventSourceId, @RequestParam String subscriberId) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            eventSourceOperation esOperation = new eventSourceOperation();
            esOperation.AssignEventsource(Integer.parseInt(eventSourceId), Integer.parseInt(subscriberId));
            model.put("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.put("success", false);
        } finally {
            return model;
        }
    }

    @RequestMapping(value = "/removeSubscriber", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> RemoveSubscriber2EventSource(int eventSourceId, int subscriberId) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            eventSourceOperation esOperation = new eventSourceOperation();
            esOperation.RemoveEventsourceSubscription(eventSourceId, subscriberId);
            model.put("success", true);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.put("success", false);
        } finally {
            return model;
        }
    }
    //endregion
}
