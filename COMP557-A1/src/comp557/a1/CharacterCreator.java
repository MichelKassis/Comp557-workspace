package comp557.a1;

import com.jogamp.opengl.GLAutoDrawable;

public class CharacterCreator {

	static public String name = "Happy - Michel Kassis AND 260662779";
	
	/** 
	 * Creates a character.
	 * @return root DAGNode
	 */
	static public DAGNode create() {
		// TODO: use for testing, and ultimately for creating a character
		// Here we just return null, which will not be very interesting, so write
		// some code to create a charcter and return the root node.
				
		FreeJoint root = new FreeJoint("Root");
		
		HingeJoint hinge = new HingeJoint("Hinge" , 2, 2, 2, 0, 0, 0, 0, 0);
		//BallJoint ball = new BallJoint("Ball" , -1, -1, -1, 0, 0, 0) ;
		
		Geometry cube = new Geometry( "Cube", Geometry.Shape.Cube,
				-2, 2, 2,
				0, 0, 0,
				2, 2, 2,
				1.0f,1.0f,1.0f);
		
		Geometry teapot = new Geometry( "Teapot", Geometry.Shape.Teapot, 
				1, 1, 1,
				0, 0, 0,
				1, 1, 1,
				0.3f,1.0f,1.0f);
		
		Geometry torso = new Geometry( "Sphere", Geometry.Shape.Sphere, 
				0, 0, 0,
				0, 0, 0,
				2, 4, 2,
				1.0f,0.0f,0.0f);
		
		Geometry neck = new Geometry( "Sphere", Geometry.Shape.Sphere, 
				0, 3.4, 0,
				0, 0, 0,
				0.7, 2, 0.7,
				1.0f,1.0f,1.0f);
		
		Geometry head = new Geometry( "Cube", Geometry.Shape.Cube,
				0,6, 0,
				0, 0, 0,
				4, 3, 3,
				0.0f,0.0f,1.0f);
		
		Geometry eye1 = new Geometry( "Sphere", Geometry.Shape.Sphere,
				-1.2,6.3, 1.5,
				0, 0, 0,
				0.3, 0.5, 0.2,
				1.0f,1.0f,1.0f);
		
		
		Geometry eye2 = new Geometry( "Sphere", Geometry.Shape.Sphere,
				1.2,6.3, 1.5,
				0, 0, 0,
				0.3, 0.5, 0.2,
				1.0f,1.0f,1.0f);
		
		
		
		root.add(torso);
		root.add(neck);
		root.add(head);
		root.add(eye1);
		root.add(eye2);
		
		//root.add(ball);
		//hinge.add(cube);
		//hinge.add(torso);
		
		
		
		//root.add(teapot);
		
		//ball.add(Sphere);
		return root;
	}
}
