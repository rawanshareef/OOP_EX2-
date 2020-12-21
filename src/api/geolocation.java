package api;

public class geolocation implements geo_location {
    private double _x;
    private double _y;
    private double _z;


    public geolocation() {
        this._x = 0 ;
        this._y = 0;
        this._z = 0;
    }
    public geolocation(double _x, double _y, double _z) {
        this._x = _x;
        this._y = _y;
        this._z = _z;
    }
    @Override
    public double x() {
        return this._x;
    }
    @Override
    public double y() {
        return this._y;
    }

    @Override
    public double z() {
        return this._z;
    }

    @Override
    public double distance(geo_location g) {
        double sum=(Math.pow(g.x()-_x, 2)+Math.pow(g.y()-_y, 2)+Math.pow(g.z()-_z, 2));
        return Math.sqrt(sum);

    }
}
