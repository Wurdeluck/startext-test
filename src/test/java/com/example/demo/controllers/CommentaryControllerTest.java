package com.example.demo.controllers;

import com.example.demo.DemoApplication;
import com.example.demo.models.Artefact;
import com.example.demo.models.Commentary;
import com.example.demo.repository.ArtefactRepository;
import com.example.demo.repository.CommentaryRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DemoApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class CommentaryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CommentaryRepository commentaryRepository;

    @Autowired
    private ArtefactRepository artefactRepository;

    @After
    public void resetDb() throws Exception {
        commentaryRepository.deleteAll();
    }

    private final static String TEST_USER_ID = "user-id-123";

    @Test
    public void getAllCommentariesInArtefact_allCommentaries() throws IOException, Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        Artefact banana = new Artefact(UUID.fromString("085c8b83-a7d0-4549-9e83-3dc3cd1b7850"), Timestamp.valueOf("2020-09-25 09:16:38.951"), "1", "Food", "Yellow and squishy");
        Commentary comment_apple = new Commentary(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "1", "Not so bad");
        Commentary comment_banana = new Commentary(UUID.fromString("14367c2e-f153-4ba9-9879-95613b575033"), UUID.fromString("085c8b83-a7d0-4549-9e83-3dc3cd1b7850"), "1", "Kinda meh");
        artefactRepository.saveAndFlush(apple);
        artefactRepository.saveAndFlush(banana);
        List<Commentary> allCommentaries = Arrays.asList(comment_apple, comment_banana);
        commentaryRepository.saveAndFlush(comment_apple);
        commentaryRepository.saveAndFlush(comment_banana);

        mvc.perform(get("/api/v1/commentaries")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$['content']", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$['content'][0].commentaryId", is("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")))
                .andExpect(jsonPath("$['content'][1].commentaryId", is("14367c2e-f153-4ba9-9879-95613b575033")));

        resetDb();
    }

    @Test
    public void getAllCommentariesInArtefact_EmptyDb() throws Exception {

        mvc.perform(get("/api/v1/commentaries")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$['content'].length()", is(0)));
    }

    @Test
    public void getAllCommentariesInArtefact_ParticularArtefact() throws Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        Commentary comment_apple = new Commentary(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "1", "Not so bad");
        artefactRepository.saveAndFlush(apple);
        List<Commentary> allCommentaries = Arrays.asList(comment_apple);
        commentaryRepository.saveAndFlush(comment_apple);

        mvc.perform(get("/api/v1/commentaries")
                .with(user(TEST_USER_ID))
                .param("artefactId", "9dffcb25-b76f-448f-b966-72c41b40c7f7")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$['content']", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$['content'][0].commentaryId", is("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")));

        resetDb();

    }

    @Test
    public void getAllCommentariesInArtefact_NoArtefactException() throws Exception {

        mvc.perform(get("/api/v1/commentaries")
                .with(user(TEST_USER_ID))
                .param("artefactId", "9dffcb25-b76f-448f-b966-72c41b40c7f7")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Artefact was not found for this id :: 9dffcb25-b76f-448f-b966-72c41b40c7f7")));
        resetDb();

    }

    @Test
    public void getCommentaryById() throws Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        Commentary comment_apple = new Commentary(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "1", "Not so bad");
        artefactRepository.saveAndFlush(apple);
        List<Commentary> allCommentaries = Arrays.asList(comment_apple);
        commentaryRepository.saveAndFlush(comment_apple);

        mvc.perform(get("/api/v1/commentaries/{id}", "8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.commentaryId", is("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")));

        resetDb();
    }

    @Test
    public void getCommentaryById_NoCommentaryException() throws Exception {
        mvc.perform(get("/api/v1/commentaries/{id}", "8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Commentary not found for this id :: 8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")));

    }

    @Test
    public void createCommentary() throws Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        Commentary comment_apple = new Commentary(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "1", "Not so bad");
        artefactRepository.saveAndFlush(apple);

        mvc.perform(post("/api/v1/commentaries")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(comment_apple)))
                .andDo(print());

        List<Commentary> found = commentaryRepository.findAll();
        assertThat(found).extracting(Commentary::getCommentaryId).containsOnly(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"));
        resetDb();
    }

    @Test
    public void createCommentary_CommentaryExists() throws Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        Commentary comment_apple = new Commentary(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "1", "Not so bad");
        artefactRepository.saveAndFlush(apple);
        commentaryRepository.saveAndFlush(comment_apple);

        mvc.perform(post("/api/v1/commentaries")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(comment_apple)))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", is("Commentary with this id already exists in database :: 8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")));

        resetDb();

    }

    @Test
    public void updateCommentary() throws Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        artefactRepository.saveAndFlush(apple);
        Commentary comment_apple = new Commentary(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "1", "Not so bad");
        commentaryRepository.saveAndFlush(comment_apple);
        Commentary new_comment_apple = new Commentary(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "1", "Kinda meh");


        mvc.perform(put("/api/v1/commentaries/{id}", "8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(new_comment_apple)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<Commentary> found = commentaryRepository.findAll();
        assertThat(found).extracting(Commentary::getCommentaryId).containsOnly(comment_apple.getCommentaryId());
        assertThat(found).extracting(Commentary::getContent).containsOnly(new_comment_apple.getContent());
        resetDb();
    }

    @Test
    public void updateCommentary_NoCommentaryException() throws Exception {

        Commentary comment_banana = new Commentary(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "1", "Kinda meh");
        mvc.perform(put("/api/v1/commentaries/{id}", "8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(comment_banana)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Commentary not found for this id :: 8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")));

    }

    @Test
    public void updateCommentary_UpdateNonModifiedField() throws Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        artefactRepository.saveAndFlush(apple);
        Commentary comment_apple = new Commentary(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "1", "Not so bad");
        commentaryRepository.saveAndFlush(comment_apple);
        Commentary new_comment_apple = new Commentary(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "2", "Kinda meh");


        mvc.perform(put("/api/v1/commentaries/{id}", "8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(new_comment_apple)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<Commentary> found = commentaryRepository.findAll();
        assertThat(found).extracting(Commentary::getCommentaryId).containsOnly(comment_apple.getCommentaryId());
        assertThat(found).extracting(Commentary::getUserId).containsOnly(comment_apple.getUserId());
        assertThat(found).extracting(Commentary::getContent).containsOnly(new_comment_apple.getContent());
        resetDb();
    }

    @Test
    public void deleteCommentary() throws Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        artefactRepository.saveAndFlush(apple);
        Commentary comment_apple = new Commentary(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "1", "Not so bad");
        commentaryRepository.saveAndFlush(comment_apple);

        mvc.perform(delete("/api/v1/commentaries/{id}", "8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")
                .with(user(TEST_USER_ID)));

        List<Commentary> found = commentaryRepository.findAll();
        assertThat(found).extracting(Commentary::getCommentaryId).isEmpty();
        resetDb();
    }

    @Test
    public void deleteCommentary_NoCommentaryException() throws Exception {

        mvc.perform(delete("/api/v1/commentaries/{id}", "8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")
                .with(user(TEST_USER_ID)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Commentary not found for this id :: 8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")));

    }
}