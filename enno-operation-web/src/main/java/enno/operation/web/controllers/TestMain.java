package enno.operation.web.controllers;

import com.sun.corba.se.impl.presentation.rmi.ExceptionHandlerImpl;
import enno.operation.core.model.*;
import enno.operation.core.model.Enum;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/17.
 */
public class TestMain {
    public static void main(String[] args) {
        try {
            EventSourceController eventSourceController = new EventSourceController();
            SubscriberController subController = new SubscriberController();
            EventLogController logController = new EventLogController();

            //ModelAndView d = subController.list(1,10);
            ModelAndView d = eventSourceController.list(0);
            //Map<String, Object> d = subController.detail("1");
            //ModelAndView d = logController.list(1,10);
            // Map<String, Object> d = logController.detail("1");

            //region Add Event Source Test
            /*for (int i = 4; i < 10; i++) {
                EventSourceModel es = new EventSourceModel();
                es.setSourceId("eventSource" + i);
                es.setComments("eventsource comments" + i);
                es.setEventDecoder("EventDecoder" + i);
                List<EventSourceActivityModel> l = new ArrayList<EventSourceActivityModel>();
                EventSourceActivityModel m = new EventSourceActivityModel();
                m.setValue("20" + i);
                m.setComments("Activity Comment" + i);
                m.setName("Acitvity Name" + i);
                m.setTemplateActivityId(1);
                m.setDisplayName("Activity Display Name" + i);
                l.add(m);
                es.setEventSourceActivities(l);
                es.setEventSourceTemplateId(1);

                Map<String, Object> d = eventSourceController.newEventSource(es);
            }*/
            //endregion

            //region Update Event Source Test
            /*EventSourceModel es = new EventSourceModel();
            es.setId(2);
            es.setSourceId("eventSource2.6_Update");
            es.setComments("eventsource2.6 comments Update");
            es.setEventDecoder("EventDecoder2.6");
            Map<String, Object> d =  eventSourceController.updateEventSource(es);*/
            //endregion

            //region Delete Event Source Test
            //eventSourceController.deleteEventSource("7");
            //endregion

            //region Assgin Event Source Test
            //eventSourceController.addSubscriber2EventSource("5","1");
            //endregion

            //region Update Subscriber Test
            /*SubscriberModel sub = new SubscriberModel();
            sub.setId(1);
            sub.setName("subName1");
            sub.setComments("sub1 comments Update");
            sub.setAddress("Address1");
            Map<String, Object> d =  subController.updateSubscriber(sub);*/
            //endregion

            //region Delete Subscriber Test
            //subController.deleteSubscriber("2");
            //endregion

            System.in.read();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
