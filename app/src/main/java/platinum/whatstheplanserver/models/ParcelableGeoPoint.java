package platinum.whatstheplanserver.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

public class ParcelableGeoPoint implements Parcelable {

    private GeoPoint geoPoint;

    public ParcelableGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    protected ParcelableGeoPoint(Parcel in) {
        Double lat = in.readDouble();
        Double lng = in.readDouble();
        geoPoint = new GeoPoint(lat, lng);
    }

    public static final Creator<ParcelableGeoPoint> CREATOR = new Creator<ParcelableGeoPoint>() {
        @Override
        public ParcelableGeoPoint createFromParcel(Parcel in) {
            return new ParcelableGeoPoint(in);
        }

        @Override
        public ParcelableGeoPoint[] newArray(int size) {
            return new ParcelableGeoPoint[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(geoPoint.getLatitude());
        parcel.writeDouble(geoPoint.getLongitude());
    }
}
