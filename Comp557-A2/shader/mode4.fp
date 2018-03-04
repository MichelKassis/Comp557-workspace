// mode 4 - ambient and Lambertian and Specular with shadow map

uniform sampler2D shadowMap; 
uniform float sigma;

varying vec3 N;  // surface normal in camera 
varying vec3 v;  // surface fragment location in camera 
varying vec4 vL; // surface fragment location in light view NDC
 
void main(void) {

	// TODO: Objective 6: ambient, Labertian, and Specular with shadow map.
	// Note that the shadow map lookup should only modulate the Lambertian and Specular component.

    gl_FragColor = vec4(fract(v.xyz),1);
}