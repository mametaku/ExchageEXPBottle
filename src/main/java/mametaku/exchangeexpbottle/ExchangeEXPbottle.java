package mametaku.exchangeexpbottle;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

import static java.lang.Integer.parseInt;

public final class ExchangeEXPbottle extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("automendingmachine is run.");
        getServer().getPluginManager().registerEvents(this, this);
        // config.ymlが存在しない場合はファイルに出力します。
        saveDefaultConfig();
        // config.ymlを読み込みます。
        FileConfiguration config = getConfig();
        reloadConfig();
        if (!config.getBoolean("mode")) {
            getLogger().info("automendingmachine is not run.");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void event(PlayerInteractEvent e) {
        Random random = new Random();

        Player p = e.getPlayer();

        Action action = e.getAction();

        ItemStack item = p.getInventory().getItemInOffHand();

        ItemStack items = new ItemStack(Material.EXPERIENCE_BOTTLE);


        if(!p.isSneaking()) return;
        p.sendMessage("動く");

        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))  return;
        p.sendMessage("動く1");
        if(item.getType() == Material.GLASS_BOTTLE) {
            p.sendMessage("動く2");
            while (item.getAmount() != 0){
                int ia = p.getInventory().getItemInOffHand().getAmount();
                int Expamount = p.getTotalExperience();
                p.sendMessage(Expamount+"");
                int empty = p.getInventory().firstEmpty();
                if (empty == -1){
                    p.sendMessage("インベントリに空きがありません！");
                    break;
                }
                if (p.getLevel() == 0){
                    p.sendMessage("レベルが足りません！");
                    break;
                }
                ia--;
                Expamount -= 3+(random.nextInt(7));
                item.setAmount(ia);
                p.setTotalExperience(Expamount);
                p.getInventory().addItem(items);
                p.sendMessage("動く3");
            }
        }
    }
}
