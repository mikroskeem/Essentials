package com.earth2me.essentials.commands;

import com.earth2me.essentials.User;
import com.earth2me.essentials.craftbukkit.InventoryWorkaround;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collections;
import java.util.List;

import static com.earth2me.essentials.I18n.tl;


public class Commandhat extends EssentialsCommand {
    public Commandhat() {
        super("hat");
    }

    @Override
    protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length > 0 && (args[0].contains("rem") || args[0].contains("off") || args[0].equalsIgnoreCase("0"))) {
            final PlayerInventory inv = user.getBase().getInventory();
            final ItemStack head = inv.getHelmet();
            if (head == null || head.getType() == Material.AIR) {
                user.sendMessage(tl("hatEmpty"));
            } else {
                // mikroskeem start - Don't allow removing helmets with 'Curse of Binding' enchantment
                if (head.containsEnchantment(org.bukkit.enchantments.Enchantment.BINDING_CURSE)) {
                    user.sendMessage(tl("hatCursed"));
                    return;
                }
                // mikroskeem end
                final ItemStack air = new ItemStack(Material.AIR);
                inv.setHelmet(air);
                InventoryWorkaround.addItems(user.getBase().getInventory(), head);
                user.sendMessage(tl("hatRemoved"));
            }
        } else {
            final ItemStack hand = user.getItemInHand();
            // mikroskeem start - Don't allow removing helmets with 'Curse of Binding' enchantment
            ItemStack headItem = user.getBase().getInventory().getHelmet();
            if ((headItem != null && headItem.getType() != Material.AIR) && headItem.containsEnchantment(org.bukkit.enchantments.Enchantment.BINDING_CURSE)) {
                user.sendMessage(tl("hatCursed"));
                return;
            }
            // mikroskeem end
            if (hand != null && hand.getType() != Material.AIR) {
                if (hand.getType().getMaxDurability() == 0) {
                    final PlayerInventory inv = user.getBase().getInventory();
                    final ItemStack head = inv.getHelmet();
                    inv.setHelmet(hand);
                    inv.setItemInHand(head);
                    user.sendMessage(tl("hatPlaced"));
                } else {
                    user.sendMessage(tl("hatArmor"));
                }
            } else {
                user.sendMessage(tl("hatFail"));
            }
        }
    }

    @Override
    protected List<String> getTabCompleteOptions(final Server server, final User user, final String commandLabel, final String[] args) {
        if (args.length == 1) {
            return Lists.newArrayList("remove", "wear"); // "wear" isn't real
        } else {
            return Collections.emptyList();
        }
    }
}