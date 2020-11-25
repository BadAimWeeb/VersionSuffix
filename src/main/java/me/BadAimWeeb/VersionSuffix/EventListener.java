package me.BadAimWeeb.VersionSuffix;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;

import org.geysermc.floodgate.FloodgateAPI;

import java.util.logging.Logger;

import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.events.NametagEvent;
import com.nametagedit.plugin.api.events.NametagFirstLoadedEvent;

import net.milkbowl.vault.chat.Chat;

public class EventListener implements Listener {
    private Logger logger;
    private Chat chat;

    public EventListener(Logger l, Chat c) {
        logger = l;
        chat = c;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateNametag(event.getPlayer(), false, true);
    }

    @EventHandler
    public void onNametagFirstLoadedEvent(NametagFirstLoadedEvent event) {
        updateNametag(event.getPlayer(), true, false);
    }

    @EventHandler
    public void onNametagEvent(NametagEvent event) {
        if ((event.getChangeType() == NametagEvent.ChangeType.SUFFIX
                || event.getChangeType() == NametagEvent.ChangeType.PREFIX_AND_SUFFIX)
                && event.getChangeReason() != NametagEvent.ChangeReason.API)
            updateNametag(Bukkit.getPlayerExact(event.getPlayer()), false, false);
    }

    private void updateNametag(Player p, boolean log, boolean isChatSuffix) {
        String version;

        // Checking if player joined using MCBE
        boolean mcbe = FloodgateAPI.isBedrockPlayer(p);
        if (mcbe) {
            version = "Bedrock";
        } else {
            // Trying to get player's version
            ProtocolVersion pr = ProtocolSupportAPI.getProtocolVersion(p);
            version = pr.getName();
        }

        // Log it
        if (log)
            logger.info(p.getName() + " (UUID " + p.getUniqueId().toString() + ") - Version: " + version);

        String s = " §b§l[" + version + "]";

        // chat suffix...
        if (isChatSuffix) {
            chat.setPlayerSuffix(null, p, s);
        } else {
            NametagEdit.getApi().setSuffix(p, s);
        }
    }
}
