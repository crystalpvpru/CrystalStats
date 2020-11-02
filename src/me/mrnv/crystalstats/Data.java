package me.mrnv.crystalstats;

public class Data
{
	private String updatetype;
	private String player;
	private String uuid;
	
	private int kills;
	private int deaths;
	private int joins;
	private long lastonline;
	
	public String getUpdateType( )
	{
		return updatetype;
	}
	
	public void setUpdateType( String updatetype )
	{
		this.updatetype = updatetype;
	}
	
	public String getPlayer( )
	{
		return player;
	}
	
	public void setPlayer( String player )
	{
		this.player = player;
	}
	
	public String getUUID( )
	{
		return uuid;
	}
	
	public void setUUID( String uuid )
	{
		this.uuid = uuid;
	}
	
	public int getKills( )
	{
		return kills;
	}
	
	public void setKills( int kills )
	{
		this.kills = kills;
	}

	public int getDeaths( )
	{
		return deaths;
	}

	public void setDeaths( int deaths )
	{
		this.deaths = deaths;
	}

	public int getJoinCount( )
	{
		return joins;
	}

	public void setJoinCount( int joins )
	{
		this.joins = joins;
	}

	public long getLastOnline( )
	{
		return lastonline;
	}

	public void setLastOnline( long lastonline )
	{
		this.lastonline = lastonline;
	}
}
