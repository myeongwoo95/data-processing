package com.example.dataprocessing.domain.metaDesignImages;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class MetaDesignImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long designImgSeq;      // 시퀀스
    private Long designSeq;         // 디자인 시퀀스

    private String imageName;       // 이미지 이름
    private String imgUrl;          // 이미지 URL
    private String imgPath;         // 이미지 경로
    private Long imgNumber;       // 이미지 순번

    private String updId;           // 수정자아이디
    private LocalDateTime updDate;  // 수정일시
    private String viewpointName;   // 대상체면
    private Integer viewpoint;      // 대상체면 번호
    private String useYn;           // default: Y
}
