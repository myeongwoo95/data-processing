package com.example.dataprocessing.web;

import com.example.dataprocessing.domain.metaDesignInfo.MetaDesignInfo;
import com.example.dataprocessing.domain.metaDesignInfo.MetaDesignInfoRepository;
import com.example.dataprocessing.service.DataProcessingService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class DataProcessingController {

    private final DataProcessingService dataProcessingService;
    private final MetaDesignInfoRepository metaDesignInfoRepository;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "it works!";
    }

    // 유미고객건으로 정제에 필요없음
//    @GetMapping("/modifyXml")
    public String ModifyXmls() {

        String filePath = "C:\\Users\\walle\\Downloads\\US디자인등록건_유미고객_7746.xlsx";
        int modifiedCount = 0;
        int modifiedX = 0;

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int numberOfRows = sheet.getPhysicalNumberOfRows();

            for (int i = 1; i < numberOfRows; i++) { // Assuming the first row is the header
                Row row = sheet.getRow(i);

                if (row != null) {
                    Cell cell1 = row.getCell(6);    // 출원번호
                    Cell cell2 = row.getCell(10);   // 등록번호

                    String 출원번호 = cell1.getStringCellValue().replace("/", ""); // e.g. 29/712485
                    String 등록번호 = cell1.getStringCellValue(); // e.g. D987320

                    // 2. metaDesignInfo에서 출원번호에 해당하는 객체 가져오기
                    List<MetaDesignInfo> MetaDesignInfos = metaDesignInfoRepository.findByApplicationNumber(출원번호);

                    // 3. 해당하는 객체가 있다면 DB 객체의 등록번호를 xml 등록번호에 overwrite
                    if(MetaDesignInfos.size() != 0) {

                        if(MetaDesignInfos.size() == 1) {
                            MetaDesignInfo metaDesignInfo = MetaDesignInfos.get(0);
                            cell2.setCellValue(metaDesignInfo.getRegistrationNumber());

                        } else {
                            String 등록번호들 = "";
                            for(MetaDesignInfo metaDesignInfo : MetaDesignInfos) {
                                등록번호들 += metaDesignInfo.getRegistrationNumber() + ", ";
                            }
                            cell2.setCellValue(등록번호들);
                        }
                        modifiedCount++;

                    } else { // 4. 해당하는 객체가 없다면 xml 등록번호에 X로 수정
                        cell2.setCellValue("X");
                        modifiedX++;
                    }
                }

//                if(i == 50) {
//                    break;
//                }

                System.out.println("==============================진행률: " + numberOfRows + "중 " + i + "개 완료==============================");
            }

            // 변경 사항을 엑셀 파일에 저장
            try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "실패";
        }

        System.out.println("============================== 완료 ==============================");
        System.out.println("수정 완료 개수: " + modifiedCount + "개");
        System.out.println("X로 수정된 개수: " + modifiedX + "개");
        return "성공";
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
    public String all() {
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
