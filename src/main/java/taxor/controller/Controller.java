package taxor.controller;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import taxor.handler.WSHandler;
import taxor.model.Taxist;

import java.io.IOException;
import java.util.*;

@RestController
public class Controller {

    private final Map<String, Taxist> map = new HashMap<>();

    private WSHandler wsHandler;

    @Autowired
    public void setHandler(WSHandler wsHandler) {
        this.wsHandler = wsHandler;
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    private boolean send(@RequestBody Taxist taxist) {
        map.put(taxist.getPhone(), taxist);
        System.out.println("send taxist=>" + taxist);
        return true;
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    private void remove(@RequestBody String phone) {
        Taxist taxist = map.remove(phone);
        System.out.println("removed=>" + taxist);
    }

    @RequestMapping(value = "/send-coords", method = RequestMethod.POST)
    private void onCoords(@RequestBody String phoneLatLon) {
        String[] strings = phoneLatLon.split(":");
        Taxist taxist = map.get(strings[0]);
        if (taxist == null) {
            taxist = new Taxist(strings[0]);
            map.put(strings[0], taxist);
        }
        taxist.setLat(strings[1]);
        taxist.setLon(strings[2]);
        System.out.println("received coords=>" + taxist);
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    private Collection<Taxist> get(@RequestBody String latLon) {
        String[] strings = latLon.substring(1, latLon.length() - 1).split(":");
        double lat = Double.parseDouble(strings[0]);
        double lon = Double.parseDouble(strings[1]);
        ArrayList<Taxist> taxists = new ArrayList<>(map.values());
        taxists.sort(Comparator.comparingDouble(t -> quasiDistance(lat, lon, t.getLat(), t.getLon())));
        return taxists;
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    private void onMsg(@RequestBody String msg) throws IOException, ParseException {
        System.out.println("on msg=>" + msg);
        wsHandler.call(msg);
    }

    private double quasiDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        return Math.acos(dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

}
