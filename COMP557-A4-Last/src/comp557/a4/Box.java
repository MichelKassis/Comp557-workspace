package comp557.a4;

import javax.vecmath.Point3d;

/**
 * A simple box class. A box is defined by it's lower (@see min) and upper (@see max) corner. 
 */
public class Box extends Intersectable {

	public Point3d max;
	public Point3d min;

	/**
	 * Default constructor. Creates a 2x2x2 box centered at (0,0,0)
	 */
	public Box() {
		super();
		this.max = new Point3d( 1, 1, 1 );
		this.min = new Point3d( -1, -1, -1 );
	}	

	@Override
	public void intersect(Ray ray, IntersectResult result) {
		// TODO: Objective 6: Finish this class
		double txMin = (min.x - ray.eyePoint.x)/ray.viewDirection.x;
		double txMax = (max.x - ray.eyePoint.x)/ray.viewDirection.x;
		double tyMin = (min.y - ray.eyePoint.y)/ray.viewDirection.y;
		double tyMax = (max.y - ray.eyePoint.y)/ray.viewDirection.y;
		double tzMin = (min.z - ray.eyePoint.z)/ray.viewDirection.z;
		double tzMax = (max.z - ray.eyePoint.z)/ray.viewDirection.z;
		boolean swapX = false;
		boolean swapY = false;
		boolean swapZ = false;
		if(txMin > txMax){
			txMin = swap(txMax, txMax = txMin);
			swapX = true;
		}
		if(tyMin > tyMax){
			tyMin = swap(tyMax, tyMax = tyMin);
			swapY = true;
		}
		if(tzMin > tzMax){
			tzMin = swap(tzMax, tzMax = tzMin);
			swapZ = true;
		}
		double tMin = Math.max(txMin, Math.max(tyMin, tzMin));
		double tMax = Math.min(txMax, Math.min(tyMax, tzMax));
		if(tMin < tMax && tMin > 1e-9 && tMin < result.t){
			result.t = tMin;
			result.p.scaleAdd(tMin, ray.viewDirection, ray.eyePoint);
			result.material = this.material;
			if(txMin > Math.max(tyMin, tzMin)){
				if(!swapX){
					result.n.set(-1,0,0);
				}else{
					result.n.set(1,0,0);
				}
			}else if(tyMin > Math.max(txMin, tzMin)){
				if(!swapY){
					result.n.set(0,-1,0);
				}else{
					result.n.set(0,1,0);
				}
			}else{
				if(!swapZ){
					result.n.set(0,0,-1);
				}else{
					result.n.set(0, 0, 1);
				}
			}

		}
	}	

	private double swap(double a, double b){
		return a;
	}

}
