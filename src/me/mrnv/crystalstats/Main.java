package me.mrnv.crystalstats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import email.com.gmail.cosmoconsole.bukkit.deathmsg.DeathMessagesPrime;
import me.mrnv.crystalstats.events.DeathMessagesHandler;
import me.mrnv.crystalstats.events.PlayerJoinLeave;

public class Main extends JavaPlugin
{
	private static Utils utils;
	private static CacheHandler cacheHandler;
	
	private DeathMessagesPrime dmpplugin;
	
	// events
	private DeathMessagesHandler deathMessagesHandler;
	private PlayerJoinLeave playerJoinLeave;
	
	public void onEnable( )
	{
		Bukkit.getLogger( ).info( "[CrystalStats] Initializing" );
		
		utils = new Utils( this );
		cacheHandler = new CacheHandler( this );

		dmpplugin = ( DeathMessagesPrime )Bukkit.getPluginManager( ).getPlugin( "DeathMessagesPrime" );
		
		deathMessagesHandler = new DeathMessagesHandler( this );
		playerJoinLeave = new PlayerJoinLeave( this );
		
		Bukkit.getPluginManager( ).registerEvents( deathMessagesHandler, this );
		Bukkit.getPluginManager( ).registerEvents( playerJoinLeave, this );
	}
	
	public void onDisable( )
	{
		getCacheHandler( ).writeCache( );
		Bukkit.getScheduler( ).cancelTasks( this );
	}
	
	// MOVE THIS TO ASYNCCHATEVENT or something
	public boolean onCommand( CommandSender sender, Command cmd, String lable, String[ ] args )
	{
		if( cmd.getName( ).equalsIgnoreCase( "stats" ) )
		{
			if( args.length < 1 ) return false;
			
			Player player = Bukkit.getPlayer( args[ 0 ] );
			
			Data data = null;
			if( player != null )
				data = getCacheHandler( ).getInCache( player.getUniqueId( ).toString( ), player.getName( ) );
			else
				data = getCacheHandler( ).getInCache( null, args[ 0 ] );
			
			if( data == null )
			{
				data = getCacheHandler( ).getByUsername( args[ 0 ] );
				if( data != null )
					getCacheHandler( ).cache( data );
			}
				
			if( data == null )
			{
				sender.sendMessage( ChatColor.YELLOW + "Игрок " + ChatColor.RED + args[ 0 ] +
						ChatColor.YELLOW + " не играл на этом сервере" );
			}
			else
			{
				String kdr = "0.00";
				if( data.getDeaths( ) > 0 )
					kdr = String.format( "%.2f", ( float )( data.getKills( ) / data.getDeaths( ) ) );
				else
				{
					if( data.getKills( ) > 0 )
						kdr = String.format( "%d.00", data.getKills( ) );
				}
				
				String str =
						ChatColor.GRAY + "[ " + ChatColor.RED + args[ 0 ] + ChatColor.GRAY + " ]" + "\n" +
						ChatColor.GRAY + "Убийств: " + ChatColor.RED + data.getKills( ) + "\n" +
						ChatColor.GRAY + "Смертей: " + ChatColor.RED + data.getDeaths( ) + "\n" +
						ChatColor.GRAY + "KDR: " + ChatColor.RED + kdr + "\n" +
						ChatColor.GRAY + "Заходов: " + ChatColor.RED + data.getJoinCount( ) + "\n" +
						ChatColor.GRAY + "Последний онлайн: ";
				
				if( player != null )
					str += ChatColor.GREEN + "сейчас";
				else
					str += ChatColor.RED + Long.toString( data.getLastOnline( ) );
				
				sender.sendMessage( str );
			}

			return true;
		}
		else if( cmd.getName( ).equalsIgnoreCase( "forcewrite" ) )
		{
			getCacheHandler( ).writeCache( );
			return true;
		}
		
		return false; // later replace with false
	}
	
	public Utils getUtils( )
	{
		return utils;
	}
	
	public CacheHandler getCacheHandler( )
	{
		return cacheHandler;
	}
}
