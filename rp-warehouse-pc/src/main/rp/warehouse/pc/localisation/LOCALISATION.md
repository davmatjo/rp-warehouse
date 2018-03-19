# Localisation

## Usage

An instance of the `Localisation` class is needed. From there, the `Localisation.getPosition()` method can be called, which will run for a somewhat extended period of time. Once the robots are learnt their positions in the warehouse, the `RobotLocation` representation of the robot will be returned. For example:

```java
Localisation local = new Localiser(new Communication(ID, name, this));
RobotLocation location = local.getLocation();
```

## Approach

The `Localisation` interface has a method `Localisation.getPosition()`, which returns a `RobotLocation` representing the estimated location of the robot.

In order to establish the position of the robot, the current approach for the localisation implementation will record ranges in blocks of 1 range - i.e. booleans determining whether it is possible to move in a given direction - storing the possible positions by comparing the ranges against the stored map of the warehouse. The robot will move, record the ranges again and remove any points from the current possible positions by seeing which ones would be possible to exist given the previous position and the movement of which has just occurred.

This process will repeat continuously until there is only 1 possible position to exist, or until a set threshold of repetitions has been met.

As the initial heading is unknown, all of the possible positions have to be calculated 4 times simultaneously. This is done so that the robot can assume its heading as all 4 directions until it finds one of which is correct - 1 position found. This process is aided by the `LocalisationCollection` class, which essentially stores an individual list of possible points, the assumed starting heading, and the current heading based on the assumed starting one. All of this information is then used to return the `RobotLocation` of the robot once it has filtered the positions down to a final one.

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

The up, right, down and left ranges are then taken using a heading of 90, 0, -90 and 180 respectively, using the method `world.rangeToObstacleFromGridPosition(x, y, heading)`.

These are then used to create a `Ranges ranges = new Ranges(up, right, down, left, Ranges.virtualConverter)`, which is then used in the following to map the ranges to the given point:

```java
warehouseMap.put(ranges, point);
```

### Converters

There are two types of converters used for localisation, **virtual** and **physical**. These are used to convert the ranges in the simulated and real worlds repsectively.

```
rawRanges -> converter -> Ranges object
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