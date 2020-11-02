package me.mrnv.crystalstats.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.mrnv.crystalstats.Data;
import me.mrnv.crystalstats.Main;

public class PlayerJoinLeave implements Listener
{
	private Main plugin;
	
	public PlayerJoinLeave( Main plugin )
	{
		this.plugin = plugin;
	}
	
	@EventHandler( ignoreCancelled = true, priority = EventPriority.HIGHEST )
	public void onPlayerJoin( PlayerJoinEvent event )
	{
		Data data = new Data( );
		Player player = event.getPlayer( );
		
		data.setUpdateType( "PLAYER_JOIN" );
		data.setJoinCount( 1 );
		data.setPlayer( player.getName( ) );
		data.setLastOnline( System.currentTimeMillis( ) );
		data.setUUID( player.getUniqueId( ).toString( ) );
		
		plugin.getUtils( ).updateCache( data );
	}
	
	@EventHandler( ignoreCancelled = true, priority = EventPriority.HIGHEST )
	public void onPlayerLeave( PlayerQuitEvent event )
	{
		Data data = new Data( );
		Player player = event.getPlayer( );
		
		data.setUpdateType( "PLAYER_LEAVE" );
		data.setPlayer( player.getName( ) );
		data.setLastOnline( System.currentTimeMillis( ) );
		data.setUUID( player.getUniqueId( ).toString( ) );
		
		plugin.getUtils( ).updateCache( data );
	}
	
	@EventHandler( ignoreCancelled = true, priority = EventPriority.HIGHEST )
	public void onPlayerKick( PlayerKickEvent event )
	{
		Data data = new Data( );
		Player player = event.getPlayer( );
		
		data.setUpdateType( "PLAYER_LEAVE" );
		data.setPlayer( player.getName( ) );
		data.setLastOnline( System.currentTimeMillis( ) );
		data.setUUID( player.getUniqueId( ).toString( ) );
		
		plugin.getUtils( ).updateCache( data );
	}
}
