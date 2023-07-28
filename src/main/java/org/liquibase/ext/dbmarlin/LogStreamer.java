package org.liquibase.ext.dbmarlin;

import com.datical.liquibase.ext.logging.structured.StructuredLogService;
import liquibase.license.LicenseServiceUtils;
import org.liquibase.ext.dbmarlin.DBmarlinLogFormatter;
import java.util.logging.Formatter;

public class LogStreamer extends StructuredLogService {

    @Override
    public int getPriority() {
        return super.getPriority() + 100;
    }

    @Override
    public Formatter getCustomFormatter() {
        return LicenseServiceUtils.isProLicenseValid() ? new DBmarlinLogFormatter() : null;
    }
}