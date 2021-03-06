package it.tigierrei.fwparty.listener;

import it.tigierrei.fwparty.FWParty;
import it.tigierrei.fwparty.party.PartyManager;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.message.MessageChannelEvent;

public class PlayerListener {

    private final FWParty plugin;

    public PlayerListener(FWParty plugin) {
        this.plugin = plugin;
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onEntityDamaged(DamageEntityEvent event) {
        Object root = event.getCause().root();
        if(root instanceof EntityDamageSource && event.getTargetEntity() instanceof Player){
            if(root instanceof IndirectEntityDamageSource) {
                IndirectEntityDamageSource src = (IndirectEntityDamageSource) root;
                Entity indirectSource = src.getIndirectSource();
                if(indirectSource instanceof Player){
                    Player damager = (Player) indirectSource;
                    Player target = (Player) event.getTargetEntity();
                    if(plugin.getPartyManager().areInSameParty(damager, target)){
                        event.setCancelled(true);
                    }
                }
            }else{
                EntityDamageSource src = (EntityDamageSource)root;
                if (src.getSource() instanceof Player) {
                    Player player = (Player) src.getSource();
                    Player target = (Player) event.getTargetEntity();
                    if (plugin.getPartyManager().areInSameParty(player, target)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @Listener(order = Order.FIRST, beforeModifications = true)
    public void onPlayerChat(MessageChannelEvent.Chat event) {
        if(event.getSource() instanceof Player){
            Player player = (Player)event.getSource();
            String message = event.getRawMessage().toPlain();
            PartyManager partyManager = plugin.getPartyManager();
            if (partyManager.isPlayerChatting(player.getUniqueId())) {
                event.setCancelled(true);
                partyManager.sendMessageToPartyMembers(partyManager.getPlayerParty(player.getUniqueId()).getLeader(),"&2[PARTY] &a" + player.getName() + ": " + message);
            }
        }
    }
}
