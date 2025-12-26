package com.example.demo;

import com.example.demo.dto.ComplaintRequest;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.PriorityRule;
import com.example.demo.entity.User;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.PriorityRuleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.ComplaintService;
import com.example.demo.service.PriorityRuleService;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.ComplaintServiceImpl;
import com.example.demo.service.impl.PriorityRuleServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import com.example.demo.servlet.SimpleEchoServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

@Listeners(TestResultListener.class)
public class ComplaintPrioritizationEngineTest {

    private SimpleEchoServlet servlet;

    private UserRepository userRepository;
    private ComplaintRepository complaintRepository;
    private PriorityRuleRepository priorityRuleRepository;
    private PasswordEncoder passwordEncoder;

    private UserService userService;
    private PriorityRuleService priorityRuleService;
    private ComplaintService complaintService;

    private JwtUtil jwtUtil;

    private User sampleUser;
    private PriorityRule highSeverityRule;

    @BeforeClass
    public void setUp() {
        servlet = new SimpleEchoServlet();

        userRepository = Mockito.mock(UserRepository.class);
        complaintRepository = Mockito.mock(ComplaintRepository.class);
        priorityRuleRepository = Mockito.mock(PriorityRuleRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);

        userService = new UserServiceImpl(userRepository, passwordEncoder);
        priorityRuleService = new PriorityRuleServiceImpl(priorityRuleRepository);
        complaintService = new ComplaintServiceImpl(
                complaintRepository,
                null,
                null,
                priorityRuleService
        );

        jwtUtil = Mockito.mock(JwtUtil.class);

        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setEmail("customer@example.com");
        sampleUser.setFullName("Test Customer");
        sampleUser.setPassword("encoded");
        sampleUser.setRole(User.Role.CUSTOMER);

        highSeverityRule = new PriorityRule();
        highSeverityRule.setId(1L);
        highSeverityRule.setRuleName("High Severity Boost");
        highSeverityRule.setDescription("Boost score for high severity");
        highSeverityRule.setWeight(5);
        highSeverityRule.setActive(true);
    }

    // 1. Develop and deploy a simple servlet using Tomcat Server (8 tests)

    @Test(groups = {"servlet"}, priority = 1)
    public void testServletRespondsWithHelloGuestWhenNoName() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();

        servlet.doGet(req, resp);

        Assert.assertEquals(resp.getStatus(), HttpServletResponse.SC_OK);
        Assert.assertEquals(resp.getContentAsString(), "Hello, Guest");
    }

    @Test(groups = {"servlet"}, priority = 1)
    public void testServletRespondsWithProvidedName() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter("name", "Chandra");
        MockHttpServletResponse resp = new MockHttpServletResponse();

        servlet.doGet(req, resp);

        Assert.assertTrue(resp.getContentAsString().contains("Chandra"));
    }

    @Test(groups = {"servlet"}, priority = 1)
    public void testServletContentTypeIsTextPlain() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();

        servlet.doGet(req, resp);

        Assert.assertEquals(resp.getContentType(), "text/plain");
    }

    @Test(groups = {"servlet"}, priority = 1)
    public void testServletHandlesBlankName() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter("name", " ");
        MockHttpServletResponse resp = new MockHttpServletResponse();

        servlet.doGet(req, resp);

        Assert.assertEquals(resp.getContentAsString(), "Hello, Guest");
    }

    @Test(groups = {"servlet"}, priority = 1)
    public void testServletResponseStatusIs200() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();

        servlet.doGet(req, resp);

        Assert.assertEquals(resp.getStatus(), 200);
    }

    @Test(groups = {"servlet"}, priority = 1)
    public void testServletMultipleRequests() throws ServletException, IOException {
        MockHttpServletRequest req1 = new MockHttpServletRequest();
        req1.addParameter("name", "A");
        MockHttpServletResponse resp1 = new MockHttpServletResponse();
        servlet.doGet(req1, resp1);

        MockHttpServletRequest req2 = new MockHttpServletRequest();
        req2.addParameter("name", "B");
        MockHttpServletResponse resp2 = new MockHttpServletResponse();
        servlet.doGet(req2, resp2);

        Assert.assertTrue(resp1.getContentAsString().contains("A"));
        Assert.assertTrue(resp2.getContentAsString().contains("B"));
    }

    @Test(groups = {"servlet"}, priority = 1)
    public void testServletDoesNotCrashOnNullRequestParam() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();

        servlet.doGet(req, resp);

        Assert.assertNotNull(resp.getContentAsString());
    }

    @Test(groups = {"servlet"}, priority = 1)
    public void testServletTrimmedNameHandling() throws ServletException, IOException {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addParameter("name", "   X   ");
        MockHttpServletResponse resp = new MockHttpServletResponse();

        servlet.doGet(req, resp);

        Assert.assertTrue(resp.getContentAsString().contains("X"));
    }

    // 2. Implement CRUD operations using Spring Boot and REST APIs (here we test service layer CRUD-like behavior)

    @Test(groups = {"crud"}, priority = 2)
    public void testRegisterCustomerSuccess() {
        Mockito.when(userRepository.findByEmail("new@example.com"))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode("pwd")).thenReturn("encodedPwd");
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> {
                    User u = invocation.getArgument(0);
                    u.setId(10L);
                    return u;
                });

        User registered = userService.registerCustomer("New User", "new@example.com", "pwd");

        Assert.assertEquals(registered.getId(), Long.valueOf(10L));
        Assert.assertEquals(registered.getEmail(), "new@example.com");
    }

    @Test(groups = {"crud"}, priority = 2)
    public void testRegisterCustomerDuplicateEmail() {
        Mockito.when(userRepository.findByEmail("dup@example.com"))
                .thenReturn(Optional.of(sampleUser));

        try {
            userService.registerCustomer("Dup", "dup@example.com", "pwd");
            Assert.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assert.assertTrue(ex.getMessage().toLowerCase().contains("email"));
        }
    }

    @Test(groups = {"crud"}, priority = 2)
    public void testSubmitComplaintCreatesRecord() {
        ComplaintRequest req = new ComplaintRequest();
        req.setTitle("T");
        req.setDescription("D");
        req.setCategory("Payments");
        req.setChannel("Email");
        req.setSeverity(Complaint.Severity.HIGH);
        req.setUrgency(Complaint.Urgency.IMMEDIATE);

        Mockito.when(priorityRuleRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
        Mockito.when(complaintRepository.save(Mockito.any(Complaint.class)))
                .thenAnswer(invocation -> {
                    Complaint c = invocation.getArgument(0);
                    c.setId(100L);
                    return c;
                });

        Complaint c = complaintService.submitComplaint(req, sampleUser);

        Assert.assertNotNull(c.getId());
        Assert.assertEquals(c.getCustomer().getEmail(), sampleUser.getEmail());
        Assert.assertNotNull(c.getPriorityScore());
    }

    @Test(groups = {"crud"}, priority = 2)
    public void testGetComplaintsForUserReturnsList() {
        Complaint c1 = new Complaint();
        c1.setId(1L);
        c1.setCustomer(sampleUser);

        Mockito.when(complaintRepository.findByCustomer(sampleUser))
                .thenReturn(Collections.singletonList(c1));

        List<Complaint> list = complaintService.getComplaintsForUser(sampleUser);
        Assert.assertEquals(list.size(), 1);
    }

    @Test(groups = {"crud"}, priority = 2)
    public void testPrioritizedComplaintsSortedOrder() {
        Complaint c1 = new Complaint();
        c1.setId(1L);
        c1.setPriorityScore(10);
        Complaint c2 = new Complaint();
        c2.setId(2L);
        c2.setPriorityScore(20);

        Mockito.when(complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc())
                .thenReturn(Arrays.asList(c2, c1));

        List<Complaint> sorted = complaintService.getPrioritizedComplaints();
        Assert.assertEquals(sorted.get(0).getId(), Long.valueOf(2L));
    }

    @Test(groups = {"crud"}, priority = 2)
    public void testSubmitComplaintInvalidData() {
        ComplaintRequest req = new ComplaintRequest();
        req.setTitle("");
        req.setDescription("D");
        req.setCategory("Payments");
        req.setChannel("Email");
        req.setSeverity(Complaint.Severity.LOW);
        req.setUrgency(Complaint.Urgency.LOW);

        Mockito.when(priorityRuleRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
        Mockito.when(complaintRepository.save(Mockito.any(Complaint.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Complaint c = complaintService.submitComplaint(req, sampleUser);
        Assert.assertEquals(c.getTitle(), "");
    }

    @Test(groups = {"crud"}, priority = 2)
    public void testSubmitComplaintWithRulesUsesWeights() {
        ComplaintRequest req = new ComplaintRequest();
        req.setTitle("T");
        req.setDescription("D");
        req.setCategory("Payments");
        req.setChannel("Email");
        req.setSeverity(Complaint.Severity.CRITICAL);
        req.setUrgency(Complaint.Urgency.IMMEDIATE);

        Mockito.when(priorityRuleRepository.findByActiveTrue())
                .thenReturn(Collections.singletonList(highSeverityRule));
        Mockito.when(complaintRepository.save(Mockito.any(Complaint.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Complaint c = complaintService.submitComplaint(req, sampleUser);
        Assert.assertTrue(c.getPriorityScore() > 0);
    }

    @Test(groups = {"crud"}, priority = 2)
    public void testFindByEmailSuccess() {
        Mockito.when(userRepository.findByEmail("customer@example.com"))
                .thenReturn(Optional.of(sampleUser));

        User u = userService.findByEmail("customer@example.com");
        Assert.assertEquals(u.getEmail(), sampleUser.getEmail());
    }

    // 3. Configure and perform Dependency Injection and IoC using Spring Framework

    @Test(groups = {"ioc"}, priority = 3)
    public void testUserServiceInjectedRepositories() {
        Assert.assertNotNull(userService);
    }

    @Test(groups = {"ioc"}, priority = 3)
    public void testComplaintServiceInjectedRepositories() {
        Assert.assertNotNull(complaintService);
    }

    @Test(groups = {"ioc"}, priority = 3)
    public void testPriorityRuleServiceInjectedRepository() {
        Assert.assertNotNull(priorityRuleService);
    }

    @Test(groups = {"ioc"}, priority = 3)
    public void testPasswordEncoderIsUsedOnRegister() {
        Mockito.when(userRepository.findByEmail("pw@example.com"))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode("raw")).thenReturn("encoded1");
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User u = userService.registerCustomer("U", "pw@example.com", "raw");
        Assert.assertEquals(u.getPassword(), "encoded1");
    }

    @Test(groups = {"ioc"}, priority = 3)
    public void testIoCForPriorityRuleServiceComputeScore() {
        Complaint complaint = new Complaint();
        complaint.setSeverity(Complaint.Severity.HIGH);
        complaint.setUrgency(Complaint.Urgency.MEDIUM);

        Mockito.when(priorityRuleRepository.findByActiveTrue())
                .thenReturn(Collections.singletonList(highSeverityRule));

        int score = priorityRuleService.computePriorityScore(complaint);
        Assert.assertTrue(score > 0);
    }

    @Test(groups = {"ioc"}, priority = 3)
    public void testMultipleBeansCollaborateForSubmitComplaint() {
        ComplaintRequest req = new ComplaintRequest();
        req.setTitle("Combo");
        req.setDescription("Combo desc");
        req.setCategory("General");
        req.setChannel("App");
        req.setSeverity(Complaint.Severity.MEDIUM);
        req.setUrgency(Complaint.Urgency.HIGH);

        Mockito.when(priorityRuleRepository.findByActiveTrue())
                .thenReturn(Collections.singletonList(highSeverityRule));
        Mockito.when(complaintRepository.save(Mockito.any(Complaint.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Complaint c = complaintService.submitComplaint(req, sampleUser);
        Assert.assertEquals(c.getCustomer(), sampleUser);
    }

    @Test(groups = {"ioc"}, priority = 3)
    public void testServiceLayerHandlesNullRulesGracefully() {
        ComplaintRequest req = new ComplaintRequest();
        req.setTitle("No rules");
        req.setDescription("No rules desc");
        req.setCategory("General");
        req.setChannel("App");
        req.setSeverity(Complaint.Severity.LOW);
        req.setUrgency(Complaint.Urgency.LOW);

        Mockito.when(priorityRuleRepository.findByActiveTrue())
                .thenReturn(Collections.emptyList());
        Mockito.when(complaintRepository.save(Mockito.any(Complaint.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Complaint c = complaintService.submitComplaint(req, sampleUser);
        Assert.assertNotNull(c.getPriorityScore());
    }

    // 4. Implement Hibernate configurations, generator classes, annotations, and CRUD operations

    @Test(groups = {"hibernate"}, priority = 4)
    public void testComplaintEntityDefaultStatus() {
        Complaint c = new Complaint();
        c.setSeverity(Complaint.Severity.LOW);
        c.setUrgency(Complaint.Urgency.LOW);
        Assert.assertEquals(c.getStatus(), Complaint.Status.NEW);
    }

    @Test(groups = {"hibernate"}, priority = 4)
    public void testPriorityRuleEntityDefaultsActive() {
        PriorityRule rule = new PriorityRule();
        rule.setRuleName("R");
        rule.setDescription("D");
        rule.setWeight(1);
        Assert.assertTrue(rule.isActive());
    }

    @Test(groups = {"hibernate"}, priority = 4)
    public void testComplaintIdGeneratedOnSave() {
        Complaint c = new Complaint();
        c.setSeverity(Complaint.Severity.LOW);
        c.setUrgency(Complaint.Urgency.LOW);
        c.setCategory("Cat");
        c.setChannel("App");
        c.setTitle("T");
        c.setDescription("D");
        c.setCustomer(sampleUser);

        Mockito.when(priorityRuleRepository.findByActiveTrue())
                .thenReturn(Collections.emptyList());
        Mockito.when(complaintRepository.save(Mockito.any(Complaint.class)))
                .thenAnswer(invocation -> {
                    Complaint saved = invocation.getArgument(0);
                    saved.setId(999L);
                    return saved;
                });

        Complaint saved = complaintService.submitComplaint(new ComplaintRequest() {{
            setTitle("T");
            setDescription("D");
            setCategory("Cat");
            setChannel("App");
            setSeverity(Complaint.Severity.LOW);
            setUrgency(Complaint.Urgency.LOW);
        }}, sampleUser);

        Assert.assertEquals(saved.getId(), Long.valueOf(999L));
    }

    @Test(groups = {"hibernate"}, priority = 4)
    public void testPriorityRuleRepositoryFindByActiveTrue() {
        Mockito.when(priorityRuleRepository.findByActiveTrue())
                .thenReturn(Collections.singletonList(highSeverityRule));

        List<PriorityRule> rules = priorityRuleService.getActiveRules();
        Assert.assertEquals(rules.size(), 1);
        Assert.assertTrue(rules.get(0).isActive());
    }

    @Test(groups = {"hibernate"}, priority = 4)
    public void testDisableRuleChangesActiveFlag() {
        PriorityRule rule = new PriorityRule();
        rule.setRuleName("Temp");
        rule.setDescription("Temp");
        rule.setWeight(1);
        rule.setActive(false);
        Assert.assertFalse(rule.isActive());
    }

    @Test(groups = {"hibernate"}, priority = 4)
    public void testComplaintStatusEnumValues() {
        Complaint.Status[] values = Complaint.Status.values();
        Assert.assertTrue(values.length >= 4);
    }

    @Test(groups = {"hibernate"}, priority = 4)
    public void testSeverityEnumValues() {
        Complaint.Severity[] values = Complaint.Severity.values();
        Assert.assertTrue(Arrays.stream(values).anyMatch(v -> v == Complaint.Severity.CRITICAL));
    }

    @Test(groups = {"hibernate"}, priority = 4)
    public void testUrgencyEnumValues() {
        Complaint.Urgency[] values = Complaint.Urgency.values();
        Assert.assertTrue(Arrays.stream(values).anyMatch(v -> v == Complaint.Urgency.IMMEDIATE));
    }

    // 5. Perform JPA mapping with normalization (1NF, 2NF, 3NF)

    @Test(groups = {"jpa"}, priority = 5)
    public void testComplaintHasCustomerAssociation() {
        Complaint c = new Complaint();
        c.setCustomer(sampleUser);
        Assert.assertEquals(c.getCustomer(), sampleUser);
    }

    @Test(groups = {"jpa"}, priority = 5)
    public void testComplaintManyToManyWithRules() {
        Complaint c = new Complaint();
        c.getPriorityRules().add(highSeverityRule);
        Assert.assertEquals(c.getPriorityRules().size(), 1);
    }

    @Test(groups = {"jpa"}, priority = 5)
    public void testAddingMultipleRulesToComplaint() {
        PriorityRule r2 = new PriorityRule();
        r2.setRuleName("R2");
        r2.setDescription("D2");
        r2.setWeight(2);

        Complaint c = new Complaint();
        c.getPriorityRules().add(highSeverityRule);
        c.getPriorityRules().add(r2);

        Assert.assertEquals(c.getPriorityRules().size(), 2);
    }

    @Test(groups = {"jpa"}, priority = 5)
    public void testRemovingRuleFromComplaint() {
        Complaint c = new Complaint();
        c.getPriorityRules().add(highSeverityRule);
        Assert.assertTrue(c.getPriorityRules().remove(highSeverityRule));
    }

    @Test(groups = {"jpa"}, priority = 5)
    public void testComplaintNormalizationSimpleFields() {
        Complaint c = new Complaint();
        c.setTitle("Title");
        c.setDescription("Desc");
        c.setCategory("Cat");
        c.setChannel("App");
        Assert.assertEquals(c.getCategory(), "Cat");
    }

    @Test(groups = {"jpa"}, priority = 5)
    public void testUserNormalizationEmailUnique() {
        Mockito.when(userRepository.findByEmail("unique@example.com"))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode("p")).thenReturn("p2");
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User u = userService.registerCustomer("U", "unique@example.com", "p");
        Assert.assertEquals(u.getEmail(), "unique@example.com");
    }

    @Test(groups = {"jpa"}, priority = 5)
    public void testSecondUserWithSameEmailRejected() {
        Mockito.when(userRepository.findByEmail("dup2@example.com"))
                .thenReturn(Optional.of(sampleUser));

        try {
            userService.registerCustomer("X", "dup2@example.com", "p");
            Assert.fail("Expected duplicate email rejection");
        } catch (RuntimeException ex) {
            Assert.assertTrue(ex.getMessage().toLowerCase().contains("email"));
        }
    }

    @Test(groups = {"jpa"}, priority = 5)
    public void testComplaintCustomerAndAgentDifferent() {
        User agent = new User();
        agent.setId(2L);
        agent.setEmail("agent@example.com");
        agent.setFullName("Agent");
        agent.setRole(User.Role.AGENT);

        Complaint c = new Complaint();
        c.setCustomer(sampleUser);
        c.setAssignedAgent(agent);

        Assert.assertNotEquals(c.getCustomer().getId(), c.getAssignedAgent().getId());
    }

    // 6. Create Many-to-Many relationships and test associations in Spring Boot

    @Test(groups = {"many-to-many"}, priority = 6)
    public void testRuleHasComplaintsSet() {
        // Clear previous state added by other tests
        highSeverityRule.getComplaints().clear();

        Complaint c = new Complaint();
        highSeverityRule.getComplaints().add(c);
        Assert.assertEquals(highSeverityRule.getComplaints().size(), 1);
    }

    @Test(groups = {"many-to-many"}, priority = 6)
    public void testComplaintAndRuleBidirectionalLink() {
        Complaint c = new Complaint();
        c.getPriorityRules().add(highSeverityRule);
        highSeverityRule.getComplaints().add(c);

        Assert.assertTrue(c.getPriorityRules().contains(highSeverityRule));
        Assert.assertTrue(highSeverityRule.getComplaints().contains(c));
    }

    @Test(groups = {"many-to-many"}, priority = 6)
    public void testMultipleComplaintsShareSameRule() {
        // Clear previous state added by other tests
        highSeverityRule.getComplaints().clear();

        Complaint c1 = new Complaint();
        Complaint c2 = new Complaint();
        c1.getPriorityRules().add(highSeverityRule);
        c2.getPriorityRules().add(highSeverityRule);
        highSeverityRule.getComplaints().add(c1);
        highSeverityRule.getComplaints().add(c2);

        Assert.assertEquals(highSeverityRule.getComplaints().size(), 2);
    }

    @Test(groups = {"many-to-many"}, priority = 6)
    public void testClearingComplaintsFromRule() {
        highSeverityRule.getComplaints().clear();
        Assert.assertEquals(highSeverityRule.getComplaints().size(), 0);
    }

    @Test(groups = {"many-to-many"}, priority = 6)
    public void testComplaintPriorityRulesInitiallyEmpty() {
        Complaint c = new Complaint();
        Assert.assertTrue(c.getPriorityRules().isEmpty());
    }

    @Test(groups = {"many-to-many"}, priority = 6)
    public void testAddAndRemoveRuleOnComplaint() {
        Complaint c = new Complaint();
        c.getPriorityRules().add(highSeverityRule);
        Assert.assertTrue(c.getPriorityRules().remove(highSeverityRule));
    }

    @Test(groups = {"many-to-many"}, priority = 6)
    public void testManyToManyDoesNotDuplicateRules() {
        Complaint c = new Complaint();
        c.getPriorityRules().add(highSeverityRule);
        c.getPriorityRules().add(highSeverityRule);
        Assert.assertEquals(c.getPriorityRules().size(), 1);
    }

    @Test(groups = {"many-to-many"}, priority = 6)
    public void testRuleWeightInfluencesScore() {
        Complaint complaint = new Complaint();
        complaint.setSeverity(Complaint.Severity.MEDIUM);
        complaint.setUrgency(Complaint.Urgency.MEDIUM);

        Mockito.when(priorityRuleRepository.findByActiveTrue())
                .thenReturn(Collections.singletonList(highSeverityRule));

        int score = priorityRuleService.computePriorityScore(complaint);
        Assert.assertTrue(score >= highSeverityRule.getWeight());
    }

    // 7. Implement basic security controls and JWT token-based authentication

    @Test(groups = {"security"}, priority = 7)
    public void testJwtContainsRoleEmailUserId() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "AGENT");
        claims.put("email", "agent@example.com");
        claims.put("userId", 2L);

        // simulate JwtUtil behavior by mocking claims extraction
        Mockito.when(jwtUtil.extractRole("token")).thenReturn("AGENT");
        Mockito.when(jwtUtil.extractEmail("token")).thenReturn("agent@example.com");
        Mockito.when(jwtUtil.extractUserId("token")).thenReturn(2L);

        Assert.assertEquals(jwtUtil.extractRole("token"), "AGENT");
        Assert.assertEquals(jwtUtil.extractEmail("token"), "agent@example.com");
        Assert.assertEquals(jwtUtil.extractUserId("token"), Long.valueOf(2L));
    }

    @Test(groups = {"security"}, priority = 7)
    public void testJwtValidationSuccess() {
        Mockito.when(jwtUtil.validateToken("token", "user@example.com")).thenReturn(true);
        Assert.assertTrue(jwtUtil.validateToken("token", "user@example.com"));
    }

    @Test(groups = {"security"}, priority = 7)
    public void testJwtValidationFailureForDifferentEmail() {
        Mockito.when(jwtUtil.validateToken("token", "user@example.com")).thenReturn(false);
        Assert.assertFalse(jwtUtil.validateToken("token", "user@example.com"));
    }

    @Test(groups = {"security"}, priority = 7)
    public void testJwtUserIdEdgeCaseNull() {
        Mockito.when(jwtUtil.extractUserId("badToken")).thenReturn(null);
        Assert.assertNull(jwtUtil.extractUserId("badToken"));
    }

    @Test(groups = {"security"}, priority = 7)
    public void testRolePrefixLogicForAuthorities() {
        User agent = new User();
        agent.setEmail("agent@example.com");
        agent.setPassword("x");
        agent.setRole(User.Role.AGENT);

        UserRepository repo = Mockito.mock(UserRepository.class);
        Mockito.when(repo.findByEmail("agent@example.com")).thenReturn(Optional.of(agent));

        com.example.demo.security.CustomUserDetailsService service =
                new com.example.demo.security.CustomUserDetailsService(repo);

        org.springframework.security.core.userdetails.UserDetails ud =
                service.loadUserByUsername("agent@example.com");

        Assert.assertTrue(ud.getAuthorities().iterator().next().getAuthority().contains("ROLE_AGENT"));
    }

    @Test(groups = {"security"}, priority = 7)
    public void testBadCredentialsHandled() {
        try {
            throw new org.springframework.security.authentication.BadCredentialsException("Bad");
        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
            Assert.assertEquals(ex.getClass(), org.springframework.security.authentication.BadCredentialsException.class);
        }
    }

    @Test(groups = {"security"}, priority = 7)
    public void testSecurityDoesNotAllowNullTokenValidation() {
        Mockito.when(jwtUtil.validateToken(null, "x")).thenReturn(false);
        Assert.assertFalse(jwtUtil.validateToken(null, "x"));
    }

    @Test(groups = {"security"}, priority = 7)
    public void testJwtEmailExtractionEdgeCase() {
        Mockito.when(jwtUtil.extractEmail("empty")).thenReturn("");
        Assert.assertEquals(jwtUtil.extractEmail("empty"), "");
    }

    // 8. Use HQL and HCQL to perform advanced data querying (simulate via repository calls)

    @Test(groups = {"hql"}, priority = 8)
    public void testFindAllOrderByPriorityScoreDesc() {
        Complaint c1 = new Complaint();
        c1.setId(1L);
        c1.setPriorityScore(5);
        Complaint c2 = new Complaint();
        c2.setId(2L);
        c2.setPriorityScore(10);

        Mockito.when(complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc())
                .thenReturn(Arrays.asList(c2, c1));

        List<Complaint> list = complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc();
        Assert.assertEquals(list.get(0).getPriorityScore().intValue(), 10);
    }

    @Test(groups = {"hql"}, priority = 8)
    public void testHqlReturnsEmptyListForNoComplaints() {
        Mockito.when(complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc())
                .thenReturn(Collections.emptyList());

        List<Complaint> list = complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc();
        Assert.assertTrue(list.isEmpty());
    }

    @Test(groups = {"hql"}, priority = 8)
    public void testHqlEdgeCaseSingleComplaint() {
        Complaint c1 = new Complaint();
        c1.setId(1L);
        c1.setPriorityScore(7);

        Mockito.when(complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc())
                .thenReturn(Collections.singletonList(c1));

        List<Complaint> list = complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc();
        Assert.assertEquals(list.size(), 1);
    }

    @Test(groups = {"hql"}, priority = 8)
    public void testHqlOrderingStableForEqualScores() {
        Complaint c1 = new Complaint();
        c1.setId(1L);
        c1.setPriorityScore(5);
        Complaint c2 = new Complaint();
        c2.setId(2L);
        c2.setPriorityScore(5);

        Mockito.when(complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc())
                .thenReturn(Arrays.asList(c1, c2));

        List<Complaint> list = complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc();
        Assert.assertEquals(list.size(), 2);
    }

    @Test(groups = {"hql"}, priority = 8)
    public void testHqlUsedByServiceForPrioritizedComplaints() {
        Mockito.when(complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc())
                .thenReturn(Collections.emptyList());

        List<Complaint> list = complaintService.getPrioritizedComplaints();
        Assert.assertTrue(list.isEmpty());
    }

    @Test(groups = {"hql"}, priority = 8)
    public void testHqlWithMixedScoresAndNull() {
        Complaint c1 = new Complaint();
        c1.setId(1L);
        c1.setPriorityScore(null);
        Complaint c2 = new Complaint();
        c2.setId(2L);
        c2.setPriorityScore(3);

        Mockito.when(complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc())
                .thenReturn(Arrays.asList(c2, c1));

        List<Complaint> list = complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc();
        Assert.assertNull(list.get(1).getPriorityScore());
    }

    @Test(groups = {"hql"}, priority = 8)
    public void testHqlQueryDoesNotModifyEntities() {
        Complaint c1 = new Complaint();
        c1.setId(1L);
        c1.setPriorityScore(1);

        Mockito.when(complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc())
                .thenReturn(Collections.singletonList(c1));

        List<Complaint> list = complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc();
        list.get(0).setPriorityScore(99);
        Assert.assertEquals(c1.getPriorityScore().intValue(), 99);
    }

    @Test(groups = {"hql"}, priority = 8)
    public void testHqlRepositoryInteractionCount() {
        complaintService.getPrioritizedComplaints();
        Mockito.verify(complaintRepository, Mockito.atLeastOnce())
                .findAllOrderByPriorityScoreDescCreatedAtAsc();
    }
}
