package com.example.dataprocessing.domain.metaDesignImages;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "meta_design_images")
public class MetaDesignImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DESIGN_IMG_SEQ")
    private Long designImgSeq;      // 시퀀스

    @Column(name = "DESIGN_SEQ")
    private Long designSeq;         // 디자인 시퀀스

    @Column(name = "IMAGE_NAME")
    private String imageName;       // 이미지 이름

    @Column(name = "IMAGE_URL")
    private String imgUrl;          // 이미지 URL

    @Column(name = "IMAGE_PATH")
    private String imgPath;         // 이미지 경로

    @Column(name = "IMAGE_NUMBER")
    private Long imgNumber;         // 이미지 순번

    @Column(name = "UPD_ID")
    private String updId;           // 수정자아이디

    @Column(name = "UPD_DATE")
    private LocalDateTime updDate;  // 수정일시

    @Column(name = "VIEWPOINT_NAME")
    private String viewpointName;   // 대상체면

    @Column(name = "VIEWPOINT")
    private Integer viewpoint;      // 대상체면 번호

    @Column(name = "USE_YN")
    private char useYn;             // default: Y
}
