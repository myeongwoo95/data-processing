package com.example.dataprocessing.web;

import com.example.dataprocessing.service.DataProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DataProcessingController {

    private final DataProcessingService dataProcessingService;

    @GetMapping("/test")
    public String test() {
        return "it works!";
    }

    @GetMapping("/unzip")
    public String unzip() {
        try {
            dataProcessingService.unzip();
        } catch (Exception e) {
            e.printStackTrace();
            return "실패";
        }
        return "성공";
    }

//    @GetMapping("/filtering")
//    public String filtering() {
//        try {
//            dataProcessingService.filtering();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "실패";
//        }
//        return "성공";
//    }

    @GetMapping("/insert")
    public String insert() {
        try {
            dataProcessingService.insert();
        } catch (Exception e) {
            e.printStackTrace();
            return "실패";
        }
        return "성공";
    }

    @GetMapping("/all")
    public String all() {
        try {
            dataProcessingService.unzip();
            dataProcessingService.insert();
        } catch (Exception e) {
            e.printStackTrace();
            return "실패";
        }
        return "성공";
    }
}
