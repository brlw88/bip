package me.brlw.bip.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Created by vl on 10.09.16.
 */

@Controller
public class HelpPageController {

    @RequestMapping(method = RequestMethod.GET, path={"/help", "/"})
    public String helppage()
    {
        return "static/helppage.html";
    }
}
