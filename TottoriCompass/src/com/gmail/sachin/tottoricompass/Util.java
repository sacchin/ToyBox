package com.gmail.sachin.tottoricompass;

public class Util {
	/**
	 * 360度系から180度系に変換する
	 * @param deg
	 * @return
	 */
	public static int convertTo180(int deg) {
		if(0 < deg && deg <= 180){
			return deg;
		}else if(180 < deg && deg <= 360){
			return (deg - 360);
		}else{
			return 0;
		}
	}

	/**
	 * 出発点(latitude1, longitude1)から見た、到達点(latitude2, longitude2)への、360度系の角度を計算する。
	 * @param latitude1
	 * @param longitude1
	 * @param latitude2
	 * @param longitude2
	 * @return
	 */
	public static int getDirection(double latitude1, double longitude1,
			double latitude2, double longitude2){
	    double lat1 = Math.toRadians(latitude1);
	    double lat2 = Math.toRadians(latitude2);
	    double lng1 = Math.toRadians(longitude1);
	    double lng2 = Math.toRadians(longitude2);
	    double Y = Math.sin(lng2 - lng1) * Math.cos(lat2);
	    double X = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lng2 - lng1);
	    double deg = Math.toDegrees(Math.atan2(Y, X));
	    double angle = (deg + 360) % 360;
	    return (int) (Math.abs(angle) + (1 / 7200));
	}
	
	/**
	 * 2点間の距離(km)を算出する。
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @param precision
	 * @return
	 */
	public static float getDistance(double lat1, double lng1, double lat2, double lng2, int precision) {
	    int R = 6371; // km
	    double lat = Math.toRadians(lat2 - lat1);
	    double lng = Math.toRadians(lng2 - lng1);
	    double A = Math.sin(lat / 2) * Math.sin(lat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lng / 2) * Math.sin(lng / 2);
	    double C = 2 * Math.atan2(Math.sqrt(A), Math.sqrt(1 - A));
	    double decimalNo = Math.pow(10, precision);
	    double distance = R * C;
	    distance = Math.round(decimalNo * distance / 1) / decimalNo;
	    return (float) distance;
	}
}
