package com.cas.parent.controller;

import com.cas.parent.config.Utils;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by khan on 3/1/18.
 */
@Controller
@Log4j
public class AuthenticationController {

  @RequestMapping("/login")
  public String login() {
    return "redirect:/secured";
  }

  @RequestMapping("/logout")
  public String logout() {
    return "redirect:/logout/cas";
  }

  @RequestMapping("/secured")
  public String secured(Model model, HttpServletRequest httpServletRequest) {
    model.addAttribute("_userName", Utils.getUserName());
    model.addAttribute("_contextPath", httpServletRequest.getContextPath());
    return "secure";
  }

}
