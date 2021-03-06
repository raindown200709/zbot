package net.lz1998.zbot.plugin

import net.lz1998.pbbot.alias.GroupMessageEvent
import net.lz1998.pbbot.alias.GroupRequestEvent
import net.lz1998.pbbot.bot.Bot
import net.lz1998.pbbot.bot.BotPlugin
import net.lz1998.zbot.aop.annotations.PrefixFilter
import net.lz1998.zbot.service.AuthService
import net.lz1998.zbot.service.isSuperAdmin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AuthPlugin : BotPlugin() {
    @Autowired
    lateinit var authService: AuthService

    @PrefixFilter(".")
    override fun onGroupMessage(bot: Bot, event: GroupMessageEvent): Int {
        val userId = event.userId
        val groupId = event.groupId
        var rawMsg = event.rawMessage.trim()

        if (rawMsg == "授权" && isSuperAdmin(userId)) {
            authService.setAuth(groupId = groupId, isAuth = true, adminId = userId)
            bot.sendGroupMsg(group_id = groupId, message = "授权成功 $groupId")
            return MESSAGE_BLOCK
        }
        if (rawMsg.startsWith("授权+") && isSuperAdmin(userId)) {
            rawMsg = rawMsg.substring("授权+".length).trim()
            rawMsg.toLongOrNull()?.also {
                authService.setAuth(groupId = it, isAuth = true, adminId = userId)
                bot.sendGroupMsg(group_id = groupId, message = "授权成功 $it")
            } ?: bot.sendGroupMsg(group_id = groupId, message = "群号错误")
            return MESSAGE_BLOCK
        }
        if (rawMsg.startsWith("授权-") && isSuperAdmin(userId)) {
            rawMsg = rawMsg.substring("授权-".length).trim()
            rawMsg.toLongOrNull()?.also {
                authService.setAuth(groupId = it, isAuth = false, adminId = userId)
                bot.sendGroupMsg(group_id = groupId, message = "取消授权 $it")
            } ?: bot.sendGroupMsg(group_id = groupId, message = "群号错误")
            return MESSAGE_BLOCK
        }
        return if (authService.isAuth(groupId)) {
            MESSAGE_IGNORE
        } else {
            // TODO 提示
            MESSAGE_BLOCK
        }
    }

    override fun onGroupRequest(bot: Bot, event: GroupRequestEvent): Int {
        val userId = event.userId
        if (isSuperAdmin(userId)) {
            bot.setGroupAddRequest(event.flag, event.subType, true, "")
            return MESSAGE_BLOCK
        }
        return MESSAGE_IGNORE
    }
}