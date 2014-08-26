package com.kaoshixiong.datamining;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.impl.util.FileUtils;


public class NeoDatabaseFactory {
	
	private static NeoDatabaseFactory instance;
	
	private static String database ;//= "D:\JeeSpace\kaoshixiong\WebRoot\kaoshixiong-db"; 
	
	private static GraphDatabaseService graphDb;
	
	private static Index<Node> indexs;
	
	private NeoDatabaseFactory(){
		
	}
	
	public static void configureDatabase(String database){
		NeoDatabaseFactory.database = database;
	}
		
	public static NeoDatabaseFactory getInstance(){
		if(null == instance){
			instance = new NeoDatabaseFactory();
		}
		return instance;
	}
	
	private void init(){
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( database );
		
		indexs = graphDb.index().forNodes( "nodes" );
        registerShutdownHook( graphDb );
	}
	
	public GraphDatabaseService service(){
		if(graphDb == null){
			init();
		}
		return graphDb;
	} 
	
	public Index<Node> indexs(){
		return indexs;
	}
	
	public void clearDb()
    {
        try
        {
            FileUtils.deleteRecursively( new File( database ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }
	
	public void shutDown()
    {
        graphDb.shutdown();
    }

    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
}
