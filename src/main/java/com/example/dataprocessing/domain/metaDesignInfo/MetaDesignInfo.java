package com.example.dataprocessing.domain.metaDesignInfo;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "meta_design_info")
public class MetaDesignInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DESIGN_SEQ")
    private Long designSeq;                     // 1. 시퀀스

    @Column(name = "REGISTRATION_NUMBER")
    private String registrationNumber;          // 2. 등록번호                 ( us-patent-grant / us-bibliographic-data-grant / publication-reference / document-id / doc-number)

    @Column(name = "REG_REFERENCE_NUMBER")
    private String regReferenceNumber;          // 3. 등록 참조 번호            ( 나중에 )

    @Column(name = "DESIGN_NUMBER")
    private String designNumber;                // 4. 디자인 구분 번호          ( us-patent-grant / us-bibliographic-data-grant / publication-reference / kind)

    @Column(name = "APPLICATION_NUMBER")
    private String applicationNumber;           // 5. 출원번호                 ( us-patent-grant / us-bibliographic-data-grant / application-reference appl-type="design" / document-id / doc-number)

    @Column(name = "OPEN_DESIGN_STATUS")
    private String openDesignStatus;            // 6. 공개디자인 여부           ( 나중에 )

    @Column(name = "ARTICLE_NAME")
    private String articleName;                 // 7. 디자인물품명칭,출원인      ( us-patent-grant / us-bibliographic-data-grant / invention-title)

    @Column(name = "APPLICANT_NAME")
    private String applicantName;               // 8. 출원인 이름              ( us-patent-grant / us-bibliographic-data-grant / us-parties / us-applicants / us-applicant / addressbook / orgname

    @Column(name = "APPLICANT_ADDRESS")
    private String applicantAddress;            // 9. 출원인 주소              ( us-patent-grant / us-bibliographic-data-grant / us-parties / us-applicants / us-applicant / addressbook / address / country + city ) [ country 빼고 city만 넣든 ]

    @Column(name = "LAST_RIGHT_HOLDER_NAME")
    private String lastRightHolderName;         // 10. 최종권리자 이름           ( us-patent-grant / us-bibliographic-data-grant / assignees / assignee / addressbook / orgname)

    @Column(name = "LAST_RIGHT_HOLDER_ADDRESS")
    private String lastRightHolderAddress;      // 11. 최종권리자 주소           ( us-patent-grant / us-bibliographic-data-grant / assignees / assignee / addressbook / address / country + city ) [ country 빼고 city만 넣든 ]

    @Column(name = "CLASS_CODE")
    private String classCode;                   // 12. 분류코드                 ( us-patent-gran / us-bibliographic-data-grant / classification-national / main-classification) [D 1101로 되어있는데 공백을 공백아닌걸로 치환해야함]

    @Column(name = "CLASS_CODE_INT")
    private String classCodeInt;                // 13. 국제분류                 ( us-patent-gran / us-bibliographic-data-grant / classification-locarno / main-classification ) [숫자 네자리인데 가운데 - 넣어줘야함]

    @Column(name = "AGENT_NAME")
    private String agentName;                   // 14. 대리인 명칭              ( us-patent-grant / us-bibliographic-data-grant / us-parties / agents / agent / addressbook / orgname) [법인명]

    @Column(name = "AGENT_ADDRESS")
    private String agentAddress;                // 15. 대리인 주소              ( us-patent-grant / us-bibliographic-data-grant / us-parties / agents / agent / addressbook / address / country) [국가만 존재함]

    @Column(name = "REGISTRATION_DATE")
    private String registrationDate;            // 16. 등록일자                 ( us-patent-grant / us-bibliographic-data-grant / us-parties / hague-agreement-data / international-registration-date / date ) [Nullable]

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Column(name = "IMG_URL")
    private String imgUrl;                      // 17. 이미지 URL (NULL)

    @Column(name = "IMG_PATH")
    private String imgPath;                     // 18. 이미지 저장경로

    @Column(name = "PDF_URL")
    private String pdfUrl;                      // 19. PDF URL (NULL)

    @Column(name = "PDF_PATH")
    private String pdfPath;                     // 20. PDF 저장경로 (NULL)

    @Column(name = "UPD_DATE")
    private LocalDateTime updDate;              // 21. 최종수정일시

    @Column(name = "SIX_IMAGE_INSPECT_DATE")
    private LocalDateTime sixImageInspectDate;  // 22. 상세이미지 검수일자 (NULL)

    @Column(name = "SIX_IMAGE_INSPECTOR_ID")
    private String sixImageInspectorId;         // 23. 상세이미지 검수자 아이디 (NULL)

    @Column(name = "ETC")
    private String etc;                         // 24. 기타( 시험범위, 지정품목 등이 들어가는데 일단 NULL)

    @Column(name = "MANAGEMENT_YEAR")
    private String managementYear;              // 25. 관리연도 ( 일단 현재 날짜 기준으로 2024년 넣어둠 )

    @Column(name = "THUMBNAIL_IMG_FORM")
    private String thumbnailImgForm;            // 26. 대표도면이미지형식(0: 스케치, 1: 3D, 2: 실물사진) (NULL)

    @Column(name = "MODEL_SEQ")
    private Long modelSeq;                      // 27. 주석없음 (NULL)

    @Column(name = "UPD_ID")
    private String updId;                       // 28. 작업자아이디 (NULL)
}
