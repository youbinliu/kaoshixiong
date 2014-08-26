package com.kaoshixiong.datamining;

import java.util.ArrayList;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;

public class NeoOptions {
	
	public static void knows(Node a,Node b){
		a.createRelationshipTo(b, RelType.KNOWS);
		b.createRelationshipTo(a, RelType.KNOWS);
	}
	
	public static boolean exist(String id){
		Index<Node> index = NeoDatabaseFactory.getInstance().indexs();
		Node found = index.get(PersonNode.ID, id).getSingle();
		return found != null;
	}
	
	public static Node getNodeById(String id){
		Index<Node> index = NeoDatabaseFactory.getInstance().indexs();
		return index.get(PersonNode.ID, id).getSingle();
	}
	
	public static ArrayList<PersonNode> searchJob(Node node,final String job,int depth,int limit){
		
		ArrayList<PersonNode> list = new ArrayList<>();
		
		TraversalDescription traver = Traversal.description()
				.breadthFirst()
				.relationships(RelType.KNOWS)
				.uniqueness(Uniqueness.NODE_GLOBAL)
				.evaluator(Evaluators.fromDepth(1))
				.evaluator(Evaluators.toDepth(depth))
				.evaluator(new Evaluator() {
					
					@Override
					public Evaluation evaluate(Path path) {
						
						Node node = path.endNode();
						if(node!=null && node.hasProperty(PersonNode.JOB)){
							String work = (String)node.getProperty(PersonNode.JOB);
							
							if((!"".equals(work) && "all".equals(job)) || job.equals(work)){
								
								return Evaluation.INCLUDE_AND_CONTINUE;
							}
						}
						return Evaluation.EXCLUDE_AND_CONTINUE;
					}
				});
		Traverser traverser = traver.traverse(node);
		for (Path path : traverser) {
			PersonNode person = PersonNode.From(path.endNode());
			list.add(person);
		}
		
		return list;
	}
	
	public static ArrayList<String> seachList(Node node,final String job,int depth,int limit){
		
		ArrayList<String> list = new ArrayList<>();
		
		TraversalDescription traver = Traversal.description()
				.breadthFirst()
				.relationships(RelType.KNOWS)
				.uniqueness(Uniqueness.NODE_GLOBAL)
				.evaluator(Evaluators.fromDepth(1))
				.evaluator(Evaluators.toDepth(depth))
				.evaluator(new Evaluator() {
					
					@Override
					public Evaluation evaluate(Path path) {
						
						Node node = path.endNode();
						if(node!=null && node.hasProperty(PersonNode.JOB)){
							String work = (String)node.getProperty(PersonNode.JOB);
							
							if((!"".equals(work) && "all".equals(job)) || job.equals(work)){
								
								return Evaluation.INCLUDE_AND_CONTINUE;
							}
						}
						return Evaluation.EXCLUDE_AND_CONTINUE;
					}
				});
		Traverser traverser = traver.traverse(node);
		PathPrinter pathPrinter = new PathPrinter( "id" );
		String output = "";
		for (Path path : traverser) {
			output = Traversal.pathToString( path, pathPrinter );
			list.add(output);
		}
		
		return list;
	}
	
	static class PathPrinter implements Traversal.PathDescriptor<Path>
	{
	    private final String nodePropertyKey;
	 
	    public PathPrinter( String nodePropertyKey )
	    {
	        this.nodePropertyKey = nodePropertyKey;
	    }
	 
	    @Override
	    public String nodeRepresentation( Path path, Node node )
	    {
	        return node.getProperty( nodePropertyKey, "")+"";
	    }
	 
	    @Override
	    public String relationshipRepresentation( Path path, Node from, Relationship relationship )
	    {
	         return "-";
	    }
	}
}
