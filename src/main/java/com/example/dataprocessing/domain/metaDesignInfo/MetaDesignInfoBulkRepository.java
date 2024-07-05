package com.example.dataprocessing.domain.metaDesignInfo;

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
public class MetaDesignInfoBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<MetaDesignInfo> MetaDesignInfoList){
        String sql = "INSERT INTO meta_design_info (DESIGN_SEQ, AGENT_ADDRESS, AGENT_NAME, APPLICANT_ADDRESS, APPLICANT_NAME, APPLICATION_NUMBER, ARTICLE_NAME, CLASS_CODE, CLASS_CODE_INT, DESIGN_NUMBER, ETC, IMG_PATH, IMG_URL, LAST_RIGHT_HOLDER_ADDRESS, LAST_RIGHT_HOLDER_NAME, MANAGEMENT_YEAR, MODEL_SEQ, OPEN_DESIGN_STATUS, PDF_PATH, PDF_URL, REG_REFERENCE_NUMBER, REGISTRATION_DATE, REGISTRATION_NUMBER, SIX_IMAGE_INSPECT_DATE, SIX_IMAGE_INSPECTOR_ID, THUMBNAIL_IMG_FORM, UPD_DATE, UPD_ID)"+
                "VALUES (?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                MetaDesignInfoList,
                MetaDesignInfoList.size(),
//                (PreparedStatement ps, MetaDesignInfo metaDesignInfo) -> {
//                    ps.setLong(1, metaDesignInfo.getDesignSeq());
//                    ps.setString(2, metaDesignInfo.getAgentAddress());
//                    ps.setString(3, metaDesignInfo.getAgentName());
//                    ps.setString(4, metaDesignInfo.getApplicantAddress());
//                    ps.setString(5, metaDesignInfo.getApplicantName());
//                    ps.setString(6, metaDesignInfo.getApplicationNumber());
//                    ps.setString(7, metaDesignInfo.getArticleName());
//                    ps.setString(8, metaDesignInfo.getClassCode());
//                    ps.setString(9, metaDesignInfo.getClassCodeInt());
//                    ps.setString(10, metaDesignInfo.getDesignNumber());
//                    ps.setString(11, metaDesignInfo.getEtc());
//                    ps.setString(12, metaDesignInfo.getImgPath());
//                    ps.setString(13, metaDesignInfo.getImgUrl());
//                    ps.setString(14, metaDesignInfo.getLastRightHolderAddress());
//                    ps.setString(15, metaDesignInfo.getLastRightHolderName());
//                    ps.setString(16, metaDesignInfo.getManagementYear());
//                    ps.setLong(17, metaDesignInfo.getModelSeq());
//                    ps.setString(18, metaDesignInfo.getOpenDesignStatus());
//                    ps.setString(19, metaDesignInfo.getPdfPath());
//                    ps.setString(20, metaDesignInfo.getPdfUrl());
//                    ps.setString(21, metaDesignInfo.getRegReferenceNumber());
//                    ps.setString(22, metaDesignInfo.getRegistrationDate());
//                    ps.setString(23, metaDesignInfo.getRegistrationNumber());
//                    ps.setTimestamp(24, Timestamp.valueOf(metaDesignInfo.getSixImageInspectDate()));
//                    ps.setString(25, metaDesignInfo.getSixImageInspectorId());
//                    ps.setString(26, metaDesignInfo.getThumbnailImgForm());
//                    ps.setTimestamp(27, Timestamp.valueOf(metaDesignInfo.getUpdDate()));
//                    ps.setString(28, metaDesignInfo.getUpdId());
//                });
                (PreparedStatement ps, MetaDesignInfo metaDesignInfo) -> {
                    if (metaDesignInfo.getDesignSeq() != null) {
                        ps.setLong(1, metaDesignInfo.getDesignSeq());
                    } else {
                        ps.setNull(1, Types.BIGINT);
                    }

                    if (metaDesignInfo.getAgentAddress() != null) {
                        ps.setString(2, metaDesignInfo.getAgentAddress());
                    } else {
                        ps.setNull(2, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getAgentName() != null) {
                        ps.setString(3, metaDesignInfo.getAgentName());
                    } else {
                        ps.setNull(3, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getApplicantAddress() != null) {
                        ps.setString(4, metaDesignInfo.getApplicantAddress());
                    } else {
                        ps.setNull(4, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getApplicantName() != null) {
                        ps.setString(5, metaDesignInfo.getApplicantName());
                    } else {
                        ps.setNull(5, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getApplicationNumber() != null) {
                        ps.setString(6, metaDesignInfo.getApplicationNumber());
                    } else {
                        ps.setNull(6, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getArticleName() != null) {
                        ps.setString(7, metaDesignInfo.getArticleName());
                    } else {
                        ps.setNull(7, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getClassCode() != null) {
                        ps.setString(8, metaDesignInfo.getClassCode());
                    } else {
                        ps.setNull(8, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getClassCodeInt() != null) {
                        ps.setString(9, metaDesignInfo.getClassCodeInt());
                    } else {
                        ps.setNull(9, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getDesignNumber() != null) {
                        ps.setString(10, metaDesignInfo.getDesignNumber());
                    } else {
                        ps.setNull(10, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getEtc() != null) {
                        ps.setString(11, metaDesignInfo.getEtc());
                    } else {
                        ps.setNull(11, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getImgPath() != null) {
                        ps.setString(12, metaDesignInfo.getImgPath());
                    } else {
                        ps.setNull(12, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getImgUrl() != null) {
                        ps.setString(13, metaDesignInfo.getImgUrl());
                    } else {
                        ps.setNull(13, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getLastRightHolderAddress() != null) {
                        ps.setString(14, metaDesignInfo.getLastRightHolderAddress());
                    } else {
                        ps.setNull(14, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getLastRightHolderName() != null) {
                        ps.setString(15, metaDesignInfo.getLastRightHolderName());
                    } else {
                        ps.setNull(15, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getManagementYear() != null) {
                        ps.setString(16, metaDesignInfo.getManagementYear());
                    } else {
                        ps.setNull(16, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getModelSeq() != null) {
                        ps.setLong(17, metaDesignInfo.getModelSeq());
                    } else {
                        ps.setNull(17, Types.BIGINT);
                    }

                    if (metaDesignInfo.getOpenDesignStatus() != null) {
                        ps.setString(18, metaDesignInfo.getOpenDesignStatus());
                    } else {
                        ps.setNull(18, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getPdfPath() != null) {
                        ps.setString(19, metaDesignInfo.getPdfPath());
                    } else {
                        ps.setNull(19, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getPdfUrl() != null) {
                        ps.setString(20, metaDesignInfo.getPdfUrl());
                    } else {
                        ps.setNull(20, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getRegReferenceNumber() != null) {
                        ps.setString(21, metaDesignInfo.getRegReferenceNumber());
                    } else {
                        ps.setNull(21, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getRegistrationDate() != null) {
                        ps.setString(22, metaDesignInfo.getRegistrationDate());
                    } else {
                        ps.setNull(22, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getRegistrationNumber() != null) {
                        ps.setString(23, metaDesignInfo.getRegistrationNumber());
                    } else {
                        ps.setNull(23, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getSixImageInspectDate() != null) {
                        ps.setTimestamp(24, Timestamp.valueOf(metaDesignInfo.getSixImageInspectDate()));
                    } else {
                        ps.setNull(24, Types.TIMESTAMP);
                    }

                    if (metaDesignInfo.getSixImageInspectorId() != null) {
                        ps.setString(25, metaDesignInfo.getSixImageInspectorId());
                    } else {
                        ps.setNull(25, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getThumbnailImgForm() != null) {
                        ps.setString(26, metaDesignInfo.getThumbnailImgForm());
                    } else {
                        ps.setNull(26, Types.VARCHAR);
                    }

                    if (metaDesignInfo.getUpdDate() != null) {
                        ps.setTimestamp(27, Timestamp.valueOf(metaDesignInfo.getUpdDate()));
                    } else {
                        ps.setNull(27, Types.TIMESTAMP);
                    }

                    if (metaDesignInfo.getUpdId() != null) {
                        ps.setString(28, metaDesignInfo.getUpdId());
                    } else {
                        ps.setNull(28, Types.VARCHAR);
                    }
                });
    }
}
