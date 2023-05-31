package com.lojister.business.abstracts;

import com.lojister.model.entity.InsuredTransportProcessFile;
import com.lojister.core.util.FileUploadUtil;

public interface InsuredTransportProcessFileService {

    void uploadFile(FileUploadUtil.FileResult result, Long insuredTransportProcessId);

    InsuredTransportProcessFile findDataByClientTransportProcessTransportCode(String transportCode);

}
