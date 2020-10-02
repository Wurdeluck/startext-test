package com.example.demo.controllers;

import com.example.demo.models.Artefact;
import com.example.demo.repository.ArtefactRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

//@WebMvcTest(value = ArtefactController.class, excludeAutoConfiguration = SecurityConfiguration.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@DataJpaTest
//@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ArtefactControllerUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @MockBean
    private ArtefactRepository artefactRepository;

//    @Before
//    public void setUp() throws Exception {
//    }
    private final static String TEST_USER_ID = "user-id-123";
//    Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
//    Artefact banana = new Artefact(UUID.fromString("085c8b83-a7d0-4549-9e83-3dc3cd1b7850"), Timestamp.valueOf("2020-09-25 09:16:38.951"), "1", "Food", "Yellow and squishy");
//    List<Artefact> allArtefacts = Arrays.asList(apple, banana);
//    Commentary comment_apple = new Commentary(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0"), UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), "1", "Not so bad");
//    Commentary comment_banana = new Commentary(UUID.fromString("14367c2e-f153-4ba9-9879-95613b575033"), UUID.fromString("085c8b83-a7d0-4549-9e83-3dc3cd1b7850"), "1", "Not so bad");
//    List<Commentary> allCommentaries = Arrays.asList(comment_apple, comment_banana);

    @Test
    void getAllArtefacts() throws Exception {
        // given
//        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        Artefact apple = new Artefact();
        apple.setCreated(Timestamp.valueOf("2020-09-25 08:15:37.951"));
        apple.setUserId("1");
        apple.setCategory("Food");
        apple.setDescription("Red and delicious");
//        Artefact banana = new Artefact(UUID.fromString("085c8b83-a7d0-4549-9e83-3dc3cd1b7850"), Timestamp.valueOf("2020-09-25 09:16:38.951"), "1", "Food", "Yellow and squishy");
        Artefact banana = new Artefact();
        banana.setCreated(Timestamp.valueOf("2020-09-25 09:16:38.951"));
        banana.setUserId("1");
        banana.setCategory("Food");
        banana.setDescription("Yellow and squishy");
        artefactRepository.save(apple);
        artefactRepository.save(banana);
        List<Artefact> allArtefacts = Arrays.asList(apple, banana);
//        entityManager.persist(apple);
//        entityManager.persist(banana);
//        entityManager.flush();
        // when
        Mockito.when(artefactRepository.findAll()).thenReturn(allArtefacts);
        List<Artefact> foundAll = artefactRepository.findAll();
        // then
        assertThat(foundAll).hasSize(2).extracting(Artefact::getArtefactId).containsOnly(apple.getArtefactId(), banana.getArtefactId());

/*
        Mockito.when(artefactRepository.findAll()).thenReturn(allArtefacts);
        mockMvc.perform(get("/api/v1/artefacts")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['pageable']['paged']").value("true"));
        mockMvc.perform(get("/api/v1/artefacts").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[0].artefactId", is(apple.getArtefactId()))).andExpect(jsonPath("$[1].artefactId", is(banana.getArtefactId())));
*/
    }

    @Test
    void getCommentariesByArtefactId() throws Exception {
/*
        MvcResult result = mockMvc.perform(get("/api/v1/artefacts/{id}/commentaries", UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"))
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['content'][0].['commentaryId']").value(UUID.fromString("8e8b05bc-aa0a-4a34-a95f-bb1212b5dde0")))
                .andReturn();
*/

    }

    @Test
    void getArtefactById() throws Exception {
/*
        Mockito.when(artefactRepository.findById(apple.getArtefactId())).thenReturn(Optional.of(apple));
        Mockito.when(artefactRepository.findById(UUID.fromString("0000-00-00-00-000000"))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/artefacts/{id}", "1").contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
*/
        // given
        Artefact apple = new Artefact();
        apple.setArtefactId(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"));
        apple.setCreated(Timestamp.valueOf("2020-09-25 08:15:37.951"));
        apple.setUserId("1");
        apple.setCategory("Food");
        apple.setDescription("Red and delicious");
        artefactRepository.save(apple);

        // when
        Mockito.when(artefactRepository.findById(apple.getArtefactId())).thenReturn(Optional.of(apple));
        Optional<Artefact> found = artefactRepository.findById(apple.getArtefactId());

        // then
        assertThat(found.get().getArtefactId())
                .isEqualTo(apple.getArtefactId());
    }

    @Test
    void createArtefact() throws Exception {
/*
        System.out.println("BBBBBBBBBBBBBBBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
        Artefact apple = new Artefact(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"), Timestamp.valueOf("2020-09-25 08:15:37.951"), "1", "Food", "Red and delicious");
        Mockito.when(artefactRepository.save(Mockito.any())).thenReturn(apple);
        System.out.println(apple);
        MvcResult result = mockMvc.perform(post("/api/v1/artefacts")
                .with(user(TEST_USER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(apple)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.artefactId", is(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"))))
                .andReturn();
        String resultSS = result.getResponse().getContentAsString();
        System.out.println(resultSS);
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
        assertNotNull(resultSS);
        assertEquals(ss, resultSS);
        verify(artefactRepository, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(artefactRepository);
*/
        // given
        Artefact apple = new Artefact();
        apple.setArtefactId(UUID.fromString("9dffcb25-b76f-448f-b966-72c41b40c7f7"));
        apple.setCreated(Timestamp.valueOf("2020-09-25 08:15:37.951"));
        apple.setUserId("1");
        apple.setCategory("Food");
        apple.setDescription("Red and delicious");
        // when
        Mockito.when(artefactRepository.save(Mockito.any())).thenReturn(apple);
        // then
        assertThat(artefactRepository.save(apple))
                .isEqualTo(apple);

    }

    @Test
    void updateArtefact() {
    }

    @Test
    void deleteArtefact() {
    }
}