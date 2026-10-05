package cn.xinyue_neko.plugin.simpleSign;

import cn.xinyue_neko.plugin.simpleSign.event.PlayerJoinSign;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public final class SimpleSign extends JavaPlugin {

    private static Economy economy;
    private static FileConfiguration data;
    private static File dataFile;
    private static SimpleSign instance;

    public static Economy getEconomy() {
        return economy;
    }

    public static FileConfiguration getData() {
        return data;
    }

    @Override
    public void onEnable() {
        instance = this;

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("未找到 Vault 插件，签到功能无法使用！");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        RegisteredServiceProvider<Economy> rsp = getServer()
                .getServicesManager()
                .getRegistration(Economy.class);

        if (rsp == null) {
            getLogger().severe("未找到经济提供者，请确保安装了 EssentialsX 等经济插件！");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        economy = rsp.getProvider();
        getLogger().info("已成功挂钩 Vault 经济系统。");

        dataFile = new File(getDataFolder(), "data.yaml");
        if (!dataFile.exists()) {
            getDataFolder().mkdirs();
            saveResource("data.yaml", false);
        }
        data = YamlConfiguration.loadConfiguration(dataFile);

        getServer().getPluginManager().registerEvents(new PlayerJoinSign(), this);
    }

    public static void saveData() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            instance.getLogger().severe("保存签到数据失败: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        saveData();
    }
}
