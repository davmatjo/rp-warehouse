package rp.warehouse.pc.management;

import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.LineMap;
import rp.robotics.visualisation.GridMapVisualisation;

public class MapVisualisation extends GridMapVisualisation{
    public MapVisualisation(IGridMap _gridMap, LineMap _lineMap, float scaleFactor) {
        super(_gridMap, _lineMap, scaleFactor);
    }
}
