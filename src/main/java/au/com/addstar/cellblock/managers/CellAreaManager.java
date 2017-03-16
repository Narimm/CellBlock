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

package au.com.addstar.cellblock.managers;

import au.com.addstar.cellblock.CellBlock;

import au.com.addstar.cellblock.objects.CellArea;
import au.com.addstar.cellblock.utilities.Utilities;
import com.google.gson.reflect.TypeToken;
import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.PlotArea;
import com.plotsquared.bukkit.util.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import javax.annotation.Nullable;
import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Created for use for the Add5tar MC Minecraft server
 * Created by benjamincharlton on 11/03/2017.
 */
public class CellAreaManager {

    private final CellBlock plugin;
    private final ArrayList<CellArea> cellAreas;
    private final TypeToken<ArrayList<CellArea>> type = new TypeToken<ArrayList<CellArea>>() {
    };
    private final Map<String, PlotArea> plotAreas;

    public CellAreaManager(CellBlock plugin) {
        this.plugin = plugin;
        plotAreas = new HashMap<>();
        cellAreas = new ArrayList<>();
        setup();
    }

    @Nullable


    public CellArea findCellArea(org.bukkit.Location location) {
        Location ploc = BukkitUtil.getLocation(location);
        if (ploc.isPlotArea()) {
            return getCellArea(ploc.getPlotArea().id);
        }
        return null;
    }


    private void setup() {
        loadCellAreas();
        List<World> worlds = Bukkit.getWorlds();
        boolean exists = false;
        for (World world : worlds) {
            if (Objects.equals(world.getName(), plugin.config.cellWorldName)) {
                exists = true;
                plugin.p2api.getPlotSquared().hasPlotArea(plugin.config.cellWorldName);
                Set<PlotArea> areas = plugin.p2api.getPlotAreas(world);
                boolean cellAreaExists = false;
                for (PlotArea plotarea : areas) {
                    for (CellArea cellAreas : cellAreas) {
                        if (Objects.equals(cellAreas.getPlotAreaID(), plotarea.id)) {
                            cellAreaExists = true;
                            plotAreas.put(cellAreas.getPlotAreaID(), plotarea);
                            break;
                        }
                    }
                    if (cellAreaExists) {
                        break;
                    } else {
                        plotAreas.put(plotarea.id, plotarea);
                        cellAreas.add(new CellArea(plotarea.id, plugin.config.plotMaxSize, plugin.config.schematicName));
                        plugin.getLogger().log(Level.INFO, plotarea.id + " has been added as a new CellBlock Area.");
                    }
                }
            }
            if (!exists) {
                //todo
                plugin.getLogger().info("You will need to manual create a Plot World with the name " + plugin.config.cellWorldName);
                plugin.getLogger().info("It should be a normal plot world and you must set the config option in the " +
                        "file to the plot size. Currently; " + plugin.config.plotMaxSize);
            }
        }
    }

    public void saveCellAreas() {
        File parent = new File("saves");
        File file = new File(parent, "cellAreas.json");
        Utilities.saveFile(file, cellAreas, type);
        plugin.getLogger().info("Door Save Completed...");


    }

    private void loadCellAreas() {
        File parent = new File("saves");
        File file = new File(parent, "cellAreas.json");
        if (!parent.exists()) return;
        List<CellArea> loadedCellAreas = Utilities.loadFile(file, type);
        int added = 0;
        if (loadedCellAreas != null && loadedCellAreas.size() > 0) {
            if (cellAreas.size() == 0) {
                cellAreas.addAll(loadedCellAreas);
                added = cellAreas.size();
            } else {
                added = cellAreas.size();
                boolean exists = false;
                for (CellArea area : loadedCellAreas) {
                    for (CellArea existing : cellAreas) {
                        if (area.getPlotAreaID().equals(existing.getPlotAreaID())) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) break;
                    cellAreas.add(area);
                    added++;
                }
            }
        }
        plugin.getLogger().info("Loading CellAreas Completed. Loaded: " + added);
    }

    /**
     * Return the plot area
     *
     * @param id the CellID/PlotId
     * @return PlotArea
     */
    PlotArea getPlotArea(String id) {
        if (plotAreas.containsKey(id)) {
            return plotAreas.get(id);
        }
        return null;
    }

    /**
     * Finds the Cell Area base of the plotArea.id
     *
     * @param id the plotId
     * @return CellArea
     */

    public CellArea getCellArea(String id) {
        for (CellArea area : cellAreas) {
            if (id.equals(area.getPlotAreaID())) {
                return area;
            }
        }
        return null;
    }

    public void listCellAreas(CommandSender sender) {
        sender.sendMessage("----- Area List -------");
        for (CellArea area : cellAreas) {
            sender.sendMessage(area.toString());
        }
    }

    public void setCellAreaRespawn(CommandSender sender, CellArea area, org.bukkit.Location location) {
        area.setRespawn(location);
        sender.sendMessage("Respawn for area: " + area.getPlotAreaID() + " has been set to " + area.getRespawn().toString());
    }


}
