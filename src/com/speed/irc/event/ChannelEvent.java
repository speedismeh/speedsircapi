package com.speed.irc.event;

import com.speed.irc.types.Channel;
import com.speed.irc.types.ServerUser;

/**
 * 
 * Represents a channel event.
 * 
 * This file is part of Speed's IRC API.
 * 
 * Speed's IRC API is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Speed's IRC API is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Speed's IRC API. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Shivam Mistry
 * 
 */
public class ChannelEvent implements IRCEvent {

	public static final int TOPIC_CHANGED = 10, MODE_CHANGED = 11;
	private final int code;
	private final Channel channel;
	private String[] args;
	private final Object source;
	private ServerUser sender;

	public ChannelEvent(final Channel channel, final int code,
			String senderNick, final Object source, final String... args) {
		this.code = code;
		this.channel = channel;
		this.source = source;
		this.sender = channel.getUser(senderNick);
		if (this.sender == null)
			// e.g. ChanServ etc
			this.sender = channel.getServer().getUser(senderNick);
		this.args = args;
	}

	public ChannelEvent(Channel channel2, int code2, Object source2,
			final String... args) {
		this.code = code2;
		this.channel = channel2;
		this.source = source2;
		this.args = args;
	}

	public int getCode() {
		return code;
	}

	public Channel getChannel() {
		return channel;
	}

	public ServerUser getSender() {
		return sender;
	}

	public Object getSource() {
		return source;
	}

	public void callListener(IRCEventListener listener) {
		if (listener instanceof ChannelUserListener
				&& this instanceof ChannelUserEvent) {
			final ChannelUserListener l = (ChannelUserListener) listener;
			final ChannelUserEvent event = (ChannelUserEvent) this;
			switch (event.getCode()) {
			case ChannelUserEvent.USER_JOINED:
				l.channelUserJoined(event);
				break;
			case ChannelUserEvent.USER_KICKED:
				l.channelUserKicked(event);
				break;
			case ChannelUserEvent.USER_MODE_CHANGED:
				l.channelUserModeChanged(event);
				break;
			case ChannelUserEvent.USER_PARTED:
				l.channelUserParted(event);
				break;
			case ChannelUserEvent.USER_NICK_CHANGED:
				l.channelUserNickChanged(event);
				break;
			case ChannelUserEvent.USER_QUIT:
				l.channelUserQuit(event);
				break;
			}
		} else if (listener instanceof ChannelEventListener) {
			final ChannelEventListener l = (ChannelEventListener) listener;
			final ChannelEvent event = this;
			switch (event.getCode()) {
			case ChannelEvent.MODE_CHANGED:
				l.channelModeChanged(event);
				break;
			case ChannelEvent.TOPIC_CHANGED:
				l.channelTopicChanged(event);
				break;
			}
		}
	}

	public String[] getArgs() {
		return args;
	}
}
