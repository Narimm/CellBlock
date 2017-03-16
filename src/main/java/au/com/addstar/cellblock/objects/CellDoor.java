/*
 * Copyright (c) 2017
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package au.com.addstar.cellblock.objects;

import au.com.addstar.cellblock.CellBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The Cell door is a door that is in the main prison world that will send a player to their cell.... if they have one
 * or if they dont it will check permissions and create a new sell based on the permission.  Additionnally it will expand
 * a players cell if they have  a new permission that is bigger than the current cell size....expansion is done by replacing
 * surrouding walls with cracked cobblestone
 * <p>
 * Created for the AddstarMC Project.
 * Created by Narimm on 8/03/2017.
 */
public class CellDoor implements Serializable {

    private List<Material> legalDoors = new ArrayList<>();
    private final UUID uuid;
    private final Location doorBlockLocation;
    private final Location topDoorBlockLocation;
    private final CellArea cellArea;
    private final Material doorType;

    private CellDoor(UUID uuid, Location doorBlockLocation, CellArea cellArea, Material doorType) {
        this.uuid = uuid;
        this.doorBlockLocation = doorBlockLocation;
        topDoorBlockLocation = doorBlockLocation.getBlock().getRelative(BlockFace.UP).getLocation();
        this.cellArea = cellArea;
        this.doorType = doorType;
    }

    public CellDoor(Location doorBlockLocation, CellArea world, Material material) {
        this(UUID.randomUUID(), doorBlockLocation, world, material);
    }

    public UUID getUuid() {
        return uuid;
    }

    public Material getDoorType() {
        return doorType;
    }

    public Location getDoorBlockLocation() {
        return doorBlockLocation;
    }

    public Location getTopDoorBlockLocation() {
        return topDoorBlockLocation;
    }

    public CellArea getCellArea() {
        return cellArea;
    }

    public String toString() {
        return "Location:" + doorBlockLocation.toString() + " Linked Area: " + cellArea.getPlotAreaID();
    }

    public void setBlockMeta(CellBlock plugin) {
        MetadataValue value = new FixedMetadataValue(plugin, uuid.toString());
        doorBlockLocation.getBlock().setMetadata("celldoor", value);
        topDoorBlockLocation.getBlock().setMetadata("celldoor", value);
    }


}
