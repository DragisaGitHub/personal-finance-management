package com.dragi.finance_manager.transaction;

import com.dragi.finance_manager.category.Category;
import com.dragi.finance_manager.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionModelAssembler transactionModelAssembler;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();

        Category category = new Category();
        category.setId(1L);
        category.setName("Groceries");

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setDescription("Salary");
        transaction.setAmount(5000);
        transaction.setDate(LocalDate.now());
        transaction.setType(TransactionType.INCOME);
        transaction.setCategory(category);
    }

    @Test
    void shouldCreateTransaction() throws Exception {
        // Mock the SecurityContext and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn("dragisa"); // Mock the authenticated user (legal user)
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext); // Set the mocked SecurityContext

        // Mocking the transaction creation
        EntityModel<Transaction> transactionEntityModel = EntityModel.of(transaction,
                linkTo(methodOn(TransactionController.class).getTransactionById(transaction.getId())).withSelfRel());

        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);
        when(transactionModelAssembler.toModel(any(Transaction.class))).thenReturn(transactionEntityModel);

        // Perform the request and print the response for debugging
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Salary\", \"amount\": 5000, \"type\": \"INCOME\", \"category\": {\"id\": 1}}"))
                .andDo(print())  // This will print the full response to the console
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Salary"))
                .andExpect(jsonPath("$.category.name").value("Groceries"))
                .andExpect(jsonPath("$.links[0].href").exists()); // Corrected path to first link's href

        // Verify the service call
        verify(transactionService, times(1)).createTransaction(any(Transaction.class));
    }
}