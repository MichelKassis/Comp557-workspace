// mode 2 - ambient and Lambertian lighting

varying vec3 N;  // surface normal in camera 
varying vec3 v;  // surface fragment location in camera 
 
void main(void) {
	vec3 L = normalize(gl_LightSource[0].position.xyz - v); //normalized light source 

	vec3 ambient = gl_FrontMaterial.ambient * gl_FrontLightProduct[0].ambient ;
	vec3 diffuse = gl_FrontMaterial.diffuse * gl_FrontLightProduct[0].diffuse * max(dot(N,L), 0.0);

	gl_FragColor = new vec4(diffuse + ambient,1)

    //gl_FragColor = vec4( N.xyz*0.5+vec3(0.5,0.5,0.5), 1 );
}