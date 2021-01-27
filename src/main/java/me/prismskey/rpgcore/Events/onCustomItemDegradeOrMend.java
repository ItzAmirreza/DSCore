package me.prismskey.rpgcore.Events;

import me.prismskey.rpgcore.Utils.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Random;

public class onCustomItemDegradeOrMend implements Listener {

    private final Random rand;

    public onCustomItemDegradeOrMend() {
        rand = new Random();
    }

    @EventHandler
    public void onItemDegrade(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();

        NBTItem nbti = new NBTItem(item);
        if(isCustomDegradableItem(nbti)) {

            ItemMeta meta = item.getItemMeta();
            ArrayList<String> lore = (ArrayList<String>) meta.getLore();
            int currentDurability = Integer.MIN_VALUE;
            if(lore == null) {
                lore = new ArrayList<>();
            }
            for(int i = 0; i < lore.size(); i++) {
                String s = lore.get(i);
                //Durability: x/y
                if(s.contains("Durability: ")) {
                    currentDurability = Integer.parseInt(s.split(" ")[1].split("/")[0]);

                    if(item.containsEnchantment(Enchantment.DURABILITY)) {
                        int level = item.getEnchantmentLevel(Enchantment.DURABILITY);
                        double percent = (100 / (level + 1));

                        if(rand.nextInt(100) <= percent) {
                            currentDurability = currentDurability - 1;
                            currentDurability = Math.max(0, currentDurability);
                        }
                    } else {
                        currentDurability = currentDurability - 1;
                        currentDurability = Math.max(0, currentDurability);
                    }
                    lore.set(i, ChatColor.RESET + "Durability: " + currentDurability + "/" + nbti.getInteger("maxDurability"));
                }
            }
            if(currentDurability == Integer.MIN_VALUE) {
                lore.add(ChatColor.RESET + "Durability: " + nbti.getInteger("maxDurability") + "/" + nbti.getInteger("maxDurability"));
                currentDurability = nbti.getInteger("maxDurability");

            } else {

                Damageable damageable = (Damageable) meta;
                double vanillaMaxDurability = item.getType().getMaxDurability();
                double customDurability = currentDurability;
                double customMaxDurability = nbti.getInteger("maxDurability");
                double newDamage =  vanillaMaxDurability - (vanillaMaxDurability * (customDurability / customMaxDurability));

                damageable.setDamage((int) newDamage);

                meta = (ItemMeta) damageable;
            }

            meta.setLore(lore);
            item.setItemMeta(meta);

            if(currentDurability > 0) {
                event.setCancelled(true);
            }

            //else if not a custom item
        } else {
            ItemMeta meta = item.getItemMeta();
            ArrayList<String> lore = (ArrayList<String>) meta.getLore();
            int currentDurability = Integer.MIN_VALUE;
            if(lore == null) {
                lore = new ArrayList<>();
            }
            for(int i = 0; i < lore.size(); i++) {
                String s = lore.get(i);
                //Durability: x/y
                if(s.contains("Durability: ")) {
                    currentDurability = Integer.parseInt(s.split(" ")[1].split("/")[0]);

                    if(item.containsEnchantment(Enchantment.DURABILITY)) {
                        int level = item.getEnchantmentLevel(Enchantment.DURABILITY);
                        double percent = (100 / (level + 1));

                        if(rand.nextInt(100) <= percent) {
                            currentDurability = currentDurability - 1;
                            currentDurability = Math.max(0, currentDurability);
                        }
                    } else {
                        currentDurability = currentDurability - 1;
                        currentDurability = Math.max(0, currentDurability);
                    }
                    lore.set(i, ChatColor.RESET + "Durability: " + currentDurability + "/" + item.getType().getMaxDurability());
                }
            }
            if(currentDurability == Integer.MIN_VALUE) {
                lore.add(ChatColor.RESET + "Durability: " + item.getType().getMaxDurability() + "/" + item.getType().getMaxDurability());
                currentDurability = item.getType().getMaxDurability();
            } else {

                Damageable damageable = (Damageable) meta;
                double vanillaMaxDurability = item.getType().getMaxDurability();
                double customDurability = currentDurability;
                double customMaxDurability = item.getType().getMaxDurability();
                double newDamage =  vanillaMaxDurability - (vanillaMaxDurability * (customDurability / customMaxDurability));

                damageable.setDamage((int) newDamage);

                meta = (ItemMeta) damageable;
            }

            meta.setLore(lore);
            item.setItemMeta(meta);

            if(currentDurability > 0) {
                event.setCancelled(true);
            }
        }


        //int inventorySlot = event.getPlayer().getInventory().first(item);
        //event.getPlayer().sendMessage("slot: " + inventorySlot);

        //NBTItem nbti = new NBTItem(item);
        /*if(nbti.hasKey("dragonstone") || nbti.hasKey("orichalcum") || nbti.hasKey("adamant") || nbti.hasKey("mithril")) {
            event.setCancelled(true);
            double currentDurability = nbti.getInteger("currentDurability");
            double maxDurability = nbti.getInteger("maxDurability");

            currentDurability--;
            nbti.setInteger("currentDurability", (int) currentDurability);
            event.getPlayer().sendMessage("current durability: " + currentDurability);
            ItemStack item2 = nbti.getItem();
            if(currentDurability <= 0) {
                item.setType(Material.AIR);
            }

            item.getItemMeta().set

            Damageable meta = (Damageable) item.getItemMeta();
            int vanillaMaxDurability = item.getType().getMaxDurability();
            double newDamage = item.getType().getMaxDurability() - (vanillaMaxDurability * (currentDurability / maxDurability));

            meta.setDamage((int) newDamage);
            item.setItemMeta((ItemMeta) meta);

            //event.getPlayer().getInventory().setItem(inventorySlot, item);

        }*/
    }

    @EventHandler
    public void onItemMend(PlayerItemMendEvent event) {
        ItemStack item = event.getItem();

        NBTItem nbti = new NBTItem(item);
        if (isCustomDegradableItem(nbti)) {
            ItemMeta meta = item.getItemMeta();
            ArrayList<String> lore = (ArrayList<String>) meta.getLore();
            int currentDurability = Integer.MIN_VALUE;
            if(lore == null) {
                lore = new ArrayList<>();
            }
            for(int i = 0; i < lore.size(); i++) {
                String s = lore.get(i);
                //Durability: x/y
                if(s.contains("Durability: ")) {
                    currentDurability = Integer.parseInt(s.split(" ")[1].split("/")[0]);

                    event.getPlayer().sendMessage("Repair: " + event.getRepairAmount());

                    if(event.getRepairAmount() == 1) {
                        currentDurability = nbti.getInteger("maxDurability");
                    } else {
                        int repairAmount = event.getRepairAmount() + currentDurability > nbti.getInteger("maxDurability") ?
                                currentDurability + event.getRepairAmount() - nbti.getInteger("maxDurability") :
                                event.getRepairAmount();
                        currentDurability = currentDurability + repairAmount;
                    }


                    currentDurability = Math.min(nbti.getInteger("maxDurability"), currentDurability);

                    lore.set(i, ChatColor.RESET + "Durability: " + currentDurability + "/" + nbti.getInteger("maxDurability"));
                }
            }
            if(currentDurability == Integer.MIN_VALUE) {
                lore.add(ChatColor.RESET + "Durability: " + nbti.getInteger("maxDurability") + "/" + nbti.getInteger("maxDurability"));
                //currentDurability = nbti.getInteger("maxDurability");
            } else {

                Damageable damageable = (Damageable) meta;
                double vanillaMaxDurability = item.getType().getMaxDurability();
                double customDurability = currentDurability;
                double customMaxDurability = nbti.getInteger("maxDurability");
                double newDamage =  vanillaMaxDurability - (vanillaMaxDurability * (customDurability / customMaxDurability));

                damageable.setDamage((int) newDamage);

                //damageable.setDamage((int) newDamage);
                if(damageable.getDamage() == 0 && currentDurability < nbti.getInteger("maxDurability")) {
                    damageable.setDamage(1);
                }

                meta = (ItemMeta) damageable;
            }

            meta.setLore(lore);
            item.setItemMeta(meta);


        } else {
            ItemMeta meta = item.getItemMeta();
            ArrayList<String> lore = (ArrayList<String>) meta.getLore();
            int currentDurability = Integer.MIN_VALUE;
            if(lore == null) {
                lore = new ArrayList<>();
            }
            for(int i = 0; i < lore.size(); i++) {
                String s = lore.get(i);
                //Durability: x/y
                if(s.contains("Durability: ")) {
                    currentDurability = Integer.parseInt(s.split(" ")[1].split("/")[0]);

                    event.getPlayer().sendMessage("Repair: " + event.getRepairAmount());

                    if(event.getRepairAmount() == 1) {
                        currentDurability = item.getType().getMaxDurability();
                    } else {
                        int repairAmount = event.getRepairAmount() + currentDurability > item.getType().getMaxDurability() ?
                                currentDurability + event.getRepairAmount() - item.getType().getMaxDurability() :
                                event.getRepairAmount();
                        currentDurability = currentDurability + repairAmount;
                    }

                    currentDurability = Math.min(item.getType().getMaxDurability(), currentDurability);

                    lore.set(i, ChatColor.RESET + "Durability: " + currentDurability + "/" + item.getType().getMaxDurability());
                }
            }
            if(currentDurability == Integer.MIN_VALUE) {
                lore.add(ChatColor.RESET + "Durability: " + item.getType().getMaxDurability() + "/" + item.getType().getMaxDurability());
                //currentDurability = item.getType().getMaxDurability();
            } else {

                Damageable damageable = (Damageable) meta;
                double vanillaMaxDurability = item.getType().getMaxDurability();
                double customDurability = currentDurability;
                double customMaxDurability = item.getType().getMaxDurability();
                double newDamage =  vanillaMaxDurability - (vanillaMaxDurability * (customDurability / customMaxDurability));

                damageable.setDamage((int) newDamage);
                if(damageable.getDamage() == 0 && currentDurability < item.getType().getMaxDurability()) {
                    damageable.setDamage(1);
                }

                meta = (ItemMeta) damageable;
            }

            meta.setLore(lore);
            item.setItemMeta(meta);


        }
        event.setCancelled(true);
    }

    private boolean isCustomDegradableItem(NBTItem nbti) {
        return nbti.hasKey("dragonstone") || nbti.hasKey("orichalcum") || nbti.hasKey("adamant") || nbti.hasKey("mithril")
                || nbti.hasKey("ghost");
    }

}
