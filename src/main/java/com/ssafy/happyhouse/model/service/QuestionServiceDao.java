package com.ssafy.happyhouse.model.service;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionServiceDao implements QuestionService{
	
	@Autowired
	SqlSession sqlSession;

}