package bonusbot;

import java.util.List;

import bonusbot.guild.GuildExtends;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.audit.ActionType;
import sx.blah.discord.handle.audit.entry.TargetedEntry;
//import sx.blah.discord.handle.audit.ActionType;
//import sx.blah.discord.handle.audit.entry.TargetedEntry;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;
//import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
//import sx.blah.discord.handle.impl.events.guild.channel.message.MessageUpdateEvent;
//import sx.blah.discord.handle.impl.events.guild.member.UserBanEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.member.UserPardonEvent;
//import sx.blah.discord.handle.impl.events.guild.member.UserPardonEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IWebhook;
import sx.blah.discord.handle.obj.StatusType;

/**
 * Listener for Discord4J events.
 * 
 * @author EmreKara
 *
 */
class EventsListener {

	private static void checkGuildWebhook(GuildExtends guildext, String webhookname) {
		IChannel webhookchannel = guildext.getChannel("webhookChannel");
		if (webhookchannel != null) {
			List<IWebhook> webhooks = webhookchannel.getWebhooks();
			if (webhooks.isEmpty()) {
				IWebhook hook = webhookchannel.createWebhook(webhookname, Client.get().getOurUser().getAvatar());
				String githuburl = String.format("https://canary.discordapp.com/api/webhooks/%s/%s/github",
						hook.getStringID(), hook.getToken());
				Util.sendMessage(guildext.getGuild(),
						Lang.getLang("add_webhook_to_service", hook.getStringID(), hook.getToken(), githuburl));
			}
		}
	}

	/**
	 * When the bot is ready.
	 * 
	 * @param event
	 *            ReadyEvent from Discord4J
	 */
	@EventSubscriber
	public void onReady(ReadyEvent event) {
		try {
			event.getClient().changeUsername(Settings.get("name"));
			event.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, Settings.get("playing"));

			String webhookname = Settings.get("webhookName");
			List<IGuild> guilds = event.getClient().getGuilds();
			for (IGuild guild : guilds) {
				GuildExtends guildext = new GuildExtends(guild);
				if (webhookname != null) {
					checkGuildWebhook(guildext, webhookname);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(Logging.getPrintWrite());
		}

	}

	/**
	 * When a user joined the guild (first time).
	 * 
	 * @param event
	 *            UserJoinEvent from Discord4J
	 */
	@EventSubscriber
	public void onUserJoinedGuild(UserJoinEvent event) {
		GuildExtends guildext = GuildExtends.get(event.getGuild());
		IChannel greetUserChannel = guildext.getChannel("greetUserChannel");
		if (greetUserChannel != null) {
			IGuild guild = event.getGuild();
			int amountonserver = guild.getTotalMemberCount();
			String suffix = amountonserver == 1 ? "st"
					: (amountonserver == 2 ? "nd" : (amountonserver == 3 ? "rd" : "th"));
			String welcomemsg = "Welcome " + event.getUser().mention() + "!\nYou are the " + amountonserver + suffix
					+ " user 🎉";
			IChannel channel = guildext.getChannel("infoChannel");
			if (channel != null)
				welcomemsg += "\nPlease read " + channel.mention() + " in 'important' category!";
			Util.sendMessage(greetUserChannel, welcomemsg);
		}
	}

	@EventSubscriber
	public void onUserLeaveGuild(UserLeaveEvent event) {
		GuildExtends guildext = GuildExtends.get(event.getGuild());
		IChannel logchannel = guildext.getChannel("userLeaveLogChannel");
		if (logchannel != null) {
			IUser user = event.getUser();
			String msg = user.mention() + " (" + Util.getUniqueName(user) + ") has left the guild.";
			Util.sendMessage(logchannel, msg);
		}
	}

	@EventSubscriber
	public void onUserBanned(UserBanEvent event) {
		GuildExtends guildext = GuildExtends.get(event.getGuild());
		IChannel logchannel = guildext.getChannel("userBanLogChannel");
		if (logchannel != null) {
			TargetedEntry auditlog = event.getGuild().getAuditLog(ActionType.MEMBER_BAN_ADD)
					.getEntriesByTarget(event.getUser().getLongID()).get(0);
			String msg = event.getUser().mention() + " got banned by " + auditlog.getResponsibleUser().mention()
					+ ". Reason: " + auditlog.getReason().orElse("None");
			Util.sendMessage(logchannel, msg);
		}
	}

	@EventSubscriber
	public void onUserUnbanned(UserPardonEvent event) {
		GuildExtends guildext = GuildExtends.get(event.getGuild());
		IChannel logchannel = guildext.getChannel("userPardonLogChannel");
		if (logchannel != null) {
			TargetedEntry auditlog = event.getGuild().getAuditLog(ActionType.MEMBER_BAN_REMOVE)
					.getEntriesByTarget(event.getUser().getLongID()).get(0);
			String msg = event.getUser().mention() + " got unbanned by " + auditlog.getResponsibleUser().mention()
					+ ".";
			Util.sendMessage(logchannel, msg);
		}
	}

	/*
	 * @EventSubscriber public void onMessageEdited( MessageUpdateEvent event
	 * ) { if (!MainRunner.isBot(event.getAuthor())) { GuildExtends guildext =
	 * GuildExtends.get( event.getGuild() ); IChannel logchannel =
	 * guildext.getMessageUpdateLogChannel(); if (logchannel != null) { String msg =
	 * "Message got edited by "+event.getAuthor().mention()+" in "+event.getChannel(
	 * ).mention()+":\n" + event.getOldMessage().getContent();
	 * Util.sendMessage(logchannel, msg); } } }
	 * 
	 * @EventSubscriber public void onMessageDeleted( MessageDeleteEvent event
	 * ) { GuildExtends guildext = GuildExtends.get( event.getGuild() ); IChannel
	 * logchannel = guildext.getMessageDeleteLogChannel(); if (logchannel != null) {
	 * TargetedEntry auditlog =
	 * event.getGuild().getAuditLog(ActionType.MESSAGE_DELETE).getEntriesByTarget(
	 * event.getMessageID()).get(0); String msg =
	 * "Message from "+event.getAuthor().mention()+" got deleted by "+auditlog.
	 * getResponsibleUser().mention()+" in "+event.getChannel().mention()+":\n" +
	 * event.getMessage().getContent(); Util.sendMessage(logchannel, msg); } }
	 * 
	 * @EventSubscriber public void onWebhookEditRequest(WebhookEvent event) {
	 * Util.sendMessage(event.getGuild(), event.getWebhook().toString() + " by " +
	 * event.getWebhook().getDefaultName()); }
	 * 
	 * @EventSubscriber public void onWebhookCreate(WebhookCreateEvent event) {
	 * Util.sendMessage(event.getGuild(), event.getWebhook().toString() + " by " +
	 * event.getWebhook().getDefaultName()); }
	 * 
	 * @EventSubscriber public void onWebhookUpdate(WebhookUpdateEvent event) {
	 * Util.sendMessage(event.getGuild(), event.getOldWebhook().toString() + " to "
	 * + event.getNewWebhook().toString() + " by " +
	 * event.getWebhook().getDefaultName()); }
	 */
}
