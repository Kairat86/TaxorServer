package taxor.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import taxor.model.Taxist;

import java.util.*;

@RestController
public class Controller {

    private Map<String, Taxist> map = new HashMap<>();

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    private boolean send(@RequestBody Taxist taxist) {
        System.out.println("Taxist received=>" + taxist.toString());
        map.put(taxist.getPhone(), taxist);
        System.out.println("Total=>" + map.size());
        return true;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    private void remove(@RequestBody String phone) {
        phone = phone.substring(1, phone.length() - 1);
        Taxist taxist = map.remove(phone);
        System.out.println("Taxist removed=>" + taxist);
        System.out.println("Total=>" + map.size());
    }

    @RequestMapping(value = "/send-coords", method = RequestMethod.POST)
    private void sendCoords(@RequestBody String phoneLatLon) {
        String[] strings = phoneLatLon.substring(1, phoneLatLon.length() - 1).split(":");
        Taxist taxist = map.get(strings[0]);
        if (taxist == null) {
            taxist = new Taxist(strings[0]);
        }
        taxist.setLat(strings[1]);
        taxist.setLon(strings[2]);
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    private Collection<Taxist> get(@RequestBody String latLon) {
        String[] strings = latLon.substring(1, latLon.length() - 1).split(":");
        Double lat = Double.valueOf(strings[0]);
        Double lon = Double.valueOf(strings[1]);
        ArrayList<Taxist> taxists = new ArrayList<>(map.values());
        taxists.sort(Comparator.comparingDouble(t -> quasiDistance(lat, lon, t.getLat(), t.getLon())));
        return taxists;
    }

    private double quasiDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        return Math.acos(dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

}
