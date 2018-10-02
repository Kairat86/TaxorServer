package taxor.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import taxor.model.Taxist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
        System.out.println("phone number=>" + phone);
        Taxist taxist = map.remove(phone);
        System.out.println("Taxist removed=>" + taxist);
        System.out.println("Total=>" + map.size());
    }

    @RequestMapping(value = "/send-coords", method = RequestMethod.POST)
    private void sendCoords(@RequestBody String phoneLatLon) {
        String[] strings = phoneLatLon.substring(1, phoneLatLon.length() - 1).split(":");
        Taxist taxist = map.get(strings[0]);
        taxist.setLat(strings[1]);
        taxist.setLon(strings[2]);
        System.out.println("Updated coords of=>" + taxist);
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    private Collection<Taxist> get(@RequestBody String latLon) {
        System.out.println("Get taxists=>" + map.values());
        String[] strings = latLon.substring(1, latLon.length() - 1).split(":");
        Double lat = Double.valueOf(strings[0]);
        Double lon = Double.valueOf(strings[1]);
        ArrayList<Taxist> taxists = new ArrayList<>(map.values());
        taxists.sort((t1, t2) -> {
            double d1 = (t1.getLat() - lat) * (t1.getLat() - lat) - (t1.getLon() - lon) * (t1.getLon() - lon);
            double d2 = (t2.getLat() - lat) * (t2.getLat() - lat) - (t2.getLon() - lon) * (t2.getLon() - lon);
            return Double.compare(d2, d1);
        });
        return taxists;
    }
}
