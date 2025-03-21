package com.pluto.tool.modules.ruler.internal

/**
 * Data class that represents a pair of x and y coordinates.
 *
 * This class is used throughout the ruler tool to track various coordinate points
 * such as touch positions, click positions, and movement positions.
 */
internal class CoordinatePair {
    /** The x-coordinate value */
    var x = 0f

    /** The y-coordinate value */
    var y = 0f
}

/**
 * Data class that represents the dimensions of the screen.
 *
 * This class is used to store the height and width of the screen in density-independent pixels (dp),
 * which is useful for calculating ruler measurements that are consistent across different device densities.
 */
internal class ScreenMeasurement {
    /** The height of the screen in dp */
    var height = 0

    /** The width of the screen in dp */
    var width = 0
}
