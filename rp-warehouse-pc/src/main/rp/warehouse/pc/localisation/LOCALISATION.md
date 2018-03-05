
# Localisation

## Usage

An instance of the `Localisation` class is needed. From there, the `Localisation.getPosition()` method can be called, which will run for a somewhat extended period of time. Once the robots are learnt their positions in the warehouse, the `RobotLocation` representation of the robot will be returned. For example:

```java
Localisation local = new LocalisationImpl();
RobotLocation estimatedLocation = local.getLocation();
```

## Approach

The `Localisation` interface has a method `Localisation.getPosition()`, which returns a `RobotLocation` representing the estimated location of the robot.

In order to establish this position, the current approach for the localisation implementation will record ranges, storing the possible positions by comparing the ranges against the stored map of the warehouse. The robot will move, record the ranges again and remove any points from the current possible positions by seeing which ones would be possible to exist given the previous position and the movement of which has just occured.

This process will repeat continuously until there is only 1 possible position to exist, or until a set threshold of repetitions has been met.

## Generating Ranges

Initialising a `Localisation` class generates an `ArrayList<Point> blockedPoints` using the `Warehouse.getBlockedLocations()` method.

For the localisation to work, the ranges recorded at every point need to be generated beforehand so that the ranges recorded from the robot can be looked up and matched against the known map.

To do this, a `GridMap world` is stored in the class, using the `Warehouse.build()` method to populate it. This then allows for the `world.getXSize()` and `world.getYSize()` methods to be used to get the max X and Y co-ordinates of the world, which can then be used within a for loop to iterate over all of the co-ordinates:

```java
for (int x = 0; x < world.getXSize(); x++) {
	for (int y = 0; y < world.getYSize(); y++) {
		// Do stuff...
	}
}
```

At the start of the iteration of the inner loop, a `Point point = new Point(x, y)` is created. This is then checked to see whether it is **not** contained within `blockedPoints` before proceeding.

<<<<<<< HEAD
The up, right, down and left ranges are then taken using a heading of 0, 90, 180 and 270 respectively, using the method `(int) world.rangeToObstacleFromGridPosition(x, y, heading)`. It is casted to an `int` so that it removes the floating point, meaning that it clamps it to the co-ordinate system used by the `GridMap`.

These are then used to create a `Ranges ranges = new Ranges(up, right, down, left)`, which is then used in the following to map the ranges to the given point:
=======
The north, east, south and west ranges are then taken using a heading of 0, 90, 180 and 270 respectively, using the method `(int) world.rangeToObstacleFromGridPosition(x, y, heading)`. It is casted to an `int` so that it removes the floating point, meaning that it clamps it to the co-ordinate system used by the `GridMap`.

These are then used to create a `new Ranges(north, east, south, west)`, which is then used in the following to map the ranges to the given point:
>>>>>>> 4bed532a67731429eddc7f20eb879635ec6bceb7

```java
warehouseMap.put(ranges, point);
```

## Filtering

When a new set of ranges has been recorded, `next`, the previous set of ranges `initial` is compared to see which points could still be feasible given the change in location from `initial` to `next`. This change in location is stored as a `Point change`, and is one of the following:

|UP|RIGHT|DOWN|LEFT|
|--|--|--|--|
|`new Point(0, 1)`|`new Point(1, 0)`|`new Point(0, -1)`|`new Point(-1, 0)`|

On top of this, all points in `next` are checked to see if they are contained within `blockedPoints` so that only possible locations are kept within the list of points.

The line responsible for this filtering is the following:

```java
next.removeIf(p -> !initial.contains(p.subtract(change)) || blockedPoints.contains(p));
```

## Assumptions

These assumptions are subject to change as the complexity of the localisation implementation increases.

1. All robots initially face **UP** *(Relative to the `GridMap` representation of the warehouse)*

## Requirements

1. **Movement** (forward) of the robot
2. **Rotation** of the robot
3. **Ranges** read from the sensors of the robot
 - *Preferably some code on the robot to take a burst of readings, then return the median*

The objective for this is to be able to move the robot around the co-ordinates so that the ranges can be recorded at different points.