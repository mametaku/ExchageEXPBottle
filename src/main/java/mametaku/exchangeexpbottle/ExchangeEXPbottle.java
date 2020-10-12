package mametaku.exchangeexpbottle;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Random;

public final class ExchangeEXPbottle extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("ExchangeEXPBottle is run.");
        getServer().getPluginManager().registerEvents(this, this);
        // config.ymlが存在しない場合はファイルに出力します。
        saveDefaultConfig();
        // config.ymlを読み込みます。
        FileConfiguration config = getConfig();
        reloadConfig();
        if (!config.getBoolean("mode")) {
            getLogger().info("ExchangeEXPBottle is not run.");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



    @EventHandler
    public void event(PlayerInteractEvent e) {
        FileConfiguration config = getConfig();
        if (!config.getBoolean("mode")) {
            return;
        }

        Random random = new Random();

        Player p = e.getPlayer();

        Action action = e.getAction();

        ItemStack item = p.getInventory().getItemInOffHand();

        ItemStack items = new ItemStack(Material.EXPERIENCE_BOTTLE);

        if(!p.isSneaking()) return;

        if (!p.hasPermission("eeb.use")) {
            p.sendMessage("§3§l[§a§lEEB§3§l]§f§lあなたはまだその機能を使えません！");
            return;
        }

        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK))  return;
        if(item.getType() == Material.GLASS_BOTTLE) {
            while (item.getAmount() != 0){
                int Expamount = ExpManager.getTotalExperience(p);
                int ia = p.getInventory().getItemInOffHand().getAmount();
                int empty = p.getInventory().firstEmpty();
                if (empty == -1){
                    p.sendMessage("§3§l[§a§lEEB§3§l]§f§lインベントリに空きがありません！");
                    break;
                }
                if (p.getLevel() == 0){
                    p.sendMessage("§3§l[§a§lEEB§3§l]§f§lレベルが足りません！");
                    break;
                }
                ia--;
                Expamount -= 5+(random.nextInt(8));
                if (Math.signum(Expamount) == -1.0){
                    p.sendMessage("§3§l[§a§lEEB§3§l]§f§lレベルが足りません！");
                    break;
                }
                item.setAmount(ia);
                ExpManager.setTotalExperience(p,Expamount);
                p.getInventory().addItem(items);
            }
        }
    }
}
