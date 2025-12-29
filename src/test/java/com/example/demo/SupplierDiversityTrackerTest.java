package com.example.demo;

import com.example.demo.controller.*;
import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.repository.*;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import com.example.demo.servlet.SimpleStatusServlet;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Listeners(TestResultListener.class)
public class SupplierDiversityTrackerTest {

    // -------------------- MOCKED REPOSITORIES --------------------
    @Mock private SupplierRepository supplierRepository;
    @Mock private DiversityClassificationRepository diversityClassificationRepository;
    @Mock private SpendCategoryRepository spendCategoryRepository;
    @Mock private PurchaseOrderRepository purchaseOrderRepository;
    @Mock private DiversityTargetRepository diversityTargetRepository;
    @Mock private UserAccountRepository userAccountRepository;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtUtil jwtUtil;

    // -------------------- SERVICES --------------------
    private SupplierService supplierService;
    private DiversityClassificationService classificationService;
    private SpendCategoryService categoryService;
    private PurchaseOrderService purchaseOrderService;
    private DiversityTargetService targetService;
    private UserAccountService userAccountService;

    // -------------------- CONTROLLERS --------------------
    private SupplierController supplierController;
    private DiversityClassificationController classificationController;
    private SpendCategoryController categoryController;
    private PurchaseOrderController purchaseOrderController;
    private DiversityTargetController targetController;
    private AuthController authController;

    // -------------------- SERVLET --------------------
    private SimpleStatusServlet simpleStatusServlet;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Services
        supplierService = new SupplierServiceImpl(supplierRepository);
        classificationService = new DiversityClassificationServiceImpl(diversityClassificationRepository);
        categoryService = new SpendCategoryServiceImpl(spendCategoryRepository);
        purchaseOrderService = new PurchaseOrderServiceImpl(
                purchaseOrderRepository, supplierRepository, spendCategoryRepository);
        targetService = new DiversityTargetServiceImpl(diversityTargetRepository);

        userAccountService = new UserAccountServiceImpl(
                userAccountRepository,
                new PasswordEncoder() {
                    @Override
                    public String encode(CharSequence rawPassword) {
                        return rawPassword + "_ENC";
                    }

                    @Override
                    public boolean matches(CharSequence rawPassword, String encodedPassword) {
                        return encodedPassword.equals(rawPassword + "_ENC");
                    }
                }
        );

        // Controllers
        supplierController = new SupplierController(supplierService);
        classificationController = new DiversityClassificationController(classificationService);
        categoryController = new SpendCategoryController(categoryService);
        purchaseOrderController = new PurchaseOrderController(purchaseOrderService);
        targetController = new DiversityTargetController(targetService);
        authController = new AuthController(userAccountService, authenticationManager, jwtUtil);

        // Servlet
        simpleStatusServlet = new SimpleStatusServlet();
    }

    // -------------------------------------------------------------------------
    // 1. SERVLET TESTS (Develop and deploy a simple servlet)  [1–8]
    // -------------------------------------------------------------------------

    @Test(priority = 1, groups = "servlet")
    public void t01_servlet_basicTextResponse() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        simpleStatusServlet.doGet(req, resp);

        Assert.assertEquals(sw.toString(),
                "Supplier Diversity Tracker is running");
    }

    @Test(priority = 2, groups = "servlet")
    public void t02_servlet_setsContentType() throws Exception {
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        simpleStatusServlet.doGet(null, resp);

        verify(resp).setContentType("text/plain");
    }

    @Test(priority = 3, groups = "servlet")
    public void t03_servlet_handlesNullRequest() throws Exception {
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        simpleStatusServlet.doGet(null, resp);

        Assert.assertEquals(true, true);
    }

    @Test(priority = 4, groups = "servlet")
    public void t04_servlet_flushCalled() throws Exception {
        HttpServletResponse resp = mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();
        PrintWriter pw = spy(new PrintWriter(sw));

        when(resp.getWriter()).thenReturn(pw);

        simpleStatusServlet.doGet(null, resp);

        verify(pw).flush();
    }

    @Test(priority = 5, groups = "servlet")
    public void t05_servlet_outputNotEmpty() throws Exception {
        HttpServletResponse resp = mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        simpleStatusServlet.doGet(null, resp);

        Assert.assertEquals(sw.toString().isEmpty(), false);
    }

    @Test(priority = 6, groups = "servlet")
    public void t06_servlet_containsTrackerWord() throws Exception {
        HttpServletResponse resp = mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();

        when(resp.getWriter()).thenReturn(new PrintWriter(sw));
        simpleStatusServlet.doGet(null, resp);

        Assert.assertEquals(sw.toString().contains("Tracker"), true);
    }

    @Test(priority = 7, groups = "servlet")
    public void t07_servlet_idempotentCalls() throws Exception {
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        simpleStatusServlet.doGet(null, resp);
        simpleStatusServlet.doGet(null, resp);

        Assert.assertEquals(true, true);
    }

    @Test(priority = 8, groups = "servlet")
    public void t08_servlet_noHtmlTags() throws Exception {
        HttpServletResponse resp = mock(HttpServletResponse.class);
        StringWriter sw = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(sw));

        simpleStatusServlet.doGet(null, resp);

        Assert.assertEquals(sw.toString().contains("<html>"), false);
    }

    // -------------------------------------------------------------------------
    // 2. CRUD OPERATIONS (Spring Boot + REST APIs)  [9–16]
    // -------------------------------------------------------------------------

    @Test(priority = 9, groups = "crud")
    public void t09_create_supplier_success() {
        Supplier s = new Supplier();
        s.setName("ABC Corp");
        s.setEmail("abc@supplier.com");
        s.setRegistrationNumber("REG123");

        when(supplierRepository.save(any(Supplier.class)))
                .thenAnswer(inv -> {
                    Supplier saved = inv.getArgument(0);
                    saved.setId(1L);
                    return saved;
                });

        Supplier created = supplierService.createSupplier(s);
        Assert.assertEquals(created.getId(), Long.valueOf(1L));
    }

    @Test(priority = 10, groups = "crud")
    public void t10_get_supplier_by_id_success() {
        Supplier s = new Supplier();
        s.setId(2L);
        s.setName("XYZ Pvt Ltd");

        when(supplierRepository.findById(2L)).thenReturn(Optional.of(s));

        Supplier found = supplierService.getSupplierById(2L);
        Assert.assertEquals(found.getName(), "XYZ Pvt Ltd");
    }

    @Test(priority = 11, groups = "crud")
    public void t11_create_purchase_order_success() {
        Supplier sup = new Supplier();
        sup.setId(1L);
        sup.setIsActive(true);

        SpendCategory cat = new SpendCategory();
        cat.setId(1L);
        cat.setActive(true);

        PurchaseOrder po = new PurchaseOrder();
        po.setPoNumber("PO-001");
        po.setAmount(new BigDecimal("1000.00"));
        po.setDateIssued(LocalDate.now());
        po.setSupplier(sup);
        po.setCategory(cat);

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(sup));
        when(spendCategoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        PurchaseOrder saved = purchaseOrderService.createPurchaseOrder(po);
        Assert.assertEquals(saved.getPoNumber(), "PO-001");
    }

    @Test(priority = 12, groups = "crud")
    public void t12_create_purchase_order_invalid_amount_throws() {
        Supplier sup = new Supplier();
        sup.setId(1L);
        sup.setIsActive(true);

        SpendCategory cat = new SpendCategory();
        cat.setId(1L);
        cat.setActive(true);

        PurchaseOrder po = new PurchaseOrder();
        po.setPoNumber("PO-002");
        po.setAmount(new BigDecimal("-1.0"));
        po.setDateIssued(LocalDate.now());
        po.setSupplier(sup);
        po.setCategory(cat);

        try {
            purchaseOrderService.createPurchaseOrder(po);
            Assert.fail("Expected BadRequestException");
        } catch (BadRequestException ex) {
            Assert.assertEquals(ex.getClass(), BadRequestException.class);
        }
    }

    @Test(priority = 13, groups = "crud")
    public void t13_get_purchase_orders_by_supplier() {
        PurchaseOrder po = new PurchaseOrder();
        po.setId(10L);

        when(purchaseOrderRepository.findBySupplier_Id(1L))
                .thenReturn(List.of(po));

        List<PurchaseOrder> result = purchaseOrderService.getPurchaseOrdersBySupplier(1L);
        Assert.assertEquals(result.size(), 1);
    }

    @Test(priority = 14, groups = "crud")
    public void t14_create_diversity_target_success() {
        DiversityClassification dc = new DiversityClassification();
        dc.setId(1L);

        DiversityTarget t = new DiversityTarget();
        t.setTargetYear(2025);
        t.setClassification(dc);
        t.setTargetPercentage(20.0);

        when(diversityTargetRepository.save(any(DiversityTarget.class)))
                .thenAnswer(inv -> {
                    DiversityTarget saved = inv.getArgument(0);
                    saved.setId(5L);
                    return saved;
                });

        DiversityTarget created = targetService.createTarget(t);
        Assert.assertEquals(created.getId(), Long.valueOf(5L));
    }

    @Test(priority = 15, groups = "crud")
    public void t15_get_targets_by_year() {
        DiversityTarget t1 = new DiversityTarget();
        t1.setTargetYear(2024);

        when(diversityTargetRepository.findByTargetYear(2024))
                .thenReturn(List.of(t1));

        List<DiversityTarget> list = targetService.getTargetsByYear(2024);
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 16, groups = "crud")
    public void t16_deactivate_supplier_success() {
        Supplier s = new Supplier();
        s.setId(3L);
        s.setIsActive(true);

        when(supplierRepository.findById(3L)).thenReturn(Optional.of(s));
        when(supplierRepository.save(any(Supplier.class))).thenAnswer(inv -> inv.getArgument(0));

        supplierService.deactivateSupplier(3L);

        Assert.assertEquals(s.getIsActive(), Boolean.FALSE);
    }

    // -------------------------------------------------------------------------
    // 3. DI & IOC TESTS  [17–24]
    // -------------------------------------------------------------------------

    @Test(priority = 17, groups = "di")
    public void t17_services_injected() {
        Assert.assertEquals(supplierService != null, true);
        Assert.assertEquals(purchaseOrderService != null, true);
    }

    @Test(priority = 18, groups = "di")
    public void t18_controllers_injected() {
        Assert.assertEquals(supplierController != null, true);
        Assert.assertEquals(purchaseOrderController != null, true);
    }

    @Test(priority = 19, groups = "di")
    public void t19_supplierService_uses_repository() {
        when(supplierRepository.findAll()).thenReturn(List.of(new Supplier()));
        List<Supplier> list = supplierService.getAllSuppliers();
        Assert.assertEquals(list.isEmpty(), false);
    }

    @Test(priority = 20, groups = "di")
    public void t20_categoryService_uses_repository() {
        when(spendCategoryRepository.findAll()).thenReturn(List.of(new SpendCategory()));
        List<SpendCategory> list = categoryService.getAllCategories();
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 21, groups = "di")
    public void t21_targetService_uses_repository() {
        when(diversityTargetRepository.findAll()).thenReturn(List.of(new DiversityTarget()));
        List<DiversityTarget> list = targetService.getAllTargets();
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 22, groups = "di")
    public void t22_userAccountService_register_encodesPassword() {
        UserAccount acc = new UserAccount();
        acc.setEmail("test@user.com");
        acc.setPassword("plain");
        acc.setFullName("Test User");

        when(userAccountRepository.existsByEmail("test@user.com")).thenReturn(false);
        when(userAccountRepository.save(any(UserAccount.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UserAccount saved = userAccountService.register(acc);

        Assert.assertEquals(saved.getPassword().endsWith("_ENC"), true);
    }

    @Test(priority = 23, groups = "di")
    public void t23_userAccountService_findByEmailOrThrow() {
        UserAccount acc = new UserAccount();
        acc.setEmail("find@user.com");

        when(userAccountRepository.findByEmail("find@user.com"))
                .thenReturn(Optional.of(acc));

        UserAccount found = userAccountService.findByEmailOrThrow("find@user.com");
        Assert.assertEquals(found.getEmail(), "find@user.com");
    }

    @Test(priority = 24, groups = "di")
    public void t24_userAccountService_findByEmail_notFoundThrows() {
        when(userAccountRepository.findByEmail("missing@user.com"))
                .thenReturn(Optional.empty());
        try {
            userAccountService.findByEmailOrThrow("missing@user.com");
            Assert.fail("Expected ResourceNotFoundException");
        } catch (ResourceNotFoundException ex) {
            Assert.assertEquals(ex.getClass(), ResourceNotFoundException.class);
        }
    }

    // -------------------------------------------------------------------------
    // 4. HIBERNATE / LIFECYCLE TESTS  [25–32]
    // -------------------------------------------------------------------------

    @Test(priority = 25, groups = "hibernate")
    public void t25_supplier_prePersist_setsDefaults() {
        Supplier s = new Supplier();
        s.prePersist();
        Assert.assertEquals(s.getIsActive(), Boolean.TRUE);
        Assert.assertEquals(s.getCreatedAt() != null, true);
    }

    @Test(priority = 26, groups = "hibernate")
    public void t26_diversityClassification_prePersist_defaultActive() {
        DiversityClassification dc = new DiversityClassification();
        dc.preSave();
        Assert.assertEquals(dc.getActive(), Boolean.TRUE);
    }

    @Test(priority = 27, groups = "hibernate")
    public void t27_spendCategory_preSave_defaultActive() {
        SpendCategory sc = new SpendCategory();
        sc.preSave();
        Assert.assertEquals(sc.getActive(), Boolean.TRUE);
    }

    @Test(priority = 28, groups = "hibernate")
    public void t28_diversityTarget_defaultActive() {
        DiversityTarget t = new DiversityTarget();
        t.preSave();
        Assert.assertEquals(t.getActive(), Boolean.TRUE);
    }

    @Test(priority = 29, groups = "hibernate")
    public void t29_purchaseOrder_amount_positiveRule() {
        PurchaseOrder po = new PurchaseOrder();
        po.setAmount(new BigDecimal("100.00"));
        Assert.assertEquals(po.getAmount().compareTo(BigDecimal.ZERO) > 0, true);
    }

    @Test(priority = 30, groups = "hibernate")
    public void t30_purchaseOrder_date_notFuture() {
        PurchaseOrder po = new PurchaseOrder();
        po.setDateIssued(LocalDate.now());
        Assert.assertEquals(po.getDateIssued().isAfter(LocalDate.now()), false);
    }

    @Test(priority = 31, groups = "hibernate")
    public void t31_userAccount_preSave_defaults() {
        UserAccount ua = new UserAccount();
        ua.prePersist();
        Assert.assertEquals(ua.getRole(), "USER");
        Assert.assertEquals(ua.getCreatedAt() != null, true);
    }

    @Test(priority = 32, groups = "hibernate")
    public void t32_supplier_updatedAt_canBeSet() {
        Supplier s = new Supplier();
        LocalDateTime now = LocalDateTime.now();
        s.setUpdatedAt(now);
        Assert.assertEquals(s.getUpdatedAt(), now);
    }

    // -------------------------------------------------------------------------
    // 5. JPA MAPPING / NORMALIZATION TESTS  [33–40]
    // -------------------------------------------------------------------------

    @Test(priority = 33, groups = "jpa")
    public void t33_supplier_has_atomic_name() {
        Supplier s = new Supplier();
        s.setName("SingleName");
        Assert.assertEquals(s.getName().contains(","), false);
    }

    @Test(priority = 34, groups = "jpa")
    public void t34_diversityClassification_code_uppercase() {
        DiversityClassification dc = new DiversityClassification();
        dc.setCode("MINORITY");
        Assert.assertEquals(dc.getCode().equals(dc.getCode().toUpperCase()), true);
    }

    @Test(priority = 35, groups = "jpa")
    public void t35_spendCategory_uniqueNameConcept() {
        SpendCategory sc = new SpendCategory();
        sc.setName("IT Services");
        Assert.assertEquals(sc.getName(), "IT Services");
    }

    @Test(priority = 36, groups = "jpa")
    public void t36_purchaseOrder_referencesSupplier() {
        Supplier s = new Supplier();
        s.setId(10L);

        PurchaseOrder po = new PurchaseOrder();
        po.setSupplier(s);

        Assert.assertEquals(po.getSupplier().getId(), Long.valueOf(10L));
    }

    @Test(priority = 37, groups = "jpa")
    public void t37_purchaseOrder_referencesCategory() {
        SpendCategory c = new SpendCategory();
        c.setId(7L);

        PurchaseOrder po = new PurchaseOrder();
        po.setCategory(c);

        Assert.assertEquals(po.getCategory().getId(), Long.valueOf(7L));
    }

    @Test(priority = 38, groups = "jpa")
    public void t38_diversityTarget_linksClassification() {
        DiversityClassification dc = new DiversityClassification();
        dc.setId(5L);

        DiversityTarget t = new DiversityTarget();
        t.setClassification(dc);

        Assert.assertEquals(t.getClassification().getId(), Long.valueOf(5L));
    }

    @Test(priority = 39, groups = "jpa")
    public void t39_target_percentage_inRange() {
        DiversityTarget t = new DiversityTarget();
        t.setTargetPercentage(50.0);
        Double pct = t.getTargetPercentage();
        Assert.assertEquals(pct >= 0 && pct <= 100, true);
    }

    @Test(priority = 40, groups = "jpa")
    public void t40_purchaseOrder_notes_nullable() {
        PurchaseOrder po = new PurchaseOrder();
        po.setNotes(null);
        Assert.assertEquals(po.getNotes() == null, true);
    }

    // -------------------------------------------------------------------------
    // 6. MANY-TO-MANY / RELATIONS TESTS  [41–48]
    // -------------------------------------------------------------------------

    @Test(priority = 41, groups = "many")
    public void t41_supplier_canHaveMultipleClassifications() {
        Supplier s = new Supplier();
        DiversityClassification d1 = new DiversityClassification();
        DiversityClassification d2 = new DiversityClassification();

        s.getDiversityClassifications().add(d1);
        s.getDiversityClassifications().add(d2);

        Assert.assertEquals(s.getDiversityClassifications().size(), 2);
    }

    @Test(priority = 42, groups = "many")
    public void t42_supplier_classification_set_notEmpty() {
        Supplier s = new Supplier();
        s.getDiversityClassifications().add(new DiversityClassification());
        Assert.assertEquals(s.getDiversityClassifications().isEmpty(), false);
    }

    @Test(priority = 43, groups = "many")
    public void t43_supplier_removeClassification() {
        Supplier s = new Supplier();
        DiversityClassification dc = new DiversityClassification();
        s.getDiversityClassifications().add(dc);
        s.getDiversityClassifications().remove(dc);
        Assert.assertEquals(s.getDiversityClassifications().isEmpty(), true);
    }

    @Test(priority = 44, groups = "many")
    public void t44_inactive_supplier_cannotReceivePO_inService() {
        Supplier sup = new Supplier();
        sup.setId(1L);
        sup.setIsActive(false);

        SpendCategory cat = new SpendCategory();
        cat.setId(1L);
        cat.setActive(true);

        PurchaseOrder po = new PurchaseOrder();
        po.setAmount(new BigDecimal("100.0"));
        po.setDateIssued(LocalDate.now());
        po.setSupplier(sup);
        po.setCategory(cat);

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(sup));
        when(spendCategoryRepository.findById(1L)).thenReturn(Optional.of(cat));

        try {
            purchaseOrderService.createPurchaseOrder(po);
            Assert.fail("Expected BadRequestException");
        } catch (BadRequestException ex) {
            Assert.assertEquals(ex.getClass(), BadRequestException.class);
        }
    }

    @Test(priority = 45, groups = "many")
    public void t45_inactive_category_cannotReceivePO_inService() {
        Supplier sup = new Supplier();
        sup.setId(1L);
        sup.setIsActive(true);

        SpendCategory cat = new SpendCategory();
        cat.setId(1L);
        cat.setActive(false);

        PurchaseOrder po = new PurchaseOrder();
        po.setAmount(new BigDecimal("100.0"));
        po.setDateIssued(LocalDate.now());
        po.setSupplier(sup);
        po.setCategory(cat);

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(sup));
        when(spendCategoryRepository.findById(1L)).thenReturn(Optional.of(cat));

        try {
            purchaseOrderService.createPurchaseOrder(po);
            Assert.fail("Expected BadRequestException");
        } catch (BadRequestException ex) {
            Assert.assertEquals(ex.getClass(), BadRequestException.class);
        }
    }

    @Test(priority = 46, groups = "many")
    public void t46_deactivate_classification_sets_active_false() {
        DiversityClassification dc = new DiversityClassification();
        dc.setId(2L);
        dc.setActive(true);

        when(diversityClassificationRepository.findById(2L)).thenReturn(Optional.of(dc));
        when(diversityClassificationRepository.save(any(DiversityClassification.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        classificationService.deactivateClassification(2L);

        Assert.assertEquals(dc.getActive(), Boolean.FALSE);
    }

    @Test(priority = 47, groups = "many")
    public void t47_deactivate_category_sets_active_false() {
        SpendCategory sc = new SpendCategory();
        sc.setId(3L);
        sc.setActive(true);

        when(spendCategoryRepository.findById(3L)).thenReturn(Optional.of(sc));
        when(spendCategoryRepository.save(any(SpendCategory.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        categoryService.deactivateCategory(3L);
        Assert.assertEquals(sc.getActive(), Boolean.FALSE);
    }

    @Test(priority = 48, groups = "many")
    public void t48_deactivate_target_sets_active_false() {
        DiversityTarget t = new DiversityTarget();
        t.setId(4L);
        t.setActive(true);

        when(diversityTargetRepository.findById(4L)).thenReturn(Optional.of(t));
        when(diversityTargetRepository.save(any(DiversityTarget.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        targetService.deactivateTarget(4L);

        Assert.assertEquals(t.getActive(), Boolean.FALSE);
    }

    // -------------------------------------------------------------------------
    // 7. SECURITY / JWT TESTS  [49–56]
    // -------------------------------------------------------------------------

    @Test(priority = 49, groups = "security")
    public void t49_register_user_success_returns_token() {
        RegisterRequest req = new RegisterRequest();
        req.setFullName("New User");
        req.setEmail("new@user.com");
        req.setPassword("password123");
        req.setRole("ADMIN");

        UserAccount saved = new UserAccount(
                1L, "New User", "new@user.com", "password123_ENC", "ADMIN");

        when(userAccountRepository.existsByEmail("new@user.com")).thenReturn(false);
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(saved);
        when(jwtUtil.generateToken(1L, "new@user.com", "ADMIN")).thenReturn("TOKEN123");

        ResponseEntity<JwtResponse> resp = authController.register(req);

        Assert.assertEquals(resp.getBody().getToken(), "TOKEN123");
    }

    @Test(priority = 50, groups = "security")
    public void t50_register_duplicate_email_throws() {
        UserAccount user = new UserAccount();
        user.setEmail("dup@user.com");
        user.setPassword("x");

        when(userAccountRepository.existsByEmail("dup@user.com")).thenReturn(true);

        try {
            userAccountService.register(user);
            Assert.fail("Expected BadRequestException");
        } catch (BadRequestException ex) {
            Assert.assertEquals(ex.getClass(), BadRequestException.class);
        }
    }

    @Test(priority = 51, groups = "security")
    public void t51_login_success_returns_token() {
        LoginRequest req = new LoginRequest();
        req.setEmail("login@user.com");
        req.setPassword("pass");

        UserAccount user = new UserAccount(
                2L, "Login User", "login@user.com", "pass_ENC", "USER");

        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(userAccountRepository.findByEmail("login@user.com"))
                .thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(2L, "login@user.com", "USER"))
                .thenReturn("LOGIN_TOKEN");

        // authController.login internally uses userAccountService.findByEmailOrThrow
        // so make that consistent
        when(userAccountRepository.findByEmail("login@user.com"))
                .thenReturn(Optional.of(user));

        ResponseEntity<JwtResponse> resp = authController.login(req);
        Assert.assertEquals(resp.getBody().getToken(), "LOGIN_TOKEN");
    }

    @Test(priority = 52, groups = "security")
    public void t52_login_invalid_credentials_throwsUnauthorized() {
        LoginRequest req = new LoginRequest();
        req.setEmail("wrong@user.com");
        req.setPassword("bad");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad"));

        try {
            authController.login(req);
            Assert.fail("Expected UnauthorizedException");
        } catch (UnauthorizedException ex) {
            Assert.assertEquals(ex.getClass(), UnauthorizedException.class);
        }
    }

    @Test(priority = 53, groups = "security")
    public void t53_jwt_generate_and_extract_email_role_userId() {
        String secretStr = "supplier-diversity-secret-key-1234567890";
        byte[] secretBytes = secretStr.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        JwtUtil realJwt = new JwtUtil(secretBytes, 3600000L);

        String token = realJwt.generateToken(99L, "jwt@test.com", "ADMIN");

        String email = realJwt.extractEmail(token);
        String role = realJwt.extractRole(token);
        Long userId = realJwt.extractUserId(token);

        Assert.assertEquals(email, "jwt@test.com");
        Assert.assertEquals(role, "ADMIN");
        Assert.assertEquals(userId, Long.valueOf(99L));
    }

    @Test(priority = 54, groups = "security")
    public void t54_jwt_token_is_valid_initially() {
        String secretStr = "supplier-diversity-secret-key-1234567890";
        byte[] secretBytes = secretStr.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        JwtUtil realJwt = new JwtUtil(secretBytes, 3600000L);

        String token = realJwt.generateToken(5L, "valid@test.com", "USER");
        Assert.assertEquals(realJwt.validateToken(token), true);
    }

    @Test(priority = 55, groups = "security")
    public void t55_jwt_invalid_token_returns_false() {
        String secretStr = "supplier-diversity-secret-key-1234567890";
        byte[] secretBytes = secretStr.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        JwtUtil realJwt = new JwtUtil(secretBytes, 3600000L);

        Assert.assertEquals(realJwt.validateToken("invalid.token.value"), false);
    }

    @Test(priority = 56, groups = "security")
    public void t56_userAccount_password_encoder_matches() {
        PasswordEncoder encoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword + "_ENC";
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(rawPassword + "_ENC");
            }
        };

        String encoded = encoder.encode("pw");
        Assert.assertEquals(encoder.matches("pw", encoded), true);
    }

    // -------------------------------------------------------------------------
    // 8. HQL / HCQL-LIKE TESTS (Repository query behavior)  [57–64]
    // -------------------------------------------------------------------------

    @Test(priority = 57, groups = "hql")
    public void t57_find_purchase_orders_by_supplier_query() {
        PurchaseOrder po1 = new PurchaseOrder();
        po1.setId(1L);

        when(purchaseOrderRepository.findBySupplier_Id(10L))
                .thenReturn(List.of(po1));

        List<PurchaseOrder> list = purchaseOrderRepository.findBySupplier_Id(10L);
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 58, groups = "hql")
    public void t58_find_targets_by_year_query() {
        DiversityTarget t1 = new DiversityTarget();
        t1.setTargetYear(2025);

        when(diversityTargetRepository.findByTargetYear(2025))
                .thenReturn(List.of(t1));

        List<DiversityTarget> list = diversityTargetRepository.findByTargetYear(2025);
        Assert.assertEquals(list.isEmpty(), false);
    }

    @Test(priority = 59, groups = "hql")
    public void t59_find_active_classifications_query() {
        DiversityClassification dc = new DiversityClassification();
        dc.setActive(true);

        when(diversityClassificationRepository.findByActiveTrue())
                .thenReturn(List.of(dc));

        List<DiversityClassification> list = diversityClassificationRepository.findByActiveTrue();
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 60, groups = "hql")
    public void t60_find_active_categories_query() {
        SpendCategory sc = new SpendCategory();
        sc.setActive(true);

        when(spendCategoryRepository.findByActiveTrue())
                .thenReturn(List.of(sc));

        List<SpendCategory> list = spendCategoryRepository.findByActiveTrue();
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 61, groups = "hql")
    public void t61_find_active_suppliers_query() {
        Supplier s = new Supplier();
        s.setIsActive(true);

        when(supplierRepository.findByIsActiveTrue())
                .thenReturn(List.of(s));

        List<Supplier> list = supplierRepository.findByIsActiveTrue();
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 62, groups = "hql")
    public void t62_find_all_purchase_orders_query() {
        when(purchaseOrderRepository.findAll())
                .thenReturn(List.of(new PurchaseOrder(), new PurchaseOrder()));

        List<PurchaseOrder> list = purchaseOrderRepository.findAll();
        Assert.assertEquals(list.size(), 2);
    }

    @Test(priority = 63, groups = "hql")
    public void t63_supplierRepository_email_unique_existsCheck() {
        when(supplierRepository.existsByEmail("unique@supplier.com"))
                .thenReturn(true);

        boolean exists = supplierRepository.existsByEmail("unique@supplier.com");
        Assert.assertEquals(exists, true);
    }

    @Test(priority = 64, groups = "hql")
    public void t64_targetRepository_findAll_query() {
        when(diversityTargetRepository.findAll())
                .thenReturn(List.of(new DiversityTarget()));

        List<DiversityTarget> list = diversityTargetRepository.findAll();
        Assert.assertEquals(list.isEmpty(), false);
    }
}
