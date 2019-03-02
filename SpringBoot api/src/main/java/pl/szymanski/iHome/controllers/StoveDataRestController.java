package pl.szymanski.iHome.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.szymanski.iHome.domain.HttpAnswer;
import pl.szymanski.iHome.domain.StoveData;
import pl.szymanski.iHome.domain.repository.StoveRepository;

@RestController
public class StoveDataRestController {

    @Autowired
    StoveRepository stoveRepository;

    @PostMapping("/saveStoveTemperature")
    Object saveTemp (@RequestParam String temperature) {
        stoveRepository.addNewMeasurement(temperature);
        return new HttpAnswer(200, null,"OK");
    }

    @GetMapping("/getOld")
    Object readOld (@RequestParam long minutes) {
        System.out.println("/getOld mins " + minutes);
        Object temp;
        if ((temp = stoveRepository.getOld(minutes)) != null){
            return new HttpAnswer(200, null,temp);
        }
        return new HttpAnswer(204, "no content", null);
    }

    @GetMapping("/getStoveTemperature")
    Object getData(){
        StoveData stoveData = stoveRepository.getLastetstMeasurement();
        if (Double.valueOf(stoveData.getTemperature()) > 5){
            return new HttpAnswer(200,null, stoveData);
        }
        return new HttpAnswer(204,"no data",null);
    }

    @RequestMapping("/test")
    String test(){
        return "OK";
    }

}
