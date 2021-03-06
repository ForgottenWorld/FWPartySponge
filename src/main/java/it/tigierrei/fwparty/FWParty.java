package it.tigierrei.fwparty;

import com.google.inject.Inject;
import it.tigierrei.fwparty.command.PartyCommands;
import it.tigierrei.fwparty.config.ConfigManager;
import it.tigierrei.fwparty.config.ConfigValues;
import it.tigierrei.fwparty.listener.PlayerListener;
import it.tigierrei.fwparty.party.PartyManager;
import ninja.leaping.configurate.objectmapping.GuiceObjectMapperFactory;
import org.slf4j.Logger;
import org.spongepowered.api.event.world.SaveWorldEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;



import java.io.File;

@Plugin(
        id = "fwparty",
        name = "FWParty",
        description = "A simple party plugin made by Tigierrei for ForgottenWorld",
        url = "https://forgottenworld.it",
        authors = {
                "Tigierrei",
                "Markus_27"
        }
)
public class FWParty {

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    @Inject
    private GuiceObjectMapperFactory factory;

    private PartyManager partyManager;
    private PartyCommands commands;
    private ConfigManager configManager;
    private ConfigValues configValues;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        configManager = new ConfigManager(this, configDir);
        partyManager = configManager.loadParties();
        configValues = configManager.loadConfig();
        commands = new PartyCommands(this);
        commands.registerCommands();
        Sponge.getEventManager().registerListeners(this, new PlayerListener(this));
    }

    @Listener
    public void onServerReload(GameReloadEvent event){
        configValues = configManager.loadConfig();
        configManager.saveParties(partyManager);
    }

    @Listener
    public void onWorldSave(SaveWorldEvent event){
        configManager.saveParties(partyManager);
    }

    @Listener
    public void onServerStopping(GameStoppingServerEvent event){
        configManager.saveParties(partyManager);
    }

    public Logger getLogger() {
        return logger;
    }

    public File getConfigDir() {
        return configDir;
    }

    public GuiceObjectMapperFactory getFactory() {
        return factory;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public ConfigValues getConfigValues() {
        return configValues;
    }
}
