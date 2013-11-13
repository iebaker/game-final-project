package iebaker.xenon.util;

public class Fluid2D {
	public float 	GWID;
	public int 		XDIM;
	public int		YDIM;
	public float 	VISC;
	public float 	DIFF;
	public int 		ITER;
	public int 		SIZE;
	public float 	DISP;

	public float[] density;
	public float[] density_prior;
	public float[] velocity_x;
	public float[] velocity_x_prior;
	public float[] velocity_y;
	public float[] velocity_y_prior;

	public Fluid2D(float g, int xd, int yd, float v, float d) {
		this.GWID = g;
		this.XDIM = xd;
		this.YDIM = yd;
		this.VISC = v;
		this.DIFF = d;
		this.SIZE = (this.XDIM + 2) * (this.YDIM + 2);
		this.ITER = 20;

		density = new float[SIZE];
		density_prior = new float[SIZE];
		velocity_x = new float[SIZE];
		velocity_x_prior = new float[SIZE];
		velocity_y = new float[SIZE];
		velocity_y_prior = new float[SIZE];
	}
}
