package com.example.dataprocessing.domain.metaDesignInfo;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class MetaDesignInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long designSeq;                     // 1. 시퀀스

    private String registrationNumber;          // 2. 등록번호                 ( us-patent-grant / us-bibliographic-data-grant / publication-reference / document-id / doc-number)
    private String regReferenceNumber;          // 3. 등록 참조 번호            ( 나중에 )

    private String designNumber;                // 4. 디자인 구분 번호          ( us-patent-grant / us-bibliographic-data-grant / publication-reference / kind)
    private String applicationNumber;           // 5. 출원번호                 ( us-patent-grant / us-bibliographic-data-grant / application-reference appl-type="design" / document-id / doc-number)
    private String openDesignStatus;            // 6. 공개디자인 여부           ( 나중에 )
    private String articleName;                 // 7. 디자인물품명칭,출원인      ( us-patent-grant / us-bibliographic-data-grant / invention-title)

    private String applicantName;               // 8. 출원인 이름              ( us-patent-grant / us-bibliographic-data-grant / us-parties / us-applicants / us-applicant / addressbook / orgname
    private String applicantAddress;            // 9. 출원인 주소              ( us-patent-grant / us-bibliographic-data-grant / us-parties / us-applicants / us-applicant / addressbook / address / country + city ) [ country 빼고 city만 넣든 ]

    private String lastRightHolderName;         // 10. 최종권리자 이름           ( us-patent-grant / us-bibliographic-data-grant / assignees / assignee / addressbook / orgname)
    private String lastRightHolderAddress;      // 11. 최종권리자 주소           ( us-patent-grant / us-bibliographic-data-grant / assignees / assignee / addressbook / address / country + city ) [ country 빼고 city만 넣든 ]

    private String classCode;                   // 12. 분류코드                 ( us-patent-gran / us-bibliographic-data-grant / classification-national / main-classification) [D 1101로 되어있는데 공백을 공백아닌걸로 치환해야함]
    private String classCodeInt;                // 13. 국제분류                 ( us-patent-gran / us-bibliographic-data-grant / classification-locarno / main-classification ) [숫자 네자리인데 가운데 - 넣어줘야함]

    private String agentName;                   // 14. 대리인 명칭              ( us-patent-grant / us-bibliographic-data-grant / us-parties / agents / agent / addressbook / orgname) [법인명]
    private String agentAddress;                // 15. 대리인 주소              ( us-patent-grant / us-bibliographic-data-grant / us-parties / agents / agent / addressbook / address / country) [국가만 존재함]
    private String registrationDate;            // 16. 등록일자                 ( us-patent-grant / us-bibliographic-data-grant / us-parties / hague-agreement-data / international-registration-date / date ) [Nullable]

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String imgUrl;                      // 17. 이미지 URL (NULL)
    private String imgPath;                     // 18. 이미지 저장경로
    private String pdfUrl;                      // 19. PDF URL (NULL)
    private String pdfPath;                     // 20. PDF 저장경로 (NULL)

    private LocalDateTime updDate;              // 21. 최종수정일시
    private LocalDateTime sixImageInspectDate;  // 22. 상세이미지 검수일자 (NULL)
    private String sixImageInspectorId;         // 23. 상세이미지 검수자 아이디 (NULL)
    private String etc;                         // 24. 기타( 시험범위, 지정품목 등이 들어가는데 일단 NULL)
    private String managementYear;              // 25. 관리연도 ( 일단 현재 날짜 기준으로 2024년 넣어둠 )

    private String thumbnailImgForm;            // 26. 대표도면이미지형식(0: 스케치, 1: 3D, 2: 실물사진) (NULL)

    private Long modelSeq;                      // 27. 주석없음 (NULL)
    private String updId;                       // 28. 작업자아이디 (NULL)
}
