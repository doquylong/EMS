package com.bart.test.service;

import com.bart.test.config.security.UserPrincipal;
import com.bart.test.dto.request.CreateQuestionRequest;
import com.bart.test.dto.request.CreateTestRequest;
import com.bart.test.dto.request.SubmitExamRequest;
import com.bart.test.dto.response.AppResponse;

import java.util.List;

public interface TestService {
    AppResponse createTest(CreateTestRequest request);

    AppResponse createQuestion(CreateQuestionRequest request, Long testId);

    AppResponse getTestList(UserPrincipal user);

    AppResponse activeTest(Long testId);

    AppResponse startExam(Long testId, UserPrincipal user);

    AppResponse getQuestionByTest(Long testId);

    AppResponse submitExam(List<SubmitExamRequest> submit, Long examId);

    AppResponse getMyExamList(UserPrincipal me);

    AppResponse getAllExamList();
}
