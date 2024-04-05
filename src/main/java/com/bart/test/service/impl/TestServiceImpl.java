package com.bart.test.service.impl;

import com.bart.test.config.security.UserPrincipal;
import com.bart.test.dao.*;
import com.bart.test.dto.request.CreateAnswerRequest;
import com.bart.test.dto.request.CreateQuestionRequest;
import com.bart.test.dto.request.CreateTestRequest;
import com.bart.test.dto.request.SubmitExamRequest;
import com.bart.test.dto.response.AppResponse;
import com.bart.test.dto.response.ExamResponse;
import com.bart.test.dto.response.QuestionResponse;
import com.bart.test.dto.response.TestResponse;
import com.bart.test.entity.*;
import com.bart.test.exception.AppException;
import com.bart.test.exception.ErrorInfo;
import com.bart.test.service.TestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {
    private final TestDao testDao;
    private final QuestionDao questionDao;
    private final AnswerDao answerDao;
    private final UserDao userDao;
    private final ExamDao examDao;
    private final ExamResultDao examResultDao;


    public TestServiceImpl(TestDao testDao, QuestionDao questionDao, AnswerDao answerDao, UserDao userDao, ExamDao examDao, ExamResultDao examResultDao) {
        this.testDao = testDao;
        this.questionDao = questionDao;
        this.answerDao = answerDao;
        this.userDao = userDao;
        this.examDao = examDao;
        this.examResultDao = examResultDao;
    }

    @Override
    public AppResponse createTest(CreateTestRequest request) {
        Test test = Test.builder()
                .title(request.getTitle())
                .level(request.getLevel())
                .time(request.getTime())
                .active(false)
                .build();
        testDao.save(test);
        return AppResponse.ok(test.getId());
    }

    @Override
    public AppResponse getTestList(UserPrincipal user) {
        List<Test> tests;
        if (user.getRole() == Role.STUDENT)
            tests = testDao.findByDeletedIsFalseAndActiveTrue(); // only get the test with active status for student
        else
            tests = testDao.findByDeletedIsFalse(); // get all the test event deactive for manage
        List<TestResponse> data = tests.parallelStream().map(TestResponse::new).collect(Collectors.toList());
        return AppResponse.ok(data);
    }

    @Override
    public AppResponse activeTest(Long testId) {
        Optional<Test> testOpt = testDao.findByIdAndDeletedIsFalseAndActiveIs(testId,false);
        if (testOpt.isEmpty()) throw new AppException(ErrorInfo.NOT_FOUND_DATA);
        Test test = testOpt.get();
        if (test.getQuestions().isEmpty())
            throw new AppException(ErrorInfo.QUESTION_IS_REQUIRED);
        test.setActive(true);
        testDao.save(test);
        return AppResponse.ok();
    }

    @Override
    @Transactional(rollbackFor = {AppException.class, Exception.class})
    public AppResponse createQuestion(CreateQuestionRequest request, Long testId) {
        Optional<Test> testOpt = testDao.findByIdAndDeletedIsFalse(testId);
        if (testOpt.isEmpty()) throw new AppException(ErrorInfo.NOT_FOUND_DATA);
        Test test = testOpt.get();
        Question question = Question.builder()
                .level(request.getLevel())
                .content(request.getContent())
                .test(test)
                .build();
        List<CreateAnswerRequest> answersRequest = request.getAnswers();
        if (answersRequest.size() < 2 || !answersRequest.parallelStream().anyMatch(i -> i.getCorrect() == true))
            throw new AppException(ErrorInfo.CORRECTED_ANSWER_IS_REQUIRED);

        List<Answer> answers = new ArrayList<>();
        answersRequest.forEach(i -> {
            Answer answer = Answer.builder()
                    .content(i.getContent())
                    .correct(i.getCorrect())
                    .question(question)
                    .build();
            answers.add(answer);
        });
        question.setAnswers(answers);
        questionDao.save(question);
        answerDao.saveAll(answers);
        return AppResponse.ok();
    }

    @Override
    public AppResponse getQuestionByTest(Long testId) {
        Optional<Test> testOpt = testDao.findByIdAndDeletedIsFalseAndActiveIs(testId,true);
        if (testOpt.isEmpty()) throw new AppException(ErrorInfo.NOT_FOUND_DATA);
        Test test = testOpt.get();
        List<Question> questions = test.getQuestions();
        List<QuestionResponse> data = questions.stream().map(QuestionResponse::new).collect(Collectors.toList());
        return AppResponse.ok(data);
    }

    @Override
    public AppResponse startExam(Long testId, UserPrincipal user) {
        Optional<Test> testOpt = testDao.findByIdAndDeletedIsFalseAndActiveIs(testId,true);
        if (testOpt.isEmpty()) throw new AppException(ErrorInfo.NOT_FOUND_DATA);
        Test test = testOpt.get();
        User student = userDao.getReferenceById(user.getId());

        LocalDateTime startAt = LocalDateTime.now().withNano(0);
        LocalDateTime finishAt = startAt.plusMinutes(test.getTime());

        Exam exam = Exam.builder()
                .test(test)
                .student(student)
                .status(ExamStatus.STARTED)
                .startedAt(startAt)
                .finishedAt(finishAt)
                .build();
        examDao.save(exam);

        Map<String, Object> response = new HashMap<>();
        response.put("examId",exam.getId());
        response.put("finishAt",finishAt);
        return AppResponse.ok(response);
    }

    @Override
    @Transactional(rollbackFor = {AppException.class, Exception.class})
    public AppResponse submitExam(List<SubmitExamRequest> submit, Long examId) {

        LocalDateTime actualFinishAt = LocalDateTime.now().withNano(0);
        Optional<Exam> examOptional = examDao.findByIdAndStatus(examId,ExamStatus.STARTED);
        if (examOptional.isEmpty()) throw new AppException(ErrorInfo.NOT_FOUND_DATA);

        Exam exam = examOptional.get();
        exam.setActualFinishedAt(actualFinishAt);

        if (actualFinishAt.isAfter(exam.getFinishedAt())){
            exam.setStatus(ExamStatus.CANCELED);
            exam.setScore(0);
            examDao.save(exam);
            throw new AppException(ErrorInfo.TIME_OUT);
        }

        float score = calculateScore(submit);

        List<ExamResult> results = submit.stream().map(i -> {
            ExamResult examResult = ExamResult.builder()
                    .exam(exam)
                    .questionId(i.getQuestionId())
                    .answerId(i.getSelectedId())
                    .build();
            return examResult;
        }).collect(Collectors.toList());


        exam.setScore(score);
        exam.setStatus(ExamStatus.FINISHED);
        exam.setResults(results);

        examResultDao.saveAll(results);
        examDao.save(exam);
        return AppResponse.ok(score);
    }

    private float calculateScore(List<SubmitExamRequest> userSubmit) {
        //convert submit to Map
        Map<Long,Long>userSubmitMap= userSubmit.parallelStream().collect(Collectors.toMap(SubmitExamRequest::getQuestionId,SubmitExamRequest::getSelectedId));
        // get correct answer
        List<Answer> result= answerDao.findByQuestionIdInAndCorrectIsTrue(userSubmitMap.keySet());
        int numberOfCorrect=0;

        for (Answer a:result) {
            if (userSubmitMap.get(a.getQuestion().getId()) == a.getId()) {
                numberOfCorrect++;
            }
        }
        return ((float) numberOfCorrect/userSubmit.size())*10;
    }

    @Override
    public AppResponse getMyExamList(UserPrincipal me) {
        User user = userDao.getReferenceById(me.getId());
        List<Exam> exams = examDao.findAllByStudent(user);
        List<ExamResponse> response = exams.stream().map(ExamResponse::new).collect(Collectors.toList());
        return AppResponse.ok(response);
    }

    @Override
    public AppResponse getAllExamList() {
        List<Exam> exams = examDao.findAll();
        List<ExamResponse> response = exams.stream().map(ExamResponse::new).collect(Collectors.toList());
        return AppResponse.ok(response);
    }
}
