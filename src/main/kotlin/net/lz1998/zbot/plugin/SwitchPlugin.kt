package net.lz1998.zbot.plugin

import net.lz1998.pbbot.alias.GroupMessageEvent
import net.lz1998.pbbot.bot.Bot
import net.lz1998.pbbot.bot.BotPlugin
import net.lz1998.zbot.aop.annotations.PrefixFilter
import net.lz1998.zbot.aop.annotations.SwitchFilter
import net.lz1998.zbot.service.PluginSwitchService
import net.lz1998.zbot.service.isAdmin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@SwitchFilter("开关")
class SwitchPlugin : BotPlugin() {
    @Autowired
    lateinit var pluginSwitchService: PluginSwitchService

    @PrefixFilter(".")
    override fun onGroupMessage(bot: Bot, event: GroupMessageEvent): Int {
        var msg = event.rawMessage
        val groupId = event.groupId
        if (msg.startsWith("停用") && isAdmin(event.sender)) {
            msg = msg.substring("停用".length).trim { it <= ' ' }.toLowerCase()
            if (!pluginSwitchService.isPluginExist(msg)) {
                bot.sendGroupMsg(groupId, "功能不存在")
                return MESSAGE_BLOCK
            }
            pluginSwitchService.setPluginSwitch(groupId, msg, true)
            bot.sendGroupMsg(groupId, "停用成功")
            return MESSAGE_BLOCK
        }
        if (msg.startsWith("启用") && isAdmin(event.sender)) {
            msg = msg.substring("启用".length).trim { it <= ' ' }.toLowerCase()
            if (!pluginSwitchService.isPluginExist(msg)) {
                bot.sendGroupMsg(groupId, "功能不存在")
                return MESSAGE_BLOCK
            }
            pluginSwitchService.setPluginSwitch(groupId, msg, false)
            bot.sendGroupMsg(groupId, "启用成功")
            return MESSAGE_BLOCK
        }

        return if (pluginSwitchService.isPluginStop(groupId, "回复")) {
            MESSAGE_BLOCK
        } else MESSAGE_IGNORE
    }
}