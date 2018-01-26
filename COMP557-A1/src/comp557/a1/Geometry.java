package comp557.a1;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import mintools.parameters.DoubleParameter;

public class Geometry extends DAGNode {
	
	private Shape shape;
	
	double transX;
	double transY;
	double transZ;
	DoubleParameter rx;
	DoubleParameter ry;
	DoubleParameter rz;
	double scaleX;
	double scaleY;
	double scaleZ;

	
	public Geometry(String name, Shape shape,
			double transX, double transY,double transZ, 
			double rotateX,double rotateY,double rotateZ,
			double scaleX, double scaleY,double scaleZ ){
		super(name);
//		dofs.add( tx = new DoubleParameter( name+" tx", 0, -2, 2 ) );		
//		dofs.add( ty = new DoubleParameter( name+" ty", 0, -2, 2 ) );
//		dofs.add( tz = new DoubleParameter( name+" tz", 0, -2, 2 ) );
		
		
		
		this.transX = transX ;
		this.transY = transY ;
		this.transZ = transZ ;
		
		
		dofs.add( rx = new DoubleParameter( name+" rx", 0, -180, 180 ) );		
		dofs.add( ry = new DoubleParameter( name+" ry", 0, -180, 180 ) );
		dofs.add( rz = new DoubleParameter( name+" rz", 0, -180, 180 ) );
		
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
		
		this.shape = shape;
		
		
		
		
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glPushMatrix();
		
		gl.glTranslated(transX, transY, transZ);
		gl.glRotated(rx.getValue(), 1, 0, 0);
		gl.glRotated(ry.getValue(), 0, 1, 0);
		gl.glRotated(rz.getValue(), 0, 0, 1);
		
		gl.glScaled(scaleX, scaleY, scaleZ);
		
		switch(shape) {
		
		case Cube:
			gl.glColor3f(1.0f, 1.0f, 0.5f);
			glut.glutSolidCube(1);
			break;
		case Teapot:
			gl.glColor3f(0.3f, 1.0f, 1.0f);
			glut.glutSolidTeapot(1);
			break;
		case Sphere:
			gl.glColor3f(1.0f, 0.3f, 1.0f);
			glut.glutSolidSphere(1, 3, 3);
			break;
		}
		
		super.display(drawable);
		
		gl.glPopMatrix();
	
	}
	
	public enum Shape {Cube,Sphere,Teapot}
	
}
