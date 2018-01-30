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
		
		HingeJoint neckBone = new HingeJoint("Hinge" , 0, 0, 0, 1, 0, 0, 0, 20);
		BallJoint headBone = new BallJoint("Ball" , 0, 0, 0, 1, 1, 1, -180, 180, -180, 180,-180, 180) ;
		BallJoint leftShoulderBone = new BallJoint("Ball" , 0.5, 0, 0, 1, 1, 1, -360, -150, 0, 16, -180, 180) ;


		BallJoint leftArmBone = new BallJoint("Ball" , 0.5, 0.9, 0, 1, 1, 1, -270, -150, 0, 90, -180, 180) ;
		
		BallJoint rightShoulderBone = new BallJoint("Ball" , 0, 0, 0, 1, 1, 1, -360, -150, 0, 16, -180, 180) ;


		BallJoint rightArmBone = new BallJoint("Ball" , 0.5, 0.2, 0, 1, 1, 1, -270, -150, 0, 90, -180, 180) ;
		HingeJoint teapotBone = new HingeJoint("Teapot Bone" , 0, 0, 0, 1, 0, 0, 0, 20);
		
		BallJoint upperLeftLegBone = new BallJoint("Ball" , 0.3, -0.5, 0.1, 1, 1, 1, -180, 180, -180, 180,180, 270);
		BallJoint upperRightLegBone = new BallJoint("Ball" , -0.3, -0.5, 0.1, 1, 1, 1, -180, 180, -180, 180,180, 270);




		
		Geometry torso = new Geometry( "Torso", Geometry.Shape.Cube, 
				0, 0, 0,
				0, 0, 0,
				5, 5, 5,
				0.0f, 1.0f, 0.0f);
		
		Geometry neck = new Geometry( "Neck", Geometry.Shape.Sphere, 
				0, 0.6, 0,
				0, 0, 0,
				0.15,0.3,0.3,
				1.0f,1.0f,1.0f);
		
		
		
		Geometry head = new Geometry( "Head", Geometry.Shape.Cube,
				0, 2, 0,
				0, 0, 0,
				4, 3, 3,
				0.0f, 0.0f, 1.0f);
		
		Geometry eye1 = new Geometry( "eye1", Geometry.Shape.Sphere,
				-0.2, 0.2, 0.5,
				0, 0, 0,
				0.1,0.1,0.2,
				0.0f,0.0f,0.0f);
		
		
		Geometry eye2 = new Geometry( "eye2", Geometry.Shape.Sphere,
				0.2, 0.2, 0.5,
				0, 0, 0,
				0.1,0.1,0.2,
				0.0f,0.0f,0.0f);
		
		Geometry upperLeftArm = new Geometry( "upperLeftArm", Geometry.Shape.Sphere,
				0.6 ,0.07, 0,
				0, 0, 0,
				0.15,0.7,0.3,
				1.0f,0.0f,0.0f);
		
		Geometry lowerLeftArm = new Geometry( "lowerLeftArm", Geometry.Shape.Sphere,
				-0.6 ,-1.2, 0.5,
				0, 0, 0,
				0.15,0.6,0.3,
				0.0f,0.0f,1.0f);
		
		Geometry upperRightArm = new Geometry( "upperRightArm", Geometry.Shape.Sphere,
				-0.5 ,0.07, 0,
				0, 0, 0,
				0.15,0.7,0.3,
				1.0f,0.0f,0.0f);
		
		Geometry lowerRightArm = new Geometry( "lowerRightArm", Geometry.Shape.Sphere,
				-0.6 ,-1.2, 0.5,
				0, 0, 0,
				0.15,0.6,0.3,
				0.0f,0.0f,1.0f);
		
		Geometry teapot = new Geometry( "Teapot", Geometry.Shape.Teapot,
				0.2 ,0.0, 0.0,
				0, 0, 0,
				1,1,1,
				0.0f,0.0f,1.0f);
		
		
		Geometry upperLeftLeg = new Geometry( "upperLeftLeg", Geometry.Shape.Sphere, 
				0, 0.6, 0,
				0, 0, 0,
				0.15,0.6,0.2,
				1.0f,1.0f,1.0f);
		
		Geometry upperRightLeg = new Geometry( "upperRightLeg", Geometry.Shape.Sphere, 
				0, 0.6, 0,
				0, 0, 0,
				0.15,0.6,0.2,
				1.0f,1.0f,1.0f);
		
//		Geometry hat = new Geometry( "Hat", Geometry.Shape.Cone,
//				0, 2, 0,
//				0, 0, 0,
//				4, 3, 3,
//				1.0f, 0.0f, 0.0f);
//		
//		
		
		
		
		
		
		
						
		root.add(torso);
		torso.add(neckBone);
		neckBone.add(neck);
		neck.add(headBone);
		headBone.add(head);
		head.add(eye2);
		head.add(eye1);
		//head.add(hat);
		
		
		torso.add(leftShoulderBone);
		leftShoulderBone.add(upperLeftArm);
		upperLeftArm.add(leftArmBone);
		leftArmBone.add(lowerLeftArm);
		
		//lowerLeftArm.add(teapotBone);
		//teapotBone.add(teapot);
		
		torso.add(rightShoulderBone);
		rightShoulderBone.add(upperRightArm);
		upperRightArm.add(rightArmBone);
		rightArmBone.add(lowerRightArm);
		
		torso.add(upperLeftLegBone);
		torso.add(upperRightLegBone);
		
		upperLeftLegBone.add(upperLeftLeg);
		upperRightLegBone.add(upperRightLeg);
		
		
		
		
		
		

		
		
		
		//root.add(teapot);
		
		//ball.add(Sphere);
		return root;
	}
}
