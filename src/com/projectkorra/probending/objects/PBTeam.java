package com.projectkorra.probending.objects;

import java.util.Map;
import java.util.UUID;

import com.projectkorra.projectkorra.Element;

public class PBTeam
{
	private int _id;
	private String _name;
	private UUID _leader;
	private Map<UUID, TeamMemberRole> _members;

	public PBTeam(int id, String name, UUID leader, Map<UUID, TeamMemberRole> members)
	{
		_id = id;
		_name = name;
		_leader = leader;
		_members = members;
	}
	
	public int getID()
	{
		return _id;
	}

	public String getTeamName()
	{
		return _name;
	}
	
	public UUID getLeader()
	{
		return _leader;
	}
	
	public Map<UUID, TeamMemberRole> getMembers()
	{
		return _members;
	}

	public static enum TeamMemberRole
	{
		WATER(Element.WATER.getColor() + "Waterbender", true),
		AIR(Element.AIR.getColor() + "Airbender", false),
		FIRE(Element.FIRE.getColor() + "Firebender", true),
		EARTH(Element.EARTH.getColor() + "Earthbender", true),
		CHI(Element.CHI.getColor() + "Chiblocker", false);

		private String _display;
		private boolean _enabled;

		private TeamMemberRole(String display, boolean enabled)
		{
			_display = display;
			_enabled = enabled;
		}

		public String getDisplay()
		{
			return _display;
		}

		public boolean isEnabled()
		{
			return _enabled;
		}
		
		public static TeamMemberRole parseRole(String role)
		{
			for (TeamMemberRole test : TeamMemberRole.values())
			{
				if (test.toString().equalsIgnoreCase(role))
				{
					return test;
				}
			}
			
			return null;
		}
	}
}