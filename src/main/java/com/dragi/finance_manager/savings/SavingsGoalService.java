package com.dragi.finance_manager.savings;

import com.dragi.finance_manager.util.HelperUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;

    public SavingsGoalService(SavingsGoalRepository savingsGoalRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
    }

    public SavingsGoal saveOrUpdateGoal(SavingsGoal goal) {
        goal.setUsername(HelperUtils.getAuthenticatedUsername());
        SavingsGoal savedGoal = savingsGoalRepository.save(goal);
        checkGoalProgress(savedGoal);
        return savedGoal;
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

    public void checkGoalProgress(SavingsGoal goal) {
        double progress = (goal.getCurrentAmount() / goal.getTargetAmount()) * 100;

        if (progress >= 90 && progress < 100) {
            triggerAlert(goal, "You are close to reaching your savings goal!");
        } else if (progress >= 100) {
            triggerAlert(goal, "Congratulations! You've reached your savings goal!");
        } else if (goal.getCurrentAmount() < 0) {
            triggerAlert(goal, "Warning! You have overspent your goal.");
        }
    }

    private void triggerAlert(SavingsGoal goal, String message) {
        System.out.println("Notification: " + message + " for goal: " + goal.getName());
    }
}