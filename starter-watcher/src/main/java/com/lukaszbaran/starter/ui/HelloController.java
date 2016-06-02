package com.lukaszbaran.starter.ui;


import com.lukaszbaran.starter.watcher.DirectoryWatcher;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
    private static final Logger LOGGER = Logger.getLogger(HelloController.class);

    private DirectoryWatcher directoryWatcher;

    @RequestMapping("/start")
    public ModelAndView start(ModelMap model) {
        LOGGER.debug("starting DirectoryWatcher");
        model.addAttribute("enabled", false);
        try {
            directoryWatcher.afterPropertiesSet();
        } catch (Exception e) {
            LOGGER.error("Unable to start DirectoryWatcher!", e);
        }
        return new ModelAndView("hello", "enabled", true);
    }

    @RequestMapping("/stop")
    public ModelAndView stop(ModelMap model) throws Exception {
        LOGGER.debug("stoping DirectoryWatcher");
        model.addAttribute("enabled", false);
        directoryWatcher.destroy();

        return new ModelAndView("hello", "enabled", false);
    }

    public void setDirectoryWatcher(DirectoryWatcher directoryWatcher) {
        this.directoryWatcher = directoryWatcher;
    }

}