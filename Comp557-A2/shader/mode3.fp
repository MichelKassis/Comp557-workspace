//author Michel Kassis 260662779
varying vec3 N;
varying vec3 v;    

void main (void)  
{  
   vec3 L = normalize(gl_LightSource[0].position.xyz - v);   
   vec3 E = normalize(-v);   
   vec3 R = normalize(-reflect(L,N));  
 
   	vec3 ambient = 	  gl_FrontLightProduct[0].ambient ;

	vec3 diffuse =    gl_FrontLightProduct[0].diffuse * max(dot(N,L), 0.0);

   	vec3 specular =  gl_FrontLightProduct[0].specular 
                * pow(max(dot(R,E),0.0),0.3*gl_FrontMaterial.shininess);

   gl_FragColor = new vec4(ambient + diffuse + specular,1);   
}