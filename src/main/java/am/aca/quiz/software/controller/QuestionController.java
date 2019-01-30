package am.aca.quiz.software.controller;


import am.aca.quiz.software.entity.QuestionEntity;
import am.aca.quiz.software.entity.TopicEntity;
import am.aca.quiz.software.service.dto.QuestionDto;
import am.aca.quiz.software.service.dto.TopicDto;
import am.aca.quiz.software.service.implementations.AnswerServiceImp;
import am.aca.quiz.software.service.implementations.QuestionServiceImp;
import am.aca.quiz.software.service.implementations.TopicServiceImp;
import am.aca.quiz.software.service.mapper.QuestionMapper;
import am.aca.quiz.software.service.mapper.TopicMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionServiceImp questionServiceImp;
    private final TopicServiceImp topicServiceImp;
    private final QuestionMapper questionMapper;
    private final TopicMapper topicMapper;
    private final AnswerServiceImp answerServiceImp;
    private final int ANSWER_COUNT = 6;

    public QuestionController(QuestionServiceImp questionServiceImp, TopicServiceImp topicServiceImp, QuestionMapper questionMapper, TopicMapper topicMapper, AnswerServiceImp answerServiceImp) {
        this.questionServiceImp = questionServiceImp;
        this.topicServiceImp = topicServiceImp;
        this.questionMapper = questionMapper;
        this.topicMapper = topicMapper;
        this.answerServiceImp = answerServiceImp;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<QuestionDto> getById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(questionMapper.mapEntityToDto(questionServiceImp.getById(id)));
        } catch (SQLException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<QuestionDto>> getAllQuestions() {
        try {
            return ResponseEntity.ok(questionMapper.mapEntitiesToDto(questionServiceImp.getAll()));
        } catch (SQLException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/add")
    public ModelAndView addQuestion() {
        ModelAndView modelAndView = new ModelAndView("question");

        try {
            List<TopicDto> topicDtos = topicMapper.mapEntitiesToDto(topicServiceImp.getAll());
            modelAndView.addObject("topics", topicDtos);
            return modelAndView;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostMapping(value = "/add")
    @ResponseBody
    public ModelAndView postQuestion(@RequestParam Map<String, String> formData) {

        String text = formData.get("questionText");
        String level = formData.get("level");

        int points = Integer.parseInt(formData.get("points"));
        int corr_answer = Integer.parseInt(formData.get("correct_answer_count"));
        String topicName = formData.get("topic");
        try {
            TopicEntity topicEntity = topicServiceImp.getByTopicName(topicName);
            Long topicId = topicEntity.getId();
            questionServiceImp.addQuestion(text, level, corr_answer, points, topicId);
            QuestionEntity newQuestion = questionServiceImp.getQuestionEntityByQuestion(text);
            Long questionId = newQuestion.getId();

            for (int i = 1; i <= ANSWER_COUNT; i++) {
                String answerText = formData.get("answer" + i);
                if (!answerText.isEmpty()) {
                    String description = formData.get("description" + i);
                    boolean isTrue = Boolean.parseBoolean(formData.get("isCorrect" + i));
                    answerServiceImp.addAnswer(answerText, description, isTrue, questionId);
                }
            }

            List<TopicDto> topicDtos = topicMapper.mapEntitiesToDto(topicServiceImp.getAll());
            ModelAndView modelAndView = new ModelAndView("question");
            modelAndView.addObject("topics", topicDtos);
            return modelAndView;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    @PatchMapping(value = "/update/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody QuestionEntity questionEntity) {
        try {
            questionServiceImp.update(questionEntity, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
