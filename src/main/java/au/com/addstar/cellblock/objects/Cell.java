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

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.PlotId;
import com.plotsquared.bukkit.util.BukkitUtil;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created for the AddstarMC Project.
 * Created by Narimm on 8/03/2017.
 */
public class Cell implements Serializable {

    private final PlotId plotId;
    private final org.bukkit.Location doorLocation;
    private final UUID uuid;
    private final CellArea area;
    private Integer size;

    public Cell(CellArea area, PlotId plotId, Integer size, Location door) {
        this(UUID.randomUUID(), area, plotId, size, door);
    }

    private Cell(UUID uuid, CellArea area, PlotId plotId, Integer size, Location door) {
        this.uuid = uuid;
        this.area = area;
        this.plotId = plotId;
        this.size = size;
        this.doorLocation = BukkitUtil.getLocation(door);
    }

    public PlotId getPlot() {
        return plotId;
    }

    public CellArea getArea() {
        return area;
    }

    public Location getPlotDoor() {
        return BukkitUtil.getLocation(doorLocation);
    }

    public org.bukkit.Location getBukkitDoor() {
        return doorLocation;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Area: " + area.getPlotAreaID() + " PlotId: " + plotId.toString() + " World: " + doorLocation.getWorld().getName() + " Size: " + size;
    }


}
