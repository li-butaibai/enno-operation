package enno.operation.web.controllers;

import com.sun.corba.se.impl.presentation.rmi.ExceptionHandlerImpl;
import enno.operation.core.model.EventSourceModel;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by v-zoli on 2015/11/17.
 */
public class TestMain {
    public static void main(String[] args)
    {
        try {
            EventSourceController eventSourceController = new EventSourceController();
            ModelAndView d = eventSourceController.list(1, 10);
            System.in.read();
        }catch (Exception ex)
        {ex.printStackTrace();}
    }
}
