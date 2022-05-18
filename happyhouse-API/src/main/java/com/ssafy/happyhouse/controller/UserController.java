package com.ssafy.happyhouse.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssafy.happyhouse.model.MemberDto;
import com.ssafy.happyhouse.model.service.UserService;
import com.ssafy.happyhouse.model.service.UserSha256;


@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String viewLogin() {
		return "/user/login";
	}
	
	@GetMapping("/signup")
	public String viewSignUp() {
		return "/user/signup";
	}
	
	@GetMapping("/searchpwd")
	public String viewSearchpwd() {
		return "/user/searchpwd";
	}
	
	@GetMapping("/{userid}/userinfo")
	public String showinfo(@PathVariable String userid) {
		return "/user/userinfo";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/main";
	}
	
	
	@PostMapping("/login")
	public String login(@RequestParam Map<String, String> map, Model model,HttpSession session) throws Exception{
		String encryPassword = UserSha256.encrypt(map.get("memberPw"));
		map.put("memberPw", encryPassword);
	
		MemberDto memberDto = userService.login(map);
		if(memberDto != null) {
			session.setAttribute("memberDto", memberDto);
			return "redirect:/main";
		}else {
			model.addAttribute("msg", "아이디 또는 비밀번호 확인 후 다시 로그인하세요!");
			return "user/login";
		}
	}
	
	@PostMapping("/signup")
	public ModelAndView signup(MemberDto memberDto) throws Exception {
		
		String encryPassword = UserSha256.encrypt(memberDto.getMemberPw());
		memberDto.setMemberPw(encryPassword);
		userService.registerMember(memberDto);
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("redirect:/main");
		
		return mv;
	}
	
	@PostMapping("/searchpwd")
	public String searchPwd(@RequestParam Map<String, String> map,Model model,HttpSession session) throws Exception {
	
		MemberDto memberDto = userService.searchPw(map);
		System.out.println(map.toString());
		System.out.println(memberDto);
		if(memberDto == null) {
			model.addAttribute("msg", "일치하는 사용자가 없습니다!");
			return "user/searchpwd";
		}
		
		session.setAttribute("memberDto", memberDto);
		
		return "redirect:/user/" + memberDto.getMemberId() + "/userinfo";
	}
	
	
	@PostMapping("/userinfo")
	public String updateUser( MemberDto memberDto) throws Exception {
		
		String encryPassword = UserSha256.encrypt(memberDto.getMemberPw());
		memberDto.setMemberPw(encryPassword);
		
		userService.updateMember(memberDto);
		
		
		return "redirect:/main" ;
	}
	
	@GetMapping("/{userid}/delete")
	public String deleteUser(@PathVariable String userid, HttpSession session) throws Exception {
		userService.deleteMember(userid);
		session.invalidate();
		
		return "redirect:/main";
	}
	
}