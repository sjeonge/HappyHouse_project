package com.ssafy.happyhouse.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.model.MemberDto;
import com.ssafy.happyhouse.model.QNAInfo;
import com.ssafy.happyhouse.model.QuestionDto;
import com.ssafy.happyhouse.model.search.QuestionSearch;
import com.ssafy.happyhouse.model.service.QuestionService;

@RestController
@RequestMapping("/question")
public class QuestionController {
	
	@Autowired
	private QuestionService questionService;
	
	@GetMapping("/search")
	public ResponseEntity<?> viewQuestionSearchList(QuestionSearch search) throws Exception {
		return new ResponseEntity<List<QuestionDto>>(questionService.viewQuestionList(search), HttpStatus.OK);
	}
	
	// 열람
	@GetMapping("/{qno}")
	public ResponseEntity<?> viewQuestion(@PathVariable String qno, HttpSession session) throws Exception{
		
		if(session.getAttribute("memberDto") == null) {
			return new ResponseEntity<String>("로그인 후 이용해주세요", HttpStatus.BAD_REQUEST);
		}		
		
		MemberDto loginUser = (MemberDto) session.getAttribute("memberDto");
		QNAInfo qna = new QNAInfo(questionService.viewQuestion(qno, loginUser.getMemberId()));
		
		if(qna.isEmpty()) {
			return new ResponseEntity<String>("접근 권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<QNAInfo>(qna, HttpStatus.OK);
		
	}
	
	@PostMapping("/regist")
	public ResponseEntity<String> registerQuestion(QuestionDto questionDto, HttpSession session) throws Exception {
		
		if(session.getAttribute("memberDto") == null) {
			return new ResponseEntity<String>("로그인 후 이용해주세요", HttpStatus.BAD_REQUEST);
		}
		
		questionService.registerQuestion(questionDto);
		return new ResponseEntity<String>("작성 완료!", HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<String> updateQuestion(QuestionDto questionDto, HttpSession session) throws Exception {	
		
		MemberDto loginUser = (MemberDto) session.getAttribute("memberDto");
		
		if(loginUser == null || !loginUser.getMemberId().equals(questionDto.getWriter())) {
			return new ResponseEntity<String>("접근 권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		
		questionService.updateQuestion(questionDto);
		
		return new ResponseEntity<String>("수정 완료!", HttpStatus.OK);
	}
	
	@GetMapping("/delete")
	public ResponseEntity<String> deleteQuestion(String qno, HttpSession session) throws Exception {
		
		MemberDto loginUser = (MemberDto) session.getAttribute("memberDto");
		
		String writer = questionService.getWriter(qno);
		
		if(loginUser == null || !loginUser.getMemberId().equals(writer)) {
			return new ResponseEntity<String>("접근 권한이 없습니다.", HttpStatus.BAD_REQUEST);
		}
		
		questionService.deleteQuestion(Integer.parseInt(qno));
		
		return new ResponseEntity<String>("삭제 완료!", HttpStatus.OK);
	}
	
	
}
