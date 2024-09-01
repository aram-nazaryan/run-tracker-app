package am.run.tracker.api;

import am.run.tracker.api.common.run.*;
import am.run.tracker.api.common.user.*;
import am.run.tracker.core.common.datatypes.run.UpdateRunType;
import am.run.tracker.core.common.datatypes.user.Gender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RunTrackerApplicationTest {

    private static final Logger logger = LoggerFactory.getLogger(RunTrackerApplicationTest.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestRestTemplate testRestTemplate;

    private static UUID userId;

    @Test
    @Order(1)
    @DisplayName("User creation")
    void userCreationTest() {
        logger.debug("Starting user creation test...");

        UserCreationRequestDto requestDto = new UserCreationRequestDto(
                "TestUser", "TestLastName", LocalDate.of(1990, 4, 12), Gender.MALE
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserCreationRequestDto> entity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<UserDetailedRM> response = testRestTemplate.exchange(
                "/users", HttpMethod.POST, entity, UserDetailedRM.class
        );

        UserDetailedRM createdUser = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());

        userId = createdUser.getId();
        logger.info("User creation test completed with user ID: {}", userId);
    }

    @Test
    @Order(2)
    @DisplayName("User update")
    void editUserTest() {
        logger.debug("Starting user update test for user ID: {}", userId);

        UserPatchRequestDto requestDto = new UserPatchRequestDto(
                "TestUserUpdated", "TestLastNameUpdated", LocalDate.of(1991, 4, 12), Gender.FEMALE
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserPatchRequestDto> entity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<UserDetailedRM> response = testRestTemplate.exchange(
                "/users/" + userId, HttpMethod.PUT, entity, UserDetailedRM.class
        );

        UserDetailedRM updatedUser = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(updatedUser);
        assertEquals(requestDto.firstName(), updatedUser.getFirstName());
        assertEquals(requestDto.lastName(), updatedUser.getLastName());
        assertEquals(requestDto.birthDate(), updatedUser.getBirthDate());
        assertEquals(requestDto.gender(), updatedUser.getGender());

        logger.info("User update test completed for user ID: {}", userId);
    }

    @Test
    @Order(3)
    @DisplayName("Finish run without starting")
    void finishNotStartedRunTest() {
        logger.debug("Trying to finish a run without starting...");

        FinishRunRequestDto requestDto = new FinishRunRequestDto(
                new PointGeographyDto(40.467796325683594, 45.28315544128418),
                Instant.parse("2024-09-01T07:00:00.00Z"),
                UpdateRunType.FINISH,
                null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FinishRunRequestDto> entity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<RunRM> response = testRestTemplate.exchange(
                "/users/" + userId + "/runs", HttpMethod.POST, entity, RunRM.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        logger.info("Finish run without starting test completed.");
    }

    @Test
    @Order(4)
    @DisplayName("Track ~3km run")
    void startUserRunTest() {
        logger.debug("Starting user run test for user ID: {}", userId);

        StartRunRequestDto startRunRequestDto = new StartRunRequestDto(
                new PointGeographyDto(40.494747161865234, 45.28315544128418),
                Instant.parse("2024-09-01T06:30:00.00Z"),
                UpdateRunType.START
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StartRunRequestDto> startRunRequest = new HttpEntity<>(startRunRequestDto, headers);

        ResponseEntity<RunRM> startRunResponse = testRestTemplate.exchange(
                "/users/" + userId + "/runs", HttpMethod.POST, startRunRequest, RunRM.class
        );

        RunRM startRunResponseBody = startRunResponse.getBody();

        assertEquals(HttpStatus.OK, startRunResponse.getStatusCode());
        assertNotNull(startRunResponseBody);
        assertNotNull(startRunResponseBody.getId());

        FinishRunRequestDto finishRunRequestDto = new FinishRunRequestDto(
                new PointGeographyDto(40.467796325683594, 45.28315544128418),
                Instant.parse("2024-09-01T07:00:00.00Z"),
                UpdateRunType.FINISH,
                null
        );

        HttpEntity<FinishRunRequestDto> finishRunRequest = new HttpEntity<>(finishRunRequestDto, headers);

        ResponseEntity<RunRM> finishRunResponse = testRestTemplate.exchange(
                "/users/" + userId + "/runs", HttpMethod.POST, finishRunRequest, RunRM.class
        );

        RunRM finishRunResponseBody = finishRunResponse.getBody();

        assertEquals(HttpStatus.OK, finishRunResponse.getStatusCode());
        assertNotNull(finishRunResponseBody);
        assertEquals(2992, finishRunResponseBody.getDistance());
        assertEquals((double) 2992 / 1800, finishRunResponseBody.getSpeed());

        logger.info("User run test completed for user ID: {}", userId);
    }

    @Test
    @Order(5)
    @DisplayName("Testing consecutive runs")
    void startConsecutiveRunsTest() {
        logger.debug("Testing user's consecutive runs for user ID: {}", userId);

        StartRunRequestDto startRunRequestDto = new StartRunRequestDto(
                new PointGeographyDto(40.494747161865234, 45.28315544128418),
                Instant.parse("2024-09-01T18:30:00.00Z"),
                UpdateRunType.START
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StartRunRequestDto> startRunRequest = new HttpEntity<>(startRunRequestDto, headers);

        ResponseEntity<RunRM> startRunResponse = testRestTemplate.exchange(
                "/users/" + userId + "/runs", HttpMethod.POST, startRunRequest, RunRM.class
        );

        RunRM startRunResponseBody = startRunResponse.getBody();

        assertEquals(HttpStatus.OK, startRunResponse.getStatusCode());
        assertNotNull(startRunResponseBody);
        assertNotNull(startRunResponseBody.getId());

        StartRunRequestDto startSecondRunRequestDto = new StartRunRequestDto(
                new PointGeographyDto(40.467796325683594, 45.28315544128418),
                Instant.parse("2024-09-01T19:00:00.00Z"),
                UpdateRunType.START
        );

        HttpEntity<StartRunRequestDto> startSecondRunRequest = new HttpEntity<>(startSecondRunRequestDto, headers);

        ResponseEntity<RunRM> startSecondRunResponse = testRestTemplate.exchange(
                "/users/" + userId + "/runs", HttpMethod.POST, startSecondRunRequest, RunRM.class
        );

        RunRM startSecondRunResponseBody = startSecondRunResponse.getBody();

        assertEquals(HttpStatus.OK, startSecondRunResponse.getStatusCode());
        assertNotNull(startSecondRunResponseBody);
        assertNotNull(startSecondRunResponseBody.getId());

        FinishRunRequestDto finishRunRequestDto = new FinishRunRequestDto(
                new PointGeographyDto(40.49777269363403, 45.292232036590576),
                Instant.parse("2024-09-01T19:30:00.00Z"),
                UpdateRunType.FINISH,
                null
        );

        HttpEntity<FinishRunRequestDto> finishRunRequest = new HttpEntity<>(finishRunRequestDto, headers);

        ResponseEntity<RunRM> finishRunResponse = testRestTemplate.exchange(
                "/users/" + userId + "/runs", HttpMethod.POST, finishRunRequest, RunRM.class
        );

        RunRM finishRunResponseBody = finishRunResponse.getBody();

        assertEquals(HttpStatus.OK, finishRunResponse.getStatusCode());
        assertNotNull(finishRunResponseBody);
        assertEquals(3416, finishRunResponseBody.getDistance());
        assertEquals((double) 3416 / 1800, finishRunResponseBody.getSpeed());

        logger.info("Testing user's consecutive runs completed for user ID: {}", userId);
    }

    @Test
    @Order(6)
    @DisplayName("Get runs")
    void getRunsTest() throws JsonProcessingException {
        logger.debug("Getting user runs...");
        
        final ResponseEntity<String> response = testRestTemplate.exchange(
                "/users/" + userId + "/runs?from_datetime=2024-09-01T07:30:00.00Z&to_datetime=2024-09-01T18:30:00.00Z",
                HttpMethod.GET,
                null,
                String.class
        );

        PagedModel<RunRM> runRMS = objectMapper.readValue(response.getBody(), new TypeReference<PagedModel<RunRM>>() {
        });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(runRMS.getMetadata().getTotalElements(), 1);
        logger.info("Getting user runs test is completed");
    }

    @Test
    @Order(7)
    @DisplayName("User stats aggregation")
    void userStatsAggregationTest() throws JsonProcessingException {
        logger.debug("Testing user run stats aggregation...");

        final ResponseEntity<RunStatsRM> response = testRestTemplate.exchange(
                "/users/" + userId + "/runs/stats?from_datetime=2024-09-01T07:30:00.00Z&to_datetime=2024-09-01T18:30:00.00Z",
                HttpMethod.GET,
                null,
                RunStatsRM.class
        );

        final RunStatsRM runStatsRM = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(runStatsRM.getTotalRuns(), 1);
        assertEquals(runStatsRM.getTotalDistance(), 2992);
        assertEquals(runStatsRM.getAverageSpeed(), (double) 2992 / 1800);
        logger.info("Testing user run stats aggregation is completed");
    }

    @Test
    @Order(7)
    @DisplayName("User stats aggregation 2")
    void userStatsAggregationTest_2() throws JsonProcessingException {
        logger.debug("Testing user run stats aggregation 2...");

        final ResponseEntity<RunStatsRM> response = testRestTemplate.exchange(
                "/users/" + userId + "/runs/stats?from_datetime=2024-09-01T07:30:00.00Z",
                HttpMethod.GET,
                null,
                RunStatsRM.class
        );

        final RunStatsRM runStatsRM = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(runStatsRM.getTotalRuns(), 2);
        assertEquals(runStatsRM.getTotalDistance(), 6408);
        assertEquals(runStatsRM.getAverageSpeed(), 1.78d);
        logger.info("Testing user run stats aggregation 2 is completed");
    }

    @Test
    @Order(8)
    @DisplayName("User deletion")
    void deleteUserTest() {
        logger.debug("Starting user deletion test for user ID: {}", userId);

        ResponseEntity<UserDetailedRM> response = testRestTemplate.exchange(
                "/users/" + userId, HttpMethod.DELETE, null, UserDetailedRM.class
        );

        UserDetailedRM deletedUser = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(deletedUser);

        ResponseEntity<UserDetailedRM> verificationResponse = testRestTemplate.exchange(
                "/users/" + userId, HttpMethod.GET, null, UserDetailedRM.class
        );

        assertEquals(HttpStatus.NOT_FOUND, verificationResponse.getStatusCode());
        logger.info("User deletion test completed for user ID: {}", userId);
    }
}
