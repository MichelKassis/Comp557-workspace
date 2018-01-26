package comp557.a1;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;

public class BallJoint extends DAGNode {

	DoubleParameter rx;
	DoubleParameter ry;
	DoubleParameter rz;
	
	double xAxis;
	double yAxis;
	double zAxis;

	
	double tx;
	double ty;
	double tz;
	
	
	
	public BallJoint(String name, double transformationX, double transformationY, double transformationZ, double xAxis, double yAxis, double zAxis) {
		super(name);
		
		this.tx=transformationX;
		this.ty=transformationY;
		this.tz=transformationZ;
		
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.zAxis = zAxis;

		
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
