package com.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class HomeController {

    /**
     * As user to login via facebook
     * @return
     */
    @RequestMapping("/")
    public ModelAndView showLogin() {
        FBConnection fbConnection = new FBConnection();

        return new
                ModelAndView("login","message",fbConnection.getFBAuthUrl());
    }

    @RequestMapping("loginResult")
    public ModelAndView loginResult(@RequestParam("code") String code)
    {
        // See if we got anyting back as code
        if (code == null || code.equals("")) {
            throw new RuntimeException("ERROR: Did not get any code paraeter in callback");
        }

        // If code is valid, create a new fb connection
        FBConnection fbConnection = new FBConnection();
        String accessToken = fbConnection.getAccessToken(code);

        // Use the token to get fbGraph object i.e. user's info
        FBGraph fbGraph = new FBGraph(accessToken);
        String graph = fbGraph.getFBGraph();
        Map<String, String> fbProfileData = fbGraph.getGraphData(graph);

        // Create strings for displaying info
        String out = "";
        out = out.concat("<h1>Facebook Login using Java</h1>");
        out = out.concat("<h2>Application Main Menu</h2>");
        out = out.concat("<div>Welcome " + fbProfileData.get("name"));
        out = out.concat("<div>Your Email: " + fbProfileData.get("email"));
        out = out.concat("<div>Your Gender: " + fbProfileData.get("gender"));

        return new ModelAndView("mainMenu","message",out);

    }

}
