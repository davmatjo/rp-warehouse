package rp.warehouse.pc.data;

import org.junit.Test;

import rp.warehouse.pc.data.robot.RobotLocation;

import org.junit.Assert;

import java.util.HashSet;

public class LocationTest {

    @Test
    public void testEquality() {
        Assert.assertEquals(new Location(10, 25), new Location(10, 25));
        Assert.assertNotEquals(new Location(10, 25), new Location(11, 25));
        Assert.assertEquals(new RobotLocation(22, 22, 1), new RobotLocation(22, 22, 3));
    }

    @Test
    public void testHashCodeEquals() {
        Assert.assertEquals(new Location(11, 26).hashCode(), new Location(11, 26).hashCode());
        Assert.assertEquals(new RobotLocation(11, 26, 1).hashCode(), new RobotLocation(11, 26, 1).hashCode());
    }

    @Test
    public void hashSetWithLocationsWorksProperly() {
        HashSet<Location> hashSet = new HashSet<>();
        hashSet.add(new Location(27, 1));
        Assert.assertTrue(hashSet.contains(new Location(27, 1)));
    }
}