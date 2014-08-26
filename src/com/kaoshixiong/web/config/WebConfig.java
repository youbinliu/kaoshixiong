package com.kaoshixiong.web.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.kaoshixiong.datamining.NeoDatabaseFactory;
import com.kaoshixiong.web.controller.RelationController;
import com.kaoshixiong.web.model.FriendShip;
import com.kaoshixiong.web.model.SNSUser;

public class WebConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		loadPropertyFile("global.conf");
		me.setDevMode(getPropertyToBoolean("devMode", Boolean.valueOf(getProperty("devMode"))));
		NeoDatabaseFactory.configureDatabase(getProperty("NeoDB"));

	}

	@Override
	public void configRoute(Routes me) {
		me.add("/relation", RelationController.class);

	}

	@Override
	public void configPlugin(Plugins me) {
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("JdbcConn"), getProperty("user").trim(), getProperty("password").trim());
		me.add(c3p0Plugin);
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.addMapping("snsuser", SNSUser.class);	
		arp.addMapping("friendship", FriendShip.class);
	}

	@Override
	public void configInterceptor(Interceptors me) {

	}

	@Override
	public void configHandler(Handlers me) {

	}

}
