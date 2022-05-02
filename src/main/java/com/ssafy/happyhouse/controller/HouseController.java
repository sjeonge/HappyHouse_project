package com.ssafy.happyhouse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/house")
public class HouseController {
	
	@GetMapping("/search/address/{address}")
	public String searchByDong(@PathVariable String address) {
		return "/search";
	}
	
	@GetMapping("/search")
	public ModelAndView searchByAptName(String aptName) {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/search/searchByAptName");
		
		if(aptName != null && aptName.trim().length() > 0) {
			
		}
		return mv;
	}
	
}
