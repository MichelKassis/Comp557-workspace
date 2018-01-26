package comp557.a1;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;

public class BallJoint extends DAGNode {

	DoubleParameter rx;
	DoubleParameter ry;
	DoubleParameter rz;
	
	double tx;
	double ty;
	double tz;
	
	
	
	public BallJoint(String name, double tx, double ty, double tz, double xRotation, double yRotation, double zRotation) {
		super(name);
		
		dofs.add( rx = new DoubleParameter( name+" rx", 0, xRotation, xRotation ) );		
		dofs.add( ry = new DoubleParameter( name+" ry", 0, yRotation, yRotation ) );
		dofs.add( rz = new DoubleParameter( name+" rz", 0, zRotation, zRotation ) );
		
		
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glPushMatrix();
		
		gl.glTranslated(tx, ty -2, tz);
		
		gl.glRotated(rx.getValue(), 1, 0, 0);
		gl.glRotated(ry.getValue(), 0, 1, 0);
		gl.glRotated(rz.getValue(), 0, 0, 1);
		
		super.display(drawable);
		
		gl.glPopMatrix();
	
	}
	
}
