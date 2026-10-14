package cn.xinyue_neko.plugin.simpleSign

import net.milkbowl.vault.economy.Economy
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin

import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SimpleSignKT : JavaPlugin(), Listener {

    companion object {
        private lateinit var instance: SimpleSignKT
        private lateinit var economy: Economy
        private lateinit var dataFile: File
        private lateinit var data: FileConfiguration
        private val DATE = DateTimeFormatter.ofPattern(Config.DATE_TIME_FORMAT)
    }

    fun getData(): FileConfiguration {
        return data
    }

    override fun onEnable() {
        instance = this

        if (server.pluginManager.getPlugin("Vault") == null) {
            logger.info("未找到 Vault 插件, 签到功能无法使用!")
            server.pluginManager.disablePlugin(this)
            return
        }

        val rsp: RegisteredServiceProvider<Economy>? = server
            .servicesManager
            .getRegistration(Economy::class.java)

        if (rsp == null) {
            logger.info("未找到经济提供者, 请确保安装了 EssentialsX 等经济插件!")
            server.pluginManager.disablePlugin(this)
            return
        }

        economy = rsp.provider
        logger.info("已成功挂钩 Vault 经济系统.")

        dataFile = File(dataFolder, "data.yml")
        if (!dataFile.exists()) {
            dataFolder.mkdirs()
            saveResource("data.yml", false)
        }

        data = YamlConfiguration.loadConfiguration(dataFile)

        server.pluginManager.registerEvents(this, this)
    }

    fun saveData() {
        try {
            data.save(dataFile)
        } catch (e: IOException) {
            logger.severe("保存签到数据失败: ${e.message}")
        }
    }

    override fun onDisable() {
        saveData()
    }

    @EventHandler fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val economy = economy ?: return

        val uuid = player.uniqueId.toString()
        val today = LocalDate.now().format(DATE)
        val lastSign = getData().getString(uuid)

        if (today.equals(lastSign)) {
            player.sendMessage(Config.MESSAGE_ALREADY
                .replace('&', ChatColor.COLOR_CHAR)
            )
            return
        }

        val response = economy.depositPlayer(player, Config.SIGN_MONEYS)
        if (response.transactionSuccess()) {
            getData().set(uuid, today)
            saveData()
            player.world.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
            player.sendMessage(Config.MESSAGES_SUCCESS
                .replace("{money}", Config.formatMoney(Config.SIGN_MONEYS))
                .replace('&', ChatColor.COLOR_CHAR)
            )
        } else {
            player.sendMessage(Config.MESSAGE_ERROR.replace("{error}", response.errorMessage))
        }
    }
}