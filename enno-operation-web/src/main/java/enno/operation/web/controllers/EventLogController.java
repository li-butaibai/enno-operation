package enno.operation.web.controllers;

import enno.operation.core.Operation.eventLogOperation;
import enno.operation.core.model.EventLogModel;
import enno.operation.core.model.PageDivisionQueryResultModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/17.
 */
@Controller
@RequestMapping(value = "/eventlogs")
public class EventLogController {
    @RequestMapping(value = {"", "/", "/list"}, method = RequestMethod.GET)
    public ModelAndView list(int pageIndex, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/eventlogs/list");
        try {
            eventLogOperation logOp = new eventLogOperation();
            PageDivisionQueryResultModel<EventLogModel> pageDivisionEventLogList = logOp.getPageDivisionEventLogList(pageIndex);
            modelAndView.addObject("EventlogPage", pageDivisionEventLogList);
            modelAndView.addObject("success", true);
        } catch (Exception ex) {
            modelAndView.addObject("success", false);
        } finally {
            return modelAndView;
        }
    }

    @RequestMapping(value = "/detail/{eventLogId}", method = RequestMethod.GET)
    public Map<String, Object> detail(String eventLogId) {
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            eventLogOperation logOp = new eventLogOperation();
            EventLogModel log = logOp.getEventLogById(Integer.parseInt(eventLogId));
            model.put("EventLog", log);
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
}
