package com.aurea.deadcode.rest;

import com.aurea.deadcode.DeadCodeDetectorApplication;
import com.aurea.deadcode.dto.GitHubRepoDTO;
import com.aurea.deadcode.model.GitHubRepo;
import com.aurea.deadcode.model.Status;
import com.aurea.deadcode.repository.RepoRepository;
import com.aurea.deadcode.service.flow.RepositoryProcessingService;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by ilshat on 28.03.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DeadCodeDetectorApplication.class})
@AutoConfigureMockMvc
public class RepoRestServiceTest {

    private static final String LOCATION = "Location";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepoRepository repoRepository;

    @MockBean
    private RepositoryProcessingService processingService;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        //noinspection OptionalGetWithoutIsPresent
        mappingJackson2HttpMessageConverter = Arrays.stream(converters)
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny().get();
        Assert.assertNotNull("the JSON message converter must not be null", mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setUp() throws Exception {
        repoRepository.deleteAll();
    }

    private void mockProcessingService() {
        when(processingService.runProcessing(any(GitHubRepo.class))).thenReturn(any());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void repoListIsInitiallyEmpty() throws Exception {
        mockMvc.perform(get("/repos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.totalElements", is(0)));
    }

    @Test
    public void addNewRepoRespondsWithStatusCreatedAndLocationHeader() throws Exception {
        mockProcessingService();
        String repoJson = json(getValidRepoDTO());
        mockMvc.perform(post("/repos").content(repoJson).contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, notNullValue()));
    }

    @Test
    public void addedRepoIsDisplayedWhenGettingReposList() throws Exception {
        GitHubRepoDTO repoDto = getValidRepoDTO();
        sendCreateRepoRequest(repoDto);
        mockMvc.perform(get("/repos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.content[0].id", notNullValue()))
                .andExpect(jsonPath("$.content[0].name", is(repoDto.getName())))
                .andExpect(jsonPath("$.content[0].url", is(repoDto.getUrl())));
    }

    @Test
    public void addedRepoGetsStatusAddedAfterCreation() throws Exception {
        sendCreateRepoRequest();
        mockMvc.perform(get("/repos"))
                .andExpect(jsonPath("$.content[0].status", is(Status.ADDED.name())));
    }

    @Test
    public void addedRepoGetsAddedDateAfterCreation() throws Exception {
        sendCreateRepoRequest();
        mockMvc.perform(get("/repos"))
                .andExpect(jsonPath("$.content[0].addedDate", notNullValue()));
    }

    @Test
    public void processingServiceIsCalledAfterRepoCreation() throws Exception {
        when(processingService.runProcessing(any(GitHubRepo.class))).thenReturn(any());

        String repoJson = json(getValidRepoDTO());
        MvcResult result = mockMvc.perform(post("/repos").content(repoJson).contentType(contentType)).andReturn();

        String locationUrl = result.getResponse().getHeader(LOCATION);
        Long id = extractId(locationUrl);

        verify(processingService, times(1)).runProcessing(argThat(hasProperty("id", is(id))));
    }

    @Test
    public void getCodeOccurrencesForNonExistingRepoRespondsWithStatusNotFound() throws Exception {
        mockMvc.perform(get("/repos/1/deadcode-occurrences"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void codeOccurrencesListForExistingRepoRespondsWithStatusOk() throws Exception {
        String locationUrl = sendCreateRepoRequest();
        mockMvc.perform(get(locationUrl + "/deadcode-occurrences"))
                .andExpect(status().isOk());
    }

    @Test
    public void codeOccurrencesListForNewRepoIsEmpty() throws Exception {
        String locationUrl = sendCreateRepoRequest();
        mockMvc.perform(get(locationUrl + "/deadcode-occurrences"))
                .andExpect(jsonPath("$.totalElements", is(0)));
    }

    @Test
    public void existingRepoDeletionRespondsWithStatusNoContent() throws Exception {
        String locationUrl = sendCreateRepoRequest();
        mockMvc.perform(delete(locationUrl))
                .andExpect(status().isNoContent());
    }

    @Test
    public void notExistingRepoDeletionRespondsWithStatusNotFound() throws Exception {
        mockMvc.perform(delete("/repo/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deletedRepoIsNotVisibleInAllReposList() throws Exception {
        String locationUrl = sendCreateRepoRequest();
        mockMvc.perform(delete(locationUrl));
        mockMvc.perform(get("/repos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.totalElements", is(0)));
    }

    @Test
    public void attemptToCreateRepoWithExistingUrlRespondsWithStatusConflict() throws Exception {
        String repoUrl = "https://github.com/ilshatlatypov/uni-validator.git";
        GitHubRepoDTO repo1Dto = new GitHubRepoDTO("Repo 1", repoUrl);
        GitHubRepoDTO repo2Dto = new GitHubRepoDTO("Repo 2", repoUrl);

        String repo1Json = json(repo1Dto);
        String repo2Json = json(repo2Dto);

        mockMvc.perform(post("/repos").content(repo1Json).contentType(contentType)).andExpect(status().isCreated());
        mockMvc.perform(post("/repos").content(repo2Json).contentType(contentType)).andExpect(status().isConflict());
    }

    @Test
    public void attemptToDeleteProcessingRepoRespondsWithStatusBadRequest() throws Exception {
        String locationUrl = sendCreateRepoRequest();
        Long id = extractId(locationUrl);

        GitHubRepo repo = repoRepository.findOne(id);
        repo.setProcessing();
        repoRepository.save(repo);

        mockMvc.perform(delete(locationUrl))
                .andExpect(status().isBadRequest());
    }

    private Long extractId(String repoUrl) {
        return Long.parseLong(StringUtils.substringAfterLast(repoUrl, "/"));
    }

    private static GitHubRepoDTO getValidRepoDTO() {
        return new GitHubRepoDTO("Repo name", "https://github.com/ilshatlatypov/uni-validator.git");
    }

    private String sendCreateRepoRequest() throws Exception {
        GitHubRepoDTO defaultRepoDto = getValidRepoDTO();
        return sendCreateRepoRequest(defaultRepoDto);
    }

    private String sendCreateRepoRequest(GitHubRepoDTO repoDto) throws Exception {
        mockProcessingService();
        String repoJson = json(repoDto);
        MvcResult result = mockMvc.perform(
                post("/repos").content(repoJson).contentType(contentType)
        ).andReturn();
        return result.getResponse().getHeader(LOCATION);
    }

    @SuppressWarnings("unchecked")
    public String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}