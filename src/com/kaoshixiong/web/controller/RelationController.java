package com.kaoshixiong.web.controller;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import com.jfinal.core.Controller;
import com.kaoshixiong.datamining.NeoDatabaseFactory;
import com.kaoshixiong.datamining.NeoOptions;
import com.kaoshixiong.datamining.PersonNode;
import com.kaoshixiong.web.model.FriendShip;
import com.kaoshixiong.web.model.SNSUser;
import com.kaoshixiong.web.response.CommonResponse;

public class RelationController extends Controller {
	private final static Logger logger = Logger.getLogger(RelationController.class);
	
	public void index(){
		renderJson(new CommonResponse("0", "hello world"));
	}
	
	public void test(){
		String work = "";
		
		
		System.out.println(work);
		
		renderJson(new CommonResponse("0", work));
	}
	
	public void update(){
		long snsId = getParaToInt(0);
		String work = getPara(1);
		
		GraphDatabaseService service = NeoDatabaseFactory.getInstance().service();
		Transaction tx = service.beginTx();
		
		try {
			work = URLDecoder.decode(work,"UTF-8");
			Node target = NeoDatabaseFactory.getInstance().indexs().get(PersonNode.ID, snsId+"").getSingle();
			target.setProperty(PersonNode.JOB, work);

			tx.success();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			
			logger.error(sw.toString());
			renderJson(new CommonResponse("1", e.getLocalizedMessage()));
		}finally{
			tx.finish();
			renderJson(new CommonResponse("0", "update succ"));
		}
	}
	
	public void search(){
		long snsId = getParaToInt(0);
		String work = getPara(1);
		int depth = getParaToInt(2);
		int limit = getParaToInt(3);
		
		GraphDatabaseService service = NeoDatabaseFactory.getInstance().service();
		Transaction tx = service.beginTx();
		
		try {
			work = URLDecoder.decode(work,"UTF-8");
			
			Node target = NeoDatabaseFactory.getInstance().indexs().get(PersonNode.ID, snsId+"").getSingle();
			if(target == null){
				renderJson(new CommonResponse("1", "node not exist"));
			}else{
				ArrayList<PersonNode> list = NeoOptions.searchJob(target, work, depth, limit);
				renderJson(list);
			}
			
			tx.success();
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			
			logger.error(sw.toString());
		}finally{
			tx.finish();
		}
	}
	
	public void searchlist(){
		long snsId = getParaToInt(0);
		String work = getPara(1);
		int depth = getParaToInt(2);
		int limit = getParaToInt(3);
		
		GraphDatabaseService service = NeoDatabaseFactory.getInstance().service();
		Transaction tx = service.beginTx();
		
		try {
			work = URLDecoder.decode(work,"UTF-8");
			
			Node target = NeoDatabaseFactory.getInstance().indexs().get(PersonNode.ID, snsId+"").getSingle();
			if(target == null){
				renderJson(new CommonResponse("1", "node not exist"));
			}else{
				ArrayList<String> list = NeoOptions.seachList(target, work, depth, limit);
				renderJson(list);
			}
			
			tx.success();
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			
			logger.error(sw.toString());
		}finally{
			tx.finish();
		}
	}
	
	public void build(){
		long snsId = getParaToInt(0);
		
		GraphDatabaseService service = NeoDatabaseFactory.getInstance().service();
		
		Transaction tx = service.beginTx();
		try {
			
			Node userNode = NeoOptions.getNodeById(snsId+"");
			SNSUser user  = SNSUser.findBySNSid(snsId);
			
			if(user == null)return;
			
			if(user.getInt("isinit") == 1){
				//renderJson(new CommonResponse("0", ""));
				//return;
			}
			
			if(userNode != null){
				userNode.setProperty(PersonNode.JOB, user.getStr("work"));				
			}else{//´´½¨
				userNode = PersonNode.Create(user.getLong("snsId")+"", user.getStr("name"), user.getStr("work"));
			}
			
			List<FriendShip> friendShips = FriendShip.getBySNSid(snsId);
			if(friendShips == null || friendShips.isEmpty()){
				renderJson(new CommonResponse("0", ""));
				return;
			}
			
			for (FriendShip friendShip : friendShips) {
				long friendId = friendShip.getLong("friendId");
				SNSUser friend = SNSUser.findBySNSid(friendId);;
				Node friendNode = NeoOptions.getNodeById(friendId+"");
				if(friendNode == null){					
					if(friend == null)continue;
					friendNode = PersonNode.Create(friend.getLong("snsId")+"",friend.getStr("name") ,friend.getStr("work"));
				}
				
				NeoOptions.knows(userNode, friendNode);
				
				logger.info("finished "+user.getStr("name")+" and "+friend.getStr("name"));
			}
			tx.success();
			
			user.set("isinit", 1).update();
			
			renderJson(new CommonResponse("0", ""));
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			
			logger.error(sw.toString());
			renderJson(new CommonResponse("1", e.getMessage()));
		}finally{
			tx.finish();
		}
		
		
	}
}
