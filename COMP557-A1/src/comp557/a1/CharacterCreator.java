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
				2, 2, 2);
		
		Geometry teapot = new Geometry( "Teapot", Geometry.Shape.Teapot, 
				0, 0, 0,
				0, 0, 0,
				1, 1, 1);
		
		Geometry Sphere = new Geometry( "Sphere", Geometry.Shape.Sphere, 
				2, 2, 2,
				0, 0, 0,
				2, 2, 2);
		
		root.add(hinge);
		
		//root.add(ball);
		hinge.add(cube);
		hinge.add(Sphere);
		
		
		root.add(teapot);
		
		//ball.add(Sphere);
		return root;
	}
}
