package me.mrnv.crystalstats.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import email.com.gmail.cosmoconsole.bukkit.deathmsg.DeathPreDMPEvent;
import me.mrnv.crystalstats.Data;
import me.mrnv.crystalstats.Main;

public class DeathMessagesHandler implements Listener
{
	private Main plugin;
	
	public DeathMessagesHandler( Main plugin )
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onDeathPreDMPEvent( DeathPreDMPEvent event )
	{
		Player victim = event.getPlayer( );

		if( event.getDamager( ) != null &&
			event.getCause( ) != DamageCause.VOID )
		{
			Data victimupdate = new Data( );
			Data killerupdate = new Data( );
			
			victimupdate.setUpdateType( "PLAYER_DEATH" );
			victimupdate.setPlayer( victim.getName( ) );
			victimupdate.setUUID( victim.getUniqueId( ).toString( ) );
			victimupdate.setDeaths( 1 );
			
			killerupdate.setUpdateType( "PLAYER_KILL" );
			killerupdate.setKills( 1 );
			
			plugin.getUtils( ).updateCache( victimupdate );
			
			if( event.getDamager( ) instanceof EnderCrystal )
			{
				if( event.getDamager( ).hasMetadata( "dmp.enderCrystalPlacer" ) )
				{
					UUID uuid = UUID.fromString( event.getDamager( ).getMetadata( "dmp.enderCrystalPlacer" ).get( 0 ).asString( ) );
					String name = plugin.getUtils( ).getNameByUUID( uuid );
					
					if( name != null )
					{
						if( !name.equals( victim.getName( ) ) )
						{
							Player killer = Bukkit.getPlayerExact( name );
							
							if( killer != null )
							{
								killerupdate.setPlayer( name );
								killerupdate.setUUID( killer.getUniqueId( ).toString( ) );
								
								plugin.getUtils( ).updateCache( killerupdate );
							}
						}
					}
				}
				
				return;
			}
			
			if( !( event.getDamager( ) instanceof Player ) ) return;
			
			Player killer = ( Player )event.getDamager( );
			if( killer == null ) return; // how
			
			killerupdate.setPlayer( killer.getName( ) );
			killerupdate.setUUID( killer.getUniqueId( ).toString( ) );
			
			plugin.getUtils( ).updateCache( killerupdate );
		}
	}
}
