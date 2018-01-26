package comp557.a1;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;

public class HingeJoint extends DAGNode {

	DoubleParameter rx;
	DoubleParameter ry;
	DoubleParameter rz;
	
	double xAxis;
	double yAxis;
	double zAxis;
	
	double tx;
	double ty;
	double tz;
	
	
	
	public HingeJoint(String name, double xAxis, double yAxis, double zAxis,
			double tx, double ty, double tz,
			double minRotationAngle, double maxRotationAngle
			) {
		super(name);
		
		dofs.add( rx = new DoubleParameter( name+" rx", 0, minRotationAngle, maxRotationAngle ) );
		
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.zAxis = zAxis;
		
		
		
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glPushMatrix();
		
		gl.glTranslated(tx, ty, tz);
	
		gl.glRotated(rx.getValue(), xAxis, yAxis, zAxis);
		
		super.display(drawable);
		
		gl.glPopMatrix();
	
	}
	
}
