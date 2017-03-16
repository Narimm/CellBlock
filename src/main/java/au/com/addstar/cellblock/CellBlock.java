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

package au.com.addstar.cellblock;

import au.com.addstar.cellblock.listeners.DoorEventListener;
import au.com.addstar.cellblock.managers.CellAreaManager;
import au.com.addstar.cellblock.managers.CellDoorManager;
import au.com.addstar.cellblock.managers.CellManager;
import com.intellectualcrafters.plot.api.PlotAPI;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created for use for the Add5tar MC Minecraft server
 * Created by benjamincharlton on 11/03/2017.
 */

public class CellBlock extends JavaPlugin {

    private final CellBlockCommandExecutor commandExecutor = new CellBlockCommandExecutor(this);
    private final DoorEventListener eventListener = new DoorEventListener(this);
    public Config config;
    public PlotAPI p2api;
    private CellDoorManager cellDoorManager;
    private CellAreaManager cellAreaManager;
    private CellManager cellManager;

    public CellManager getCellManager() {
        return cellManager;
    }

    public CellDoorManager getCellDoorManager() {
        return cellDoorManager;
    }

    public CellAreaManager getCellAreaManager() {
        return cellAreaManager;
    }

    public void onDisable() {
        PluginManager pm = this.getServer().getPluginManager();
        cellManager.saveCells();
        cellDoorManager.save();
        cellAreaManager.saveCellAreas();
        config.save();
        p2api = null;
        this.getCommand("cellblock").setExecutor(null);
    }

    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        p2api = new PlotAPI();
        File file = new File("config.yml");
        config = new Config(file);
        config.load();
        cellDoorManager = new CellDoorManager(this);
        cellAreaManager = new CellAreaManager(this);
        cellManager = new CellManager(this);
        pm.registerEvents(eventListener, this);
        this.getCommand("cellblock").setExecutor(commandExecutor);
    }


}
