package bonusbot.lavaplayer;

import java.time.LocalDateTime;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.handle.obj.IUser;

/**
 * Class to handle audio-tracks easier.
 * 
 * @author EmreKara
 */
public class Track {
	public AudioTrack audio;
	public IUser user;
	public LocalDateTime date;

	public Track(AudioTrack audio, IUser user, LocalDateTime date) {
		this.audio = audio;
		this.user = user;
		this.date = date;
	}
}