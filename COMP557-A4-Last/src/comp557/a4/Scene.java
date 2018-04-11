package comp557.a4;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.vecmath.Color4f;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * Simple scene loader based on XML file format.
 */
public class Scene {

	/** List of surfaces in the scene */
	public List<Intersectable> surfaceList = new ArrayList<Intersectable>();

	/** All scene lights */
	public Map<String,Light> lights = new HashMap<String,Light>();

	/** Contains information about how to render the scene */
	public Render render;

	/** The ambient light colour */
	public Color4f ambient = new Color4f();

	/**A black colour to skip specular and reflection calculations*/
	final public Color4f black = new Color4f(0, 0, 0, 1);

	final public Color4f white = new Color4f(1, 1, 1, 1);

	//Basis vectors are the same for every ray so save them, don't need w, precompute -dw, use d = 1 to make things easier
	Vector3d uBase= new Vector3d();
	Vector3d vBase= new Vector3d();
	Vector3d minusDw= new Vector3d();

	//Can also precompute left, bottom and right-left and top-bottom
	double left;
	double bottom;
	double rightMinusLeft;
	double topMinusBottom;

	int n; //n = sqrt(samples) since I sample in an nxn grid

	private Random rand = new Random();


	/** 
	 * Default constructor.
	 */
	public Scene() {
		this.render = new Render();
	}

	/**
	 * renders the scene
	 */
	public void render(boolean showPanel) {

		Camera cam = render.camera; 
		int w = cam.imageSize.width;
		int h = cam.imageSize.height;

		render.init(w, h, showPanel);
		// set u,v,minudDw, left, bottom, rightminusLeft, topMinusBottom
		Vector3d W = new Vector3d();
		W.sub(cam.from , cam.to);
		W.normalize();
		uBase.cross(cam.up, W);
		uBase.normalize();
		vBase.cross(W, uBase);
		vBase.normalize();
		minusDw.scale(-1, W);

		bottom = Math.atan(Math.toRadians(cam.fovy/2));
		topMinusBottom = -2 * bottom;
		left = -(cam.imageSize.width * bottom )/ cam.imageSize.height ;
		rightMinusLeft = -2 * left;

		n = (int)Math.sqrt(render.samples);
		Point2d[] pairs = new Point2d[n*n];

		Ray ray = new Ray();
		Color4f c = new Color4f();
		for ( int i = 0; i < h && !render.isDone(); i++ ) {
			for ( int j = 0; j < w && !render.isDone(); j++ ) {

			

				for(int p = 0; p < n; p++){
					for(int q = 0; q < n; q++){
						generateRay(i, j, p, q, cam, ray);
						IntersectResult result = new IntersectResult();
						for(Intersectable surface : surfaceList){
							surface.intersect(ray, result);
						}
						
				

				int r = (int)(255*c.x);
				int g = (int)(255*c.y);
				int b = (int)(255*c.z);
				int a = (int)(255*c.w);
				int argb = (a<<24 | r<<16 | g<<8 | b);    

				// update the render image
				render.setPixel(j, i, argb);
			}
		}

		// save the final render image
		render.save();

		// wait for render viewer to close
		render.waitDone();

	}


		}
		}

	private void setColour(IntersectResult result, Point3d from, Color4f c, Point2d[] pairs, int p, int q, Color4f scale, int depth){
		if(result.material != null){
			result.n.normalize();
			colour.set(result.material.diffuse.x*ambient.x, result.material.diffuse.y*ambient.y, result.material.diffuse.z*ambient.z, 1);
			colour.set(colour.x*scale.x, colour.y*scale.y, colour.z*scale.z, 1);
			c.add(colour);
			for(Light light : lights.values()){
				if(!inShadow(result, light, new IntersectResult(), new Ray(), pairs[p*n + q])){
					addColour(result, light, from, c, pairs, p, q, scale, depth);
				}
			}
		}else{
			colour.set(render.bgcolor.x, render.bgcolor.y, render.bgcolor.z, 1f);
			colour.set(colour.x*scale.x, colour.y*scale.y, colour.z*scale.z, 1);
			c.add(colour);
		}
	}
	Vector3d l = new Vector3d();
	Vector3d view = new Vector3d();
	Vector3d half = new Vector3d();
	Color4f colour = new Color4f();
	Vector3d r = new Vector3d();
	Vector3d nScaled = new Vector3d();
	Ray reflectionRay = new Ray();
	private void addColour(IntersectResult result, Light light, Point3d from, Color4f c, Point2d[] pairs, int p, int q, Color4f scale, int depth){
		l.sub(light.from, result.p);
		l.normalize();
		view.sub(from, result.p);
		view.normalize();
		half.add(view, l);
		half.normalize();
		colour.set(result.material.diffuse.x*light.color.x, result.material.diffuse.y*light.color.y, result.material.diffuse.z*light.color.z, 1);
		colour.scale((float) light.power);
		colour.scale((float) Math.max(0, result.n.dot(l)));
		colour.set(colour.x*scale.x, colour.y*scale.y, colour.z*scale.z, 1);
		c.add(colour);
		if(!result.material.specular.epsilonEquals(black, (float) 1e-9)){
			colour.set(result.material.specular.x*light.color.x, result.material.specular.y*light.color.y, result.material.specular.z*light.color.z, 1);
			colour.scale((float) light.power);
			colour.scale((float)Math.pow(Math.max(0, result.n.dot(half)), result.material.shinyness));
			colour.set(colour.x*scale.x, colour.y*scale.y, colour.z*scale.z, 1);
			c.add(colour);
			if(depth < 2){
				nScaled.scale(2*view.dot(result.n), result.n);
				r.sub(nScaled, view);
				r.normalize();
				reflectionRay.eyePoint.set(result.p);
				reflectionRay.viewDirection.set(r);
				scale.set(result.material.specular.x*scale.x, result.material.specular.y*scale.y, result.material.specular.z*scale.z, 1);
				result = new IntersectResult();
				for(Intersectable surface : surfaceList){
					surface.intersect(reflectionRay, result);
				}
				setColour(result, reflectionRay.eyePoint, c, pairs, p, q, scale, depth + 1);
			}
		}
	}



	double u;
	double v;
	/**
	 * Generate a ray through pixel (i,j).
	 * 
	 * @param i The pixel row.
	 * @param j The pixel column.
	 * @param offset The offset from the center of the pixel, in the range [-0.5,+0.5] for each coordinate. 
	 * @param cam The camera.
	 * @param ray Contains the generated ray.
	 */
	public void generateRay(final int i, final int j, final int p, final int q, final Camera cam, Ray ray) {

		// TODO: Objective 1: finish this method.
		u = left + rightMinusLeft*(j + (q + epsilon())/n)/cam.imageSize.width;
		v = bottom + topMinusBottom*(i + (p + epsilon())/n)/cam.imageSize.height;
		ray.eyePoint = cam.from;
		ray.viewDirection.set(minusDw);
		ray.viewDirection.scaleAdd(u, uBase, ray.viewDirection);
		ray.viewDirection.scaleAdd(v, vBase, ray.viewDirection);;
		ray.viewDirection.normalize();

	}
	//if jittering is on, returns a random displacement in [0,1), otherwise returns 0.5
	private double epsilon(){
		if(render.jittering){
			return rand.nextDouble();
		}else{
			return 0.5;
		}
	}

	/**
	 * Shoot a shadow ray in the scene and get the result.
	 * 
	 * @param result Intersection result from raytracing. 
	 * @param light The light to check for visibility.
	 * @param root The scene node.
	 * @param shadowResult Contains the result of a shadow ray test.
	 * @param shadowRay Contains the shadow ray used to test for visibility.
	 * 
	 * @return True if a point is in shadow, false otherwise. 
	 */
	Point3d newLight = new Point3d();
	public boolean inShadow(final IntersectResult result, final Light light,  IntersectResult shadowResult, Ray shadowRay, Point2d lightOffset) {

		// TODO: Objective 5: finish this method and use it in your lighting computation
		newLight.set(light.from);
		if(render.smoothShadows){//if using smooth shadows, jitter the light in the xz plane before doing shadow calculations
			newLight.x = newLight.x + lightOffset.x;
			newLight.z = newLight.z + lightOffset.y;
		}
		shadowRay.eyePoint.set(result.p);
		shadowRay.viewDirection.sub(newLight, shadowRay.eyePoint);
		boolean occluded = false;
		for(Intersectable surface : surfaceList){
			surface.intersect(shadowRay, shadowResult);
			if(shadowResult.material != null && shadowResult.t < 1){
				occluded = true;
				break;
			}
		}
		return occluded;
	}    
}
