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

package au.com.addstar.cellblock.listeners;

import au.com.addstar.cellblock.CellBlock;

import au.com.addstar.cellblock.objects.CellDoor;
import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.PlotPlayer;
import com.plotsquared.bukkit.object.BukkitPlayer;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DoorEventListener implements Listener {

    private final CellBlock plugin;

    public DoorEventListener(CellBlock plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onDoorOpen(PlayerInteractEvent event) {
        if (plugin.getCellDoorManager().isCellDoor(event.getClickedBlock())) {
            CellDoor door = plugin.getCellDoorManager().getCellDoor(event.getClickedBlock());
            if (door != null) {
                Location loc = plugin.getCellManager().getCellSpawn(event.getPlayer(), door.getCellArea());
                PlotPlayer player = new BukkitPlayer(event.getPlayer());
                if (loc != null) {
                    if (player.canTeleport(loc)) {
                        player.teleport(loc);
                    }
                }
                player.sendMessage("We could not find a cell for you...you are homeless...mine some more.");
                event.setCancelled(true);
            }
        }
    }
}
