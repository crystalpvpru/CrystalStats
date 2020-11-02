package me.mrnv.crystalstats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CacheHandler
{
	private Main plugin;
	private List< Data > cache = new ArrayList< >( );//Map< UUID, Data > cache = new HashMap< >( );
	private BufferedReader reader;
	
	public CacheHandler( Main plugin )
	{
		this.plugin = plugin;
		this.cache = new ArrayList< >( );//= new HashMap< >( );
		
		File f = new File( plugin.getDataFolder( ) + "/" );
		if( !f.exists( ) )
			f.mkdir( );
	}
	
	public void cache( Data data )
	{
		boolean found = false;
		
		for( Data d : cache )
		{
			if( ( d.getPlayer( ) != null && d.getPlayer( ).equalsIgnoreCase( data.getPlayer( ) ) ) ||
				( d.getUUID( ) != null && d.getUUID( ).equalsIgnoreCase( data.getUUID( ) ) ) )
			{
				cache.remove( d );
				cache.add( data );
				
				found = true;
				break;
			}
		}
		
		
		if( !found )
			cache.add( data );
	}
	
	/*public Data getByUUID( UUID uuid )
	{
		for( Data data : cache )
		{
			if( data.getUUID( ).equals( uuid ) )
				return data;
		}
		
		return null;
	}*/
	
	public Data getInCache( String uuid, String player )
	{
		for( Data data : cache )
		{
			if( ( data.getUUID( ) != null && data.getUUID( ).equals( uuid ) ) ||
				( data.getPlayer( ) != null && data.getPlayer( ).equals( player ) ) )
				return data;
		}
		
		return null;
	}
	
	public Data getByFileName( String uuid )
	{
		File f = new File( plugin.getDataFolder( ) + "/" );
		for( File file : f.listFiles( ) )
		{
			if( file.getName( ).startsWith( uuid.toString( ) ) )
			{
				try
				{
					FileInputStream fis = new FileInputStream( file );
					InputStreamReader isr = new InputStreamReader( fis, StandardCharsets.UTF_8 );
					reader = new BufferedReader( isr );
					
					StringBuilder sb = new StringBuilder( );
					String line = "";
					
					while( ( line = reader.readLine( ) ) != null )
						sb.append( line );
					
					Gson gson = new Gson( );
					
					Data data = gson.fromJson( sb.toString( ), Data.class );
					
					return data;
				}
				catch( IOException e )
				{
					e.printStackTrace( );
					return null; // fuck it, return null
				}
			}
		}
		
		return null;
	}
	
	public Data getByUsername( String player )
	{
		File f = new File( plugin.getDataFolder( ) + "/" );
		for( File file : f.listFiles( ) )
		{
			try
			{
				FileInputStream fis = new FileInputStream( file );
				InputStreamReader isr = new InputStreamReader( fis, StandardCharsets.UTF_8 );
				reader = new BufferedReader( isr );
				
				StringBuilder sb = new StringBuilder( );
				String line = "";
				
				while( ( line = reader.readLine( ) ) != null )
					sb.append( line );
				
				Gson gson = new Gson( );
				
				Data data = gson.fromJson( sb.toString( ), Data.class );
				
				if( data.getPlayer( ).equalsIgnoreCase( player ) )
					return data;
			}
			catch( IOException e )
			{
				e.printStackTrace( );
			}
		}
		
		return null;
	}
	
	public Data createData( UUID uuid, String player )
	{
		// creating new data
		if( player != null )
		{
			Data data = new Data( );
			
			data.setPlayer( player );
			
			if( uuid != null )
				data.setUUID( uuid.toString( ) );
			else
			{
				for( Player pl : Bukkit.getOnlinePlayers( ) )
				{
					if( pl.getName( ).equals( player ) )
					{
						data.setUUID( pl.getUniqueId( ).toString( ) );
						break;
					}
				}
			}
			
			return data;
		}
		
		return null;
	}
	
	public void write( Data data )
	{
		Gson gson = new Gson( );
		
		String json = gson.toJson( data );
		
		Bukkit.broadcastMessage( json );
		
		if( data.getUUID( ).equals( "" ) ||
			data.getPlayer( ).equals( "" ) )
		{
			Bukkit.getLogger( ).info( "[CrystalStats] Failed to write data [no uuid/player] [json -> " + json + "]" );
			return;
		}
		
		String filename = data.getUUID( ).toString( ) + ".json";
		
		File file = new File( plugin.getDataFolder( ) + "/" + filename );
		
		try
		{
			FileWriter writer = new FileWriter( file, false );
			
			writer.write( json );
			writer.close( );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}
	
	public void writeCache( )
	{
		for( Data data : cache )
			write( data );
	}
	
	public void clearCache( )
	{
		cache.clear( );
	}
}
