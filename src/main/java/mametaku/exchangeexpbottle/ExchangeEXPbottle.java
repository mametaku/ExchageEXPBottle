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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginAwareness;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public final class ExchangeEXPbottle extends JavaPlugin implements Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("eeb.use")) {
            p.sendMessage("Unknown command. Type \"/help\" for help.");
            return true;
        }
        if (args.length == 0) {
            p.sendMessage("§a§l ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
            p.sendMessage("§a§l                   [ExchageEXPBottle]                   ");
            p.sendMessage("§a§l              左手にガラス瓶を持って左クリック！");
            p.sendMessage("§a§l            レベルを消費して経験値瓶を作りだします。");
            p.sendMessage("§a§l ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
            return true;
        }
        return false;
    }

    @Override
    public void onEnable() {
        getLogger().info("ExchangeEXPBottle is run.");
        getServer().getPluginManager().registerEvents(this, this);
        // config.ymlが存在しない場合はファイルに出力します。
        saveDefaultConfig();
        // config.ymlを読み込みます。
        FileConfiguration config = getConfig();
        reloadConfig();
        getCommand("eeb").setExecutor(this);
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

        if(e.getHand() == EquipmentSlot.HAND) return;

        FileConfiguration config = getConfig();
        if (!config.getBoolean("mode")) {
            return;
        }

        VaultManager v = new VaultManager(this);

        Random random = new Random();

        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        double pay = 0;

        Action action = e.getAction();

        ItemStack item = p.getInventory().getItemInOffHand();

        ItemStack items = new ItemStack(Material.EXPERIENCE_BOTTLE);

        if(!p.isSneaking()) return;

        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK))  return;
        if(item.getType() == Material.GLASS_BOTTLE) {
            if (!p.hasPermission("eeb.use")) {
                p.sendMessage("§3§l[§a§lEEB§3§l]§f§lあなたはまだその機能を使えません！");
                return;
            }
            while (item.getAmount() != 0){
                int Expamount = ExpManager.getTotalExperience(p);
                int ia = p.getInventory().getItemInOffHand().getAmount();
                int empty = p.getInventory().firstEmpty();
                double money = config.getDouble("tax");
                double pm = v.getBalance(uuid);
                if (empty == -1){
                    p.sendMessage("§3§l[§a§lEEB§3§l]§f§lインベントリに空きがありません！");
                    break;
                }
                if (p.getLevel() == 0){
                    p.sendMessage("§3§l[§a§lEEB§3§l]§f§lレベルが足りません！");
                    break;
                }
                if (pm < money){
                    p.sendMessage("§3§l[§a§lEEB§3§l]§f§lお金が足りません！");
                    break;
                }
                v.withdraw(uuid,money);
                ia--;
                Expamount -= 5+(random.nextInt(8));
                if (Math.signum(Expamount) == -1.0){
                    p.sendMessage("§3§l[§a§lEEB§3§l]§f§lレベルが足りません！");
                    break;
                }
                pay += money;
                item.setAmount(ia);
                ExpManager.setTotalExperience(p,Expamount);
                p.getInventory().addItem(items);
            }
            if (pay == 0){
                return;
            }
            p.sendMessage(pay+"$支払いました");
            return;
        }
    }
}
