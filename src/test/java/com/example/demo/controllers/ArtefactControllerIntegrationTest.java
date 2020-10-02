package com.example.demo.controllers;

import com.example.demo.DemoApplication;
import com.example.demo.models.Artefact;
import com.example.demo.models.Commentary;
import com.example.demo.repository.ArtefactRepository;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DemoApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
// @TestPropertySource(locations = "classpath:application-integrationtest.properties")
@AutoConfigureTestDatabase
public class ArtefactControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ArtefactRepository artefactRepository;

    @After
    public void resetDb() throws Exception {
        artefactRepository.deleteAll();
    }

    private final static String TEST_USER_ID = "user-id-123";


//    @Test
//    public void getAllArtefacts() {
//        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
//        Artefact banana = new Artefact(UUID.fromString("085c8b83-a7d0-4549-9e83-3dc3cd1b7850"), Timestamp.valueOf("2020-09-25 09:16:38.951"), "1", "Food", "Yellow and squishy");
//        List<Artefact> allArtefacts = Arrays.asList(apple, banana);
//        artefactRepository.saveAndFlush(apple);
//        artefactRepository.saveAndFlush(banana);
//        // when
//        List<Artefact> foundAll = artefactRepository.findAll();
//        // then
//        assertThat(foundAll).hasSize(2).extracting(Artefact::getArtefactId).containsOnly(apple.getArtefactId(), banana.getArtefactId());
//
//    }

    @Test
    public void getAllArtefacts() throws IOException, Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        Artefact banana = new Artefact(UUID.fromString("085c8b83-a7d0-4549-9e83-3dc3cd1b7850"), Timestamp.valueOf("2020-09-25 09:16:38.951"), "1", "Food", "Yellow and squishy");

        artefactRepository.saveAndFlush(apple);
        artefactRepository.saveAndFlush(banana);

        mvc.perform(get("/api/v1/artefacts")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$['content']", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$['content'][0].artefactId", is("9dffcb25-b76f-448f-b966-72c41b40c7f7")))
                .andExpect(jsonPath("$['content'][1].artefactId", is("085c8b83-a7d0-4549-9e83-3dc3cd1b7850")));

        resetDb();
    }

    @Test
    public void getAllArtefacts_EmptyDb() throws Exception {
        mvc.perform(get("/api/v1/artefacts")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$['content'].length()", is(0)));

        resetDb();
    }

    @Test
    public void getCommentariesByArtefactId() throws Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        Commentary comment_1 = new Commentary(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "1", "Not so bad");
        Commentary comment_2 = new Commentary(UUID.fromString("14367c2e-f153-4ba9-9879-95613b575033"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "2", "Like");
        List<Commentary> allCommentaries = Arrays.asList(comment_1, comment_2);
        apple.setCommentaries(allCommentaries);
        artefactRepository.saveAndFlush(apple);

        mvc.perform(get("/api/v1/artefacts/{id}/commentaries", "9dffcb25-b76f-448f-b966-72c41b40c7f7")
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
    public void getCommentariesByArtefactId_NoArtefactException() throws Exception {
        mvc.perform(get("/api/v1/artefacts/{id}/commentaries", "9dffcb25-b76f-448f-b966-72c41b40c7f7")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Commentaries not found. Artefact does not exist for this id :: 9dffcb25-b76f-448f-b966-72c41b40c7f7")));
        resetDb();
    }

    @Test
    public void getArtefactById() throws IOException, Exception {

        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");

        artefactRepository.saveAndFlush(apple);

        mvc.perform(get("/api/v1/artefacts/{id}", "9dffcb25-b76f-448f-b966-72c41b40c7f7")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.artefactId", is("9dffcb25-b76f-448f-b966-72c41b40c7f7")));

        resetDb();

//        mockMvc.perform(MockMvcRequestBuilders.get("/users")
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$['pageable']['paged']")
//                        .value("true"));

    }

    @Test
    public void getArtefactById_NoArtefactException() throws IOException, Exception {

        mvc.perform(get("/api/v1/artefacts/{id}", "9dffcb25-b76f-448f-b966-72c41b40c7f7")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Artefact not found for this id :: 9dffcb25-b76f-448f-b966-72c41b40c7f7")));
        resetDb();
    }

    @Test
    public void createArtefact() throws IOException, Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");

        mvc.perform(post("/api/v1/artefacts")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(apple)))
                .andDo(print());

        List<Artefact> found = artefactRepository.findAll();
        assertThat(found).extracting(Artefact::getArtefactId).containsOnly(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"));
        resetDb();
    }

    @Test
    public void createArtefact_ArtefactExist() throws IOException, Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        artefactRepository.saveAndFlush(apple);
        mvc.perform(post("/api/v1/artefacts")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(apple)))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message", is("Artefact with this id already exists in database :: 9dffcb25-b76f-448f-b966-72c41b40c7f7")));

        resetDb();
    }


    @Test
    public void updateArtefact() throws IOException, Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");

        artefactRepository.saveAndFlush(apple);

        Artefact newApple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 09:16:38.951"), "1", "Food", "Yellow and squishy");

        mvc.perform(put("/api/v1/artefacts/{id}", "9dffcb25-b76f-448f-b966-72c41b40c7f7")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(newApple)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<Artefact> found = artefactRepository.findAll();
        assertThat(found).extracting(Artefact::getArtefactId).containsOnly(apple.getArtefactId());
        assertThat(found).extracting(Artefact::getDescription).containsOnly(newApple.getDescription());
        resetDb();
    }

    @Test
    public void updateArtefact_NoArtefactException() throws IOException, Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        mvc.perform(put("/api/v1/artefacts/{id}", "9dffcb25-b76f-448f-b966-72c41b40c7f7")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(apple)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Artefact not found for this id :: 9dffcb25-b76f-448f-b966-72c41b40c7f7")));

        resetDb();
    }

    @Test
    public void updateArtefact_UpdateNonModifiedField() throws IOException, Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        artefactRepository.saveAndFlush(apple);
        Artefact newApple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("9999-09-29 09:19:39.999"), "1", "Food", "Red and delicious");

        mvc.perform(put("/api/v1/artefacts/{id}", "9dffcb25-b76f-448f-b966-72c41b40c7f7")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(newApple)))
                .andDo(print())
                .andExpect(status().isOk());

        List<Artefact> found = artefactRepository.findAll();
        assertThat(found).extracting(Artefact::getCreated).containsOnly(apple.getCreated());

        resetDb();
    }

    @Test
    public void deleteArtefact() throws Exception {
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");

        artefactRepository.saveAndFlush(apple);

        mvc.perform(delete("/api/v1/artefacts/{id}", "9dffcb25-b76f-448f-b966-72c41b40c7f7")
                .with(user(TEST_USER_ID)));

        List<Artefact> found = artefactRepository.findAll();
        assertThat(found).extracting(Artefact::getArtefactId).isEmpty();
        resetDb();
    }

    @Test
    public void deleteArtefact_NoArtefactException() throws Exception {

        mvc.perform(delete("/api/v1/artefacts/{id}", "9dffcb25-b76f-448f-b966-72c41b40c7f7")
                .with(user(TEST_USER_ID)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Artefact not found for this id :: 9dffcb25-b76f-448f-b966-72c41b40c7f7")));

        resetDb();
    }
}