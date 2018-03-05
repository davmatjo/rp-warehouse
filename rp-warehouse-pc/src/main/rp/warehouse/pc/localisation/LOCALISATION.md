# Localisation

## Approach

The `Localisation` interface has a method `Localisation.getPosition()`, which returns a `RobotLocation` representing the estimated location of the robot.

In order to establish this position, the current approach for the localisation implementation will record ranges, storing the possible positions by comparing the ranges against the stored map of the warehouse. The robot will move, record the ranges again and remove any points from the current possible positions by seeing which ones would be possible to exist given the previous position and the movement of which has just occured.

This process will repeat continuously until there is only 1 possible position to exist, or until a set threshold of repetitions has been met.

## Assumptions

These assumptions are subject to change as the complexity of the localisation implementation increases.

1. All robots initially face **north** *(Relative to the `GridMap` representation of the warehouse)*

## Requirements

1. **Movement** (forward) of the robot
2. **Rotation** of the robot
3. **Ranges** read from the sensors of the robot
 - *Preferably some code on the robot to take a burst of readings, then return the median*

The objective for this is to be able to move the robot around the co-ordinates so that the ranges can be recorded at different points.