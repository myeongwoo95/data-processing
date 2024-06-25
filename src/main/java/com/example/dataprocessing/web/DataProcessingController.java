package com.example.dataprocessing.web;

import com.example.dataprocessing.service.DataProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DataProcessingController {

    private final DataProcessingService dataProcessingService;

    @GetMapping("/unzip")
    public String step1() {
        try {
            dataProcessingService.unzip();
        } catch (Exception e) {
            e.printStackTrace();
            return "실패";
        }
        return "성공";
    }

    @GetMapping("/filtering")
    public String filtering() {
        try {
            dataProcessingService.filtering();
        } catch (Exception e) {
            e.printStackTrace();
            return "실패";
        }
        return "성공";
    }

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
    public String test() {
        try {
            dataProcessingService.unzip();
            dataProcessingService.filtering();
            dataProcessingService.insert();
        } catch (Exception e) {
            e.printStackTrace();
            return "실패";
        }
        return "성공";
    }
}
