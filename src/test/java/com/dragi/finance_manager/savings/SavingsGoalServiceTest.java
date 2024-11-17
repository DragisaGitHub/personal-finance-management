package com.dragi.finance_manager.savings;

import com.dragi.finance_manager.utils.HelperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;

class SavingsGoalServiceTest {

    @InjectMocks
    SavingsGoalService savingsGoalService;

    @Mock
    SavingsGoalRepository savingsGoalRepository;

    @Mock
    Authentication authentication;

    @Mock
    SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn("dragisa");
    }

    @Test
    void testSavingsGoalProgress() {
        String username = HelperUtils.getAuthenticatedUsername();

        SavingsGoal goal = new SavingsGoal();
        goal.setName("Vacation Fund");
        goal.setTargetAmount(1000.00);
        goal.setCurrentAmount(900.00);
        goal.setUsername(username);

        when(savingsGoalRepository.save(any(SavingsGoal.class))).thenReturn(goal);

        savingsGoalService.saveOrUpdateGoal(goal);

        verify(savingsGoalRepository, times(1)).save(goal);
    }
}