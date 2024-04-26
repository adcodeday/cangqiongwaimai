package com.Lu.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "lu.fileupload")
public class FileOperationProperties {
    public int defaultFileSize;
    public String defaultPath;
    public int defaultNameLength;
}
