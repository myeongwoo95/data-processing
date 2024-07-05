package com.example.dataprocessing.domain.metaDesignImages;

import com.example.dataprocessing.domain.metaDesignInfo.MetaDesignInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MetaDesignImagesBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<MetaDesignImages> metaDesignImagesList){
        String sql = "INSERT INTO meta_design_images (DESIGN_IMG_SEQ, DESIGN_SEQ, IMAGE_NAME, IMAGE_NUMBER, IMAGE_PATH, IMAGE_URL, UPD_DATE, UPD_ID, USE_YN, VIEWPOINT, VIEWPOINT_NAME)"+
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                metaDesignImagesList,
                metaDesignImagesList.size(),
//                (PreparedStatement ps, MetaDesignImages metaDesignImages) -> {
//                    ps.setLong(1, metaDesignImages.getDesignImgSeq());
//                    ps.setLong(2, metaDesignImages.getDesignSeq());
//                    ps.setString(3, metaDesignImages.getImageName());
//                    ps.setLong(4, metaDesignImages.getImgNumber());
//                    ps.setString(5, metaDesignImages.getImgPath());
//                    ps.setString(6, metaDesignImages.getImgUrl());
//                    ps.setTimestamp(7, Timestamp.valueOf(metaDesignImages.getUpdDate()));
//                    ps.setString(8, metaDesignImages.getUpdId());
//                    ps.setString(9, Character.toString(metaDesignImages.getUseYn()));
//                    ps.setInt(10, metaDesignImages.getViewpoint());
//                    ps.setString(11, metaDesignImages.getViewpointName());
//                });

                (PreparedStatement ps, MetaDesignImages metaDesignImages) -> {
                    if (metaDesignImages.getDesignImgSeq() != null) {
                        ps.setLong(1, metaDesignImages.getDesignImgSeq());
                    } else {
                        ps.setNull(1, Types.BIGINT);
                    }

                    if (metaDesignImages.getDesignSeq() != null) {
                        ps.setLong(2, metaDesignImages.getDesignSeq());
                    } else {
                        ps.setNull(2, Types.BIGINT);
                    }

                    if (metaDesignImages.getImageName() != null) {
                        ps.setString(3, metaDesignImages.getImageName());
                    } else {
                        ps.setNull(3, Types.VARCHAR);
                    }

                    if (metaDesignImages.getImgNumber() != null) {
                        ps.setLong(4, metaDesignImages.getImgNumber());
                    } else {
                        ps.setNull(4, Types.BIGINT);
                    }

                    if (metaDesignImages.getImgPath() != null) {
                        ps.setString(5, metaDesignImages.getImgPath());
                    } else {
                        ps.setNull(5, Types.VARCHAR);
                    }

                    if (metaDesignImages.getImgUrl() != null) {
                        ps.setString(6, metaDesignImages.getImgUrl());
                    } else {
                        ps.setNull(6, Types.VARCHAR);
                    }

                    if (metaDesignImages.getUpdDate() != null) {
                        ps.setTimestamp(7, Timestamp.valueOf(metaDesignImages.getUpdDate()));
                    } else {
                        ps.setNull(7, Types.TIMESTAMP);
                    }

                    if (metaDesignImages.getUpdId() != null) {
                        ps.setString(8, metaDesignImages.getUpdId());
                    } else {
                        ps.setNull(8, Types.VARCHAR);
                    }

                    ps.setString(9, Character.toString(metaDesignImages.getUseYn())); // char는 null을 가질 수 없어서 분기 처리 X

                    if (metaDesignImages.getViewpoint() != null) {
                        ps.setInt(10, metaDesignImages.getViewpoint());
                    } else {
                        ps.setNull(10, Types.INTEGER);
                    }

                    if (metaDesignImages.getViewpointName() != null) {
                        ps.setString(11, metaDesignImages.getViewpointName());
                    } else {
                        ps.setNull(11, Types.VARCHAR);
                    }
                });
    }
}
