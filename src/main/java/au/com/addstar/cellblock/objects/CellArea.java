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

import org.bukkit.Location;
import org.bukkit.Material;

import java.io.Serializable;

/**
 * Created for the AddstarMC Project.
 * Created by Narimm on 8/03/2017.
 */
public class CellArea implements Serializable {

    private final String plotAreaID;
    private final String schematicName;
    private final int maxPlotSize;
    private Material cellFiller;
    private Material cellWalls;
    private Location respawn;

    public CellArea(String plotAreaId, int maxSize, String schematicName) {
        this.plotAreaID = plotAreaId;
        this.maxPlotSize = maxSize;
        this.schematicName = schematicName;
    }

    public Location getRespawn() {
        return respawn;
    }

    public void setRespawn(Location respawn) {
        this.respawn = respawn;
    }

    public Material getCellFiller() {
        return cellFiller;
    }

    public void setCellFiller(Material cellFiller) {
        this.cellFiller = cellFiller;
    }

    public Material getCellWalls() {
        return cellWalls;
    }

    public void setCellWalls(Material cellWalls) {
        this.cellWalls = cellWalls;
    }

    public String getSchematicName() {
        return schematicName;
    }

    public String getPlotAreaID() {
        return plotAreaID;
    }

    public int getMaxPlotSize() {
        return maxPlotSize;
    }

    @Override
    public String toString() {
        return plotAreaID + " Plot Size:" + maxPlotSize + "Schematic: " + schematicName;
    }
}
