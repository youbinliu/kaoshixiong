package com.kaoshixiong.web.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class FriendShip extends Model<FriendShip> {
	
	private static final long serialVersionUID = 1L;
	public final static FriendShip dao = new FriendShip();
	
	public static List<FriendShip> getBySNSid(long snsId){
		return dao.find("select * from friendship where userId="+snsId);
	}
}
