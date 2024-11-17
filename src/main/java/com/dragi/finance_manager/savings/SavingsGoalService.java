package com.dragi.finance_manager.savings;

import com.dragi.finance_manager.notification.NotificationService;
import com.dragi.finance_manager.utils.HelperUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;
    private final NotificationService notificationService;

    public SavingsGoalService(SavingsGoalRepository savingsGoalRepository, NotificationService notificationService) {
        this.savingsGoalRepository = savingsGoalRepository;
        this.notificationService = notificationService;
    }

    public SavingsGoal saveOrUpdateGoal(SavingsGoal goal) {
        goal.setUsername(HelperUtils.getAuthenticatedUsername());
        boolean completed = checkGoalProgress(goal);
        goal.setCompleted(completed);
        return savingsGoalRepository.save(goal);
    }

    public SavingsGoal addToGoal(Long id, double amount, String username) {
        SavingsGoal goal = savingsGoalRepository.findByIdAndUsername(id, username)
                .orElseThrow(() -> new SavingsGoalNotFoundException(id));
        goal.setCurrentAmount(goal.getCurrentAmount() + amount);
        boolean completed = checkGoalProgress(goal);
        goal.setCompleted(completed);

        return savingsGoalRepository.save(goal);
    }

    public List<SavingsGoal> getUserSavingsGoals(String username) {
        return savingsGoalRepository.findByUsername(username);
    }

    public Optional<SavingsGoal> getSavingsGoalById(Long id) {
        return savingsGoalRepository.findById(id);
    }

    public void deleteSavingsGoal(Long id) {
        savingsGoalRepository.deleteById(id);
    }

    private boolean checkGoalProgress(SavingsGoal goal) {
        double progress = (goal.getCurrentAmount() / goal.getTargetAmount()) * 100;

        if (progress >= 90 && progress < 100) {
            notificationService.createNotification(goal.getUsername(),
                    "You are close to reaching your savings goal for " + goal.getName() + "!");
        } else if (progress >= 100) {
            notificationService.createNotification(goal.getUsername(),
                    "Congratulations! You've reached your savings goal for " + goal.getName() + "!");
            return true;
        } else if (goal.getCurrentAmount() < 0) {
            notificationService.createNotification(goal.getUsername(),
                    "Warning! You've overspent your savings goal for " + goal.getName() + "!");
        }
        return false;
    }
}