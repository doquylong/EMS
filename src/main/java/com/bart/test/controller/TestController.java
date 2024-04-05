package com.bart.test.controller;

import com.bart.test.config.security.LoggedInUser;
import com.bart.test.config.security.UserPrincipal;
import com.bart.test.dto.request.CreateQuestionRequest;
import com.bart.test.dto.request.CreateTestRequest;
import com.bart.test.dto.request.SubmitExamRequest;
import com.bart.test.dto.response.AppResponse;
import com.bart.test.service.TestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    // create test: teacher
    @PostMapping("/v1/test")
    public AppResponse createTest(@Valid @RequestBody CreateTestRequest request){
        return testService.createTest(request);
    }

    // active the test when has at least 1 question: teacher
    @PatchMapping("/v1/test/{testId}")
    public AppResponse activeTest(@PathVariable Long testId){
        return testService.activeTest(testId);
    }

    // get all test for everyone
    @GetMapping("/v1/test")
    public AppResponse getTestList(@LoggedInUser UserPrincipal user){
        return testService.getTestList(user);
    }

    // create question and answer for the test: teacher
    @PostMapping("/v1/test/{testId}/question")
    public AppResponse createQuestion(@Valid @RequestBody CreateQuestionRequest request, @PathVariable Long testId){
        return testService.createQuestion(request,testId);
    }

    // get detail of the test (pickup the test to perform)
    @GetMapping("/v1/test/{testId}")
    public AppResponse getDetailTest(@PathVariable Long testId){
        return testService.getQuestionByTest(testId);
    }

    // start the exam
    @PostMapping("/v1/exam")
    public AppResponse startExam(@Valid @RequestBody Long testId, @LoggedInUser UserPrincipal user){
        return testService.startExam(testId,user);
    }

    // Submit the result of exam
    @PatchMapping("/v1/exam/{examId}")
    public AppResponse submitExam(@Valid @RequestBody List<SubmitExamRequest> submit, @PathVariable Long examId){
        return testService.submitExam(submit,examId);
    }

    // get all the exam : teacher
    @GetMapping("/v1/exam")
    public AppResponse getAllExamList(){
        return testService.getAllExamList();
    }

    // get my exam
    @GetMapping("/v1/me/exam")
    public AppResponse getMyExamList(@LoggedInUser UserPrincipal me){
        return testService.getMyExamList(me);
    }
}
