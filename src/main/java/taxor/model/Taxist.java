package taxor.model;

import java.util.Objects;

public class Taxist {

    private String name;
    private String phone;
    private Double lat;
    private Double lon;

    public Taxist() {
    }

    public Taxist(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public void setLat(String lat) {
        this.lat = Double.valueOf(lat);
    }

    public void setLon(String lon) {
        this.lon = Double.valueOf(lon);
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "Taxist{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Taxist taxist = (Taxist) o;
        return Objects.equals(phone, taxist.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }
}
