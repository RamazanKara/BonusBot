package bonusbot;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * Load and manage settings.
 * @author emre1702
 *
 */
public class Settings {
	
	/** Token of the bot (Client-ID) */
	static String token;
	/** Prefix for the commands */
	public static String prefix = "!";
	/** name of the bot */
	static String name = "Bonus-Bot";
	/** playing-text of the bot */
	static String playing = "Bonus-community";
	/** admin roles */
	public static String[] admins;
	/** name of the roles-channel where you can use commands to get roles */
	public static String rolesChannel = "";
	/** name of the audio-channel where you can use audio-commands */
	public static String audioChannel = "";
	/** name of the channel where the audio-info gets posted (embed) */
	public static String audioInfoChannel = "";
	/** name of the channel where the user joining the user get greeted */
	public static String greetUserChannel = "";
	/** name of the role which can use audio-commands */
	public static String audiobotUserRole = "";
	/** name of the english-section role (!english) */
	public static String englishRole = "";
	/** name of the german-section role (!german/!deutsch) */
	public static String germanRole = "";
	/** name of the turkish-section role (!turkish/!t�rkce) */
	public static String turkishRole = "";
	/** name of the RocketLeague-section role (!rocketleague) */
	public static String rocketleagueRole = "";
	/** name of the PUBG-section role (!pubg!) */
	public static String pubgRole = "";
	/** name of the what-emoji, used for some outputs */
	public static String whatEmoji = "what";
	/** name of the haha-emoji, used for some outputs */
	public static String hahaEmoji = "haha";
	
	/**
	 * Loads the settings from discordbot.log.
	 * To disable a settings just change the value to "" (empty String).
	 * Don't disable "token"!
	 */
	public final static void loadSettings ( ) {
		final JSONParser parser = new JSONParser();
		
		try {
			final Object obj = parser.parse( new FileReader ( "bonusbot.conf" ) );
			
			final JSONObject jsonObject = (JSONObject) obj;
			token = (String) jsonObject.get( "token" );
			
			if ( jsonObject.containsKey( "prefix" ) )
				prefix = (String) jsonObject.get( "prefix" );
			if ( jsonObject.containsKey( "name" ) ) 
				name = (String) jsonObject.get( "name" );
			if ( jsonObject.containsKey( "playing" ) ) 
				playing = (String) jsonObject.get( "playing" );
			
			if ( jsonObject.containsKey( "admin" ) ) {
				String adminwithcomma = (String) jsonObject.get( "admin" );
				if ( adminwithcomma.indexOf( ',' ) != -1 ) {
					admins = adminwithcomma.split( "," );
				} else {
					admins = new String[1];
					admins[0] = adminwithcomma;
				}
			} else {
				admins = new String[1];
				admins[0] = "";
			}
				
			
			if ( jsonObject.containsKey( "rolesChannel" ) )
				rolesChannel = (String) jsonObject.get( "rolesChannel" );
			if ( jsonObject.containsKey( "audioChannel" ) )
				audioChannel = (String) jsonObject.get( "audioChannel" );
			if ( jsonObject.containsKey( "audioInfoChannel" ) )
				audioInfoChannel = (String) jsonObject.get( "audioInfoChannel" );
			if ( jsonObject.containsKey( "greetUserChannel" ) )
				greetUserChannel = (String) jsonObject.get( "greetUserChannel" );
			
			if ( jsonObject.containsKey( "audiobotUserRole" ) )
				audiobotUserRole = (String) jsonObject.get( "audiobotUserRole" );
			if ( jsonObject.containsKey( "englishRole" ) )
				englishRole = (String) jsonObject.get( "englishRole" );
			if ( jsonObject.containsKey( "germanRole" ) )
				germanRole = (String) jsonObject.get( "germanRole" );
			if ( jsonObject.containsKey( "turkishRole" ) )
				turkishRole = (String) jsonObject.get( "turkishRole" );
			
			if ( jsonObject.containsKey( "rocketleagueRole" ) )
				rocketleagueRole = (String) jsonObject.get( "rocketleagueRole" );
			if ( jsonObject.containsKey( "pubgRole" ) )
				pubgRole = (String) jsonObject.get( "pubgRole" );
			
			if ( jsonObject.containsKey( "whatEmoji" ) )
				whatEmoji = (String) jsonObject.get( "whatEmoji" );
			if ( jsonObject.containsKey( "hahaEmoji" ) )
				hahaEmoji = (String) jsonObject.get( "hahaEmoji" );
			
		} catch ( Exception e ) {
			e.printStackTrace ( Logging.getPrintWrite() );
		}
	}
}