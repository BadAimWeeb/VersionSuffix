package me.BadAimWeeb.VersionSuffix;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.nametagedit.plugin.NametagEdit;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;
import org.geysermc.floodgate.FloodgateAPI;

import net.milkbowl.vault.chat.Chat;

public class App extends JavaPlugin {
    private static Chat chat = null;

    @Override
    public void onEnable() {
        setupChat();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EventListener(getLogger(), chat), this);

        // Get every player and applies version suffix
        OfflinePlayer[] plist = getServer().getOfflinePlayers();
        for (OfflinePlayer p : plist) {
            if (p.isOnline()) {
                updateNametag(Bukkit.getPlayerExact(p.getName()), true, false);
                updateNametag(Bukkit.getPlayerExact(p.getName()), true, true);
            }
        }
    };

    @Override
    public void onDisable() {};

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
            getLogger().info(p.getName() + " (UUID " + p.getUniqueId().toString() + ") - Version: " + version);

        String s = " §b§l[" + version + "]";

        // chat suffix...
        if (isChatSuffix) {
            chat.setPlayerSuffix(null, p, s);
        } else {
            NametagEdit.getApi().setSuffix(p, s);
        }
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }
}
