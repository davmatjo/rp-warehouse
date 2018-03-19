package rp.warehouse.pc.data.robot;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import rp.warehouse.pc.data.robot.utils.RewardCounter;

public class RewardCounterTest {
    
    @Test
    public void rewardTestInitiallyZero() {
        Assertions.assertTrue(RewardCounter.getPointsEarned() == 0);
        
    }
    
    @Test
    public void rewardAddingPoints() {
        RewardCounter.addReward(0.9F);
        Assertions.assertTrue(RewardCounter.getPointsEarned() == 0.9f);
        RewardCounter.addReward(0.2F);
        Assertions.assertTrue(RewardCounter.getPointsEarned() == 1.1f);
    }

}
