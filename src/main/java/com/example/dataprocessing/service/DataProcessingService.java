package com.example.dataprocessing.service;

import com.example.dataprocessing.common.TarFileProcessor;
import com.example.dataprocessing.domain.MetaDesignInfoImagesBulkRepository;
import com.example.dataprocessing.domain.metaDesignImages.MetaDesignImages;
import com.example.dataprocessing.domain.metaDesignImages.MetaDesignImagesBulkRepository;
import com.example.dataprocessing.domain.metaDesignImages.MetaDesignImagesRepository;
import com.example.dataprocessing.domain.metaDesignInfo.MetaDesignInfo;
import com.example.dataprocessing.domain.metaDesignInfo.MetaDesignInfoBulkRepository;
import com.example.dataprocessing.domain.metaDesignInfo.MetaDesignInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RequiredArgsConstructor
@Service
public class DataProcessingService {

    private final TarFileProcessor tarFileProcessor;

    private final MetaDesignInfoRepository metaDesignInfoRepository;
    private final MetaDesignImagesRepository metaDesignImagesRepository;

    private final MetaDesignInfoBulkRepository metaDesignInfoBulkRepository;
    private final MetaDesignImagesBulkRepository metaDesignImagesBulkRepository;
    private final MetaDesignInfoImagesBulkRepository metaDesignInfoImagesBulkRepository;

    private static final String UPD_ID = "vitasoft";
    private static final String A_DIR_PATH = System.getProperty("os.name").startsWith("Windows") ? "C:/A" : "/data";
    private static final String EXTRACTED_DIR_PATH = System.getProperty("os.name").startsWith("Windows") ? "C:/extracted" : "/data/extracted";

    private static final String DESTINATION_PATH = System.getProperty("os.name").startsWith("Windows") ? "C:/design_images" : "/data/uploads/design_images";
    private static final String FILE_THUMBNAIL_SAVE_PATH = System.getProperty("os.name").startsWith("Windows") ? "C:/design_thumbnail_image" : "/data/uploads/design_thumbnail_image";

    public void md(){

    }

    // 1. 압축 해제
    public void unzip() {
        File aDir = new File(A_DIR_PATH);

        try {
            // 특정 폴더(A_DIR_PATH) 안의 모든 tar 압축파일을 특정 폴더(EXTRACTED_DIR_PATH) 폴더에 해제
            for (File tarFile : aDir.listFiles((dir, name) -> name.endsWith(".tar"))) {
                try {
                    tarFileProcessor.extractTarFile(tarFile.getAbsolutePath(), EXTRACTED_DIR_PATH);
                    tarFileProcessor.cleanUpExtractedFiles(EXTRACTED_DIR_PATH);
                } catch (IOException e) {
                    // IOException 발생 시 해당 파일만 스킵
                    System.err.println("IOException while processing file: " + tarFile.getAbsolutePath());
                    e.printStackTrace(); // 혹은 로깅
                }
            }

            // 특정 폴더(EXTRACTED_DIR_PATH) 안에서 이름이 "-SUPP"로 끝나는 폴더들을 삭제
            deleteFoldersWithSuffix(new File(EXTRACTED_DIR_PATH), "-SUPP");

            // 특정 폴더(EXTRACTED_DIR_PATH) 안에서 모든 하위 폴더를 순회하여 DESIGN 폴더를 찾고 그 폴더안에 zip 파일을 특정경로에 압축을 해제
            extractFromSourceFolder(EXTRACTED_DIR_PATH);

        } catch (Exception e) {
            // 다른 예외 처리가 필요한 경우 추가
            System.err.println("Exception in unzip process:");
            e.printStackTrace(); // 혹은 로깅
        }
    }

    // 2. fig 이미지 중복 파일 제거
    public void filtering() throws IOException, ParserConfigurationException, SAXException {
        List<File> xmlFiles = getXmlFilesFromFolders(DESTINATION_PATH);

        int count = 0;
        for (File xmlFile : xmlFiles) {

            // DOM 파서 생성
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // DTD validation 비활성화 설정
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            // XML 파일 파싱하여 Document 객체 획득
            Document document = builder.parse(xmlFile);

            // 루트 요소인 'us-patent-grant' 요소 획득
            Element root = document.getDocumentElement();

            // us-patent-grant의 'drawings' 요소 획득 (이미지 갯수)
            Element drawings = (Element) root.getElementsByTagName("drawings").item(0);

            // description 요소의 하위 요소들 중 'figure' 요소 list로 획득 후 몇개인지 count
            NodeList figures = drawings.getElementsByTagName("figure");
            int figureCount = figures.getLength();

            // us-patent-grant의 'description' 요소 획득 (이미지 갯수)
            Element description = (Element) root.getElementsByTagName("description").item(0);

            // us-patent-grant의 'description' 요소의 하위 요소들 중 'description-of-drawings' 요소 획득 (fig 갯수)
            Element descriptionOfDrawings = (Element) description.getElementsByTagName("description-of-drawings").item(0);

            // descriptionOfDrawings 요소의 하위 요소들 중 'p' 요소 list로 획득 후 몇개인지 count
            NodeList pList = descriptionOfDrawings.getElementsByTagName("p");
            int pCount = pList.getLength();

            if (figureCount == pCount) {
                count++;
            } else {
                // 파일 이름을 가져오고 확장자를 제거
                String fileNameWithoutExtension = removeExtension(xmlFile.getName());

                // 폴더 경로를 생성
                File folder = new File(DESTINATION_PATH + "/" + fileNameWithoutExtension);

                // 폴더와 그 하위 내용을 삭제하는 메서드
                deleteFolder(folder);
            }
        }

        System.out.println("최초 폴더갯수 = " + xmlFiles.size());
        System.out.println("fig 이미지 파일이 안겹치는 폴더 갯수 = " + count);

        List<File> filteredXmlFiles = getXmlFilesFromFolders(DESTINATION_PATH);
        System.out.println("최종 갯수 = " + filteredXmlFiles.size());
    }

    // 3. DB insert
    public void insert() throws IOException, ParserConfigurationException, SAXException {
        List<File> xmlFiles = getXmlFilesFromFolders(DESTINATION_PATH);

        List<MetaDesignInfo> metaDesignInfoList = new ArrayList<>();
        List<MetaDesignImages> metaDesignImagesList = new ArrayList<>();

        Long designImgSeq = 1L;

        for (int i=0; i<xmlFiles.size(); i++) {
            File xmlFile = xmlFiles.get(i);

            // DOM 파서 생성
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // DTD validation 비활성화 설정
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            // XML 파일 파싱하여 Document 객체 획득
            Document document = builder.parse(xmlFile);

            // 루트 요소인 'us-patent-grant' 요소 획득
            Element root = document.getDocumentElement();

            // 'us-bibliographic-data-grant' 요소 획득
            Element bibliographicData = (Element) root.getElementsByTagName("us-bibliographic-data-grant").item(0);

            // 'application-reference' 요소 획득
            Element applicationReference = (Element) root.getElementsByTagName("application-reference").item(0);

            // 'application-reference/document-id' 요소 획득
            Element applicationReferenceDocumentId = (Element) applicationReference.getElementsByTagName("document-id").item(0);

            // 'application-reference/document-id/doc-number' 요소 획득
            String applicationNumber = applicationReferenceDocumentId.getElementsByTagName("doc-number").item(0).getTextContent();

            // 'publication-reference' 요소 획득
            Element publicationReference = (Element) bibliographicData.getElementsByTagName("publication-reference").item(0);

            // 'document-id' 요소 획득
            Element documentId = (Element) publicationReference.getElementsByTagName("document-id").item(0);

            // 'doc-number' 요소의 값 추출 (2)
            String registrationNumber = documentId.getElementsByTagName("doc-number").item(0).getTextContent();

            // 'kind' 요소의 값 추출 (4)
            String designNumber = publicationReference.getElementsByTagName("kind").item(0).getTextContent();

            // 'invention-title' 요소의 값 추출 (7)
            String articleName = bibliographicData.getElementsByTagName("invention-title").item(0).getTextContent();

            // 'us-parties' 요소 획득
            Element usParties = (Element) bibliographicData.getElementsByTagName("us-parties").item(0);

            // 'us-applicants' 요소 획득
            Element applicants = (Element) usParties.getElementsByTagName("us-applicants").item(0);

            // 'us-applicant' 요소 획득
            Element applicant = (Element) applicants.getElementsByTagName("us-applicant").item(0);

            // 'addressbook' 요소 획득
            Element addressbook = (Element) applicant.getElementsByTagName("addressbook").item(0);

            // 'orgname' 요소의 값 추출 (8)
            String applicantName = null;
            NodeList orgnameList = addressbook.getElementsByTagName("orgname");

            if (orgnameList.getLength() > 0) {
                applicantName = orgnameList.item(0).getTextContent();
            }

            // 'address' 요소 획득
            Element address = (Element) addressbook.getElementsByTagName("address").item(0);

            // 'country' 요소의 값 추출 (9-1)
            String applicantAddressCountry = null;
            NodeList countriesList = addressbook.getElementsByTagName("country");
            if (countriesList.getLength() > 0) {
                applicantAddressCountry = countriesList.item(0).getTextContent();
            }

            // 'city' 요소의 값 추출 (9-2)
            String applicantAddressCity = null;
            NodeList citiesList = addressbook.getElementsByTagName("city");
            if (citiesList.getLength() > 0) {
                applicantAddressCity = citiesList.item(0).getTextContent();
            }

            // 'assignees' 요소 획득
            Element assignees = null;
            Element assignee = null;
            Element assigneeAddressbook = null;
            Element assigneeAddressbookAddress = null;

            String lastRightHolderName = null; // 최종권리자 이름
            String lastRightHolderAddress = null; // 최종권리자 주소
            String lastRightHolderAddressCountry = null; // 최종권리자 주소: 국가
            String lastRightHolderAddressCity = null; // 최종권리자 주소: 도시

            assignees = (Element) bibliographicData.getElementsByTagName("assignees").item(0);

            if(assignees != null) {
                assignee = (Element) assignees.getElementsByTagName("assignee").item(0);

                if(assignee != null) {
                    assigneeAddressbook = (Element) assignee.getElementsByTagName("addressbook").item(0);

                    if(assigneeAddressbook != null) {

                        // 'orgname' 요소의 값 추출 (10)
                        NodeList assigneeAddressbookOrgnameList = assigneeAddressbook.getElementsByTagName("orgname");
                        if (assigneeAddressbookOrgnameList.getLength() > 0) {
                            lastRightHolderName = assigneeAddressbookOrgnameList.item(0).getTextContent();
                        }

                        assigneeAddressbookAddress = (Element) assigneeAddressbook.getElementsByTagName("address").item(0);
                        if(assigneeAddressbookAddress != null) {

                            // 'country' 요소의 값 추출 (11-1)
                            NodeList assigneeAddressbookCountriesList = assigneeAddressbookAddress.getElementsByTagName("country");
                            if (assigneeAddressbookCountriesList.getLength() > 0) {
                                lastRightHolderAddressCountry = assigneeAddressbookCountriesList.item(0).getTextContent();
                            }

                            // 'city' 요소의 값 추출 (11-2)
                            NodeList assigneeAddressbookCitiesList = assigneeAddressbookAddress.getElementsByTagName("city");
                            if (assigneeAddressbookCitiesList.getLength() > 0) {
                                lastRightHolderAddressCity = assigneeAddressbookCitiesList.item(0).getTextContent();
                            }

                            if((lastRightHolderAddressCountry!= null && !"".equals(lastRightHolderAddressCountry)) && (lastRightHolderAddressCity != null && !"".equals(lastRightHolderAddressCity))) {
                                lastRightHolderAddress = lastRightHolderAddressCountry + ", " + lastRightHolderAddressCity;
                            }
                        }
                    }
                }
            }

            // 'classification-national' 요소 획득
            Element classificationNational = (Element) bibliographicData.getElementsByTagName("classification-national").item(0);

            // 'main-classification' 요소의 값 추출 (12)
            String classCode = null;
            NodeList mainClassificationList = classificationNational.getElementsByTagName("main-classification");
            if (mainClassificationList.getLength() > 0) {
                classCode = mainClassificationList.item(0).getTextContent();
                classCode = classCode.replace(" ", "");
            }

            // 'classification-locarno' 요소 획득
            Element classificationLocarno = (Element) bibliographicData.getElementsByTagName("classification-locarno").item(0);

            // 'main-classification' 요소의 값 추출 (13)
            String classCodeInt = null;
            NodeList locarnoMainClassificationList = classificationLocarno.getElementsByTagName("main-classification");
            if (locarnoMainClassificationList.getLength() > 0) {
                classCodeInt = locarnoMainClassificationList.item(0).getTextContent();
            }

            // 'agents' 요소 획득
            Element agents = (Element) usParties.getElementsByTagName("agents").item(0);
            String agentName = null;
            String agentAddress = null;

            if (agents != null) {
                // 'agent' 요소 획득
                Element agent = (Element) agents.getElementsByTagName("agent").item(0);

                if(agent != null) {
                    // 'addressbook' 요소 획득
                    Element agentAddressbook = (Element) agent.getElementsByTagName("addressbook").item(0);

                    if(agentAddressbook != null) {

                        // 'orgname' 요소의 값 추출 (14)
                        NodeList agentAddressbookOrgnamesList = agentAddressbook.getElementsByTagName("orgname");
                        if (agentAddressbookOrgnamesList.getLength() > 0) {
                            agentName = agentAddressbookOrgnamesList.item(0).getTextContent();
                        }

                        Element agentAddressbookAddress = (Element) agentAddressbook.getElementsByTagName("address").item(0);
                        if(agentAddressbookAddress != null) {
                            // 'country' 요소의 값 추출 (15)
                            NodeList agentAddressbookCountriesList = agentAddressbookAddress.getElementsByTagName("country");
                            if (agentAddressbookCountriesList.getLength() > 0) {
                                agentAddress = agentAddressbookCountriesList.item(0).getTextContent();
                            }
                        }
                    }
                }
            }

            // 'hague-agreement-data' 요소 획득
            Element hagueAgreementData = (Element) bibliographicData.getElementsByTagName("hague-agreement-data").item(0);
            String registrationDate = null;

            if(hagueAgreementData != null) {
                // 'international-registration-date' 요소 획득
                Element internationalRegistrationDate = (Element) hagueAgreementData.getElementsByTagName("international-registration-date").item(0);

                if(internationalRegistrationDate != null) {
                    // 'date' 요소의 값 추출 (16)
                    NodeList dateList = internationalRegistrationDate.getElementsByTagName("date");
                    if (dateList.getLength() > 0) {
                        registrationDate = dateList.item(0).getTextContent();
                    }
                }
            }

            // 240812 international doc-number 추가 e.g. DM/087143
            // priority-claims 요소 획득
            Element priorityClaims = (Element) bibliographicData.getElementsByTagName("priority-claims").item(0);
            String priorityClaimDocNumber = null;

            if (priorityClaims != null) {
                // priority-claim 요소 획득
                Element priorityClaim = (Element) priorityClaims.getElementsByTagName("priority-claim").item(0);

                if(priorityClaim != null) {
                    if (priorityClaim.getAttribute("kind").equals("international")) {
                        // doc-number 요소 획득
                        Element docNumber = (Element) priorityClaim.getElementsByTagName("doc-number").item(0);
                        if(docNumber != null) {

                            // 'doc-number' 요소의 값 추출
                            NodeList docNumberList = priorityClaim.getElementsByTagName("doc-number");
                            if (docNumberList.getLength() > 0) {
                                priorityClaimDocNumber = docNumberList.item(0).getTextContent();
                            }
                        }
                    }
                }
            }

            // XML insert 코드 작성
            MetaDesignInfo metaDesignInfo = new MetaDesignInfo();

            metaDesignInfo.setDesignSeq(Long.valueOf(i+1));             // 1. 디자인 일련번호
            metaDesignInfo.setRegistrationNumber(registrationNumber);   // 23. 디자인 구분 번호
            metaDesignInfo.setApplicationNumber(applicationNumber);     // 5. 출원번호
            metaDesignInfo.setDesignNumber(designNumber);               // 7. 디자인물품명칭,출원인

            metaDesignInfo.setArticleName(articleName);                 // 8. 출원인 이름
            metaDesignInfo.setApplicantAddress(applicantAddressCountry + ", " + applicantAddressCity); // 9. 출원인 주소

            metaDesignInfo.setLastRightHolderName(lastRightHolderName); // 10. 최종권리자 이름
            metaDesignInfo.setLastRightHolderAddress(lastRightHolderAddress); // 11. 최종권리자 주소

            metaDesignInfo.setClassCode(classCode);                     // 12. 분류코드
            metaDesignInfo.setClassCodeInt(classCodeInt);               // 13. 분류코드(국제)

            metaDesignInfo.setAgentName(agentName);                     // 14. 대리인 이름
            metaDesignInfo.setAgentAddress(agentAddress);               // 15. 대리인 주소
            metaDesignInfo.setRegistrationDate(registrationDate);       // 16. 국제등록일
            metaDesignInfo.setInternationalDocNumber(priorityClaimDocNumber); // added 240812 국제 문서 번호


            // D00000.TIF 파일복사
            String xmlFileName = xmlFile.getName(); // 파일명 가져오기
            int dotIndex = xmlFileName.lastIndexOf('.'); // 파일명에서 확장자 분리 (폴더명)
            String nameWithoutExtension = xmlFileName.substring(0, dotIndex);
            File thumbnailFile = new File(DESTINATION_PATH + "/" + nameWithoutExtension + "/" + nameWithoutExtension + "-" + "D00000" + ".TIF");

            // 파일 존재 여부 확인
            if (thumbnailFile.exists()) {
                Path sourcePath = thumbnailFile.toPath(); // 복사할 파일의 새로운 경로 설정
                Path targetPath = Paths.get(FILE_THUMBNAIL_SAVE_PATH, thumbnailFile.getName());

                try {
                    Files.copy(sourcePath, targetPath); // 파일 복사
                    // System.out.println("파일이 성공적으로 복사되었습니다: " + targetPath);
                } catch (IOException e) {
                    // System.out.println("파일 복사 중 오류가 발생했습니다: " + e.getMessage());
                }

                metaDesignInfo.setImgPath(FILE_THUMBNAIL_SAVE_PATH + "/" +thumbnailFile.getName());

            } else {
                metaDesignInfo.setImgPath(null);
            }

            int currentYear = LocalDate.now().getYear();
            metaDesignInfo.setManagementYear(Integer.toString(currentYear));

            metaDesignInfo.setOpenDesignStatus(null); // xml에서 추출해야함 -> 없다면 nullable이니 null로 저장
            metaDesignInfo.setRegReferenceNumber(null); // 확인해야함
            metaDesignInfo.setEtc(null); // ai 측에서 update
            metaDesignInfo.setImgUrl(null);
            metaDesignInfo.setModelSeq(null);
            metaDesignInfo.setPdfPath(null);
            metaDesignInfo.setPdfUrl(null);
            metaDesignInfo.setSixImageInspectDate(null);
            metaDesignInfo.setSixImageInspectorId(null);
            metaDesignInfo.setThumbnailImgForm(null); // 실제로안쓴거같음
            metaDesignInfo.setUpdDate(null);
            metaDesignInfo.setUpdId(null);

            // return 값을 받아서 사용할 수 있도록 수정
            // MetaDesignInfo savedMetaDesignInfo = metaDesignInfoRepository.save(metaDesignInfo);
            metaDesignInfoList.add(metaDesignInfo);

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // 폴더 이름 (.xml 포함)
            String folderName  = xmlFile.getName();

            // 폴더 이름 (.xml 제거)
            if (folderName.toLowerCase().endsWith(".xml")) {
                folderName = folderName.substring(0, folderName.length() - 4);
            }

            // 폴더 경로 생성
            File folder = new File(DESTINATION_PATH + "/" + folderName);

            // 폴더안에 모든 TIF 파일을 리스트에 담기
            List<File> tifFiles = getTifFilesFromFolder(folder);

            // 현재 처리 중인 DesignSeq 값을 저장할 변수
            Long currentDesignSeq = null;

            // TIF 파일 리스트 반복문
            for (int j=0; j<tifFiles.size(); j++) {
                File tifFile = tifFiles.get(j);

                // insert 코드 작성
                MetaDesignImages metaDesignImage = new MetaDesignImages();

                metaDesignImage.setDesignImgSeq(designImgSeq++);
                metaDesignImage.setDesignSeq(Long.valueOf(i+1));
                metaDesignImage.setImageName(tifFile.getName());
                metaDesignImage.setImgPath(DESTINATION_PATH + "/" + folderName + "/" + tifFile.getName());
                metaDesignImage.setImgUrl(null);
                LocalDateTime now = LocalDateTime.now();
                metaDesignImage.setUpdDate(now);
                metaDesignImage.setImgNumber(Long.valueOf(j));
                metaDesignImage.setUpdId(UPD_ID);
                metaDesignImage.setUseYn('Y');

//                'description' 요소 획득
//                Element description = (Element) root.getElementsByTagName("description").item(0);
//
//                'description-of-drawings' 요소 획득
//                Element descriptionOfDrawings = (Element) description.getElementsByTagName("description-of-drawings").item(0);
//
//                'description-of-drawings' 요소 안에 p 태그들 가져오기
//                NodeList pTags = descriptionOfDrawings.getElementsByTagName("p");
//
//                String currentTifFileNumber = getLastCharacter(removeExtension(tifFile.getName()));
//                if(currentTifFileNumber.equals("0")){
//                    metaDesignImage.setViewpoint(7);
//                    metaDesignImage.setViewpointName("중복/추가도면");
//                } else {
//
//                    // 반복문을 통해 각 'p' 태그 처리
//                    for (int i = 0; i < pTags.getLength(); i++) {
//                        Element pTag = (Element) pTags.item(i);
//                        String id = pTag.getAttribute("id");
//                        String num = pTag.getAttribute("num");
//                        String textContent = pTag.getTextContent().trim(); // 텍스트 내용 가져오기
//
//                        num = getLastCharacter(num);
//
//                        if(currentTifFileNumber.equals(num)){
//                            if(textContent.contains("is a perspective")){ // 0, 사시도
//                                metaDesignImage.setViewpoint(0);
//                                metaDesignImage.setViewpointName("사시도");
//
//                            } else if(textContent.contains("is a front")){ // 1, 정면도
//                                metaDesignImage.setViewpoint(1);
//                                metaDesignImage.setViewpointName("정면도");
//
//                            } else if(textContent.contains("is a rear") || textContent.contains("is a back")){ // 2, 배면도
//                                metaDesignImage.setViewpoint(2);
//                                metaDesignImage.setViewpointName("배면도");
//
//                            } else if(textContent.contains("is a left side") || textContent.contains("is a left-side")){ // 3, 촤측면도
//                                metaDesignImage.setViewpoint(3);
//                                metaDesignImage.setViewpointName("촤측면도");
//
//                            } else if(textContent.contains("is a right side") || textContent.contains("is a right-side")){ // 4, 우측면도
//                                metaDesignImage.setViewpoint(4);
//                                metaDesignImage.setViewpointName("우측면도");
//
//                            } else if(textContent.contains("is a top")){ // 5, 평면도
//                                metaDesignImage.setViewpoint(5);
//                                metaDesignImage.setViewpointName("평면도");
//
//                            } else if(textContent.contains("is a bottom")){ // 6, 후면도
//                                metaDesignImage.setViewpoint(6);
//                                metaDesignImage.setViewpointName("후면도");
//                            } else {
//                                metaDesignImage.setViewpoint(7);
//                                metaDesignImage.setViewpointName("중복/추가도면");
//                            }
//                            break;
//                        }
//                    }
//                }

                metaDesignImage.setViewpoint(null);
                metaDesignImage.setViewpointName(null);

                metaDesignImagesList.add(metaDesignImage);
            }
        }

        System.out.println("metaDesignInfoList.size() = " + metaDesignInfoList.size());
        System.out.println("metaDesignImagesList.size() = " + metaDesignImagesList.size());

//        metaDesignInfoBulkRepository.saveAll(metaDesignInfoList);
//        metaDesignImagesBulkRepository.saveAll(metaDesignImagesList);
        metaDesignInfoImagesBulkRepository.saveAll(metaDesignInfoList, metaDesignImagesList);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 폴더 내의 모든 TIF 파일 리스트 반환
    private List<File> getTifFilesFromFolder(File folder) {
        List<File> tifFiles = new ArrayList<>();

        // 폴더 내의 모든 TIF 파일 추가
        for (File file : folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".tif"))) {
            tifFiles.add(file);
        }

        return tifFiles;
    }

    private List<File> getXmlFilesFromFolders(String sourcePath) {
        List<File> xmlFiles = new ArrayList<>();
        File sourceFolder = new File(sourcePath);

        for (File folder : sourceFolder.listFiles(File::isDirectory)) {
            for (File file : folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"))) {
                xmlFiles.add(file);
            }
        }
        return xmlFiles;
    }

    private void extractFromSourceFolder(String sourcePath) {
        File sourceFolder = new File(sourcePath);

        // sourcePath의 모든 하위 폴더 순회
        for (File folder : sourceFolder.listFiles(File::isDirectory)) {
            // 각 폴더 내의 DESIGN 폴더 탐색
            File designFolder = new File(folder, "DESIGN");

            // DESIGN 폴더가 있는지 확인
            if (designFolder.exists() && designFolder.isDirectory()) {
                extractFromDesignFolder(designFolder);
            }
        }
    }

    private void extractFromDesignFolder(File designFolder) {
        // DESIGN 폴더 내의 모든 파일 가져오기
        File[] files = designFolder.listFiles();

        if (files != null) {
            // 필터링된 ZIP 파일들을 담을 배열
            File[] zipFiles = Arrays.stream(files)
                    .filter(file -> file.isFile() && file.getName().toLowerCase().endsWith(".zip"))
                    .toArray(File[]::new);

            // 필터링된 각 ZIP 파일에 대해 압축 해제 수행
            for (File zipFile : zipFiles) {
                extractZipFile(zipFile, DESTINATION_PATH);
            }
        }
    }

    private void extractZipFile(File zipFile, String destinationPath) {
        byte[] buffer = new byte[1024];

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                String entryName = zipEntry.getName();
                File newFile = new File(destinationPath + File.separator + entryName);

                // 파일 디렉토리 생성
                new File(newFile.getParent()).mkdirs();

                // 파일 스트림 생성
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }

                zis.closeEntry();
                zipEntry = zis.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 재귀적으로 디렉토리와 파일들을 삭제하는 메소드
    private void deleteDirectory(File dir) throws IOException {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        Files.deleteIfExists(dir.toPath());
    }

    // 이름이 특정 접미사로 끝나는 폴더들을 재귀적으로 삭제하는 메소드
    private void deleteFoldersWithSuffix(File dir, String suffix) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory() && file.getName().endsWith(suffix)) {
                        try {
                            deleteDirectory(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    // 폴더와 그 안의 모든 파일 및 하위 폴더를 삭제하는 메서드입니다.
    private void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            // 폴더 안의 모든 파일과 하위 폴더를 삭제합니다.
            File[] files = folder.listFiles();
            if (files != null) { // Null 체크
                for (File file : files) {
                    deleteFolder(file);
                }
            }
        }
        // 폴더(또는 파일)를 삭제합니다.
        folder.delete();
    }

    // 파일 이름에서 확장자를 제거하는 메서드
    private String removeExtension(String fileName) {
        // 마지막 점의 위치를 찾습니다.
        int lastDotIndex = fileName.lastIndexOf('.');

        // 점이 없거나, 점이 파일 이름의 첫 번째 문자인 경우 확장자가 없습니다.
        if (lastDotIndex == -1 || lastDotIndex == 0) {
            return fileName;
        }

        // 확장자를 제거한 파일 이름을 반환합니다.
        return fileName.substring(0, lastDotIndex);
    }

    // 문자열의 마지막 문자를 반환하는 메서드
    private String getLastCharacter(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("문자열이 null이거나 빈 문자열입니다.");
        }
        return str.substring(str.length() - 1);
    }
}
