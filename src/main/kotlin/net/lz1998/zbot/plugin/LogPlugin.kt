package net.lz1998.zbot.plugin

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.micrometer.core.instrument.Metrics
import net.lz1998.pbbot.alias.*
import net.lz1998.pbbot.bot.Bot
import net.lz1998.pbbot.bot.BotPlugin
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

// TODO
@Component
class LogPlugin : BotPlugin() {

    val log: Logger = LoggerFactory.getLogger(this::class.java)
    val objMapper = ObjectMapper()
            .configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, true)
            .configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)


    override fun onFriendAddNotice(bot: Bot, event: FriendAddNoticeEvent): Int {
        return super.onFriendAddNotice(bot, event)
    }

    override fun onFriendRequest(bot: Bot, event: FriendRequestEvent): Int {
        return super.onFriendRequest(bot, event)
    }

    override fun onGroupAdminNotice(bot: Bot, event: GroupAdminNoticeEvent): Int {
        return super.onGroupAdminNotice(bot, event)
    }

    override fun onGroupBanNotice(bot: Bot, event: GroupBanNoticeEvent): Int {
        return super.onGroupBanNotice(bot, event)
    }

    override fun onGroupDecreaseNotice(bot: Bot, event: GroupDecreaseNoticeEvent): Int {
        return super.onGroupDecreaseNotice(bot, event)
    }

    override fun onGroupIncreaseNotice(bot: Bot, event: GroupIncreaseNoticeEvent): Int {
        return super.onGroupIncreaseNotice(bot, event)
    }

    override fun onGroupMessage(bot: Bot, event: GroupMessageEvent): Int {
//        log.info("RECV GROUP_MSG {}", objMapper.writeValueAsString(event))
        Metrics.counter(
                "recv_group_message",
                "self_id", bot.selfId.toString(),
                "group_id", event.groupId.toString(),
                "user_id", event.userId.toString(),
                "role", event.sender.role,
        ).increment()
        log.info("RECV GROUP MSG groupId={} userId={} msg={}", event.groupId, event.userId, event.rawMessage)
        return super.onGroupMessage(bot, event)
    }

    override fun onGroupRequest(bot: Bot, event: GroupRequestEvent): Int {
        return super.onGroupRequest(bot, event)
    }

    override fun onGroupUploadNotice(bot: Bot, event: GroupUploadNoticeEvent): Int {
        return super.onGroupUploadNotice(bot, event)
    }

    override fun onPrivateMessage(bot: Bot, event: PrivateMessageEvent): Int {
//        log.info("RECV PRIVATE_MSG {}", objMapper.writeValueAsString(event))
        Metrics.counter("recv_private_message",
                "self_id", bot.selfId.toString(),
                "user_id", event.userId.toString()
        ).increment()
        log.info("RECV PRIVATE MSG userId={} msg={}", event.userId, event.rawMessage)
        return super.onPrivateMessage(bot, event)
    }
}