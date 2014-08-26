package com.kaoshixiong.web.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class SNSUser extends Model<SNSUser> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final SNSUser dao = new SNSUser();
	
	public static SNSUser findBySNSid(long snsId){
		List<SNSUser> users = dao.find("select * from snsuser where snsId="+snsId);
		
		if(users == null || users.isEmpty()){
			return null;
		}
		return users.get(0);
	}
	
}
