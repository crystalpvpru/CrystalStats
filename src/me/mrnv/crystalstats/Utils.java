package me.mrnv.crystalstats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Utils
{
	private Main plugin;
	
	public Utils( Main plugin )
	{
		this.plugin = plugin;
	}
	
	private Map< UUID, String > uuiddata = new HashMap< >( );
	
	public String getNameByUUID( UUID uuid )
	{
		for( UUID saveduuid : uuiddata.keySet( ) )
		{
			if( uuid.equals( saveduuid ) )
				return uuiddata.get( saveduuid );
		}
		
		for( Player player : Bukkit.getOnlinePlayers( ) )
		{
			if( player.getUniqueId( ).equals( uuid ) )
			{
				uuiddata.put( uuid, player.getName( ) );
				return player.getName( );
			}
		}
		
		return null;
	}
	
	public void updateCache( Data data )
	{
		Data cachedata = plugin.getCacheHandler( ).getInCache( data.getUUID( ), data.getPlayer( ) );
		
		if( cachedata == null )
		{
			cachedata = plugin.getCacheHandler( ).getByFileName( data.getUUID( ) );
			if( cachedata == null )
				cachedata = plugin.getCacheHandler( ).getByUsername( data.getPlayer( ) );
		}
		
		if( cachedata != null )
		{
			if( data.getUpdateType( ).equals( "PLAYER_KILL" ) )
				cachedata.setKills( cachedata.getKills( ) + 1 );
			else if( data.getUpdateType( ).equals( "PLAYER_DEATH" ) )
				cachedata.setDeaths( cachedata.getDeaths( ) + 1 );
			else if( data.getUpdateType( ).equals( "PLAYER_JOIN" ) )
			{
				cachedata.setJoinCount( cachedata.getJoinCount( ) + 1 );
				cachedata.setLastOnline( data.getLastOnline( ) );
			}
			else if( data.getUpdateType( ).equals( "PLAYER_LEAVE" ) )
				cachedata.setLastOnline( data.getLastOnline( ) );
			
			plugin.getCacheHandler( ).cache( cachedata );
		}
		else
			plugin.getCacheHandler( ).cache( data );
	}
}
