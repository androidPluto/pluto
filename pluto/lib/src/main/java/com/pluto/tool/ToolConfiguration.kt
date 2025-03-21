package com.pluto.tool

import androidx.annotation.DrawableRes
import com.pluto.utilities.list.ListItem

/**
 * Data class containing configuration for a tool.
 *
 * This class defines the visual properties of a tool, such as its name and icon.
 * It also generates a unique identifier based on the name.
 *
 * @property name The display name of the tool
 * @property icon The resource ID of the tool's icon
 */
internal data class ToolConfiguration(
    val name: String,
    @DrawableRes val icon: Int
) : ListItem() {
    /**
     * Unique identifier for the tool, generated from the name.
     *
     * The identifier is created by converting the name to lowercase and
     * replacing spaces with underscores.
     */
    val identifier = name.lowercase().replace(" ", "_", true)

    /**
     * Compares this tool configuration with another object for equality.
     *
     * Tool configurations are considered equal if they have the same identifier.
     *
     * @param other The object to compare with
     * @return True if the objects are equal, false otherwise
     */
    override fun equals(other: Any?): Boolean {
        return other is ToolConfiguration && identifier == other.identifier
    }

    /**
     * Returns a hash code value for this tool configuration.
     *
     * The hash code is based on the identifier to ensure consistency with equals.
     *
     * @return The hash code value
     */
    override fun hashCode(): Int {
        return identifier.hashCode()
    }
}
